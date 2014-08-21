package com.neueda.minion.ext.quotes;

import com.netflix.governator.annotations.Configuration;
import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.HipChatMessage;

public class QuotesExtension extends Extension {

    private static final String PATTERN = "quote";
    private static final String COLOR = "yellow";
    private QuotesGenerator quotesGenerator;

    @Configuration("ext.quotes.prefix")
    private String prefix = "(jobs)";

    @Override
    public void initialize() {
        onHipChatMessage(this::handleMessage);
        quotesGenerator = new QuotesGenerator();
    }

    private void handleMessage(HipChatMessage message) {
        String body = message.getBody();
        if (body.contains(PATTERN)) {
            hipChatNotification(COLOR, prefix + " " + quotesGenerator.getRandom(), true);
        }
    }

}
