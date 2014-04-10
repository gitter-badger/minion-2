package com.neueda.minion.ext.player;

import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.Patterns;
import com.neueda.minion.ext.messaging.MessageBus;
import com.neueda.minion.ext.player.event.PlayerAction;
import com.neueda.minion.ext.player.event.PlayerSoundEffect;
import com.neueda.minion.ext.player.event.PlayerStop;
import com.neueda.minion.ext.player.event.PlayerStream;
import com.neueda.minion.ext.result.ExtensionResult;
import com.neueda.minion.ext.result.ExtensionResultCommand;
import com.neueda.minion.ext.result.ExtensionResultIdle;

import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerExtension implements Extension {

    private static final String PLAYER_EVENT = "com.neueda.minion.ext.player";
    private static final String STREAM_MESSAGE = "com.neueda.minion.ext.player.stream";
    private static final String SFX_MESSAGE = "com.neueda.minion.ext.player.sfx";

    private static final Pattern STOP_PATTERN = Patterns.sentence("stop!");

    private final MessageBus messageBus;

    @Inject
    public PlayerExtension(MessageBus messageBus) {
        this.messageBus = messageBus;
    }

    @Override
    public void initialize() {
        messageBus.subscribe(STREAM_MESSAGE, this::processAction, new PlayerStream.Reader());
        messageBus.subscribe(SFX_MESSAGE, this::processAction, new PlayerSoundEffect.Reader());
    }

    @Override
    public ExtensionResult process(String message) {
        Matcher matcher = STOP_PATTERN.matcher(message);
        if (matcher.matches()) {
            return new ExtensionResultCommand(PLAYER_EVENT, new PlayerStop());
        } else {
            return new ExtensionResultIdle();
        }
    }

    private ExtensionResult processAction(PlayerAction action) {
        return new ExtensionResultCommand(PLAYER_EVENT, action);
    }

}
