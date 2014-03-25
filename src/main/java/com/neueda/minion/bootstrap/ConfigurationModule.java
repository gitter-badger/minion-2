package com.neueda.minion.bootstrap;

import com.google.inject.AbstractModule;
import com.neueda.minion.config.AppCfg;

class ConfigurationModule extends AbstractModule {

    private final AppCfg appCfg;

    public ConfigurationModule(AppCfg appCfg) {
        this.appCfg = appCfg;
    }

    @Override
    protected void configure() {
        bind(AppCfg.class).toInstance(appCfg);
    }

}
