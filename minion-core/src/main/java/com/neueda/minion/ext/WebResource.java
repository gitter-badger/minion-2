package com.neueda.minion.ext;

import java.util.UUID;

public class WebResource {

    private final String base;
    private final String contextPath;
    private final String bootstrap;
    private UUID uuid;

    private WebResource(Builder builder) {
        base = builder.base;
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

    public String getContextPath() {
        return contextPath;
    }

    public String getBootstrap() {
        return bootstrap;
    }

    public static class Builder {

        private String base;
        private String contextPath;
        private String bootstrap;

        private Builder() {
        }

        public Builder base(String base) {
            this.base = base;
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
