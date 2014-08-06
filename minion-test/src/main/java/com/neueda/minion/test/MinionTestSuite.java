package com.neueda.minion.test;

import com.google.inject.AbstractModule;
import com.netflix.governator.guice.LifecycleInjectorBuilder;
import com.netflix.governator.guice.LifecycleInjectorBuilderSuite;
import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.bootstrap.ConfigurationBootstrapModule;
import com.neueda.minion.ext.messaging.MessageBus;

import java.util.Map;
import java.util.Properties;

public class MinionTestSuite extends AbstractModule implements LifecycleInjectorBuilderSuite {

    private final Class<? extends Extension> extensionClass;
    private final MessageBus messageBus;
    private final Properties properties;

    public MinionTestSuite(Class<? extends Extension> extensionClass,
                           MessageBus messageBus,
                           Map<String, ?> configuration) {
        this.extensionClass = extensionClass;
        this.messageBus = messageBus;
        Properties properties = new Properties();
        properties.putAll(configuration);
        this.properties = properties;
    }

    @Override
    protected void configure() {
        bind(MessageBus.class).toInstance(messageBus);
        bind(extensionClass);
    }

    @Override
    public void configure(LifecycleInjectorBuilder builder) {
        builder.withAdditionalBootstrapModules(new ConfigurationBootstrapModule(properties))
                .withAdditionalModules(this);
    }
}
