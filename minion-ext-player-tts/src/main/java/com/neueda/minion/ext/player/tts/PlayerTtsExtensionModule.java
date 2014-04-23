package com.neueda.minion.ext.player.tts;

import com.neueda.minion.ext.ExtensionModule;

public class PlayerTtsExtensionModule extends ExtensionModule {

    @Override
    protected void configureExtensions() {
        registerExtension(PlayerTtsExtension.class);
    }

}
