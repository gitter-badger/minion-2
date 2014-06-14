package com.neueda.minion.application;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.netflix.governator.annotations.WarmUp;
import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.messaging.MessageBus;
import com.neueda.minion.hipchat.*;
import com.neueda.minion.hipchat.cfg.HipChatCfg;
import com.neueda.minion.hipchat.dto.RoomResponse;
import com.neueda.minion.hipchat.dto.UserResponse;
import com.neueda.minion.web.CommandsBroadcaster;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.neueda.minion.ext.messaging.MessageBus.dataBuilder;

@Singleton
public class Minion implements ChatMessageListener {

    private final Logger logger = LoggerFactory.getLogger(Minion.class);
    private final String email;
    private final List<String> rooms;
    private final XmppConnectorFactory xmppFactory;
    private final HipChat hipChat;
    private final CommandsBroadcaster commandsBroadcaster;
    private final MessageBus messageBus;
    private final Set<Extension> extensions;
    private String self;

    private final Cache<String, ChatMessageSender> senders =
            CacheBuilder.<String, ChatMessageSender>newBuilder()
                    .expireAfterWrite(1, TimeUnit.MINUTES)
                    .build();

    @Inject
    public Minion(HipChatCfg cfg,
                  XmppConnectorFactory xmppFactory,
                  HipChat hipChat,
                  CommandsBroadcaster commandsBroadcaster,
                  MessageBus messageBus,
                  Set<Extension> extensions) {
        email = cfg.getEmail();
        rooms = cfg.getRoomsAsList();
        this.xmppFactory = xmppFactory;
        this.hipChat = hipChat;
        this.commandsBroadcaster = commandsBroadcaster;
        this.messageBus = messageBus;
        this.extensions = extensions;
    }

    @WarmUp
    public void warmUp() {
        logger.info("Starting up");
        UserResponse user = hipChat.getUser(email);
        self = user.getName();
        for (Extension extension : extensions) {
            logger.info("Initializing extension: {}", extension.getClass().getName());
            extension.initialize();
        }
        XmppConnector xmpp = xmppFactory.create(user.getXmppJid());
        xmpp.connect(this);
        for (String roomName : rooms) {
            RoomResponse room = hipChat.getRoom(roomName);
            xmpp.joinRoom(self, room.getXmppJid(), this);
        }
        listen();
    }

    private void listen() {
        messageBus.subscribe(
                "com.neueda.minion.hipChat.reply",
                this::hipChatReply,
                new HipChatReply.Reader());
        messageBus.subscribe(
                "com.neueda.minion.hipChat.notification",
                this::hipChatNotification,
                new HipChatNotification.Reader());
        messageBus.subscribe(
                "com.neueda.minion.web.event",
                this::webEvent,
                new WebEvent.Reader());
    }

    @Override
    public void onMessage(final ChatMessageSender sender, Message message) {
        String from = message.getFrom();
        Optional<String> resource = XmppConnector.getResource(from);
        if (resource.isPresent() && resource.get().equals(self)) {
            return;
        }
        String body = message.getBody();
        if (body == null) {
            return;
        }
        logger.debug("Message from {}: {}", from, body);

        String senderId = UUID.randomUUID().toString();
        senders.put(senderId, sender);
        messageBus.publish("com.neueda.minion.hipChat.message", dataBuilder()
                .put("sender", senderId)
                .put("from", from)
                .put("body", body)
                .build());
    }

    private void hipChatReply(HipChatReply hipChatReply) {
        String senderId = hipChatReply.getSender();
        ChatMessageSender sender = senders.getIfPresent(senderId);
        if (sender == null) {
            logger.warn("Failed reply: sender {} doesn't exist", senderId);
            return;
        }
        String text = hipChatReply.getText();
        try {
            sender.send(text);
        } catch (XMPPException | SmackException e) {
            logger.error("Failed to send XMPP response", e);
        }
    }

    private void hipChatNotification(HipChatNotification hipChatNotification) {
        String color = hipChatNotification.getColor();
        String text = hipChatNotification.getText();
        boolean notify = hipChatNotification.isNotify();
        for (String roomName : rooms) {
            hipChat.sendNotification(roomName, color, text, notify);
        }
    }

    private void webEvent(WebEvent webEvent) {
        String event = webEvent.getEvent();
        Object data = webEvent.getData();
        commandsBroadcaster.broadcast(event, data);
    }

}
