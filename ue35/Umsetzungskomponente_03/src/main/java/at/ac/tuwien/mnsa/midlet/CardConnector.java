package at.ac.tuwien.mnsa.midlet;

import javax.microedition.contactless.TargetListener;
import javax.microedition.contactless.TargetProperties;
import javax.microedition.contactless.TargetType;
import java.io.IOException;

public class CardConnector implements TargetListener {

    private final Logger logger;
    private CardConnection connection;
    private String uid;


    public CardConnector() {
        logger = Logger.getLogger(getClass().getName());
    }

    public void targetDetected(TargetProperties[] targetProperties) {
        for (int i = 0; i < targetProperties.length; i++) {
            logger.info(i + " Target detected");
            if (targetProperties[i].hasTargetType(TargetType.ISO14443_CARD)
                    && targetProperties[i].getConnectionNames().length > 0) {
                TargetProperties targetProperty = targetProperties[i];
                logger.info("SmartCard detected ");

                try {
                    uid = targetProperty.getUid();
                    connection = CardConnection.open(targetProperty);
                } catch (IOException e) {
                    logger.error("Unable to open card connection", e);
                }
            }
        }
    }

    public CardConnection getCardConnection() throws IOException {
        if (connection == null) {
            throw new IOException("No target has been detected");
        }
        return connection;
    }

    public String getCardUid() throws IOException {
        if (uid == null) {
            throw new IOException("No target has been detected");
        }
        return uid;
    }

    public boolean isCardPresent() {
        return connection != null && uid != null;
    }
}
