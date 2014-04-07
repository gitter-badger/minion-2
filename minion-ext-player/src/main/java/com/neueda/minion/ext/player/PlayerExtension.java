package com.neueda.minion.ext.player;

import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.PatternHelper;
import com.neueda.minion.ext.player.event.PlayerLiveStream;
import com.neueda.minion.ext.player.event.PlayerSoundEffect;
import com.neueda.minion.ext.result.ExtensionResult;
import com.neueda.minion.ext.result.ExtensionResultCommand;
import com.neueda.minion.ext.result.ExtensionResultProceed;
import com.neueda.minion.ext.result.ExtensionResultRespond;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerExtension implements Extension {

    private static final String DEFAULT_STREAM_FORMAT = "mp3";

    private static final String PLAYER_EVENT = "player";

    private static final String SFX_RESOURCE_PATH = "com/neueda/minion/web/player/sfx/%s.mp3";
    private static final String SFX_WEB_PATH = "/player/sfx/%s.mp3";

    private static final Pattern PATTERN = PatternHelper.preamble("play");
    private static final Pattern WORD_PATTERN = Pattern.compile("\\s*[a-z]+\\s*");

    private final Logger logger = LoggerFactory.getLogger(PlayerExtension.class);
    private final Map<String, PlayerLiveStream> streams = new HashMap<>();

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
            streams.put(name, new PlayerLiveStream(url, format));
        });
    }

    @Override
    public ExtensionResult process(String message) {
        Matcher matcher = PATTERN.matcher(message);
        if (matcher.matches()) {
            String name = matcher.group(1);
            if (streams.containsKey(name)) {
                return new ExtensionResultCommand(PLAYER_EVENT, streams.get(name));
            } else if (WORD_PATTERN.matcher(name).matches()) {
                URL resource = getClass().getClassLoader().getResource(String.format(SFX_RESOURCE_PATH, name));
                if (resource != null) {
                    PlayerSoundEffect data = new PlayerSoundEffect(String.format(SFX_WEB_PATH, name));
                    return new ExtensionResultCommand(PLAYER_EVENT, data);
                }
            }
            return new ExtensionResultRespond("(unknown) Nothing to play named \"" + name + "\"");
        }
        return new ExtensionResultProceed();
    }

}
