package com.neueda.minion.web.dto;

import java.util.Map;
import java.util.Set;

public final class Manifest {

    private Map<String, String> configuration;
    private Set<String> resources;
    private Map<String, Map<String, Object>> states;

    public Map<String, String> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Map<String, String> configuration) {
        this.configuration = configuration;
    }

    public Set<String> getResources() {
        return resources;
    }

    public void setResources(Set<String> resources) {
        this.resources = resources;
    }

    public Map<String, Map<String, Object>> getStates() {
        return states;
    }

    public void setStates(Map<String, Map<String, Object>> states) {
        this.states = states;
    }

}
