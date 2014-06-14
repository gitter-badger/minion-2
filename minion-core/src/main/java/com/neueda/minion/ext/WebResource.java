package com.neueda.minion.ext;

import java.util.UUID;

public class WebResource {

    private final String base;
    private final boolean file;
    private final String contextPath;
    private final String bootstrap;
    private UUID uuid;

    private WebResource(Builder builder) {
        base = builder.base;
        file = builder.file;
        contextPath = builder.contextPath;
        bootstrap = builder.bootstrap;
    }

    void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getBase() {
        return base;
    }

    public boolean isFile() {
        return file;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getBootstrap() {
        return bootstrap;
    }

    public static class Builder {

        private String base;
        private boolean file;
        private String contextPath;
        private String bootstrap;

        private Builder() {
        }

        public Builder resourceRoot(String path) {
            this.base = path;
            this.file = false;
            return this;
        }

        public Builder folder(String path) {
            this.base = path;
            this.file = true;
            return this;
        }

        public Builder contextPath(String contextPath) {
            this.contextPath = contextPath;
            return this;
        }

        public Builder bootstrap(String bootstrap) {
            this.bootstrap = bootstrap;
            return this;
        }

        public WebResource build() {
            return new WebResource(this);
        }

    }

}
