package com.neueda.perspective.config;

public class AppCfg {

    private XmppCfg xmpp;
    private HipChatCfg hipChat;

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

}
