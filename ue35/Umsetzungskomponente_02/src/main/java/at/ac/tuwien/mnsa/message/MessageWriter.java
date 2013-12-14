package at.ac.tuwien.mnsa.message;

import java.io.IOException;
import java.io.OutputStream;

public class MessageWriter {

    private final OutputStream outputStream;

    public MessageWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(Message message) throws MessageException {
        Message.MessageType type = message.getType();
        byte[] payload = message.getPayload();
        try {
            byte nodeAddress = type.getByteValue();
            int length = payload.length;
            byte lnl = (byte) length;
            byte lnh = (byte) (length >> 8);
            outputStream.write(new byte[]{type.getByteValue(), nodeAddress, lnh, lnl});

            if (payload.length > 0) {
                outputStream.write(payload);
            }
            outputStream.flush();
        } catch (IOException e) {
            throw new MessageException("Unable to write Message: " + String.format("%02x", payload), e);
        }
    }
}
