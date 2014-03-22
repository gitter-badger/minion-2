package com.neueda.perspective.bot.ext;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import java.util.HashMap;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class ExtensionLoader {

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
            extensionMap.put(extension.name(), extension);
        });
        return extensions.stream()
                .<Extension>map(extensionMap::get)
                .filter(e -> e != null)
                .collect(Collectors.toList());
    }

}
