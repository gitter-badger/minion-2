package com.neueda.minion.ext.messaging;

import java.util.Map;

public class DefaultMessageBusReader implements MessageBusReader<Map<String, Object>> {

    @Override
    public Map<String, Object> read(Map<String, Object> data) {
        return data;
    }

}
