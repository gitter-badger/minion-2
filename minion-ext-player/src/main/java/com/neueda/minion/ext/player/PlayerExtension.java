package com.neueda.minion.ext.player;

import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.result.ExtensionResult;
import com.neueda.minion.ext.result.ExtensionResultCommand;
import com.neueda.minion.ext.result.ExtensionResultProceed;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerExtension implements Extension {

    private static final String EVENT = "player";
    private static final String MP3 = "player/gong.mp3";
    private static final Pattern PATTERN = Pattern.compile("\\s*[Ff]{2,}[Uu]{3,}.*");

    @Override
    public ExtensionResult process(String message) {
        Matcher matcher = PATTERN.matcher(message);
        if (matcher.matches()) {
            return new ExtensionResultCommand(EVENT, MP3);
        }
        return new ExtensionResultProceed();
    }

}
