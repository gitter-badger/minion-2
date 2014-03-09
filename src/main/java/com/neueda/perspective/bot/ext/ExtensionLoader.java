package com.neueda.perspective.bot.ext;

import com.neueda.perspective.config.AppCfg;

import java.util.HashMap;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class ExtensionLoader {

    private final List<String> ext;
    private ServiceLoader<Extension> loader;

    public ExtensionLoader(AppCfg cfg) {
        ext = cfg.getBot().getExt();
        this.loader = ServiceLoader.load(Extension.class);
    }

    public List<Extension> load() {
        HashMap<String, Extension> extensions = new HashMap<>();
        loader.iterator().forEachRemaining(extension -> {
            extensions.put(extension.name(), extension);
        });
        return ext.stream()
                .<Extension>map(extensions::get)
                .filter(e -> e != null)
                .collect(Collectors.toList());
    }

}
