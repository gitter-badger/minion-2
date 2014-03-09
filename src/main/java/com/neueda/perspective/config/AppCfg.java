package com.neueda.perspective.config;

public class AppCfg {

    private XmppCfg xmpp;
    private HipChatCfg hipChat;
    private BotCfg botCfg;

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
        return botCfg;
    }

    public void setBot(BotCfg botCfg) {
        this.botCfg = botCfg;
    }

}
