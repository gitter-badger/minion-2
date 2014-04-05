package com.neueda.minion.ext.player;

import com.google.common.io.Resources;
import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.PatternHelper;
import com.neueda.minion.ext.player.event.PlayerSoundEffect;
import com.neueda.minion.ext.result.ExtensionResult;
import com.neueda.minion.ext.result.ExtensionResultCommand;
import com.neueda.minion.ext.result.ExtensionResultProceed;
import com.neueda.minion.ext.result.ExtensionResultRespond;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerExtension implements Extension {

    private static final String EVENT = "player";
    private static final String SFX_RESOURCE_PATH = "com/neueda/minion/web/player/sfx/%s.mp3";
    public static final String SFX_WEB_PATH = "/player/sfx/%s.mp3";
    private static final Pattern PATTERN = PatternHelper.preamble("\\w+", "sfx");

    @Override
    public ExtensionResult process(String message) {
        Matcher matcher = PATTERN.matcher(message);
        if (matcher.matches()) {
            String name = matcher.group(1);
            try {
                Resources.getResource(String.format(SFX_RESOURCE_PATH, name));
                PlayerSoundEffect data = new PlayerSoundEffect(String.format(SFX_WEB_PATH, name));
                return new ExtensionResultCommand(EVENT, data);
            } catch (IllegalArgumentException e) {
                return new ExtensionResultRespond("(unknown) No SFX named \"" + name + "\"");
            }
        }
        return new ExtensionResultProceed();
    }

}
