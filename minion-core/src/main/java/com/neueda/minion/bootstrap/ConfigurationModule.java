package com.neueda.minion.bootstrap;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Properties;

class ConfigurationModule extends AbstractModule {

    private final Properties configuration;

    public ConfigurationModule(Properties configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    @Named("configuration")
    Properties provideConfigurationProperties() {
        return configuration;
    }

}
