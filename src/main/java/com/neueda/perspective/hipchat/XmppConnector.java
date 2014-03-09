package com.neueda.perspective.hipchat;

import com.google.common.base.Throwables;
import com.neueda.perspective.config.AppCfg;
import com.neueda.perspective.config.XmppCfg;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class XmppConnector {

    public static final String XMPP_PASSWORD = "xmpp.password";

    private final Logger logger = LoggerFactory.getLogger(XmppConnector.class);
    private final String conf;
    private final String username;
    private final XMPPConnection xmpp;
    private final ScheduledExecutorService scheduler;

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

    public void connect() {
        String host = xmpp.getHost();
        int port = xmpp.getPort();
        logger.info("Connecting as {} to xmpp://{}:{}", username, host, port);
        String password = System.getProperty(XMPP_PASSWORD);
        try {
            xmpp.connect();
            xmpp.login(username, password);
        } catch (XMPPException e) {
            throw Throwables.propagate(e);
        }
        keepAlive();
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
        xmpp.disconnect();
        scheduler.shutdown();
    }

}
