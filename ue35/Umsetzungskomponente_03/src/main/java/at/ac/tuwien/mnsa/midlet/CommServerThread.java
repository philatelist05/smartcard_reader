package at.ac.tuwien.mnsa.midlet;

import at.ac.tuwien.mnsa.message.MeMessage;
import at.ac.tuwien.mnsa.message.MeMessageException;
import at.ac.tuwien.mnsa.message.MeMessageReader;
import at.ac.tuwien.mnsa.message.MeMessageWriter;

import javax.microedition.io.CommConnection;
import javax.microedition.io.Connector;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CommServerThread extends Thread {

    private final CommConnection commConnection;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final MeMessageReader meMessageReader;
    private final MeMessageWriter meMessageWriter;
    private final CardConnector cardConnector;
    private final Logger logger;
    private volatile boolean isAlive;
    private CardConnection cardConnection;

    public CommServerThread(String commPort, CardConnector cardConnector) throws IOException {
        String ports = System.getProperty("microedition.commports");
        int index = ports.indexOf("USB", 0);
        if (index == -1) {
            throw new RuntimeException("No USB port found in the device");
        }
        commConnection = (CommConnection) Connector.open(commPort);
        inputStream = commConnection.openInputStream();
        outputStream = commConnection.openOutputStream();
        meMessageReader = new MeMessageReader(inputStream);
        meMessageWriter = new MeMessageWriter(outputStream);
        this.cardConnector = cardConnector;
        logger = Logger.getLogger(getClass().getName());
        isAlive = true;
    }

    public void run() {
        logger.info("Started mainLoop for Serverthread");
        while (isAlive) {
            try {
                logger.info("Trying to read new message ...");
                MeMessage requestMeMessage = meMessageReader.read();
                MeMessage responseMeMessage = null;
                byte messageType = requestMeMessage.getType();

                if (messageType == MeMessage.MessageType.ATR) {
                    logger.info("Got ATR message");
                    try {
                        String uid = cardConnector.getCardUid();
                        responseMeMessage = new MeMessage(new MeMessage.MessageType(MeMessage.MessageType.ATR), uid.getBytes());
                    } catch (IOException e) {
                        logger.error("No card is present when receiving ATR message");
                        responseMeMessage = new MeMessage(new MeMessage.MessageType(MeMessage.MessageType.ERROR));
                    }
                } else if (messageType == MeMessage.MessageType.CARD_PRESENT) {
                    logger.info("Got CARD_PRESENT message");
                    boolean cardPresent = cardConnector.isCardPresent();
                    byte[] payload = {(byte) (cardPresent ? 1 : 0)};
                    logger.info("Card present " + cardPresent);
                    responseMeMessage = new MeMessage(new MeMessage.MessageType(MeMessage.MessageType.CARD_PRESENT), payload);
                } else if (messageType == MeMessage.MessageType.CONNECT) {
                    logger.info("Got CONNECT message");
                    try {
                        cardConnection = cardConnector.getCardConnection();
                        responseMeMessage = new MeMessage(new MeMessage.MessageType(MeMessage.MessageType.CONNECT));
                    } catch (IOException e) {
                        logger.error("Can not open new card connection", e);
                        responseMeMessage = new MeMessage(new MeMessage.MessageType(MeMessage.MessageType.ERROR));
                    }
                } else if (messageType == MeMessage.MessageType.CLOSE) {
                    logger.info("Got CLOSE message");
                    try {
                        close();
                        responseMeMessage = new MeMessage(new MeMessage.MessageType(MeMessage.MessageType.CLOSE));
                    } catch (IOException e) {
                        logger.error("Unable to close connection", e);
                    }
                } else if (messageType == MeMessage.MessageType.TEST) {
                    logger.info("Got TEST message");
                    responseMeMessage = requestMeMessage;
                } else if (messageType == MeMessage.MessageType.APDU) {
                    logger.info("Got APDU message");
                    if (cardConnector.isCardPresent() && cardConnection != null) {
                        try {
                            byte[] responsePayload = cardConnection.exchangeData(requestMeMessage.getPayload());
                            responseMeMessage = new MeMessage(new MeMessage.MessageType(MeMessage.MessageType.APDU), responsePayload);
                        } catch (IOException e) {
                            logger.error("Unable to exchange APDU message", e);
                            responseMeMessage = new MeMessage(new MeMessage.MessageType(MeMessage.MessageType.ERROR));
                        }
                    } else {
                        logger.error("Either no card inserted or no connection is present");
                        responseMeMessage = new MeMessage(new MeMessage.MessageType(MeMessage.MessageType.ERROR));
                    }
                } else {
                    logger.error("Unknown message Type " + messageType);
                }

                sendResponse(responseMeMessage);
            } catch (MeMessageException e) {
                logger.error("Unable to receive message", e);
            }
        }
    }

    private void sendResponse(MeMessage responseMeMessage) {
        if (responseMeMessage != null) {
            try {
                meMessageWriter.write(responseMeMessage);
                logger.info("Sending Message " + responseMeMessage.toString());
            } catch (MeMessageException e) {
                try {
                    close();
                } catch (IOException e1) {
                    logger.error("Unable to close connection", e1);
                } finally {
                    logger.error("Unable to send message " + responseMeMessage, e);
                }
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
                try {
                    commConnection.close();
                } finally {
                    if (cardConnection != null) {
                        cardConnection.close();
                    }
                }
            }
        }
    }
}
