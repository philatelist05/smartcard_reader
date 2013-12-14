package at.ac.tuwien.mnsa.nokiaspi;

import at.ac.tuwien.mnsa.comm.SerialConnection;
import at.ac.tuwien.mnsa.comm.SerialConnectionException;
import at.ac.tuwien.mnsa.message.MessageException;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

public class NokiaTerminal extends CardTerminal {

    public final static String NAME = "NokiaTerminal.Terminal";
    private static NokiaCard card = null;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Card connect(String string) throws CardException {
        try {
            if (card == null) {
                SerialConnection connection = SerialConnection.open();
                card = new NokiaCard(connection);
            }
            return card;
        } catch (SerialConnectionException ex) {
            throw new CardException(ex);
        } catch (MessageException ex) {
            throw new CardException(ex);
        }
    }

    @Override
    public boolean isCardPresent() throws CardException {
        return card != null && card.isPresent();
    }

    @Override
    public boolean waitForCardPresent(long l) throws CardException {
        return true;
    }

    @Override
    public boolean waitForCardAbsent(long l) throws CardException {
        return false;
    }
}
