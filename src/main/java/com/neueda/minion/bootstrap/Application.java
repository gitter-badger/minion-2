package com.neueda.minion.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.neueda.minion.Minion;
import com.neueda.minion.config.AppCfg;

import javax.inject.Singleton;
import java.io.IOException;
import java.net.URL;

@Singleton
public class Application {

    private final Minion minion;

    @Inject
    private Application(Minion minion) {
        this.minion = minion;
    }

    public static void main(String[] args) throws Exception {
        CommunicationModule communicationModule = new CommunicationModule();
        ObjectMapper objectMapper = communicationModule.providesYamlObjectMapper();
        AppCfg appCfg = loadConfiguration(objectMapper);
        Injector injector = Guice.createInjector(
                communicationModule,
                new ConfigurationModule(appCfg),
                new ExtensionModule(),
                new ExecutorModule()
        );
        Application application = injector.getInstance(Application.class);
        application.start();
        System.out.println("Up and running! Press <Enter> to quit.");
        System.in.read();
        application.stop();
    }

    private static AppCfg loadConfiguration(ObjectMapper objectMapper) throws IOException {
        URL resource = Resources.getResource("com/neueda/minion/config/app.yaml");
        return objectMapper.readValue(resource, AppCfg.class);
    }

    private void start() {
        minion.start();
    }

    private void stop() {
        minion.shutdown();
    }

}
