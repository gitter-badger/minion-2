package com.neueda.minion.hipchat;

public interface XmppConnectorFactory {
    XmppConnector create(String jid);
}
