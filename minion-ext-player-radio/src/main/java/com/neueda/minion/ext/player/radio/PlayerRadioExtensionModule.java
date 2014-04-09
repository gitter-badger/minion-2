package com.neueda.minion.ext.player.radio;

import com.neueda.minion.ext.ExtensionModule;

public class PlayerRadioExtensionModule extends ExtensionModule {

    @Override
    protected void configureExtensions() {
        registerExtension(PlayerRadioExtension.class);
    }

}
