package com.neueda.minion.hipchat;

import org.jivesoftware.smack.XMPPException;

public interface ChatMessageSender {
    void send(String text) throws XMPPException;
}
