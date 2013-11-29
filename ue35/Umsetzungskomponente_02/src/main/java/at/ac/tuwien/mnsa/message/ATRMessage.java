package at.ac.tuwien.mnsa.message;

public class ATRMessage implements Message<byte[]> {
	
	private final byte[] payload;

	public ATRMessage(byte[] payload) {
		this.payload = payload;
	}

	@Override
	public byte getIdentifier() {
		return 1;
	}

	@Override
	public byte[] getPayload() {
		return payload;
	}
	
}
