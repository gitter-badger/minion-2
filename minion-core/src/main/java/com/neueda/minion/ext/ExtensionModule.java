package com.neueda.minion.ext;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

public abstract class ExtensionModule extends AbstractModule {

    private Multibinder<Extension> extensionBinder;

    @Override
    protected void configure() {
        extensionBinder = Multibinder.newSetBinder(binder(), Extension.class);
        configureExtensions();
    }

    protected abstract void configureExtensions();

    protected void registerExtension(Class<? extends Extension> extensionClass) {
        extensionBinder.addBinding()
                .to(extensionClass)
                .in(Scopes.SINGLETON);
    }

}
