package com.neueda.minion.ext.scripting;

import com.neueda.minion.ext.ExtensionModule;

public class ScriptingExtensionModule extends ExtensionModule {

    @Override
    protected void configureExtensions() {
        registerExtension(ScriptingExtension.class);
    }

}
