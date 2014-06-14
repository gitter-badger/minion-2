package com.neueda.minion.bootstrap;

import com.netflix.governator.configuration.PropertiesConfigurationProvider;
import com.netflix.governator.guice.BootstrapBinder;
import com.netflix.governator.guice.BootstrapModule;

import java.util.Properties;

class ConfigurationBootstrapModule implements BootstrapModule {

    private final Properties properties;

    ConfigurationBootstrapModule(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void configure(BootstrapBinder binder) {
        binder.bindConfigurationProvider()
                .toInstance(new PropertiesConfigurationProvider(properties));
    }

}
