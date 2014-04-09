package com.neueda.minion.ext.player.sfx;

import com.neueda.minion.ext.ExtensionModule;

public class PlayerSfxExtensionModule extends ExtensionModule {

    @Override
    protected void configureExtensions() {
        registerExtension(PlayerSfxExtension.class);
    }

}
