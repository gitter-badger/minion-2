package com.neueda.minion.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.neueda.minion.Minion;
import com.neueda.minion.config.AppCfg;
import com.neueda.minion.ext.ExtensionModule;

import javax.inject.Singleton;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ServiceLoader;

@Singleton
public class Application {

    private final Minion minion;

    @Inject
    private Application(Minion minion) {
        this.minion = minion;
    }

    public static void main(String[] args) throws Exception {
        List<AbstractModule> modules = collectModules();
        Injector injector = Guice.createInjector(modules);
        Application application = injector.getInstance(Application.class);
        application.start();
        System.out.println("Up and running! Press <Enter> to quit.");
        System.in.read();
        application.stop();
    }

    private static List<AbstractModule> collectModules() throws IOException {
        CommunicationModule communicationModule = new CommunicationModule();
        ObjectMapper objectMapper = communicationModule.providesYamlObjectMapper();
        AppCfg appCfg = loadConfiguration(objectMapper);
        List<AbstractModule> modules = Lists.newArrayList(
                communicationModule,
                new ConfigurationModule(appCfg),
                new ExecutorModule()
        );
        ServiceLoader.load(ExtensionModule.class).forEach(modules::add);
        return modules;
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
