package com.neueda.minion.ext.messaging;

import java.util.Map;

public interface MessageBusReader<T> {
    T read(Map<String, Object> data) throws IllegalArgumentException;

    @SuppressWarnings("unchecked")
    public static <T> T castArgument(Object argument)
            throws IllegalArgumentException {
        try {
            return (T) argument;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
    }
}
