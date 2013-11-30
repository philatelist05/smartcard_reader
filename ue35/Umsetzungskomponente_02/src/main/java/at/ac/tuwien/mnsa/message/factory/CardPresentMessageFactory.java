package at.ac.tuwien.mnsa.message.factory;

import at.ac.tuwien.mnsa.ClassUtils;
import at.ac.tuwien.mnsa.message.type.CardPresentMessage;
import at.ac.tuwien.mnsa.message.Message;
import at.ac.tuwien.mnsa.message.MessageCreationException;
import at.ac.tuwien.mnsa.message.MessageFactory;

public class CardPresentMessageFactory implements MessageFactory<Boolean> {

	public Message<Boolean> create(byte type, byte[] payload) throws MessageCreationException {
		byte expectedType = ClassUtils.lookupSerial(CardPresentMessage.class);
		if (expectedType != type) {
			throw new MessageCreationException("Expected type is " + 
					expectedType + ", but " + type + " given");
		}
		return new CardPresentMessage((payload[0] & 0x01) != 0);
	}
}
