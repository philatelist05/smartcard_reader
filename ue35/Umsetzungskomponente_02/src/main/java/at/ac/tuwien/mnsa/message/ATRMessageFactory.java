package at.ac.tuwien.mnsa.message;

public class ATRMessageFactory implements MessageFactory<byte[]> {

	public Message<byte[]> create(int type, byte[] payload) throws MessageCreationException{
		if (type != 1) {
			throw new MessageCreationException("Expected type is 1, but " + type + " given");
		}
		return new ATRMessage(payload);
	}
}
