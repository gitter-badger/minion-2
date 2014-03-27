package com.neueda.minion.config;

import java.util.List;

public class AppCfg {

    private XmppCfg xmpp;
    private HipChatCfg hipChat;
    private List<String> join;

    public XmppCfg getXmpp() {
        return xmpp;
    }

    public void setXmpp(XmppCfg xmpp) {
        this.xmpp = xmpp;
    }

    public HipChatCfg getHipChat() {
        return hipChat;
    }

    public void setHipChat(HipChatCfg hipChat) {
        this.hipChat = hipChat;
    }

    public List<String> getJoin() {
        return join;
    }

    public void setJoin(List<String> join) {
        this.join = join;
    }

}
