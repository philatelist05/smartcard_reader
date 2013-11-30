package at.ac.tuwien.mnsa.message.factory;

import at.ac.tuwien.mnsa.message.type.CardPresentMessage;
import at.ac.tuwien.mnsa.message.Message;
import at.ac.tuwien.mnsa.message.MessageCreationException;
import at.ac.tuwien.mnsa.message.MessageFactory;

public class CardPresentMessageFactory implements MessageFactory<Boolean> {

	public Message<Boolean> create(int type, byte[] payload) throws MessageCreationException {
		if (type != 3) {
			throw new MessageCreationException("Expected type is 3, but " + type + " given");
		}
		return new CardPresentMessage((payload[0] & 0x01) != 0);
	}
}
