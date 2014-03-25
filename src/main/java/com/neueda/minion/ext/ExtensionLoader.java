package com.neueda.minion.ext;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class ExtensionLoader {

    private final Logger logger = LoggerFactory.getLogger(ExtensionLoader.class);
    private final List<String> extensions;
    private ServiceLoader<Extension> loader;

    @Inject
    public ExtensionLoader(@Assisted List<String> extensions) {
        this.extensions = extensions;
        this.loader = ServiceLoader.load(Extension.class);
    }

    public List<Extension> load() {
        HashMap<String, Extension> extensionMap = new HashMap<>();
        loader.iterator().forEachRemaining(extension -> {
            String name = extension.name();
            logger.info("Found extension: {}", name);
            extensionMap.put(name, extension);
        });
        return extensions.stream()
                .<Extension>map(extensionMap::get)
                .filter(e -> e != null)
                .collect(Collectors.toList());
    }

}
