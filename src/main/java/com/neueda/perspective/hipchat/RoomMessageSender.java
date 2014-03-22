package com.neueda.perspective.hipchat;

import org.jivesoftware.smack.XMPPException;

public interface RoomMessageSender {
    void send(String text) throws XMPPException;
}
