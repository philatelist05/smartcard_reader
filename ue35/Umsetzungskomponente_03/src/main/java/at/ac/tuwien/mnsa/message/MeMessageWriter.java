package at.ac.tuwien.mnsa.message;

import at.ac.tuwien.mnsa.midlet.Logger;

import java.io.IOException;
import java.io.OutputStream;

public class MeMessageWriter {

    private final OutputStream outputStream;
    private final Logger logger;

    public MeMessageWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
        logger = Logger.getLogger(getClass().getName());
    }

    public void write(MeMessage meMessage) throws MeMessageException {
        byte type = meMessage.getType();
        byte[] payload = meMessage.getPayload();
        try {
            byte nodeAddress = type;
            short length = (short) payload.length;
            byte lsb = (byte) (length & 0xff);
            byte msb = (byte) (length >> 8);
            outputStream.write(new byte[]{type, nodeAddress, msb, lsb});
            if (length > 0) {
                outputStream.write(payload);
            }
            outputStream.flush();
        } catch (IOException e) {
            throw new MeMessageException("Unable to write Message: " + payload, e);
        }
    }
}
