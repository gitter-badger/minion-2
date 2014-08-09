package com.neueda.minion.ext.lmgtfy;

import com.netflix.governator.annotations.Configuration;
import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.HipChatMessage;
import com.neueda.minion.ext.Patterns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LmgtfyExtension extends Extension {
    private static final Pattern PATTERN = Patterns.preamble("lmgtfy");
    private final Logger logger = LoggerFactory.getLogger(LmgtfyExtension.class);

    @Configuration("ext.lmgtfy.prefix")
    private String prefix = "(bunny)";

    @Override
    public void initialize() {
        onHipChatMessage(this::handleMessage);
    }

    private void handleMessage(HipChatMessage message) {
        String body = message.getBody();

        Matcher matcher = PATTERN.matcher(body);
        if (matcher.matches()) {
            String subject = matcher.group(1);
            String sender = message.getSender();
            hipChatReply(sender, prefix + " " + createLmgtfyLink(subject));
        }
    }

    private String createLmgtfyLink(String subject) {
        return "http://lmgtfy.com/?q=" + prepareSubject(subject);
    }

    private String prepareSubject(String subject) {
        try {
            return URLEncoder.encode(subject, "UTF-8");
        } catch (Exception e) {
            logger.error("Failed to urlencode: '" + subject);
            return subject;
        }
    }
}
