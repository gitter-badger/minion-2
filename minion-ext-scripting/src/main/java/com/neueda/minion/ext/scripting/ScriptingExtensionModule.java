package com.neueda.minion.ext.scripting;

import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.neueda.minion.ext.ExtensionModule;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class ScriptingExtensionModule extends ExtensionModule {

    @Override
    protected void configureExtensions() {
        install(new FactoryModuleBuilder()
                .build(MinionScriptWrapperFactory.class));

        registerExtension(ScriptingExtension.class);
    }

    @Provides
    @Named("com.neueda.minion.ext.scripting.httpClient")
    Client provideHttpClient() {
        return ClientBuilder.newClient();
    }

}
