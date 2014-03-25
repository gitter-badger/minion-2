package com.neueda.minion.config;

import java.util.List;

public class BotCfg {

    private List<String> extensions;
    private List<String> join;

    public List<String> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<String> extensions) {
        this.extensions = extensions;
    }

    public List<String> getJoin() {
        return join;
    }

    public void setJoin(List<String> join) {
        this.join = join;
    }

}
