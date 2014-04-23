package com.neueda.minion.ext.player.event;

import com.neueda.minion.ext.messaging.MessageBusReader;

import java.util.Map;

public class PlayerSpeech extends PlayerAction {

    private final String text;
    private final String voice;

    public PlayerSpeech(String text, String voice) {
        super("tts");
        this.text = text;
        this.voice = voice;
    }

    public String getText() {
        return text;
    }

    public String getVoice() {
        return voice;
    }

    public static class Reader implements MessageBusReader<PlayerSpeech> {
        @Override
        public PlayerSpeech read(Map<String, Object> data) throws IllegalArgumentException {
            String text = MessageBusReader.getArgument(data, "text");
            String voice = MessageBusReader.getNullableArgument(data, "voice");
            return new PlayerSpeech(text, voice);
        }
    }

}
