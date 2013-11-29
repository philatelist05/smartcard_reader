package at.ac.tuwien.mnsa.message;

public class ATRMessage implements Message {
	
	private final byte[] payload;

	public ATRMessage(byte[] payload) {
		this.payload = payload;
	}

	public byte getIdentifier() {
		return 1;
	}

	public byte[] getPayload() {
		return payload;
	}
	
}
