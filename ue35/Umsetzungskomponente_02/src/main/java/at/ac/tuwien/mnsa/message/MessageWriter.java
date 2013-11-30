package at.ac.tuwien.mnsa.message;

import at.ac.tuwien.mnsa.ClassUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import org.apache.log4j.Logger;

public class MessageWriter {

	private final OutputStream outputStream;
	private final Logger logger = Logger.getLogger(MessageWriter.class);

	public MessageWriter(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public <T extends Serializable> void write(Message<T> message) throws MessageException {
		try {
			writeHeader(message);
			writePayload(message);
		} catch (IOException ex) {
			throw new MessageException("Unable to write message", ex);
		}
	}

	private <T extends Serializable> void writeHeader(Message<T> message) throws IOException {
		byte mty = ClassUtils.lookupSerial(message.getClass());
		byte nad = mty;
		byte[] payloadData = getObject(message.getPayload());
		int length = payloadData.length;
		byte lnl = (byte) length;
		byte lnh = (byte) (length >> 8);
		outputStream.write(new byte[]{mty, nad, lnh, lnl});
	}

	private <T extends Serializable> void writePayload(Message<T> message) throws IOException {
		byte[] payloadData = getObject(message.getPayload());
		outputStream.write(payloadData);
	}

	private <T> byte[] getObject(T payload) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			return out.toByteArray();
		} catch (IOException ex) {
			logger.error("IOException while ", ex);
			return new byte[]{};
		}
	}
}
