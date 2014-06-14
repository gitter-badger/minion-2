package com.neueda.minion.web.dto;

import java.util.Map;
import java.util.Set;

public final class Manifest {

    private Set<String> resources;
    private Map<String, Map<String, Object>> states;

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
