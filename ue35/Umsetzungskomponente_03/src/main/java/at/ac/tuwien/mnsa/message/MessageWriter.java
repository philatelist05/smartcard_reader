package at.ac.tuwien.mnsa.message;

import java.io.IOException;
import java.io.OutputStream;

public class MessageWriter {

	private final OutputStream outputStream;

	public MessageWriter(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void write(Message message) throws MessageException {
		byte type = message.getType();
		byte[] payload = message.getPayload();
		try {
			byte nodeAddress = type;
			short length = (short) payload.length;
            byte lsb = (byte) (length & 0xff);
            byte msb = (byte) (length >> 8);
			outputStream.write(new byte[]{type, nodeAddress, msb, lsb});
			outputStream.flush();
		} catch (IOException e) {
			throw new MessageException("Unable to write Message: " + payload, e);
		}
	}
}
