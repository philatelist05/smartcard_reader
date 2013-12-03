package at.ac.tuwien.mnsa.message;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MessageReader {

	private final InputStream inputStream;

	public MessageReader(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public byte[] read() throws MessageException {
		try {
			int length = readHeader();
			byte[] payload = new byte[length];
			inputStream.read(payload);
			return payload;
		} catch (IOException e) {
			throw new MessageException("Unable to read body", e);
		}
	}

	private int readHeader() throws MessageException {
		try {
			byte[] header = new byte[4];
			inputStream.read(header);
			byte messageType = header[0];
			byte nodeAddress = header[1];
			if (messageType != nodeAddress) {
				throw new MessageException("Message type does not match node address.");
			}
			if (messageType == MessageType.ERROR.getByteValue()) {
				throw new MessageException("Got error message");
			}
			return ByteBuffer
					.wrap(new byte[]{header[2], header[3]})
					.order(ByteOrder.BIG_ENDIAN)
					.getInt();
		} catch (IOException e) {
			throw new MessageException("Unable to read header", e);
		}
	}
}
