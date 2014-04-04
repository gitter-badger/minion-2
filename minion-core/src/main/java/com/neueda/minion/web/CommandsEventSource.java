package com.neueda.minion.web;

import org.eclipse.jetty.servlets.EventSource;

import javax.inject.Inject;
import java.io.IOException;

public class CommandsEventSource implements EventSource {

    private final CommandsBroadcaster commandsBroadcaster;
    private Emitter emitter;

    @Inject
    public CommandsEventSource(CommandsBroadcaster commandsBroadcaster) {
        this.commandsBroadcaster = commandsBroadcaster;
    }

    @Override
    public void onOpen(Emitter emitter) throws IOException {
        this.emitter = emitter;
        commandsBroadcaster.add(this);
    }

    public void emitEvent(String name, String data) throws IOException {
        emitter.event(name, data);
    }

    @Override
    public void onClose() {
        commandsBroadcaster.remove(this);
    }

}
