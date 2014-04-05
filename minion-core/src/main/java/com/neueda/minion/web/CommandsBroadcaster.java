package com.neueda.minion.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Set;

@Singleton
public class CommandsBroadcaster {

    private final Logger logger = LoggerFactory.getLogger(CommandsBroadcaster.class);
    private final ObjectMapper objectMapper;
    private final Set<CommandsEventSource> eventSources;

    @Inject
    public CommandsBroadcaster(@Named("internal") ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        eventSources = Sets.newConcurrentHashSet();
    }

    public void add(CommandsEventSource eventSource) {
        eventSources.add(eventSource);
    }

    public void remove(CommandsEventSource eventSource) {
        eventSources.remove(eventSource);
    }

    public void broadcast(String eventName, Object object) {
        String data;
        try {
            data = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize event", e);
            return;
        }
        for (CommandsEventSource eventSource : eventSources) {
            try {
                eventSource.emitEvent(eventName, data);
            } catch (IOException e) {
                logger.error("Failed to deliver event \"{}\": {}", eventName, data, e);
            }
        }
    }

}
