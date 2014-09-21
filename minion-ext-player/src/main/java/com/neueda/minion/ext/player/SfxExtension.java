package com.neueda.minion.ext.player;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.HipChatMessage;
import com.neueda.minion.ext.Patterns;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.neueda.minion.ext.messaging.MessageBus.dataBuilder;

public class SfxExtension extends Extension {

    private static final Pattern PATTERN = Patterns.preamble("sfx");
    private static final String SFX_RESOURCE_PATH = "com/neueda/minion/web/player/sfx/%s.mp3";
    private static final String SFX_FILE_PATH = "sfx/%s.mp3";
    private static final String SFX_WEB_PATH = "/player/sfx/%s/%s.mp3";

    @Override
    public void initialize() {
        onHipChatMessage(this::handleMessage);
    }

    private void handleMessage(HipChatMessage message) {
        String body = message.getBody();
        Matcher matcher = PATTERN.matcher(body);
        if (matcher.matches()) {
            String name = matcher.group(1);
            String sender = message.getSender();
            if (name.equals("?")) {
                hipChatReply(sender, "(doge) such sfx: " + Joiner.on(", ").join(listSfx()));
            } else {
                String type = locateSfx(name);
                if (type != null) {
                    messageBus.publish(Messages.SFX_MESSAGE, dataBuilder()
                            .put("path", String.format(SFX_WEB_PATH, type, name))
                            .put("cached", true)
                            .build());
                } else {
                    hipChatReply(sender, "(doge) no sfx named \"" + name + "\"");
                }
            }
        }
    }

    private List<String> listSfx() {
        List<String> sfxList = Lists.newArrayList(
                "applause",
                "evil",
                "gong",
                "rimshot",
                "trombone"
        );
        File[] files = new File("sfx").listFiles();
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                if (file.isFile() && name.endsWith(".mp3")) {
                    sfxList.add(name.substring(0, name.length() - 4));
                }
            }
        }
        Collections.sort(sfxList);
        return sfxList;
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
