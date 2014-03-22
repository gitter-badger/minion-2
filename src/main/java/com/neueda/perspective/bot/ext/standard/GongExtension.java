package com.neueda.perspective.bot.ext.standard;

import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;
import com.neueda.perspective.bot.ext.Extension;
import com.neueda.perspective.bot.ext.result.ExtensionResult;
import com.neueda.perspective.bot.ext.result.ExtensionResultRespond;
import com.neueda.perspective.bot.ext.result.ExtensionResultProceed;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GongExtension implements Extension {

    private static final String NAME = "gong";
    private static final String MP3 = "com/neueda/perspective/bot/ext/standard/gong.mp3";
    private static final Pattern PATTERN = Pattern.compile("\\s*[Ff]{2,}[Uu]{3,}.*");
    private static final String RESPONSE = "(tableflip)";

    private final Logger logger = LoggerFactory.getLogger(GongExtension.class);
    private final Executor executor = Executors.newSingleThreadExecutor();
    private InputSupplier<InputStream> sound;

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void initialize() {
        URL resource = Resources.getResource(MP3);
        sound = Resources.newInputStreamSupplier(resource);
    }

    @Override
    public ExtensionResult process(String message) {
        Matcher matcher = PATTERN.matcher(message);
        if (matcher.matches()) {
            gong();
            return new ExtensionResultRespond(RESPONSE);
        }
        return new ExtensionResultProceed();
    }

    private void gong() {
        executor.execute(() -> {
            try {
                Player player = new Player(sound.getInput());
                player.play();
            } catch (JavaLayerException | IOException e) {
                logger.error("Failed to play sound", e);
            }
        });
    }

}
