package com.neueda.minion.ext.player.sfx;

import com.google.common.collect.ImmutableMap;
import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.Patterns;
import com.neueda.minion.ext.messaging.MessageBus;
import com.neueda.minion.ext.result.ExtensionResult;
import com.neueda.minion.ext.result.ExtensionResultIdle;
import com.neueda.minion.ext.result.ExtensionResultRespond;

import javax.inject.Inject;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerSfxExtension implements Extension {

    private static final Pattern PATTERN = Patterns.preamble("sfx");
    private static final String SFX_RESOURCE_PATH = "com/neueda/minion/web/player/sfx/%s.mp3";
    private static final String SFX_WEB_PATH = "/player/sfx/%s.mp3";
    private static final String SFX_MESSAGE = "com.neueda.minion.ext.player.sfx";

    private final MessageBus messageBus;

    @Inject
    public PlayerSfxExtension(MessageBus messageBus) {
        this.messageBus = messageBus;
    }

    @Override
    public ExtensionResult process(String message) {
        Matcher matcher = PATTERN.matcher(message);
        if (matcher.matches()) {
            String name = matcher.group(1);
            URL resource = getClass().getClassLoader().getResource(String.format(SFX_RESOURCE_PATH, name));
            if (resource != null) {
                messageBus.publish(SFX_MESSAGE, ImmutableMap.<String, Object>builder()
                        .put("path", String.format(SFX_WEB_PATH, name))
                        .build());
            } else {
                return new ExtensionResultRespond("(unknown) Nothing to play named \"" + name + "\"");
            }
        }
        return new ExtensionResultIdle();
    }

}
