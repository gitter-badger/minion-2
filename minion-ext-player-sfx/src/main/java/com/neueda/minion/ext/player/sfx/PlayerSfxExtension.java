package com.neueda.minion.ext.player.sfx;

import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.HipChatMessage;
import com.neueda.minion.ext.Patterns;

import java.io.File;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.neueda.minion.ext.messaging.MessageBus.dataBuilder;

public class PlayerSfxExtension extends Extension {

    private static final Pattern PATTERN = Patterns.preamble("sfx");
    private static final String SFX_RESOURCE_PATH = "com/neueda/minion/web/player/sfx/%s.mp3";
    private static final String SFX_FILE_PATH = "sfx/%s.mp3";
    private static final String SFX_WEB_PATH = "/player/sfx/%s/%s.mp3";
    private static final String SFX_MESSAGE = "com.neueda.minion.ext.player.sfx";

    @Override
    public void initialize() {
        onHipChatMessage(this::handleMessage);
    }

    private void handleMessage(HipChatMessage message) {
        String body = message.getBody();
        Matcher matcher = PATTERN.matcher(body);
        if (matcher.matches()) {
            String name = matcher.group(1);
            String type = locateSfx(name);
            if (type != null) {
                messageBus.publish(SFX_MESSAGE, dataBuilder()
                        .put("path", String.format(SFX_WEB_PATH, type, name))
                        .put("cached", true)
                        .build());
            } else {
                String sender = message.getSender();
                hipChatReply(sender, "(unknown) Nothing to play named \"" + name + "\"");
            }
        }
    }

    private String locateSfx(String name) {
        if (new File(String.format(SFX_FILE_PATH, name)).isFile()) {
            return "custom";
        }
        URL resource = getClass().getClassLoader()
                .getResource(String.format(SFX_RESOURCE_PATH, name));
        if (resource != null) {
            return "core";
        }
        return null;
    }

}
