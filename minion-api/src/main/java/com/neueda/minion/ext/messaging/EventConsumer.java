package com.neueda.minion.ext.messaging;

import java.util.function.Consumer;

final class EventConsumer {

    private final String event;
    private final Consumer<?> consumer;

    public EventConsumer(String event, Consumer<?> consumer) {
        this.event = event;
        this.consumer = consumer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventConsumer)) return false;

        EventConsumer that = (EventConsumer) o;

        if (!consumer.equals(that.consumer)) return false;
        if (!event.equals(that.event)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = event.hashCode();
        result = 31 * result + consumer.hashCode();
        return result;
    }

}
