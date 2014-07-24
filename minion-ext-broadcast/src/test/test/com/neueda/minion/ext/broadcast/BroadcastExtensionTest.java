package com.neueda.minion.ext.broadcast;

import com.neueda.minion.ext.Extension;
import com.neueda.minion.ext.messaging.MessageBus;
import com.neueda.minion.test.ExtensionTester;
import org.junit.Rule;
import org.junit.Test;

import static com.neueda.minion.ext.messaging.MessageBus.dataBuilder;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class BroadcastExtensionTest {

    private final MessageBus messageBus = spy(new MessageBus());

    @Rule
    public ExtensionTester tester = new ExtensionTester(BroadcastExtension.class, messageBus);

    @Test
    public void testBroadcast() {
        assertThat(tester.extension(), notNullValue());

        tester.hipChatMessage("good news: foo");

        verify(messageBus).publish(
                Extension.HIPCHAT_NOTIFICATION,
                dataBuilder()
                        .put("color", "yellow")
                        .put("text", "(goodnews) foo")
                        .put("notify", true)
                        .build()
        );
    }

}
