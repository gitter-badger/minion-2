package com.neueda.minion.ext.lmgtfy;

import com.neueda.minion.ext.ExtensionModule;

public class LmgtfyExtensionModule extends ExtensionModule {
    @Override
    protected void configureExtensions() {
        registerExtension(LmgtfyExtension.class);
    }
}
