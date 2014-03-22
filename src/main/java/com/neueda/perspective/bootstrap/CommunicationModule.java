package com.neueda.perspective.bootstrap;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
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
        ObjectMapper objectMapper = createObjectMapper(null);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        return objectMapper;
    }

    @Provides
    @Named("yaml")
    ObjectMapper providesYamlObjectMapper() {
        return createObjectMapper(new YAMLFactory());
    }

    private ObjectMapper createObjectMapper(JsonFactory factory) {
        ObjectMapper objectMapper = new ObjectMapper(factory);
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        return objectMapper;
    }

    @Provides
    Client providesRestClient(@Named("json") ObjectMapper objectMapper) {
        return ClientBuilder.newBuilder()
                .register(new JacksonJsonProvider(objectMapper))
                .build();
    }

}
