package com.neueda.minion.ext.player;

import com.neueda.minion.ext.ExtensionModule;
import com.neueda.minion.ext.WebExtension;

public class PlayerExtensionModule extends ExtensionModule {

    public static final String WEB_BASE = "com/neueda/minion/web/player";

    @Override
    protected void configureExtensions() {
        registerExtension(PlayerExtension.class);
        registerWebExtension(WebExtension.builder()
                .base(WEB_BASE)
                .contextPath("/player")
                .bootstrap("player/player.js")
                .build());
    }

}
