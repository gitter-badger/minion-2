package com.neueda.minion.ext.player;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.FileWatcher;
import com.neueda.minion.ext.HipChatMessage;
import com.neueda.minion.ext.Patterns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RadioExtension extends Extension {

    private static final String DEFAULT_STREAM_FORMAT = "mp3";
    private static final Pattern PATTERN = Patterns.preamble("play");

    private final Logger logger = LoggerFactory.getLogger(RadioExtension.class);
    private final Map<String, Map<String, Object>> streams = new ConcurrentHashMap<>();

    @Inject
    private FileWatcher fileWatcher;

    @Override
    public void initialize() {
        File streamsFile = new File("streams.csv");
        fileWatcher.watch(streamsFile, this::updateStreams);
        onHipChatMessage(this::handleMessage);
    }

    private void updateStreams(File streamsFile) {
        FileReader fileReader;
        try {
            fileReader = new FileReader(streamsFile);
        } catch (FileNotFoundException e) {
            logger.warn("Streams file not found: " + streamsFile.getAbsolutePath());
            return;
        }
        CSVReader<String[]> csvReader = CSVReaderBuilder.newDefaultReader(fileReader);
        logger.info("Updating live streams");
        streams.clear();
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
    }

    private void handleMessage(HipChatMessage message) {
        String body = message.getBody();
        Matcher matcher = PATTERN.matcher(body);
        if (matcher.matches()) {
            String name = matcher.group(1);
            String sender = message.getSender();
            if (name.equals("?")) {
                hipChatReply(sender, "(doge) such streams: " + Joiner.on(", ").join(listStreams()));
            } else {
                Map<String, Object> stream = streams.get(name);
                if (stream != null) {
                    messageBus.publish(Messages.STREAM_MESSAGE, stream);
                    hipChatReply(sender, "(doge) wow! playing \"" + name + "\"");
                } else {
                    hipChatReply(sender, "(doge) no stream named \"" + name + "\"");
                }
            }
        }
    }

    private List<String> listStreams() {
        List<String> streamList = Lists.newArrayList(streams.keySet());
        Collections.sort(streamList);
        return streamList;
    }

}
