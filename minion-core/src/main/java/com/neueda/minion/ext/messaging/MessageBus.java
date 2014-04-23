package com.neueda.minion.ext.messaging;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

@Singleton
public class MessageBus {

    private final Logger logger = LoggerFactory.getLogger(MessageBus.class);
    private final Set<EventConsumer> eventConsumers;
    private final Multimap<String, Consumer<Map<String, Object>>> subscriptions;
    private final ReadWriteLock subscriptionsLock;

    public MessageBus() {
        eventConsumers = Sets.newHashSet();
        subscriptions = HashMultimap.create();
        subscriptionsLock = new ReentrantReadWriteLock();
    }

    public static ImmutableMap.Builder<String, Object> dataBuilder() {
        return ImmutableMap.<String, Object>builder();
    }

    public void subscribe(String event,
                          final Consumer<Map<String, Object>> consumer) {
        subscribe(event, consumer, new DefaultMessageBusReader());
    }

    public <T> void subscribe(String event,
                              final Consumer<? super T> consumer,
                              final MessageBusReader<T> reader) {
        Preconditions.checkNotNull(event);
        Preconditions.checkNotNull(consumer);
        final EventConsumer eventConsumer = new EventConsumer(event, consumer);
        Preconditions.checkArgument(!eventConsumers.contains(eventConsumer),
                "Duplicate event-consumer pair");
        Preconditions.checkNotNull(reader);

        subscriptionsLock.writeLock().lock();
        try {
            subscriptions.put(event, data -> {
                try {
                    T object = reader.read(data);
                    consumer.accept(object);
                } catch (IllegalArgumentException e) {
                    logger.error("Failed to read event \"{}\" message: {}", event, data, e);
                }
            });
            eventConsumers.add(eventConsumer);
        } finally {
            subscriptionsLock.writeLock().unlock();
        }
    }

    public void publish(String event) {
        publish(event, Collections.emptyMap());
    }

    public void publish(String event, final Map<String, Object> data) {
        Preconditions.checkNotNull(event);
        Preconditions.checkNotNull(data);
        Collection<Consumer<Map<String, Object>>> consumers;
        subscriptionsLock.readLock().lock();
        try {
            consumers = subscriptions.get(event); // never null
        } finally {
            subscriptionsLock.readLock().unlock();
        }
        consumers.parallelStream()
                .forEach(consumer -> consumer.accept(data));
    }

}
