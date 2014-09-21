package com.neueda.minion.ext.player;

import com.google.common.collect.ImmutableMap;
import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.HipChatMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.neueda.minion.ext.messaging.MessageBus.dataBuilder;

public class TtsExtension extends Extension {

    private static final Pattern PATTERN = Pattern.compile("say\\s*(?:,\\s*([^:]+)\\s*)?:\\s*(.+)\\s*");

    @Override
    public void initialize() {
        onHipChatMessage(this::handleMessage);
    }

    private void handleMessage(HipChatMessage message) {
        String body = message.getBody();
        Matcher matcher = PATTERN.matcher(body);
        if (matcher.matches()) {
            String voice = matcher.group(1);
            String text = matcher.group(2);
            ImmutableMap.Builder<String, Object> builder = dataBuilder().put("text", text);
            if (voice != null) {
                builder.put("voice", voice.toLowerCase());
            }
            messageBus.publish(Messages.TTS_MESSAGE, builder.build());
        }
    }

}
