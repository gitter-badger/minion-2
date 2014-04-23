package com.neueda.minion.ext.player.event;

import com.neueda.minion.ext.messaging.MessageBusReader;

import java.util.Map;

public class PlayerSoundEffect extends PlayerAction {

    private final String path;
    private final boolean cached;

    public PlayerSoundEffect(String path, boolean cached) {
        super("sfx");
        this.path = path;
        this.cached = cached;
    }

    public String getPath() {
        return path;
    }

    public boolean isCached() {
        return cached;
    }

    public static class Reader implements MessageBusReader<PlayerSoundEffect> {
        @Override
        public PlayerSoundEffect read(Map<String, Object> data) throws IllegalArgumentException {
            String path = MessageBusReader.getArgument(data, "path");
            boolean cached = MessageBusReader.getArgument(data, "cached");
            return new PlayerSoundEffect(path, cached);
        }
    }

}
