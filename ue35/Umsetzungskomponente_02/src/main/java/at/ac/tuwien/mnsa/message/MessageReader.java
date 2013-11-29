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

	public Message read(MessageFactory factory) throws IOException {
		byte[] header = new byte[4];
		inputStream.read(header);
		byte mty = header[0];
		byte nad = header[1];
		int length = ByteBuffer
				.wrap(new byte[]{header[2], header[3]})
				.order(ByteOrder.BIG_ENDIAN)
				.getInt();

		byte[] payload = new byte[length];
		inputStream.read(payload);
		return factory.create(mty, payload);
	}
}
