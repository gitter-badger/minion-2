package com.neueda.minion.bootstrap;

import com.google.inject.servlet.ServletModule;
import com.neueda.minion.web.CommandsEventSource;
import com.neueda.minion.web.EmbeddedServer;

class WebModule extends ServletModule {

    @Override
    protected void configureServlets() {
        bind(EmbeddedServer.class).asEagerSingleton();
        serve("/commands").with(CommandsEventSource.class);
    }

}
