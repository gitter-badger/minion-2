package com.neueda.perspective.bootstrap;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.neueda.perspective.bot.ext.ExtensionLoaderFactory;

public class ExtensionModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .build(ExtensionLoaderFactory.class));
    }

}
