package com.neueda.perspective.bot.ext.standard;

import com.neueda.perspective.bot.ext.Extension;
import com.neueda.perspective.bot.ext.PatternHelper;
import com.neueda.perspective.bot.ext.result.ExtensionResult;
import com.neueda.perspective.bot.ext.result.ExtensionResultNotify;
import com.neueda.perspective.bot.ext.result.ExtensionResultProceed;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BroadcastExtension implements Extension {

    private static final String NAME = "broadcast";
    private static final Pattern PATTERN = PatternHelper.preamble("good", "news");
    private static final String COLOR = "yellow";
    private static final String PREFIX = "(goodnews) ";

    @Override
    public String name() {
        return NAME;
    }

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
        return new ExtensionResultProceed();
    }

}
