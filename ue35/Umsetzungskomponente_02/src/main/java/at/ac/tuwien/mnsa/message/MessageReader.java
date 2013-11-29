package at.ac.tuwien.mnsa.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MessageReader {

	private final InputStream inputStream;

	public MessageReader(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public <T extends Serializable> Message<T> read(MessageFactory factory)
			throws MessageException {
		try {
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
		} catch (ClassCastException ex) {
			throw new MessageException("Can not instantiate message "
					+ "because of wrong message type Byte", ex);
		} catch(IllegalStateException ex) {
			throw new MessageException(ex);
		} catch (IOException ex) {
			throw new MessageException("Unable to read message", ex);
		}
	}
}
