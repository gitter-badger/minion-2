package com.neueda.minion.ext.scripting;

import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.HipChatMessage;
import com.neueda.minion.ext.messaging.MessageBus;

import static com.neueda.minion.ext.messaging.MessageBus.dataBuilder;

public class MinionScriptWrapper {

    private final MessageBus messageBus;
    private final HipChatMessage message;

    public MinionScriptWrapper(MessageBus messageBus, HipChatMessage message) {
        this.messageBus = messageBus;
        this.message = message;
    }

    public void reply(String text) {
        messageBus.publish(Extension.HIPCHAT_REPLY, dataBuilder()
                .put("sender", message.getSender())
                .put("text", text)
                .build());
    }

}
