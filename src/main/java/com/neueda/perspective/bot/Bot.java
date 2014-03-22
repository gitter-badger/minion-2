package com.neueda.perspective.bot;

import com.neueda.perspective.bot.ext.Extension;
import com.neueda.perspective.bot.ext.ExtensionLoaderFactory;
import com.neueda.perspective.bot.ext.result.ExtensionResult;
import com.neueda.perspective.config.AppCfg;
import com.neueda.perspective.hipchat.HipChat;
import com.neueda.perspective.hipchat.XmppConnector;
import com.neueda.perspective.hipchat.dto.UserObject;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class Bot implements MessageListener {

    private final Logger logger = LoggerFactory.getLogger(Bot.class);
    private final XmppConnector xmpp;
    private final HipChat hipChat;
    private final List<Extension> extensions;
    private final String email;
    private final List<String> rooms;

    @Inject
    public Bot(XmppConnector xmpp,
               HipChat hipChat,
               AppCfg cfg,
               ExtensionLoaderFactory loaderFactory) {
        this.xmpp = xmpp;
        this.hipChat = hipChat;
        email = cfg.getHipChat().getEmail();
        List<String> ext = cfg.getBot().getExt();
        rooms = cfg.getXmpp().getRooms();
        extensions = loaderFactory.create(ext).load();
    }

    public void start() {
        UserObject user = hipChat.getUser(email);
        xmpp.connect(user.getName(), rooms);
    }

    public void shutdown() {
        xmpp.shutdown();
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        String from = message.getFrom();
        String body = message.getBody();
        logger.debug("Message from {}: {}", from, body);

        Optional<String> response = runExtensions(extensions.iterator(), from, body);
        response.ifPresent(s -> {
            try {
                chat.sendMessage(s);
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
            public Optional<String> visitProceed(String message) {
                return runExtensions(extensionIterator, from, message);
            }

            @Override
            public Optional<String> visitRespond(String response) {
                return Optional.of(response);
            }
        });
    }
}
