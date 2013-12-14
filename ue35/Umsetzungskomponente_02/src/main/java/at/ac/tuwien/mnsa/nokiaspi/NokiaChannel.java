package at.ac.tuwien.mnsa.nokiaspi;

import at.ac.tuwien.mnsa.comm.SerialConnection;

import javax.smartcardio.*;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * CardChannel implementation class.
 */
public class NokiaChannel extends CardChannel {

    private final NokiaCard card;
    private final int channel;
    private final SerialConnection connection;

    public NokiaChannel(NokiaCard card, int channel, SerialConnection connection) {
        this.card = card;
        this.channel = channel;
        this.connection = connection;
    }

    @Override
    public Card getCard() {
        return card;
    }

    @Override
    public int getChannelNumber() {
        return channel;
    }

    @Override
    public ResponseAPDU transmit(CommandAPDU capdu) throws CardException {
        return card.transmitCommand(capdu);
    }

    @Override
    public int transmit(ByteBuffer bb, ByteBuffer bb1) throws CardException {
        ResponseAPDU response = transmit(new CommandAPDU(bb));
        byte[] binaryResponse = response.getBytes();
        bb1.put(binaryResponse);
        return binaryResponse.length;
    }

    @Override
    public void close() throws CardException {
        try {
            connection.close();
        } catch (IOException ex) {
            throw new CardException(ex);
        }
    }

}
