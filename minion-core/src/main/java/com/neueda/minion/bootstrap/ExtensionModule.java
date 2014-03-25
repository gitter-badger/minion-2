package com.neueda.minion.bootstrap;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.neueda.minion.ext.ExtensionLoaderFactory;

class ExtensionModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .build(ExtensionLoaderFactory.class));
    }

}
