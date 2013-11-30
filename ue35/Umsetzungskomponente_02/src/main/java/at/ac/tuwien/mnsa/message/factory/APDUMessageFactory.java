package at.ac.tuwien.mnsa.message.factory;

import at.ac.tuwien.mnsa.message.type.APDUMessage;
import at.ac.tuwien.mnsa.message.Message;
import at.ac.tuwien.mnsa.message.MessageCreationException;
import at.ac.tuwien.mnsa.message.MessageFactory;

public class APDUMessageFactory implements MessageFactory<byte[]> {

	public Message<byte[]> create(int type, byte[] payload) throws MessageCreationException {
		if (type != 2) {
			throw new MessageCreationException("Expected type is 2, but " + type + " given");
		}
		return new APDUMessage(payload);
	}
}
