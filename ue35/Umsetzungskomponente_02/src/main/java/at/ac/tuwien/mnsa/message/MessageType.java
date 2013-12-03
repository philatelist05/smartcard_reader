package at.ac.tuwien.mnsa.message;

public enum MessageType {
	TEST((byte) 0x00),
	APDU((byte) 0x01),
	CARD_PRESENT((byte) 0x03),
	ATR((byte) 0x02),
	CONNECT((byte) 0x04),
	CLOSE((byte) 0x05),
	ERROR((byte) 0x7F);

	private final byte b;

	private MessageType(byte b) {
		this.b = b;
	}

	public byte getByteValue() {
		return b;
	}
}
