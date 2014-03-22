package com.neueda.perspective.config;

public class AppCfg {

    private XmppCfg xmpp;
    private HipChatCfg hipChat;
    private BotCfg bot;

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

    public BotCfg getBot() {
        return bot;
    }

    public void setBot(BotCfg bot) {
        this.bot = bot;
    }

}
