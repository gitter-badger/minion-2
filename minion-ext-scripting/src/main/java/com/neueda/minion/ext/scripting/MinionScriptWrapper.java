package com.neueda.minion.ext.scripting;

import com.google.inject.assistedinject.Assisted;
import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.HipChatMessage;
import com.neueda.minion.ext.messaging.MessageBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;

import static com.neueda.minion.ext.messaging.MessageBus.dataBuilder;

public class MinionScriptWrapper {

    private final Logger logger = LoggerFactory.getLogger(MinionScriptWrapper.class);

    private final MessageBus messageBus;
    private final Client httpClient;
    private final HipChatMessage message;

    @Inject
    public MinionScriptWrapper(
            MessageBus messageBus,
            @Named("com.neueda.minion.ext.scripting.httpClient") Client httpClient,
            @Assisted HipChatMessage message) {
        this.messageBus = messageBus;
        this.httpClient = httpClient;
        this.message = message;
    }

    public void reply(String text) {
        messageBus.publish(Extension.HIPCHAT_REPLY, dataBuilder()
                .put("sender", message.getSender())
                .put("text", text)
                .build());
    }

    public void notify(String text) {
        messageBus.publish(Extension.HIPCHAT_NOTIFICATION, dataBuilder()
                .put("color", "yellow")
                .put("text", text)
                .put("notify", true)
                .build());
    }

    public void sse(String event, Object data) {
        messageBus.publish(Extension.SERVER_SENT_EVENT, dataBuilder()
                .put("event", event)
                .put("data", data)
                .build());
    }

    public String json(String url) {
        return httpClient.target(url)
                .request().get()
                .readEntity(String.class);
    }

    public void log(String text) {
        logger.info(text);
    }

}
