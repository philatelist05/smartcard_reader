package at.ac.tuwien.mnsa.message.factory;

import at.ac.tuwien.mnsa.ClassUtils;
import at.ac.tuwien.mnsa.converter.ByteConverter;
import at.ac.tuwien.mnsa.message.type.APDUMessage;
import at.ac.tuwien.mnsa.message.type.Message;
import at.ac.tuwien.mnsa.message.exception.MessageCreationException;

public class APDUMessageFactory extends MessageFactory<byte[]> {

	public APDUMessageFactory(ByteConverter<byte[]> converter) {
		super(converter);
	}

	@Override
	public Message<byte[]> create(byte type, byte[] payload) throws MessageCreationException {
		byte expectedType = ClassUtils.lookupSerial(APDUMessage.class);
		if (expectedType != type) {
			throw new MessageCreationException("Expected type is " + 
					expectedType + ", but " + type + " given");
		}
		return new APDUMessage(payload);
	}
}
