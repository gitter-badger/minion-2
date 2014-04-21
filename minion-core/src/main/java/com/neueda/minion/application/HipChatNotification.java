package com.neueda.minion.application;

import com.neueda.minion.ext.messaging.MessageBusReader;

import java.util.Map;

final class HipChatNotification {

    private final String color;
    private final String text;
    private final boolean notify;

    private HipChatNotification(String color, String text, boolean notify) {
        this.color = color;
        this.text = text;
        this.notify = notify;
    }

    public String getColor() {
        return color;
    }

    public String getText() {
        return text;
    }

    public boolean isNotify() {
        return notify;
    }

    public static class Reader implements MessageBusReader<HipChatNotification> {
        Reader() {
        }

        @Override
        public HipChatNotification read(Map<String, Object> data) throws IllegalArgumentException {
            String color = MessageBusReader.getArgument(data, "color");
            String text = MessageBusReader.getArgument(data, "text");
            boolean notify = MessageBusReader.getArgument(data, "notify");
            return new HipChatNotification(color, text, notify);
        }
    }

}
