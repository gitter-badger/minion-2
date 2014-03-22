package com.neueda.perspective.hipchat;

import org.jivesoftware.smack.packet.Message;

public interface ChatMessageListener {
    void onMessage(ChatMessageSender sender, Message message);
}
