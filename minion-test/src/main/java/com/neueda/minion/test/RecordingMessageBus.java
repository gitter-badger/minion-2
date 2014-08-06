package com.neueda.minion.test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.neueda.minion.ext.messaging.MessageBus;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Map;

@Singleton
public class RecordingMessageBus extends MessageBus {

    private Multimap<String, Map<String, Object>> records = LinkedListMultimap.create();

    @Override
    public void publish(String event, Map<String, Object> data) {
        super.publish(event, data);
        record(event, data);
    }

    private synchronized void record(String event, Map<String, Object> data) {
        records.put(event, data);
    }

    public synchronized Collection<Map<String, Object>> getRecords(String event) {
        return ImmutableList.copyOf(records.get(event));
    }

    public synchronized boolean wasPublished(String event, Map<String, Object> data) {
        return records.containsEntry(event, data);
    }

}
