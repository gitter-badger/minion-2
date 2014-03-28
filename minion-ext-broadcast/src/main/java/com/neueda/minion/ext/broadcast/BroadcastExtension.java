package com.neueda.minion.ext.broadcast;

import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.PatternHelper;
import com.neueda.minion.ext.result.ExtensionResult;
import com.neueda.minion.ext.result.ExtensionResultNotify;
import com.neueda.minion.ext.result.ExtensionResultProceed;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BroadcastExtension implements Extension {

    private static final Pattern PATTERN = PatternHelper.preamble("good", "news");
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
        return new ExtensionResultProceed();
    }

}
