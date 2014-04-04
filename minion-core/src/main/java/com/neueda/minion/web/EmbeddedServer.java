package com.neueda.minion.web;

import com.google.inject.servlet.GuiceFilter;
import com.netflix.governator.annotations.WarmUp;
import com.neueda.minion.web.cfg.EmbeddedServerCfg;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EmbeddedServer {

    public static final String STATIC_ROOT = "com/neueda/minion/web";

    private final int port;
    private Server server;

    @Inject
    public EmbeddedServer(EmbeddedServerCfg cfg) {
        port = cfg.getPort();
    }

    @WarmUp
    void start() throws Exception {
        server = new Server(port);

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        context.setBaseResource(Resource.newClassPathResource(STATIC_ROOT));

        FilterHolder guiceFilter = new FilterHolder(GuiceFilter.class);
        guiceFilter.setAsyncSupported(true);
        context.addFilter(guiceFilter, "/*", null);
        context.addServlet(DefaultServlet.class, "/");

        server.setHandler(context);
        server.start();
    }

    @PreDestroy
    void stop() throws Exception {
        server.stop();
    }

}
