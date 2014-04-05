package com.neueda.minion.ext;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

public abstract class ExtensionModule extends AbstractModule {

    private Multibinder<Extension> extensionBinder;
    private Multibinder<WebExtension> webExtensionBinder;

    @Override
    protected void configure() {
        extensionBinder = Multibinder.newSetBinder(binder(), Extension.class);
        webExtensionBinder = Multibinder.newSetBinder(binder(), WebExtension.class);
        configureExtensions();
    }

    protected abstract void configureExtensions();

    protected void registerExtension(Class<? extends Extension> extensionClass) {
        extensionBinder.addBinding()
                .to(extensionClass)
                .in(Scopes.SINGLETON);
    }

    protected void registerWebExtension(WebExtension webExtension) {
        webExtensionBinder.addBinding()
                .toInstance(webExtension);
    }

}
