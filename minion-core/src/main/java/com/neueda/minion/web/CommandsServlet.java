package com.neueda.minion.web;

import com.google.inject.Inject;
import org.eclipse.jetty.servlets.EventSource;
import org.eclipse.jetty.servlets.EventSourceServlet;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;

@Singleton
public class CommandsServlet extends EventSourceServlet {

    @Inject
    private CommandsEventSourceFactory eventSourceFactory;

    @Override
    protected EventSource newEventSource(HttpServletRequest request) {
        return eventSourceFactory.create();
    }

}
