package at.ac.tuwien.mnsa.message;

import java.io.IOException;
import java.io.OutputStream;

public class MessageWriter {

	private final OutputStream outputStream;

	public MessageWriter(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void write(Message message) throws IOException {
		writeHeader(message);
		writePayload(message);
	}

	private void writeHeader(Message message) throws IOException {
		byte mty = message.getIdentifier();
		byte nad = mty;
		int length = message.getPayload().length;
		byte lnl = (byte) length;
		byte lnh = (byte) (length >> 8);
		outputStream.write(new byte[]{mty, nad, lnh, lnl});
	}

	private void writePayload(Message message) throws IOException {
		outputStream.write(message.getPayload());
	}
}
