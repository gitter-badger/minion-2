package com.neueda.perspective.bot;

import com.neueda.perspective.bot.ext.Extension;
import com.neueda.perspective.bot.ext.ExtensionLoaderFactory;
import com.neueda.perspective.bot.ext.result.ExtensionResult;
import com.neueda.perspective.config.AppCfg;
import com.neueda.perspective.hipchat.*;
import com.neueda.perspective.hipchat.dto.RoomData;
import com.neueda.perspective.hipchat.dto.UserData;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class Bot implements RoomMessageListener {

    private final Logger logger = LoggerFactory.getLogger(Bot.class);
    private final XmppConnectorFactory xmppFactory;
    private XmppConnector xmpp;
    private final HipChat hipChat;
    private final List<Extension> extensions;
    private final String email;
    private String self;
    private final List<String> join;

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
        join = cfg.getBot().getJoin();
    }

    public void start() {
        UserData user = hipChat.getUser(email);
        self = user.getName();
        for (Extension extension : extensions) {
            logger.info("Initializing extension: {}", extension.name());
            extension.initialize();
        }
        xmpp = xmppFactory.create(user.getXmppJid());
        xmpp.connect(this);
        for (String roomName : join) {
            RoomData room = hipChat.getRoom(roomName);
            xmpp.joinRoom(self, room.getXmppJid(), this);
        }
    }

    public void shutdown() {
        if (xmpp != null) {
            xmpp.shutdown();
        }
    }

    @Override
    public void onMessage(RoomMessageSender sender, Message message) {
        String from = message.getFrom();
        Optional<String> resource = XmppConnector.getResource(from);
        if (resource.isPresent() && resource.get().equals(self)) {
            return;
        }
        String body = message.getBody();
        logger.debug("Message from {}: {}", from, body);

        Optional<String> response = runExtensions(extensions.iterator(), from, body);
        response.ifPresent(s -> {
            try {
                sender.send(s);
            } catch (XMPPException e) {
                logger.error("Failed to send XMPP response", e);
            }
        });
    }

    private Optional<String> runExtensions(final Iterator<Extension> extensionIterator,
                                           final String from,
                                           final String message) {
        if (!extensionIterator.hasNext()) {
            return Optional.empty();
        }
        Extension ext = extensionIterator.next();
        ExtensionResult result = ext.process(message);
        return result.accept(new ExtensionResult.Visitor<Optional<String>>() {
            @Override
            public Optional<String> visitProceed() {
                return runExtensions(extensionIterator, from, message);
            }

            @Override
            public Optional<String> visitFinish(String response) {
                return Optional.of(response);
            }
        });
    }
}
