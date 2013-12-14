package at.ac.tuwien.nokiaspi;


import at.ac.tuwien.mnsa.message.Message;
import at.ac.tuwien.mnsa.message.MessageReader;
import at.ac.tuwien.mnsa.message.MessageWriter;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TestMessaging {

    @Test
    public void testTestMessage() throws Exception {
        Message expectedMessage = new Message(Message.MessageType.TEST);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MessageWriter messageWriter = new MessageWriter(outputStream);
        messageWriter.write(expectedMessage);

        MessageReader messageReader = new MessageReader(new ByteArrayInputStream(outputStream.toByteArray()));
        Message actualMessage = messageReader.read();
        assertArrayEquals(expectedMessage.getPayload(), actualMessage.getPayload());
        assertEquals(expectedMessage.getType().getByteValue(), actualMessage.getType().getByteValue());
    }

    @Test
    public void testTestMessageWithPayload() throws Exception {
        Message expectedMessage = new Message(Message.MessageType.TEST, new byte[]{1});
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MessageWriter messageWriter = new MessageWriter(outputStream);
        messageWriter.write(expectedMessage);

        MessageReader messageReader = new MessageReader(new ByteArrayInputStream(outputStream.toByteArray()));
        Message actualMessage = messageReader.read();
        assertArrayEquals(expectedMessage.getPayload(), actualMessage.getPayload());
        assertEquals(expectedMessage.getType().getByteValue(), actualMessage.getType().getByteValue());
    }

    @Test
    public void testTestMessagePayloadData() throws Exception {
        Message message = new Message(Message.MessageType.TEST, new byte[]{1});
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MessageWriter messageWriter = new MessageWriter(outputStream);
        messageWriter.write(message);
        byte[] expected = new byte[]{0,0,0,1,1};
        assertArrayEquals(expected, outputStream.toByteArray());
    }
}
