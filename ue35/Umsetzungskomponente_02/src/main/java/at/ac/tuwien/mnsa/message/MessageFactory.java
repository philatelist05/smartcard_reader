package at.ac.tuwien.mnsa.message;

public class MessageFactory {

	public Message create(int type, byte[] payload) {
		switch (type) {
			case 1:
				return new ATRMessage(payload);
			case 2:
				return new APDUMessage(payload);
			case 3:
				return new CardPresentMessage(payload);
			default:
				throw new AssertionError();
		}
	}
}
