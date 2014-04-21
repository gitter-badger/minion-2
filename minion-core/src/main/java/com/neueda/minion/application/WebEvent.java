package com.neueda.minion.application;

import com.neueda.minion.ext.messaging.MessageBusReader;

import java.util.Map;

final class WebEvent {

    private final String event;
    private final Object data;

    private WebEvent(String event, Object data) {
        this.event = event;
        this.data = data;
    }

    public String getEvent() {
        return event;
    }

    public Object getData() {
        return data;
    }

    public static class Reader implements MessageBusReader<WebEvent> {
        Reader() {
        }

        @Override
        public WebEvent read(Map<String, Object> data) throws IllegalArgumentException {
            String event = MessageBusReader.getArgument(data, "event");
            Object argData = MessageBusReader.getArgument(data, "data");
            return new WebEvent(event, argData);
        }
    }

}
