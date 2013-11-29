package at.ac.tuwien.mnsa.message;

import java.io.Serializable;

public class MessageFactory {

	public <T extends Serializable> Message<T> create(int type, byte[] payload) {
		switch (type) {
			case 1:
				return (Message<T>) new ATRMessage(payload);
			case 2:
				return (Message<T>) new APDUMessage(payload);
			case 3:
				return (Message<T>) new CardPresentMessage((payload[0] & 0x01) != 0);
			default:
				throw new IllegalStateException("Message type " + type + " not supported yet");
		}
	}
}
