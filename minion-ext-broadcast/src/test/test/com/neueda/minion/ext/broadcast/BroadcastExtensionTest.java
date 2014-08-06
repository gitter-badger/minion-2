package com.neueda.minion.ext.broadcast;

import com.neueda.minion.ext.Extension;
import com.neueda.minion.test.ExtensionTester;
import com.neueda.minion.test.RecordingMessageBus;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collections;

import static com.neueda.minion.ext.messaging.MessageBus.dataBuilder;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class BroadcastExtensionTest {

    private RecordingMessageBus messageBus = new RecordingMessageBus();

    @Rule
    public ExtensionTester tester = new ExtensionTester(
            BroadcastExtension.class,
            messageBus,
            Collections.singletonMap("ext.broadcast.prefix", "(heart)")
    );

    @Test
    public void testBroadcast() {
        assertThat(tester.extension(), notNullValue());

        tester.hipChatMessage("good news: foo");

        assertThat(messageBus.getRecords(Extension.HIPCHAT_NOTIFICATION).size(), equalTo(1));
        assertTrue(messageBus.wasPublished(Extension.HIPCHAT_NOTIFICATION, dataBuilder()
                .put("color", "yellow")
                .put("text", "(heart) foo")
                .put("notify", true)
                .build()));
    }

}
