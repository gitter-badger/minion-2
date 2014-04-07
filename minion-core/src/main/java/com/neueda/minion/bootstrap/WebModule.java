package com.neueda.minion.bootstrap;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.neueda.minion.web.BootstrapServlet;
import com.neueda.minion.web.CommandsEventSourceFactory;
import com.neueda.minion.web.CommandsServlet;
import com.neueda.minion.web.EmbeddedServer;

import javax.inject.Named;

class WebModule extends ServletModule {

    private final ClassLoader extensionClassLoader;

    public WebModule(ClassLoader extensionClassLoader) {
        this.extensionClassLoader = extensionClassLoader;
    }

    @Override
    protected void configureServlets() {
        bind(ClassLoader.class)
                .annotatedWith(Names.named("extensions"))
                .toInstance(extensionClassLoader);
        bind(EmbeddedServer.class).asEagerSingleton();

        install(new FactoryModuleBuilder()
                .build(CommandsEventSourceFactory.class));
        serve("/commands").with(CommandsServlet.class);
        serve("/bootstrap").with(BootstrapServlet.class);
    }

    @Provides
    @Named("internal")
    ObjectMapper providesInternalObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        return objectMapper;
    }

}
