package com.neueda.minion.hipchat;

import org.jivesoftware.smack.packet.Message;

public interface ChatMessageListener {
    void onMessage(ChatMessageSender sender, Message message);
}
