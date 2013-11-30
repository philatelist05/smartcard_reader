package at.ac.tuwien.mnsa.message;

import java.io.Serializable;

public interface MessageFactory<T extends Serializable> {
	public Message<T> create(byte type, byte[] payload) throws MessageCreationException;
}
