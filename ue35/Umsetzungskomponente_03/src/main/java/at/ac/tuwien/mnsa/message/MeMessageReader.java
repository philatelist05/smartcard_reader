package at.ac.tuwien.mnsa.message;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class MeMessageReader {

    private final InputStream inputStream;


    public MeMessageReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public MeMessage read() throws MeMessageException {
        try {
            short length;
            byte messageType;
            try {
                byte[] header = new byte[4];
                readFully(inputStream, header);

                messageType = header[0];
                byte nodeAddress = header[1];
                if (messageType != nodeAddress) {
                    throw new MeMessageException("Message type does not match node address.");
                }
                if (messageType == MeMessage.MessageType.ERROR) {
                    throw new MeMessageException("Got error message");
                }
                length = toShort(header[2], header[3]);
            } catch (IOException e) {
                throw new MeMessageException("Unable to read header", e);
            }
            byte[] payload = new byte[length];
            readFully(inputStream, payload);
            return new MeMessage(MeMessage.MessageType.valueOf(messageType), payload);
        } catch (IOException e) {
            throw new MeMessageException("Unable to read body", e);
        }
    }

    private void readFully(InputStream inputStream, byte[] buffer) throws IOException {
        int remaining = buffer.length;
        while (remaining > 0) {
            int location = buffer.length - remaining;
            int count = inputStream.read(buffer, 0 + location, remaining);
            if (count == -1) { // EOF
                break;
            }
            remaining -= count;
        }
        int actual = buffer.length - remaining;
        if (actual != buffer.length) {
            throw new EOFException("Length to read: " + buffer.length + " actual: " + actual);
        }
    }

    private short toShort(byte msb, byte lsb) {
        return (short) ((msb << 8) | lsb);
    }
}
