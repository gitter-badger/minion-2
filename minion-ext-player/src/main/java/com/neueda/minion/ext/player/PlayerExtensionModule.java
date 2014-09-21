package com.neueda.minion.ext.player;

import com.neueda.minion.ext.ExtensionModule;
import com.neueda.minion.ext.WebResource;

public class PlayerExtensionModule extends ExtensionModule {

    @Override
    protected void configureExtensions() {
        registerExtension(PlayerExtension.class);

        registerExtension(RadioExtension.class);
        registerExtension(SfxExtension.class);
        registerExtension(TtsExtension.class);

        registerWebResource(WebResource.builder()
                .resourceRoot("com/neueda/minion/web/player")
                .contextPath("/player")
                .bootstrap("player/player.js")
                .build());
    }

}
