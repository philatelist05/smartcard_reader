package at.ac.tuwien.mnsa.midlet;

import at.ac.tuwien.mnsa.message.Message;
import at.ac.tuwien.mnsa.message.MessageException;
import at.ac.tuwien.mnsa.message.MessageReader;
import at.ac.tuwien.mnsa.message.MessageWriter;

import javax.microedition.contactless.TargetProperties;
import javax.microedition.io.CommConnection;
import javax.microedition.io.Connector;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CommServerThread extends Thread {

	private final CommConnection commConnection;
	private final InputStream inputStream;
	private final OutputStream outputStream;
	private final MessageReader messageReader;
	private final MessageWriter messageWriter;
	private final CardConnector cardConnector;
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
		messageReader = new MessageReader(inputStream);
		messageWriter = new MessageWriter(outputStream);
		cardConnector = new CardConnector();
		isAlive = true;
	}

	public void run() {
		while (isAlive) {
			try {
				Message requestMessage = messageReader.read();
				Message responseMessage;
				switch (requestMessage.getType()) {
					case ATR:
						if (cardConnector.isCardPresent()) {
							TargetProperties targetProperties = cardConnector.getLatestTargetProperties();
							String uid = targetProperties.getUid();
							responseMessage = new Message(Message.MessageType.ATR, uid.getBytes());
						} else {
							responseMessage = new Message(Message.MessageType.ERROR);
						}
						break;
					case CARD_PRESENT:
						boolean cardPresent = cardConnector.isCardPresent();
						byte[] payload = {(byte) (cardPresent ? 1 : 0)};
						responseMessage = new Message(Message.MessageType.CARD_PRESENT, payload);
						break;
					case CONNECT:

						break;
				}
			} catch (MessageException e) {

			}

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
