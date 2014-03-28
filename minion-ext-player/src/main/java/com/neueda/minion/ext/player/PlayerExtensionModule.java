package com.neueda.minion.ext.player;

import com.neueda.minion.ext.ExtensionModule;

public class PlayerExtensionModule extends ExtensionModule {

    @Override
    protected void configureExtensions() {
        registerExtension(PlayerExtension.class);
    }

}
