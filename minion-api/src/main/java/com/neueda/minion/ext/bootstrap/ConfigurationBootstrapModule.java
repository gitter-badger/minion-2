package com.neueda.minion.ext.bootstrap;

import com.netflix.governator.configuration.PropertiesConfigurationProvider;
import com.netflix.governator.guice.BootstrapBinder;
import com.netflix.governator.guice.BootstrapModule;

import java.util.Properties;

public class ConfigurationBootstrapModule implements BootstrapModule {

    private final Properties properties;

    public ConfigurationBootstrapModule(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void configure(BootstrapBinder binder) {
        binder.bindConfigurationProvider()
                .toInstance(new PropertiesConfigurationProvider(properties));
    }

}
