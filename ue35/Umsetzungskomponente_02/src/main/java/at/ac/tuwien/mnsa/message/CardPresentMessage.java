package at.ac.tuwien.mnsa.message;

public class CardPresentMessage implements Message<Boolean> {
	
	private final boolean payload;

	public CardPresentMessage(boolean payload) {
		this.payload = payload;
	}

	@Override
	public byte getIdentifier() {
		return 3;
	}

	@Override
	public Boolean getPayload() {
		return payload;
	}
	
}
