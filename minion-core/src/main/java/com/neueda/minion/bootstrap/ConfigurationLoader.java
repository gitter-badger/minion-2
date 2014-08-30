package com.neueda.minion.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class ConfigurationLoader {

    public static final String MINION_ENV_PREFIX = "MINION_";

    private final Logger logger = LoggerFactory.getLogger(ConfigurationLoader.class);
    private final Properties properties;

    public ConfigurationLoader() {
        properties = new Properties();
    }

    public Properties getProperties() {
        return properties;
    }

    public void loadFromFile(File file) throws IOException {
        if (file.exists()) {
            try (InputStream is = new FileInputStream(file)) {
                properties.load(is);
            }
            logger.info("Loaded settings from {}", file.getAbsolutePath());
        } else {
            logger.warn("Configuration file not found: " + file.getAbsolutePath());
        }
    }

    public void loadFromEnv() {
        Map<String, String> env = System.getenv();
        int prefixLength = MINION_ENV_PREFIX.length();
        for (Map.Entry<String, String> entry : env.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(MINION_ENV_PREFIX)) {
                String minionKey = key.substring(prefixLength);
                properties.setProperty(minionKey, entry.getValue());
                logger.info("Loaded from ENV: {}", minionKey);
            }
        }
    }

}
