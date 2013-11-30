package at.ac.tuwien.mnsa.message.type;

public class ATRMessage implements Message<byte[]> {
	
	private final byte[] payload;

	public ATRMessage(byte[] payload) {
		this.payload = payload;
	}

	@Override
	public byte[] getPayload() {
		return payload;
	}
	
}
