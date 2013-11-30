package at.ac.tuwien.mnsa.message.factory;

import at.ac.tuwien.mnsa.ClassUtils;
import at.ac.tuwien.mnsa.converter.ByteConverter;
import at.ac.tuwien.mnsa.message.type.ATRMessage;
import at.ac.tuwien.mnsa.message.type.Message;
import at.ac.tuwien.mnsa.message.exception.MessageCreationException;

public class ATRMessageFactory extends MessageFactory<byte[]> {

	public ATRMessageFactory(ByteConverter<byte[]> converter) {
		super(converter);
	}

	@Override
	public Message<byte[]> create(byte type, byte[] payload) throws MessageCreationException{
		byte expectedType = ClassUtils.lookupSerial(ATRMessage.class);
		if (expectedType != type) {
			throw new MessageCreationException("Expected type is " + 
					expectedType + ", but " + type + " given");
		}
		return new ATRMessage(payload);
	}
}
