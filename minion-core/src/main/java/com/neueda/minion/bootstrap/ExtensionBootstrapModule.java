package com.neueda.minion.bootstrap;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.WebResource;

import javax.inject.Named;
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
        Multibinder.newSetBinder(binder(), WebResource.class);
    }

    @Provides
    @Named("extension")
    Map<UUID, ClassLoader> provideExtensionClassLoaders() {
        return classLoaders;
    }

}
