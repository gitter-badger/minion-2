package com.neueda.minion.ext.quotes;

import com.neueda.minion.ext.ExtensionModule;

public class QuotesExtensionModule extends ExtensionModule {

    @Override
    protected void configureExtensions() {
        registerExtension(QuotesExtension.class);
    }

}
