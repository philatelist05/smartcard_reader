package at.ac.tuwien.mnsa.midlet;

import javax.microedition.contactless.ContactlessException;
import javax.microedition.contactless.TargetProperties;
import javax.microedition.contactless.sc.ISO14443Connection;
import javax.microedition.io.Connector;
import java.io.IOException;

public class CardConnection {

	private final ISO14443Connection connection;

	public CardConnection(ISO14443Connection connection) {
		this.connection = connection;
	}

	public static CardConnection open(TargetProperties targetProperties) throws IOException {
		String url = targetProperties.getUrl(targetProperties.getConnectionNames()[0]);
		ISO14443Connection connection = (ISO14443Connection) Connector.open(url);
		return new CardConnection(connection);
	}

	public byte[] exchangeData(byte[] request) throws IOException {
		try {
			return connection.exchangeData(request);
		} catch (ContactlessException e) {
			throw new IOException(e);
		}
	}

	public void close() throws IOException {
		connection.close();
	}
}
