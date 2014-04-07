package com.neueda.minion.bootstrap;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.netflix.governator.guice.LifecycleInjector;
import com.netflix.governator.lifecycle.LifecycleManager;
import com.neueda.minion.application.Minion;
import com.neueda.minion.ext.ExtensionModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;

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
        ConfigurationModule configurationModule = loadConfiguration();
        List<AbstractModule> modules = collectModules();
        injector = LifecycleInjector.builder()
                .withBootstrapModule(configurationModule)
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

    private static ConfigurationModule loadConfiguration() throws IOException {
        File configFile = new File("minion.properties");
        if (!configFile.exists()) {
            throw new RuntimeException("Configuration file not found: " + configFile.getAbsolutePath());
        }
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(configFile)) {
            properties.load(is);
        }
        return new ConfigurationModule(properties);
    }

    private List<AbstractModule> collectModules() throws IOException {
        Path root = FileSystems.getDefault().getPath(".").toAbsolutePath();
        logger.info("Looking for extensions at {}", root);
        ClassLoader extensionClassLoader = localJarClassLoader(root);
        List<AbstractModule> modules = Lists.newArrayList(
                new CommunicationModule(),
                new ExecutorModule(),
                new WebModule(extensionClassLoader)
        );
        ServiceLoader.load(ExtensionModule.class, extensionClassLoader).forEach(module -> {
            logger.info("Found extension: {}", module.getClass());
            modules.add(module);
        });
        return modules;
    }

    private ClassLoader localJarClassLoader(Path root) throws IOException {
        URL[] urls = Files.find(root, 1, (path, attrs) -> {
            return attrs.isRegularFile() && path.toString().endsWith(".jar");
        }, FileVisitOption.FOLLOW_LINKS).map(path -> {
            try {
                logger.info("Found JAR: {}", path.getFileName());
                return path.normalize().toUri().toURL();
            } catch (MalformedURLException e) {
                throw Throwables.propagate(e);
            }
        }).toArray(URL[]::new);
        return URLClassLoader.newInstance(urls);
    }

}
