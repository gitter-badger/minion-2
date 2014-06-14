package com.neueda.minion.ext.player.sfx;

import com.neueda.minion.ext.ExtensionModule;
import com.neueda.minion.ext.WebResource;

public class PlayerSfxExtensionModule extends ExtensionModule {

    @Override
    protected void configureExtensions() {
        registerExtension(PlayerSfxExtension.class);
        registerWebResource(WebResource.builder()
                .resourceRoot("com/neueda/minion/web/player/sfx")
                .contextPath("/player/sfx/core")
                .build());
        registerWebResource(WebResource.builder()
                .folder("sfx")
                .contextPath("/player/sfx/custom")
                .build());
    }

}
