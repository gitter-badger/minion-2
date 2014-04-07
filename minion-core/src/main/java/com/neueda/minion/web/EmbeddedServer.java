package com.neueda.minion.web;

import com.google.common.base.Preconditions;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Singleton
public class EmbeddedServer {

    public static final String STATIC_ROOT = "com/neueda/minion/web";

    private final Logger logger = LoggerFactory.getLogger(EmbeddedServer.class);
    private final int port;
    private final Set<WebExtension> webExtensions;
    private final Map<UUID, ClassLoader> extensionClassLoaders;
    private Server server;

    @Inject
    public EmbeddedServer(EmbeddedServerCfg cfg,
                          Set<WebExtension> webExtensions,
                          @Named("extension") Map<UUID, ClassLoader> extensionClassLoaders) {
        port = cfg.getPort();
        this.webExtensions = webExtensions;
        this.extensionClassLoaders = extensionClassLoaders;
    }

    @WarmUp
    void start() throws Exception {
        server = new Server(port);
        ContextHandlerCollection contextHandlers = new ContextHandlerCollection();

        ContextHandler rootResourceContext =
                getResourceContext("/", STATIC_ROOT, getClass().getClassLoader());
        contextHandlers.addHandler(rootResourceContext);

        for (WebExtension webExtension : webExtensions) {
            String contextPath = webExtension.getContextPath();
            String base = webExtension.getBase();
            ClassLoader classLoader = extensionClassLoaders.get(webExtension.getUUID());
            Preconditions.checkNotNull(classLoader,
                    "No class loader found for web extension: {}",
                    webExtension.getClass());
            ContextHandler context = getResourceContext(contextPath, base, classLoader);
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

    private ContextHandler getResourceContext(
            String contextPath,
            String base,
            ClassLoader classLoader) throws IOException {
        ContextHandler context = new ContextHandler();
        context.setContextPath(contextPath);
        ResourceHandler resourceHandler = new ResourceHandler();
        URL url = classLoader.getResource(base);
        Resource baseResource = Resource.newResource(url);
        logger.info("Creating static context \"{}\" at {}", contextPath, baseResource);
        resourceHandler.setBaseResource(baseResource);
        context.setHandler(resourceHandler);
        context.setClassLoader(classLoader);
        return context;
    }

    @PreDestroy
    void stop() throws Exception {
        server.stop();
    }

}
