package com.neueda.minion.web;

import com.google.inject.servlet.GuiceFilter;
import com.netflix.governator.annotations.WarmUp;
import com.neueda.minion.ext.WebExtension;
import com.neueda.minion.web.cfg.EmbeddedServerCfg;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class EmbeddedServer {

    public static final String STATIC_ROOT = "com/neueda/minion/web";

    private final int port;
    private final Set<WebExtension> webExtensions;
    private Server server;

    @Inject
    public EmbeddedServer(EmbeddedServerCfg cfg,
                          Set<WebExtension> webExtensions) {
        port = cfg.getPort();
        this.webExtensions = webExtensions;
    }

    @WarmUp
    void start() throws Exception {
        server = new Server(port);
        ContextHandlerCollection contextHandlers = new ContextHandlerCollection();

        Resource staticRoot = Resource.newClassPathResource(STATIC_ROOT);
        ContextHandler rootResourceContext = getResourceContext("/", staticRoot);
        contextHandlers.addHandler(rootResourceContext);

        for (WebExtension webExtension : webExtensions) {
            String contextPath = webExtension.getContextPath();
            Resource base = webExtension.getBase();
            ContextHandler context = getResourceContext(contextPath, base);
            contextHandlers.addHandler(context);
        }

        ServletContextHandler servletContext = new ServletContextHandler();
        servletContext.setContextPath("/");
        contextHandlers.addHandler(servletContext);

        FilterHolder guiceFilter = new FilterHolder(GuiceFilter.class);
        guiceFilter.setAsyncSupported(true);
        servletContext.addFilter(guiceFilter, "/*", null);
        servletContext.addServlet(DefaultServlet.class, "/");

        server.setHandler(contextHandlers);
        server.start();
    }

    private ContextHandler getResourceContext(String contextPath, Resource base) {
        ContextHandler context = new ContextHandler();
        context.setContextPath(contextPath);
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setBaseResource(base);
        context.setHandler(resourceHandler);
        return context;
    }

    @PreDestroy
    void stop() throws Exception {
        server.stop();
    }

}
