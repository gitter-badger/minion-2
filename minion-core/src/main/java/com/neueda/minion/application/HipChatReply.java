package com.neueda.minion.application;

import com.neueda.minion.ext.messaging.MessageBusReader;

import java.util.Map;

final class HipChatReply {

    private final String sender;
    private final String text;

    private HipChatReply(String sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public static class Reader implements MessageBusReader<HipChatReply> {
        Reader() {
        }

        @Override
        public HipChatReply read(Map<String, Object> data) throws IllegalArgumentException {
            String sender = MessageBusReader.getArgument(data, "sender");
            String text = MessageBusReader.getArgument(data, "text");
            return new HipChatReply(sender, text);
        }
    }

}
