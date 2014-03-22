package com.neueda.perspective.hipchat;

import com.google.common.base.Throwables;
import com.neueda.perspective.config.AppCfg;
import com.neueda.perspective.config.XmppCfg;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class XmppConnector {

    public static final String XMPP_PASSWORD = "xmpp.password";

    private final Logger logger = LoggerFactory.getLogger(XmppConnector.class);
    private final String conf;

    private final String username;

    private final XMPPConnection xmpp;
    private final ScheduledExecutorService scheduler;
    private final Map<String, MultiUserChat> rooms = new HashMap<>();

    @Inject
    public XmppConnector(AppCfg cfg,
                         @Named("scheduler.keepAlive") ScheduledExecutorService scheduler) {
        XmppCfg xmppCfg = cfg.getXmpp();
        String host = xmppCfg.getHost();
        int port = xmppCfg.getPort();
        ConnectionConfiguration config = new ConnectionConfiguration(host, port);
        xmpp = new XMPPConnection(config);
        conf = xmppCfg.getConf();
        String jid = xmppCfg.getJid();
        username = String.format("%s@%s", jid, host);
        this.scheduler = scheduler;
    }

    public void connect(String nickname,
                        List<String> roomNames,
                        RoomMessageListener messageListener) {
        connectAndLogin();
        for (String roomName : roomNames) {
            final MultiUserChat room = joinRoom(nickname, roomName);
            room.addMessageListener(packet -> {
                if (packet instanceof Message) {
                    messageListener.onMessage(room, (Message) packet);
                }
            });
        }
        keepAlive();
    }

    private void connectAndLogin() {
        String host = xmpp.getHost();
        int port = xmpp.getPort();
        String usernameWithResource = username + "/bot";
        logger.info("Connecting as {} to xmpp://{}:{}", usernameWithResource, host, port);
        String password = System.getProperty(XMPP_PASSWORD);
        try {
            xmpp.connect();
            xmpp.login(usernameWithResource, password);
        } catch (XMPPException e) {
            throw Throwables.propagate(e);
        }
        logger.info("Connected and logged in");
    }

    private MultiUserChat joinRoom(String nickname, String roomName) {
        MultiUserChat room = new MultiUserChat(xmpp, roomName + "@" + conf);
        try {
            room.join(nickname, null, noHistory(), SmackConfiguration.getPacketReplyTimeout());
        } catch (XMPPException e) {
            throw Throwables.propagate(e);
        }
        rooms.put(roomName, room);
        logger.info("Joined room: {}", roomName);
        return room;
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
