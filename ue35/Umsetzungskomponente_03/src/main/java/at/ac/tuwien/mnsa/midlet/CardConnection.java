package at.ac.tuwien.mnsa.midlet;


import javax.microedition.contactless.ContactlessException;
import javax.microedition.contactless.TargetProperties;
import javax.microedition.contactless.sc.ISO14443Connection;
import javax.microedition.io.Connector;
import java.io.IOException;

public class CardConnection {

    private static ISO14443Connection connection = null;
    private static final Logger logger = Logger.getLogger(CardConnection.class.getName());

    private CardConnection() {
    }

    public static CardConnection open(TargetProperties targetProperties) throws IOException {
        String url = targetProperties.getUrl(targetProperties.getConnectionNames()[0]);
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                logger.error("Unable to close previous connection", e);
            }
        }
        connection = (ISO14443Connection) Connector.open(url);
        return new CardConnection();
    }

    public byte[] exchangeData(byte[] request) throws IOException {
        if (connection == null) {
            throw new IOException("Lost connection to card");
        }
        try {
            return connection.exchangeData(request);
        } catch (ContactlessException e) {
                close();
                throw new IOException(e.toString());
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (IOException e) {
            logger.error("Unable to close card connection", e);
        }
    }
}
