package at.ac.tuwien.mnsa.message;

public class CardPresentMessage implements Message {
	
	private final byte[] payload;

	public CardPresentMessage(byte[] payload) {
		this.payload = payload;
	}

	public byte getIdentifier() {
		return 3;
	}

	public byte[] getPayload() {
		return payload;
	}
	
}
