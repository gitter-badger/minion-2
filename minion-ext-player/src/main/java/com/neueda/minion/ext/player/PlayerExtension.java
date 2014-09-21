package com.neueda.minion.ext.player;

import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.HipChatMessage;
import com.neueda.minion.ext.Patterns;
import com.neueda.minion.ext.player.event.*;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerExtension extends Extension {

    private static final String PLAYER_EVENT = "com.neueda.minion.ext.player";
    private static final String STREAM_MESSAGE = "com.neueda.minion.ext.player.stream";
    private static final String SFX_MESSAGE = "com.neueda.minion.ext.player.sfx";
    private static final String TTS_MESSAGE = "com.neueda.minion.ext.player.tts";

    private static final Pattern STOP_PATTERN = Patterns.sentence("stop!");

    private PlayerStream nowPlaying;

    @Override
    public void initialize() {
        onHipChatMessage(this::handleMessage);
        messageBus.subscribe(STREAM_MESSAGE, this::handlePlayerStream, new PlayerStream.Reader());
        messageBus.subscribe(SFX_MESSAGE, this::handlePlayerAction, new PlayerSoundEffect.Reader());
        messageBus.subscribe(TTS_MESSAGE, this::handlePlayerAction, new PlayerSpeech.Reader());
    }

    @Override
    public String getQualifier() {
        return PLAYER_EVENT;
    }

    @Override
    public Map<String, Object> getState() {
        return Collections.singletonMap("nowPlaying", nowPlaying);
    }

    private void handleMessage(HipChatMessage message) {
        String body = message.getBody();
        Matcher matcher = STOP_PATTERN.matcher(body);
        if (matcher.matches()) {
            nowPlaying = null;
            serverSentEvent(PLAYER_EVENT, new PlayerStop());
        }
    }

    private void handlePlayerStream(PlayerStream playerStream) {
        nowPlaying = playerStream;
        handlePlayerAction(playerStream);
    }

    private void handlePlayerAction(PlayerAction action) {
        serverSentEvent(PLAYER_EVENT, action);
    }

}
