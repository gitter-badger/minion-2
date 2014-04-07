package com.neueda.minion.ext;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

import java.util.UUID;

public abstract class ExtensionModule extends AbstractModule {

    private final UUID uuid = UUID.randomUUID();
    private Multibinder<Extension> extensionBinder;
    private Multibinder<WebExtension> webExtensionBinder;

    public UUID getUUID() {
        return uuid;
    }

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
        webExtension.setUUID(uuid);
        webExtensionBinder.addBinding()
                .toInstance(webExtension);
    }

}
