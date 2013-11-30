package at.ac.tuwien.mnsa.message.type;

import at.ac.tuwien.mnsa.message.Message;

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
