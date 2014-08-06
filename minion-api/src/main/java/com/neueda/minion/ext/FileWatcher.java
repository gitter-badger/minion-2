package com.neueda.minion.ext;

import com.google.common.collect.HashMultimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static java.nio.file.StandardWatchEventKinds.*;

@Singleton
public class FileWatcher {

    private final Logger logger = LoggerFactory.getLogger(FileWatcher.class);
    private final ScheduledExecutorService watcher;
    private final HashMultimap<Path, File> targets = HashMultimap.create();
    private WatchService watchService;

    @Inject
    public FileWatcher(@Named("scheduler.watcher") ScheduledExecutorService watcher) {
        this.watcher = watcher;
    }

    @PostConstruct
    void postConstruct() {
        FileSystem fs = FileSystems.getDefault();
        try {
            watchService = fs.newWatchService();
        } catch (IOException e) {
            logger.error("Failed to initialize file watch service", e);
        }
    }

    public void watch(final File file, final Consumer<File> callback) {
        if (watchService == null) {
            return;
        }
        if (!file.isFile()) {
            logger.error("Failed to set up watcher, not a file: " + file.getAbsolutePath());
        }

        File absoluteFile = file.getAbsoluteFile();
        Path fileParent = absoluteFile.toPath().getParent();
        boolean newPath, newFile;
        synchronized (this) {
            newPath = !targets.containsKey(fileParent);
            newFile = targets.put(fileParent, absoluteFile);
        }
        if (newPath) {
            watchPath(fileParent, callback);
        }
        if (newFile) {
            logger.info("Watching for changes: " + file.getAbsolutePath());
            if (file.exists()) {
                callback.accept(file);
            }
        }
    }

    private void watchPath(Path path, Consumer<File> callback) {
        final WatchKey key;
        try {
            key = path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY);
        } catch (IOException e) {
            logger.warn("Failed to set up watcher for path: " + path);
            return;
        }
        watcher.scheduleWithFixedDelay(() -> {
            List<WatchEvent<?>> events = key.pollEvents();
            if (events.isEmpty()) {
                return;
            }
            Set<File> files;
            synchronized (FileWatcher.this) {
                files = targets.get(path);
            }
            events.stream()
                    .filter(event -> {
                        WatchEvent.Kind<?> kind = event.kind();
                        return kind != OVERFLOW;
                    })
                    .map(event -> {
                        @SuppressWarnings("unchecked")
                        WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
                        return pathEvent.context().toFile().getAbsoluteFile();
                    })
                    .distinct()
                    .filter(files::contains)
                    .forEach(callback::accept);
        }, 0, 1, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void shutdown() {
        watcher.shutdown();
    }

}
