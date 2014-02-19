package com.neueda.perspective.bootstrap;

import com.google.inject.AbstractModule;
import com.neueda.perspective.config.AppCfg;

class ConfigurationModule extends AbstractModule {

    private final AppCfg app;

    public ConfigurationModule(AppCfg app) {
        this.app = app;
    }

    @Override
    protected void configure() {
        bind(AppCfg.class).toInstance(app);
    }

}
