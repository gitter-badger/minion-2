package com.neueda.minion.ext.standard;

import com.neueda.minion.ext.ExtensionModule;

public class StandardExtensionModule extends ExtensionModule {

    @Override
    protected void configureExtensions() {
        registerExtension(BroadcastExtension.class);
        registerExtension(GongExtension.class);
    }

}
