package at.ac.tuwien.mnsa.message;

public class APDUMessage implements Message {
	
	private final byte[] payload;

	public APDUMessage(byte[] payload) {
		this.payload = payload;
	}

	public byte getIdentifier() {
		return 2;
	}

	public byte[] getPayload() {
		return payload;
	}
	
}
