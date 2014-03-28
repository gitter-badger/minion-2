package com.neueda.minion.ext.broadcast;

import com.neueda.minion.ext.ExtensionModule;

public class BroadcastExtensionModule extends ExtensionModule {

    @Override
    protected void configureExtensions() {
        registerExtension(BroadcastExtension.class);
    }

}
