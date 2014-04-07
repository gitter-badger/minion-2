package com.neueda.minion.bootstrap;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.WebExtension;

import java.util.Map;
import java.util.UUID;

public class ExtensionBootstrapModule extends AbstractModule {

    private final Map<UUID, ClassLoader> classLoaders;

    public ExtensionBootstrapModule(Map<UUID, ClassLoader> classLoaders) {
        this.classLoaders = classLoaders;
    }

    @Override
    protected void configure() {
        Multibinder.newSetBinder(binder(), Extension.class);
        Multibinder.newSetBinder(binder(), WebExtension.class);
        bind(new TypeLiteral<Map<UUID, ClassLoader>>() {
        })
                .annotatedWith(Names.named("extension"))
                .toInstance(classLoaders);
    }

}
