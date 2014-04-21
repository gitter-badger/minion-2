package com.neueda.minion.ext.player.radio;

import com.google.common.collect.ImmutableMap;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.HipChatMessage;
import com.neueda.minion.ext.Patterns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerRadioExtension extends Extension {

    private static final String DEFAULT_STREAM_FORMAT = "mp3";
    private static final Pattern PATTERN = Patterns.preamble("play");
    private static final String STREAM_MESSAGE = "com.neueda.minion.ext.player.stream";

    private final Logger logger = LoggerFactory.getLogger(PlayerRadioExtension.class);
    private final Map<String, Map<String, Object>> streams = new HashMap<>();

    @Override
    public void initialize() {
        File streamsFile = new File("streams.csv");
        FileReader fileReader;
        try {
            fileReader = new FileReader(streamsFile);
        } catch (FileNotFoundException e) {
            logger.warn("Streams file not found: " + streamsFile.getAbsolutePath());
            return;
        }
        CSVReader<String[]> csvReader = CSVReaderBuilder.newDefaultReader(fileReader);
        csvReader.forEach(line -> {
            String name = line[0];
            String url = line[1];
            String format = line.length > 2 ? line[2] : DEFAULT_STREAM_FORMAT;
            logger.info("Registered live stream \"{}\": {}", name, url);
            streams.put(name, ImmutableMap.<String, Object>builder()
                    .put("url", url)
                    .put("format", format)
                    .build());
        });
        onHipChatMessage(this::handleMessage);
    }

    private void handleMessage(HipChatMessage message) {
        String body = message.getBody();
        Matcher matcher = PATTERN.matcher(body);
        if (matcher.matches()) {
            String name = matcher.group(1);
            Map<String, Object> stream = streams.get(name);
            if (stream != null) {
                messageBus.publish(STREAM_MESSAGE, stream);
            }
        }
    }

}
