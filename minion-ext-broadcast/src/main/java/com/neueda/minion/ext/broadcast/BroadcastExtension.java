package com.neueda.minion.ext.broadcast;

import com.netflix.governator.annotations.Configuration;
import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.HipChatMessage;
import com.neueda.minion.ext.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BroadcastExtension extends Extension {

    private static final Pattern PATTERN = Patterns.preamble("good", "news");
    private static final String COLOR = "yellow";

    @Configuration("ext.broadcast.prefix")
    private String prefix = "(goodnews)";

    @Override
    public void initialize() {
        onHipChatMessage(this::handleMessage);
    }

    private void handleMessage(HipChatMessage message) {
        String body = message.getBody();
        Matcher matcher = PATTERN.matcher(body);
        if (matcher.matches()) {
            String text = matcher.group(1);
            hipChatNotification(COLOR, prefix + " " + text, true);
        }
    }

}
