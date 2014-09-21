package com.neueda.minion.ext.scripting;

import com.google.common.base.Charsets;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.HipChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map;

public class ScriptingExtension extends Extension {

    private final Logger logger = LoggerFactory.getLogger(ScriptingExtension.class);
    private Multimap<ScriptEngine, String> scripts;

    @Override
    public void initialize() throws IOException {
        Map<String, ScriptEngine> engines = loadEngines();
        scripts = loadScripts(engines);
        onHipChatMessage(this::handleMessage);
    }

    private Map<String, ScriptEngine> loadEngines() {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        Map<String, ScriptEngine> engines = Maps.newHashMap();
        for (ScriptEngineFactory factory : engineManager.getEngineFactories()) {
            ScriptEngine engine = factory.getScriptEngine();
            logger.info("Language support: {} {}", factory.getLanguageName(), factory.getLanguageVersion());
            for (String extension : factory.getExtensions()) {
                engines.put(extension, engine);
            }
        }
        return engines;
    }

    private Multimap<ScriptEngine, String> loadScripts(Map<String, ScriptEngine> engines) throws IOException {
        Multimap<ScriptEngine, String> scripts = HashMultimap.create();
        Path root = FileSystems.getDefault().getPath("scripts").toAbsolutePath();
        if (!root.toFile().exists()) {
            logger.warn("Directory \"scripts\" not found");
            return scripts;
        }
        java.nio.file.Files.walk(root, 1).forEach(path -> {
            File file = path.toFile();
            if (file.isFile()) {
                String name = file.getName();
                String extension = Files.getFileExtension(name);
                ScriptEngine engine = engines.get(extension);
                if (engine != null) {
                    try {
                        String script = Files.toString(file, Charsets.UTF_8);
                        scripts.put(engine, script);
                        logger.info("Loaded script: {}", file.getAbsolutePath());
                    } catch (IOException ignored) {
                    }
                }
            }
        });
        return scripts;
    }

    private void handleMessage(HipChatMessage message) {
        for (Map.Entry<ScriptEngine, String> entry : scripts.entries()) {
            ScriptEngine engine = entry.getKey();
            String script = entry.getValue();
            try {
                SimpleBindings bindings = new SimpleBindings();
                bindings.put("from", message.getFrom());
                bindings.put("message", message.getBody());
                bindings.put("minion", new MinionScriptWrapper(messageBus, message));
                engine.eval(script, bindings);
            } catch (ScriptException e) {
                logger.error("Script failed", e);
            }
        }
    }

}
