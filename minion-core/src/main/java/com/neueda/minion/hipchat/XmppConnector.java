package com.neueda.minion.hipchat;

import com.google.common.base.Throwables;
import com.google.inject.assistedinject.Assisted;
import com.neueda.minion.hipchat.cfg.XmppCfg;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class XmppConnector {

    private final Logger logger = LoggerFactory.getLogger(XmppConnector.class);
    private final Map<String, MultiUserChat> rooms = new HashMap<>();
    private final ScheduledExecutorService scheduler;
    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final XMPPTCPConnection xmpp;
    private final Chat selfChat;

    @Inject
    public XmppConnector(XmppCfg cfg,
                         @Named("scheduler.keepAlive") ScheduledExecutorService scheduler,
                         @Assisted String jid) {
        this.scheduler = scheduler;
        host = cfg.getHost();
        port = cfg.getPort();
        ConnectionConfiguration config = new ConnectionConfiguration(host, port);
        username = String.format("%s@%s", jid, host);
        password = cfg.getPassword();
        xmpp = new XMPPTCPConnection(config);
        selfChat = ChatManager.getInstanceFor(xmpp).createChat(username, null);
    }

    public void connect(ChatMessageListener messageListener) {
        connectAndLogin();
        keepAlive();
        listen(messageListener);
    }

    private void connectAndLogin() {
        String usernameWithResource = username + "/bot";
        logger.info("Connecting as {} to xmpp://{}:{}", usernameWithResource, host, port);
        try {
            xmpp.connect();
            xmpp.login(usernameWithResource, password);
        } catch (SmackException | XMPPException | IOException e) {
            throw Throwables.propagate(e);
        }
        logger.info("Connected and logged in");
    }

    public void joinRoom(String nickname, String roomJid, ChatMessageListener messageListener) {
        MultiUserChat room = new MultiUserChat(xmpp, roomJid);
        try {
            room.join(nickname, null, noHistory(), SmackConfiguration.getDefaultPacketReplyTimeout());
        } catch (XMPPException | SmackException e) {
            throw Throwables.propagate(e);
        }
        rooms.put(roomJid, room);
        logger.info("Joined room: {}", roomJid);
        room.addMessageListener(packet -> {
            if (packet instanceof Message) {
                messageListener.onMessage(room::sendMessage, (Message) packet);
            }
        });
    }

    private DiscussionHistory noHistory() {
        DiscussionHistory history = new DiscussionHistory();
        history.setMaxStanzas(0);
        history.setMaxChars(0);
        return history;
    }

    // HipChat-specific keep-alive mechanism
    // See: http://help.hipchat.com/knowledgebase/articles/64377-xmpp-jabber-support-details
    private void keepAlive() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                selfChat.sendMessage(" ");
            } catch (XMPPException | SmackException ignored) {
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    private void listen(ChatMessageListener messageListener) {
        ChatManager.getInstanceFor(xmpp).addChatListener((chat, createdLocally) -> {
            chat.addMessageListener((privateChat, message) -> {
                messageListener.onMessage(privateChat::sendMessage, message);
            });
        });
    }

    @PreDestroy
    public void shutdown() {
        try {
            for (MultiUserChat room : rooms.values()) {
                room.leave();
            }
            xmpp.disconnect();
        } catch (SmackException.NotConnectedException ignored) {
        }
        selfChat.close();
        scheduler.shutdown();
    }

    public static Optional<String> getResource(String jid) {
        int resourceIndex = jid.indexOf('/');
        if (resourceIndex == -1) {
            return Optional.empty();
        }
        return Optional.of(jid.substring(resourceIndex + 1));
    }

}
