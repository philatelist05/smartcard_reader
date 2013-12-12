package at.ac.tuwien.mnsa.message;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MessageReader {

	private final InputStream inputStream;

	public MessageReader(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public Message read() throws MessageException {
		try {
			int length;
			byte messageType;
			try {
				byte[] header = new byte[4];
				readFully(inputStream, header);
				messageType = header[0];
				byte nodeAddress = header[1];
				if (messageType != nodeAddress) {
					throw new MessageException("Message type does not match node address.");
				}
				if (messageType == Message.MessageType.ERROR.getByteValue()) {
					throw new MessageException("Got error message");
				}
				length = ByteBuffer
						.wrap(new byte[]{header[2], header[3]})
						.order(ByteOrder.BIG_ENDIAN)
						.getInt();
			} catch (IOException e) {
				throw new MessageException("Unable to read header", e);
			}
			byte[] payload = new byte[length];
			readFully(inputStream, payload);
			return new Message(Message.MessageType.valueOf(messageType), payload);
		} catch (IOException e) {
			throw new MessageException("Unable to read body", e);
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

}
