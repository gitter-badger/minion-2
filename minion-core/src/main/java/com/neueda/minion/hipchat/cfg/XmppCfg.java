package com.neueda.minion.hipchat.cfg;

import com.netflix.governator.annotations.Configuration;

public final class XmppCfg {

    @Configuration("xmpp.host")
    private String host = "chat.hipchat.com";

    @Configuration("xmpp.port")
    private int port = 5222;

    @Configuration("xmpp.password")
    private String password;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
