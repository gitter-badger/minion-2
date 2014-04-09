package com.neueda.minion.ext.player.event;

import com.neueda.minion.ext.messaging.MessageBusReader;

import java.util.Map;

public class PlayerStream extends PlayerAction {

    private final String url;
    private final String format;

    public PlayerStream(String url, String format) {
        super("stream");
        this.url = url;
        this.format = format;
    }

    public String getUrl() {
        return url;
    }

    public String getFormat() {
        return format;
    }

    public static class Reader implements MessageBusReader<PlayerStream> {
        @Override
        public PlayerStream read(Map<String, Object> data) throws IllegalArgumentException {
            String url = MessageBusReader.castArgument(data.get("url"));
            String format = MessageBusReader.castArgument(data.get("format"));
            return new PlayerStream(url, format);
        }
    }

}
