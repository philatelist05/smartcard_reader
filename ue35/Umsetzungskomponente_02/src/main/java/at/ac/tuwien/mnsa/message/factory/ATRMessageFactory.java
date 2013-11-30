package at.ac.tuwien.mnsa.message.factory;

import at.ac.tuwien.mnsa.message.type.ATRMessage;
import at.ac.tuwien.mnsa.message.Message;
import at.ac.tuwien.mnsa.message.MessageCreationException;
import at.ac.tuwien.mnsa.message.MessageFactory;

public class ATRMessageFactory implements MessageFactory<byte[]> {

	public Message<byte[]> create(int type, byte[] payload) throws MessageCreationException{
		if (type != 1) {
			throw new MessageCreationException("Expected type is 1, but " + type + " given");
		}
		return new ATRMessage(payload);
	}
}
