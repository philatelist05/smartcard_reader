package at.ac.tuwien.mnsa.message;

import java.io.Serializable;

public interface Message<T extends Serializable> {
	byte getIdentifier();
	T getPayload();
}
