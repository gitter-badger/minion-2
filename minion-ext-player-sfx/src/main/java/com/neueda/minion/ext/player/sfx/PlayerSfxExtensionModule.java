package com.neueda.minion.ext.player.sfx;

import com.neueda.minion.ext.ExtensionModule;
import com.neueda.minion.ext.WebResource;

public class PlayerSfxExtensionModule extends ExtensionModule {

    public static final String WEB_BASE = "com/neueda/minion/web/player/sfx";

    @Override
    protected void configureExtensions() {
        registerExtension(PlayerSfxExtension.class);
        registerWebResource(WebResource.builder()
                .base(WEB_BASE)
                .contextPath("/player/sfx")
                .build());
    }

}
