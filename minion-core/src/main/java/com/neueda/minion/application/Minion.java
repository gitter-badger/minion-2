package com.neueda.minion.application;

import com.netflix.governator.annotations.WarmUp;
import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.messaging.MessageBus;
import com.neueda.minion.ext.result.ExtensionResult;
import com.neueda.minion.hipchat.*;
import com.neueda.minion.hipchat.cfg.HipChatCfg;
import com.neueda.minion.hipchat.dto.RoomResponse;
import com.neueda.minion.hipchat.dto.UserResponse;
import com.neueda.minion.web.CommandsBroadcaster;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

        ExtensionResult.Visitor visitor = new ExtensionResult.Visitor() {
            @Override
            public void visitRespond(String response) {
                message(sender, response);
            }

            @Override
            public void visitNotify(String color, String text, boolean notify) {
                broadcast(color, text, notify);
            }

            @Override
            public void visitCommand(String event, Object data) {
                commandsBroadcaster.broadcast(event, data);
            }
        };

        extensions.parallelStream()
                .forEach(extension -> extension.process(body).accept(visitor));
        messageBus.getResults().parallelStream()
                .forEach(result -> result.accept(visitor));
    }

    private void message(ChatMessageSender sender, String text) {
        try {
            sender.send(text);
        } catch (XMPPException e) {
            logger.error("Failed to send XMPP response", e);
        }
    }

    private void broadcast(String color, String text, boolean notify) {
        for (String roomName : rooms) {
            hipChat.sendNotification(roomName, color, text, notify);
        }
    }

}
