package com.neueda.minion.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.WebResource;
import com.neueda.minion.web.dto.Manifest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class BootstrapServlet extends HttpServlet {

    private final ObjectMapper objectMapper;
    private final Set<Extension> extensions;
    private final Map<String, String> configuration;
    private final Set<String> resources;

    @Inject
    public BootstrapServlet(@Named("internal") ObjectMapper objectMapper,
                            Set<Extension> extensions,
                            @Named("configuration") Properties configuration,
                            Set<WebResource> webResources) {
        this.objectMapper = objectMapper;
        this.extensions = extensions;
        this.configuration = Maps.newHashMap();
        configuration.stringPropertyNames().stream()
                .filter(key -> key.startsWith("web."))
                .forEach(key -> this.configuration.put(key, configuration.getProperty(key)));
        resources = webResources.stream()
                .map(WebResource::getBootstrap)
                .filter(bootstrap -> bootstrap != null)
                .collect(Collectors.toSet());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Manifest manifest = new Manifest();
        manifest.setConfiguration(configuration);
        manifest.setResources(resources);
        Map<String, Map<String, Object>> states = Maps.newHashMap();
        extensions.stream()
                .filter(extension -> extension.getQualifier() != null)
                .forEach(extension -> states.put(extension.getQualifier(), extension.getState()));
        manifest.setStates(states);
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getOutputStream(), manifest);
        resp.getOutputStream().flush();
    }

}
