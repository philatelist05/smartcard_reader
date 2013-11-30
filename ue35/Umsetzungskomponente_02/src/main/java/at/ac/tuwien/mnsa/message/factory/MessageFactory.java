package at.ac.tuwien.mnsa.message.factory;

import at.ac.tuwien.mnsa.converter.ByteConverter;
import at.ac.tuwien.mnsa.message.type.Message;
import at.ac.tuwien.mnsa.message.exception.MessageCreationException;
import java.io.Serializable;

public abstract class MessageFactory<T extends Serializable> {
	
	private final ByteConverter<T> converter;

	public MessageFactory(ByteConverter<T> converter) {
		this.converter = converter;
	}
	
	public Message<T> create(byte type, byte[] payload) throws MessageCreationException {
		return create(type, converter.convert(payload));
	}
	
	protected abstract Message<T> create(byte type, T t) throws MessageCreationException;
}
