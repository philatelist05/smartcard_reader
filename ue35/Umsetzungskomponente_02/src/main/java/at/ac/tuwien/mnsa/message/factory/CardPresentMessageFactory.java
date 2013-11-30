package at.ac.tuwien.mnsa.message.factory;

import at.ac.tuwien.mnsa.ClassUtils;
import at.ac.tuwien.mnsa.converter.ByteConverter;
import at.ac.tuwien.mnsa.message.type.CardPresentMessage;
import at.ac.tuwien.mnsa.message.type.Message;
import at.ac.tuwien.mnsa.message.exception.MessageCreationException;

public class CardPresentMessageFactory extends MessageFactory<Boolean> {

	public CardPresentMessageFactory(ByteConverter<Boolean> converter) {
		super(converter);
	}

	@Override
	protected Message<Boolean> create(byte type, Boolean t) throws MessageCreationException {
		byte expectedType = ClassUtils.lookupSerial(CardPresentMessage.class);
		if (expectedType != type) {
			throw new MessageCreationException("Expected type is "
					+ expectedType + ", but " + type + " given");
		}
		return new CardPresentMessage(t);
	}
}
