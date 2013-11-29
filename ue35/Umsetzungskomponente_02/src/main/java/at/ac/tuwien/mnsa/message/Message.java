package at.ac.tuwien.mnsa.message;

public interface Message {
	byte getIdentifier();
	byte[] getPayload();
}
