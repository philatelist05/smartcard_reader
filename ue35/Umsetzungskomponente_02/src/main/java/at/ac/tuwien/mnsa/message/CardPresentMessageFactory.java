package at.ac.tuwien.mnsa.message;

public class CardPresentMessageFactory implements MessageFactory<Boolean> {

	public Message<Boolean> create(int type, byte[] payload) throws MessageCreationException {
		if (type != 3) {
			throw new MessageCreationException("Expected type is 3, but " + type + " given");
		}
		return new CardPresentMessage((payload[0] & 0x01) != 0);
	}
}
