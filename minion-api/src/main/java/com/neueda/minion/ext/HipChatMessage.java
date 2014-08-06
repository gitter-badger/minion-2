package com.neueda.minion.ext;

import com.neueda.minion.ext.messaging.MessageBusReader;

import java.util.Map;

public final class HipChatMessage {

    private final String sender;
    private final String from;
    private final String body;

    private HipChatMessage(String sender, String from, String body) {
        this.sender = sender;
        this.from = from;
        this.body = body;
    }

    public String getSender() {
        return sender;
    }

    public String getFrom() {
        return from;
    }

    public String getBody() {
        return body;
    }

    public static class Reader implements MessageBusReader<HipChatMessage> {
        Reader() {
        }

        @Override
        public HipChatMessage read(Map<String, Object> data) throws IllegalArgumentException {
            String sender = MessageBusReader.getArgument(data, "sender");
            String from = MessageBusReader.getArgument(data, "from");
            String body = MessageBusReader.getArgument(data, "body");
            return new HipChatMessage(sender, from, body);
        }
    }

}
