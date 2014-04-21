package com.neueda.minion.ext.player.event;

import com.neueda.minion.ext.messaging.MessageBusReader;

import java.util.Map;

public class PlayerSoundEffect extends PlayerAction {

    private final String path;

    public PlayerSoundEffect(String path) {
        super("sfx");
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public static class Reader implements MessageBusReader<PlayerSoundEffect> {
        @Override
        public PlayerSoundEffect read(Map<String, Object> data) throws IllegalArgumentException {
            String path = MessageBusReader.getArgument(data, "path");
            return new PlayerSoundEffect(path);
        }
    }

}
