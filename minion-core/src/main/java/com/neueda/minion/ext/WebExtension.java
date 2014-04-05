package com.neueda.minion.ext;

import org.eclipse.jetty.util.resource.Resource;

public class WebExtension {

    private final Resource base;
    private final String contextPath;
    private final String bootstrap;

    public WebExtension(Builder builder) {
        base = builder.base;
        contextPath = builder.contextPath;
        bootstrap = builder.bootstrap;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Resource getBase() {
        return base;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getBootstrap() {
        return bootstrap;
    }

    public static class Builder {

        private Resource base;
        private String contextPath;
        private String bootstrap;

        private Builder() {
        }

        public Builder base(String base) {
            this.base = Resource.newClassPathResource(base);
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

        public WebExtension build() {
            return new WebExtension(this);
        }

    }

}
