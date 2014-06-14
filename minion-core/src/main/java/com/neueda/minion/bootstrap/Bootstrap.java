package com.neueda.minion.bootstrap;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.netflix.governator.guice.LifecycleInjector;
import com.netflix.governator.lifecycle.LifecycleManager;
import com.neueda.minion.application.Minion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public final class Bootstrap {

    private final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
    private Injector injector;
    private LifecycleManager lifecycleManager;

    public static void main(String[] args) throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.start();
            System.out.println("Up and running; press <Enter> to quit");
            System.in.read();
        } catch (Exception ignored) {
        }
        bootstrap.stop();
    }

    public void start() {
        try {
            tryStart();
        } catch (Exception e) {
            logger.error("Bootstrap failed", e);
            throw Throwables.propagate(e);
        }
    }

    private void tryStart() throws Exception {
        logger.info("Starting up Neueda Minion");
        Properties configuration = loadConfiguration();

        Path root = FileSystems.getDefault().getPath(".").toAbsolutePath();
        ExtensionLoader extensionLoader = new ExtensionLoader();
        extensionLoader.loadExtensions(root);
        List<Module> extensionModules = extensionLoader.getModules();
        Map<UUID, ClassLoader> extensionClassLoaders = extensionLoader.getClassLoaders();

        List<Module> modules = Lists.newArrayList(
                new ConfigurationModule(configuration),
                new ExtensionBootstrapModule(extensionClassLoaders),
                new CommunicationModule(),
                new ExecutorModule(),
                new WebModule()
        );
        modules.addAll(extensionModules);

        injector = LifecycleInjector.builder()
                .withBootstrapModule(new ConfigurationBootstrapModule(configuration))
                .withModules(modules)
                .build()
                .createInjector();
        injector.getInstance(Minion.class);

        lifecycleManager = injector.getInstance(LifecycleManager.class);
        lifecycleManager.start();
    }

    public void stop() {
        logger.info("Shutting down");
        lifecycleManager.close();
        injector = null;
    }

    private static Properties loadConfiguration() throws IOException {
        File configFile = new File("minion.properties");
        if (!configFile.exists()) {
            throw new RuntimeException("Configuration file not found: " + configFile.getAbsolutePath());
        }
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(configFile)) {
            properties.load(is);
        }
        return properties;
    }


}
