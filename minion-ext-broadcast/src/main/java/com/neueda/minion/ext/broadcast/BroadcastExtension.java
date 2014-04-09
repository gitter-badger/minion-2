package com.neueda.minion.ext.broadcast;

import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.Patterns;
import com.neueda.minion.ext.result.ExtensionResult;
import com.neueda.minion.ext.result.ExtensionResultIdle;
import com.neueda.minion.ext.result.ExtensionResultNotify;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BroadcastExtension implements Extension {

    private static final Pattern PATTERN = Patterns.preamble("good", "news");
    private static final String COLOR = "yellow";
    private static final String PREFIX = "(goodnews) ";

    @Override
    public void initialize() {
    }

    @Override
    public ExtensionResult process(String message) {
        Matcher matcher = PATTERN.matcher(message);
        if (matcher.matches()) {
            String text = matcher.group(1);
            return new ExtensionResultNotify(COLOR, PREFIX + text, true);
        }
        return new ExtensionResultIdle();
    }

}
