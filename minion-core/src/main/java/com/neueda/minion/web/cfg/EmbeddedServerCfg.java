package com.neueda.minion.web.cfg;

import com.netflix.governator.annotations.Configuration;

public final class EmbeddedServerCfg {

    @Configuration("webapp.port")
    private int port = 8080;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
