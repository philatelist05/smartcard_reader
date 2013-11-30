package at.ac.tuwien.mnsa.message.factory;

import at.ac.tuwien.mnsa.ClassUtils;
import at.ac.tuwien.mnsa.message.type.APDUMessage;
import at.ac.tuwien.mnsa.message.Message;
import at.ac.tuwien.mnsa.message.MessageCreationException;
import at.ac.tuwien.mnsa.message.MessageFactory;

public class APDUMessageFactory implements MessageFactory<byte[]> {

	public Message<byte[]> create(byte type, byte[] payload) throws MessageCreationException {
		byte expectedType = ClassUtils.lookupSerial(APDUMessage.class);
		if (expectedType != type) {
			throw new MessageCreationException("Expected type is " + 
					expectedType + ", but " + type + " given");
		}
		return new APDUMessage(payload);
	}
}
