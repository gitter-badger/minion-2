package com.neueda.minion.ext.messaging;

import com.google.common.base.Preconditions;

import java.util.Map;

public interface MessageBusReader<T> {
    T read(Map<String, Object> data) throws IllegalArgumentException;

    public static <A> A getArgument(Map<String, Object> data, String name)
            throws IllegalArgumentException {
        Preconditions.checkArgument(data.get(name) != null);
        return getNullableArgument(data, name);
    }

    @SuppressWarnings("unchecked")
    public static <A> A getNullableArgument(Map<String, Object> data, String name) {
        try {
            return (A) data.get(name);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
    }
}
