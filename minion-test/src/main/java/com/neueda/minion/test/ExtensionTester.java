package com.neueda.minion.test;

import com.netflix.governator.guice.LifecycleTester;
import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.messaging.MessageBus;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static com.neueda.minion.ext.messaging.MessageBus.dataBuilder;

public class ExtensionTester extends LifecycleTester {

    private final Class<? extends Extension> extensionClass;
    private final MessageBus messageBus;
    private Extension extension;

    public ExtensionTester(Class<? extends Extension> extensionClass,
                           MessageBus messageBus) {
        this(extensionClass, messageBus, Collections.emptyMap());
    }

    public ExtensionTester(Class<? extends Extension> extensionClass,
                           MessageBus messageBus,
                           Map<String, ?> configuration) {
        super(new MinionTestSuite(extensionClass, messageBus, configuration));
        this.extensionClass = extensionClass;
        this.messageBus = messageBus;
    }

    @Override
    protected void before() throws Throwable {
        start();
        extension = getInstance(extensionClass);
        extension.initialize();
    }

    public Extension extension() {
        return extension;
    }

    public void hipChatMessage(String body) {
        hipChatMessage("ExtensionTester", body);
    }

    public void hipChatMessage(String from, String body) {
        messageBus.publish(Extension.HIPCHAT_MESSAGE, dataBuilder()
                .put("sender", UUID.randomUUID().toString())
                .put("from", from)
                .put("body", body)
                .build());
    }

}
