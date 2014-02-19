package com.neueda.perspective.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.neueda.perspective.config.AppCfg;
import com.neueda.perspective.xmpp.XmppConnector;

import javax.inject.Singleton;
import java.io.IOException;
import java.net.URL;

@Singleton
public class Application {

    private final XmppConnector xmppConnector;

    @Inject
    private Application(XmppConnector xmppConnector) {
        this.xmppConnector = xmppConnector;
    }

    public static void main(String[] args) throws Exception {
        SerializationModule serializationModule = new SerializationModule();
        ObjectMapper objectMapper = serializationModule.providesYamlObjectMapper();
        AppCfg appCfg = loadConfiguration(objectMapper);
        Injector injector = Guice.createInjector(
                serializationModule,
                new ConfigurationModule(appCfg),
                new ExecutorModule()
        );
        Application application = injector.getInstance(Application.class);
        application.start();
        System.in.read();
        application.stop();
    }

    private static AppCfg loadConfiguration(ObjectMapper objectMapper) throws IOException {
        URL resource = Resources.getResource("com/neueda/perspective/config/app.yaml");
        return objectMapper.readValue(resource, AppCfg.class);
    }

    private void start() {
        xmppConnector.connect();
    }

    private void stop() {
        xmppConnector.shutdown();
    }

}
