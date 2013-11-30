package at.ac.tuwien.mnsa.message.type;

import at.ac.tuwien.mnsa.message.Message;

public class APDUMessage implements Message<byte[]> {
	
	private final byte[] payload;

	public APDUMessage(byte[] payload) {
		this.payload = payload;
	}

	@Override
	public byte[] getPayload() {
		return payload;
	}
	
}
