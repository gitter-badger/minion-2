package com.neueda.perspective.bot;

import com.neueda.perspective.bot.ext.Extension;
import com.neueda.perspective.bot.ext.ExtensionLoaderFactory;
import com.neueda.perspective.bot.ext.result.ExtensionResult;
import com.neueda.perspective.config.AppCfg;
import com.neueda.perspective.hipchat.*;
import com.neueda.perspective.hipchat.dto.RoomResponse;
import com.neueda.perspective.hipchat.dto.UserResponse;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class Bot implements ChatMessageListener {

    private final Logger logger = LoggerFactory.getLogger(Bot.class);
    private final XmppConnectorFactory xmppFactory;
    private XmppConnector xmpp;
    private final HipChat hipChat;
    private final List<Extension> extensions;
    private final String email;
    private String self;
    private final List<String> rooms;

    @Inject
    public Bot(XmppConnectorFactory xmppFactory,
               HipChat hipChat,
               AppCfg cfg,
               ExtensionLoaderFactory loaderFactory) {
        this.xmppFactory = xmppFactory;
        this.hipChat = hipChat;
        email = cfg.getHipChat().getEmail();
        List<String> ext = cfg.getBot().getExtensions();
        extensions = loaderFactory.create(ext).load();
        rooms = cfg.getBot().getJoin();
    }

    public void start() {
        logger.info("Starting up");
        UserResponse user = hipChat.getUser(email);
        self = user.getName();
        for (Extension extension : extensions) {
            logger.info("Initializing extension: {}", extension.name());
            extension.initialize();
        }
        xmpp = xmppFactory.create(user.getXmppJid());
        xmpp.connect(this);
        for (String roomName : rooms) {
            RoomResponse room = hipChat.getRoom(roomName);
            xmpp.joinRoom(self, room.getXmppJid(), this);
        }
    }

    public void shutdown() {
        logger.info("Shutting down");
        if (xmpp != null) {
            xmpp.shutdown();
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
        logger.debug("Message from {}: {}", from, body);
        for (Extension extension : extensions) {
            boolean stop = extension.process(body).accept(new ExtensionResult.Visitor<Boolean>() {
                @Override
                public Boolean visitProceed() {
                    return false;
                }

                @Override
                public Boolean visitRespond(String response) {
                    message(sender, response);
                    return true;
                }

                @Override
                public Boolean visitNotify(String color, String text, boolean notify) {
                    broadcast(color, text, notify);
                    return true;
                }
            });
            if (stop) {
                break;
            }
        }
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
