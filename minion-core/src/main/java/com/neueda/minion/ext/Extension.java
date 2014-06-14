package com.neueda.minion.ext;

import com.neueda.minion.ext.messaging.MessageBus;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

import static com.neueda.minion.ext.messaging.MessageBus.dataBuilder;

public abstract class Extension {

    @Inject
    protected MessageBus messageBus;

    public void initialize() {
    }

    public String getQualifier() {
        return null;
    }

    public Map<String, Object> getState() {
        return Collections.emptyMap();
    }

    protected void onHipChatMessage(final Consumer<HipChatMessage> consumer) {
        messageBus.subscribe("com.neueda.minion.hipChat.message", consumer, new HipChatMessage.Reader());
    }

    protected void hipChatReply(String sender, String text) {
        messageBus.publish("com.neueda.minion.hipChat.reply", dataBuilder()
                .put("sender", sender)
                .put("text", text)
                .build());
    }

    protected void hipChatNotification(String color, String text, boolean notify) {
        messageBus.publish("com.neueda.minion.hipChat.notification", dataBuilder()
                .put("color", color)
                .put("text", text)
                .put("notify", notify)
                .build());
    }

    protected void webEvent(String event, Object data) {
        messageBus.publish("com.neueda.minion.web.event", dataBuilder()
                .put("event", event)
                .put("data", data)
                .build());
    }

}
