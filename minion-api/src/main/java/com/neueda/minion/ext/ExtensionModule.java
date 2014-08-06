package com.neueda.minion.ext;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

import java.util.UUID;

public abstract class ExtensionModule extends AbstractModule {

    private final UUID uuid = UUID.randomUUID();
    private Multibinder<Extension> extensionBinder;
    private Multibinder<WebResource> webResourceBinder;

    public UUID getUUID() {
        return uuid;
    }

    @Override
    protected void configure() {
        extensionBinder = Multibinder.newSetBinder(binder(), Extension.class);
        webResourceBinder = Multibinder.newSetBinder(binder(), WebResource.class);
        configureExtensions();
    }

    protected abstract void configureExtensions();

    protected void registerExtension(Class<? extends Extension> extensionClass) {
        extensionBinder.addBinding()
                .to(extensionClass)
                .in(Scopes.SINGLETON);
    }

    protected void registerWebResource(WebResource webResource) {
        webResource.setUUID(uuid);
        webResourceBinder.addBinding()
                .toInstance(webResource);
    }

}
