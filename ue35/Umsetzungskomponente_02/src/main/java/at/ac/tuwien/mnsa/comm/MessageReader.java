package at.ac.tuwien.mnsa.comm;

import java.io.InputStream;

public class MessageReader {

	private final InputStream inputStream;

	public MessageReader(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public byte[] readAPDU() {
		return null;
	}

	public byte[] readATR() {
		return null;
	}
	
	public boolean isCardPresent() {
		return true;
	}
}
