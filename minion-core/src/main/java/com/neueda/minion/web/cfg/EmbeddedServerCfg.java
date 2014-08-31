package com.neueda.minion.web.cfg;

import com.netflix.governator.annotations.Configuration;

public final class EmbeddedServerCfg {

    @Configuration("webapp.host")
    private String host = "127.0.0.1";

    @Configuration("webapp.port")
    private int port = 8080;

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

}
