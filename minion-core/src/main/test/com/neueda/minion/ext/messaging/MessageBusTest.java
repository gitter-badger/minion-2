package com.neueda.minion.ext.messaging;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MessageBusTest {

    private MessageBus messageBus;

    @Before
    public void setUp() {
        messageBus = new MessageBus();
    }

    @Test
    public void testPubSub() throws Exception {
        AtomicBoolean fooFlag = new AtomicBoolean(false);
        AtomicBoolean barFlag = new AtomicBoolean(false);
        messageBus.subscribe("foo", data -> fooFlag.set(true));
        messageBus.subscribe("bar", data -> barFlag.set(true));
        messageBus.publish("foo");
        messageBus.publish("bar");

        assertTrue(fooFlag.get());
        assertTrue(barFlag.get());
    }

    @Test
    public void testDoubleSub() {
        AtomicLong counter = new AtomicLong(0);
        messageBus.subscribe("foo", data -> counter.incrementAndGet());
        messageBus.subscribe("foo", data -> counter.incrementAndGet());
        messageBus.publish("foo");

        assertEquals(2, counter.get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateSub() {
        Consumer<Map<String, Object>> consumer = data -> {
        };
        messageBus.subscribe("foo", consumer);
        messageBus.subscribe("foo", consumer);
    }

    @Test
    public void publishNoSubs() {
        messageBus.publish("foo");
    }

}
