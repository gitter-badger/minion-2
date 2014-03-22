package com.neueda.perspective.hipchat;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;

public interface RoomMessageListener {
    void onMessage(MultiUserChat room, Message message);
}
