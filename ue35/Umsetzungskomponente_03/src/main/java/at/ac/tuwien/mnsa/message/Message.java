package at.ac.tuwien.mnsa.message;

public class Message {
	public static class MessageType {
		public static final byte TEST = (byte) 0x00;
		public static final byte APDU = (byte) 0x01;
		public static final byte CARD_PRESENT=(byte) 0x03;
		public static final byte ATR=(byte) 0x02;
		public static final byte CONNECT=(byte) 0x04;
		public static final byte CLOSE=(byte) 0x05;
		public static final byte ERROR=(byte) 0x7F;

        private final byte value;

        public MessageType(byte value) {
            this.value = value;
        }

        public static MessageType valueOf(byte b) throws MessageException {
            if (b == TEST) {
                return new MessageType(TEST);
            } else if (b == APDU) {
                return new MessageType(APDU);
            } else if (b == CARD_PRESENT) {
                return new MessageType(CARD_PRESENT);
            } else if (b == ATR) {
                return new MessageType(ATR);
            } else if (b == CONNECT) {
                return new MessageType(CONNECT);
            } else if (b == CLOSE) {
                return new MessageType(CLOSE);
            } else if (b == ERROR) {
                return new MessageType(ERROR);
            }
            throw new MessageException("Unknown Type " +  b);
        }
    }

	private final byte type;

	private final byte[] payload;

	public Message(MessageType type, byte[] payload) {
		this.type = type.value;
		this.payload = payload;
	}

	public Message(MessageType type) {
		this(type, new byte[]{});
	}

	public byte[] getPayload() {
		return payload;
	}

	public byte getType() {
		return type;
	}
}
