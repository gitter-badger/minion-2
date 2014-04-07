package com.neueda.minion.bootstrap;

import com.google.common.base.Throwables;
import com.google.inject.Module;
import com.neueda.minion.ext.ExtensionModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Singleton
public class ExtensionLoader {

    private final Logger logger = LoggerFactory.getLogger(ExtensionLoader.class);
    private final List<Module> modules;
    private final Map<UUID, ClassLoader> classLoaders;

    public ExtensionLoader() {
        modules = new ArrayList<>();
        classLoaders = new HashMap<>();
    }

    public void loadExtensions(Path root) throws IOException {
        logger.info("Looking for extensions");
        Files.find(root, 1, (path, attrs) -> {
            return attrs.isRegularFile() && path.toString().endsWith(".jar");
        }, FileVisitOption.FOLLOW_LINKS).forEach(this::loadExtension);
    }

    private void loadExtension(Path path) {
        Path normalPath = path.normalize();
        logger.info("Loading extensions from: {}", normalPath.toFile());
        URL url;
        try {
            url = normalPath.toUri().toURL();
        } catch (MalformedURLException e) {
            throw Throwables.propagate(e);
        }
        URLClassLoader extensionClassLoader = URLClassLoader.newInstance(new URL[]{url});
        ServiceLoader.load(ExtensionModule.class, extensionClassLoader).forEach(module -> {
            logger.info("Found extension module: {}", module.getClass());
            modules.add(module);
            classLoaders.put(module.getUUID(), extensionClassLoader);
        });
    }

    public List<Module> getModules() {
        return modules;
    }

    public Map<UUID, ClassLoader> getClassLoaders() {
        return classLoaders;
    }

}
