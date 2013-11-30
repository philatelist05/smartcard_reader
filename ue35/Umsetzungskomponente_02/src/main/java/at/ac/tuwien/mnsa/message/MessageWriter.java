package at.ac.tuwien.mnsa.message;

import at.ac.tuwien.mnsa.message.exception.MessageException;
import at.ac.tuwien.mnsa.message.type.Message;
import at.ac.tuwien.mnsa.ClassUtils;
import at.ac.tuwien.mnsa.converter.ByteConverter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import org.apache.log4j.Logger;

public class MessageWriter {

	private final OutputStream outputStream;
	private final Logger logger = Logger.getLogger(MessageWriter.class);

	public MessageWriter(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public <T extends Serializable> void write(Message<T> message, ByteConverter<T> converter) throws MessageException {
		try {
			writeHeader(message, converter);
			writePayload(message, converter);
		} catch (IOException ex) {
			throw new MessageException("Unable to write message", ex);
		}
	}

	private <T extends Serializable> void writeHeader(Message<T> message, ByteConverter<T> converter) throws IOException {
		byte mty = ClassUtils.lookupSerial(message.getClass());
		byte nad = mty;
		byte[] payloadData = converter.convert(message.getPayload());
		int length = payloadData.length;
		byte lnl = (byte) length;
		byte lnh = (byte) (length >> 8);
		outputStream.write(new byte[]{mty, nad, lnh, lnl});
	}

	private <T extends Serializable> void writePayload(Message<T> message, ByteConverter<T> converter) throws IOException {
		byte[] payloadData = converter.convert(message.getPayload());
		outputStream.write(payloadData);
	}
}
