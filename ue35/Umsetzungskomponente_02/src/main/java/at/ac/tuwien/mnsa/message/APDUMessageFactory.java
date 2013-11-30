package at.ac.tuwien.mnsa.message;

public class APDUMessageFactory implements MessageFactory<byte[]> {

	public Message<byte[]> create(int type, byte[] payload) throws MessageCreationException {
		if (type != 2) {
			throw new MessageCreationException("Expected type is 2, but " + type + " given");
		}
		return new APDUMessage(payload);
	}
}
