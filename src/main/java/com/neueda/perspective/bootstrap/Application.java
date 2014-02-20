package com.neueda.perspective.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.neueda.perspective.config.AppCfg;
import com.neueda.perspective.hipchat.HipChat;
import com.neueda.perspective.hipchat.XmppConnector;

import javax.inject.Singleton;
import java.io.IOException;
import java.net.URL;

@Singleton
public class Application {

    private final XmppConnector xmppConnector;
    private final HipChat hipChat;

    @Inject
    private Application(XmppConnector xmppConnector,
                        HipChat hipChat) {
        this.xmppConnector = xmppConnector;
        this.hipChat = hipChat;
    }

    public static void main(String[] args) throws Exception {
        CommunicationModule communicationModule = new CommunicationModule();
        ObjectMapper objectMapper = communicationModule.providesYamlObjectMapper();
        AppCfg appCfg = loadConfiguration(objectMapper);
        Injector injector = Guice.createInjector(
                communicationModule,
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
        hipChat.getRooms();
    }

    private void stop() {
        xmppConnector.shutdown();
    }

}
