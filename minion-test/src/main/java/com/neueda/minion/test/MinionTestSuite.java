package com.neueda.minion.test;

import com.netflix.governator.guice.SimpleLifecycleInjectorBuilderSuite;
import com.neueda.minion.bootstrap.ConfigurationModule;
import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.messaging.MessageBus;

import java.util.Properties;

public class MinionTestSuite extends SimpleLifecycleInjectorBuilderSuite {

    private final Class<? extends Extension> extensionClass;
    private final MessageBus messageBus;

    public MinionTestSuite(Class<? extends Extension> extensionClass, MessageBus messageBus) {
        this.extensionClass = extensionClass;
        this.messageBus = messageBus;
    }

    @Override
    protected void configure() {
        install(new ConfigurationModule(new Properties()));
        bind(MessageBus.class).toInstance(messageBus);
        bind(extensionClass);
    }

}
