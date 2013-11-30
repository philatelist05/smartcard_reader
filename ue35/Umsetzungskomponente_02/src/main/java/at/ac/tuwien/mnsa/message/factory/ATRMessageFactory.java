package at.ac.tuwien.mnsa.message.factory;

import at.ac.tuwien.mnsa.ClassUtils;
import at.ac.tuwien.mnsa.message.type.ATRMessage;
import at.ac.tuwien.mnsa.message.Message;
import at.ac.tuwien.mnsa.message.MessageCreationException;
import at.ac.tuwien.mnsa.message.MessageFactory;

public class ATRMessageFactory implements MessageFactory<byte[]> {

	public Message<byte[]> create(byte type, byte[] payload) throws MessageCreationException{
		byte expectedType = ClassUtils.lookupSerial(ATRMessage.class);
		if (expectedType != type) {
			throw new MessageCreationException("Expected type is " + 
					expectedType + ", but " + type + " given");
		}
		return new ATRMessage(payload);
	}
}
