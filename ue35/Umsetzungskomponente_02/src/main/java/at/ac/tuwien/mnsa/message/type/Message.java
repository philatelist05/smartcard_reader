package at.ac.tuwien.mnsa.message.type;

import java.io.Serializable;

public interface Message<T extends Serializable> extends Serializable {
	T getPayload();
}