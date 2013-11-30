package at.ac.tuwien.mnsa.message;

import java.io.Serializable;

public interface MessageFactory<T extends Serializable> {
	public Message<T> create(int type, byte[] payload) throws MessageCreationException;
}
