package at.ac.tuwien.mnsa;

import javax.microedition.io.CommConnection;
import javax.microedition.io.Connector;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CommServerThread extends Thread {

	private final CommConnection commConnection;
	private final InputStream inputStream;
	private final OutputStream outputStream;
	private volatile boolean isAlive;

	public CommServerThread(String commPort) throws IOException {
		String ports = System.getProperty("microedition.commports");
		int index = ports.indexOf("USB", 0);
		if (index == -1) {
			throw new RuntimeException("No USB port found in the device");
		}
		commConnection = (CommConnection) Connector.open(commPort);
		inputStream = commConnection.openInputStream();
		outputStream = commConnection.openOutputStream();
		isAlive = true;
	}

	public void run() {
		while (isAlive) {

		}
	}

	public void close() throws IOException {
		isAlive = false;
		try {
			inputStream.close();
		} finally {
			try {
				outputStream.close();
			} finally {
				commConnection.close();
			}
		}
	}
}
