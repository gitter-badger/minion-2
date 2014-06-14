package com.neueda.minion.web;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class BootstrapServlet extends HttpServlet {

    private final ObjectMapper objectMapper;
    private final Set<String> extensions;

    @Inject
    public BootstrapServlet(@Named("internal") ObjectMapper objectMapper,
                            Set<WebResource> webResources) {
        this.objectMapper = objectMapper;
        extensions = webResources.stream()
                .map(WebResource::getBootstrap)
                .filter(bootstrap -> bootstrap != null)
                .collect(Collectors.toSet());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Manifest manifest = new Manifest();
        manifest.setExtensions(extensions);
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getOutputStream(), manifest);
        resp.getOutputStream().flush();
    }

}
