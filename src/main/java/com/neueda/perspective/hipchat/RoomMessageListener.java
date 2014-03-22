package com.neueda.perspective.hipchat;

import org.jivesoftware.smack.packet.Message;

public interface RoomMessageListener {
    void onMessage(RoomMessageSender sender, Message message);
}
