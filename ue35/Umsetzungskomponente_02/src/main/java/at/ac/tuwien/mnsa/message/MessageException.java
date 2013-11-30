package at.ac.tuwien.mnsa.message;


public class MessageException extends Exception {
	
	public MessageException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public MessageException(Throwable ex) {
		super(ex);
	}
	
	public MessageException(String message) {
		super(message);
	}
}
