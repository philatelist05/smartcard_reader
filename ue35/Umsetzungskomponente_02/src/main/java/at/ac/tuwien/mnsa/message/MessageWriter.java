package at.ac.tuwien.mnsa.message;

import java.io.IOException;
import java.io.OutputStream;

public class MessageWriter {

	private final OutputStream outputStream;

	public MessageWriter(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void write(MessageType type) throws MessageException {
		write(type, new byte[]{});
	}

	public void write(MessageType type, byte[] payloadData) throws MessageException {
		try {
			byte nodeAddress = type.getByteValue();
			int length = payloadData.length;
			byte lnl = (byte) length;
			byte lnh = (byte) (length >> 8);
			outputStream.write(new byte[]{type.getByteValue(), nodeAddress, lnh, lnl});
			outputStream.flush();
		} catch (IOException e) {
			throw new MessageException("Unable to write Message: " + String.format("%02x", payloadData), e);
		}
	}
}
