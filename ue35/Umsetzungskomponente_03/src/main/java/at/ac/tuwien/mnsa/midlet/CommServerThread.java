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
    private CardConnection cardConnection;

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
                Message responseMessage = null;
                byte messageType = requestMessage.getType();

                if (messageType == Message.MessageType.ATR) {
                    if (cardConnector.isCardPresent()) {
                        TargetProperties targetProperties = cardConnector.getLatestTargetProperties();
                        String uid = targetProperties.getUid();
                        responseMessage = new Message(new Message.MessageType(Message.MessageType.ATR), uid.getBytes());
                    } else {
                        responseMessage = new Message(new Message.MessageType(Message.MessageType.ERROR));
                    }
                } else if (messageType == Message.MessageType.CARD_PRESENT) {
                    boolean cardPresent = cardConnector.isCardPresent();
                    byte[] payload = {(byte) (cardPresent ? 1 : 0)};
                    responseMessage = new Message(new Message.MessageType(Message.MessageType.CARD_PRESENT), payload);
                } else if (messageType == Message.MessageType.CONNECT) {
                    try {
                        TargetProperties targetProperties = cardConnector.getLatestTargetProperties();
                        cardConnection = CardConnection.open(targetProperties);
                        responseMessage = new Message(new Message.MessageType(Message.MessageType.CONNECT));
                    } catch (IOException e) {
                        responseMessage = new Message(new Message.MessageType(Message.MessageType.ERROR));
                    }
                } else if (messageType == Message.MessageType.CLOSE) {
                    try {
                        close();
                        responseMessage = new Message(new Message.MessageType(Message.MessageType.CLOSE));
                    } catch (IOException e) {
                        //log Error
                    }
                } else if (messageType == Message.MessageType.TEST) {
                    responseMessage = requestMessage;
                } else if (messageType == Message.MessageType.APDU) {
                    if (cardConnector.isCardPresent() && cardConnection != null) {
                        try {
                            byte[] responsePayload = cardConnection.exchangeData(requestMessage.getPayload());
                            responseMessage = new Message(new Message.MessageType(Message.MessageType.APDU), responsePayload);
                        } catch (IOException e) {
                            responseMessage = new Message(new Message.MessageType(Message.MessageType.ERROR));
                        }
                    } else {
                        responseMessage = new Message(new Message.MessageType(Message.MessageType.ERROR));
                    }
                } else {
                    //log error
                }

                sendResponse(responseMessage);
            } catch (MessageException e) {
                //log error read Message
            }
        }
    }

    private void sendResponse(Message responseMessage) {
        if (responseMessage != null) {
            try {
                messageWriter.write(responseMessage);
            } catch (MessageException e) {
                try {
                    close();
                } catch (IOException e1) {
                    //Log error
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
