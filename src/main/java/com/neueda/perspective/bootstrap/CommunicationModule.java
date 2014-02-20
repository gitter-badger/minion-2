package com.neueda.perspective.bootstrap;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class CommunicationModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Named("json")
    ObjectMapper providesJsonObjectMapper() {
        return createObjectMapper(null);
    }

    @Provides
    @Named("yaml")
    ObjectMapper providesYamlObjectMapper() {
        return createObjectMapper(new YAMLFactory());
    }

    private ObjectMapper createObjectMapper(JsonFactory factory) {
        ObjectMapper objectMapper = new ObjectMapper(factory);
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Provides
    Client providesRestClient(ObjectMapper objectMapper) {
        return ClientBuilder.newBuilder()
                .register(new JacksonJsonProvider(objectMapper))
                .build();
    }

}
