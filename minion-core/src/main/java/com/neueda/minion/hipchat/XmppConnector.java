package com.neueda.minion.hipchat;

import com.google.common.base.Throwables;
import com.google.inject.assistedinject.Assisted;
import com.neueda.minion.hipchat.cfg.XmppCfg;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class XmppConnector {

    private final Logger logger = LoggerFactory.getLogger(XmppConnector.class);
    private final Map<String, MultiUserChat> rooms = new HashMap<>();
    private final ScheduledExecutorService scheduler;
    private final XMPPConnection xmpp;
    private final String username;
    private final String password;

    @Inject
    public XmppConnector(XmppCfg cfg,
                         @Named("scheduler.keepAlive") ScheduledExecutorService scheduler,
                         @Assisted String jid) {
        this.scheduler = scheduler;
        String host = cfg.getHost();
        int port = cfg.getPort();
        ConnectionConfiguration config = new ConnectionConfiguration(host, port);
        xmpp = new XMPPConnection(config);
        username = String.format("%s@%s", jid, host);
        password = cfg.getPassword();
    }

    public void connect(ChatMessageListener messageListener) {
        connectAndLogin();
        keepAlive();
        listen(messageListener);
    }

    private void connectAndLogin() {
        String host = xmpp.getHost();
        int port = xmpp.getPort();
        String usernameWithResource = username + "/bot";
        logger.info("Connecting as {} to xmpp://{}:{}", usernameWithResource, host, port);
        try {
            xmpp.connect();
            xmpp.login(usernameWithResource, password);
        } catch (XMPPException e) {
            throw Throwables.propagate(e);
        }
        logger.info("Connected and logged in");
    }

    public void joinRoom(String nickname, String roomJid, ChatMessageListener messageListener) {
        MultiUserChat room = new MultiUserChat(xmpp, roomJid);
        try {
            room.join(nickname, null, noHistory(), SmackConfiguration.getPacketReplyTimeout());
        } catch (XMPPException e) {
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

    private void keepAlive() {
        Chat self = xmpp.getChatManager().createChat(username, null);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                self.sendMessage(" ");
            } catch (XMPPException ignored) {
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    private void listen(ChatMessageListener messageListener) {
        xmpp.getChatManager().addChatListener((chat, createdLocally) -> {
            chat.addMessageListener((privateChat, message) -> {
                messageListener.onMessage(privateChat::sendMessage, message);
            });
        });
    }

    @PreDestroy
    public void shutdown() {
        for (MultiUserChat room : rooms.values()) {
            room.leave();
        }
        xmpp.disconnect();
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
