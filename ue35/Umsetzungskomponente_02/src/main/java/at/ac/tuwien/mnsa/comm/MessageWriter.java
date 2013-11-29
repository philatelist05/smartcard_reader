package at.ac.tuwien.mnsa.comm;

import java.io.OutputStream;


public class MessageWriter {
	
	private final OutputStream outputStream;

	public MessageWriter(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
	public void writeAPDU(byte[] data) {
		
	}
}
