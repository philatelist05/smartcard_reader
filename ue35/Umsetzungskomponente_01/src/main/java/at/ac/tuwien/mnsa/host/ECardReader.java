package at.ac.tuwien.mnsa.host;

import org.apache.log4j.Logger;

import javax.smartcardio.*;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ListIterator;

public class ECardReader {

    // Select APDU
    // private static byte[] SELECT = {(byte) 0x00, (byte) 0xA4, (byte) 0x00, (byte) 0x0C, (byte) 0x02, (byte) 0x3f, (byte) 0x00};
    //private static final byte[] SELECT = {(byte) 0x00, (byte) 0xA4, (byte) 0x04, (byte) 0x0C, (byte) 0x08, (byte) 0xD0, (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x17, (byte) 0x01, (byte) 0x01, (byte) 0x01};
    private static byte[] SELECT = { (byte) 0x00, (byte) 0xA4, (byte) 0x04, (byte) 0x00};

    private static final CommandAPDU SELECT_APDU = new CommandAPDU(SELECT);

    private static final byte[] READ_BINARY = {(byte) 0x00, (byte) 0xB0, (byte) 0x82, (byte) 0x00, (byte) 0x00};
    private static final CommandAPDU READ_BINARY_APDU = new CommandAPDU(READ_BINARY);

    private final static String terminalName = "NokiaTerminal.Terminal";

    private final static String T_1 = "T=1";
    private final static String T_0 = "T=0";

    protected final static Logger logger = Logger.getLogger(ECardReader.class);

    public static void main(String[] args) {

        // Factory erstellen
        TerminalFactory tf = null;
        try {
            tf = TerminalFactory.getInstance("NokiaProvider", null);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Unable to find NokiaProvider", e);
        }

        // Terminals holen
        CardTerminals ct = tf.terminals();
        List<CardTerminal> l = null;
        Card card;

        try {
            l = ct.list();
        } catch (CardException e) {
            logger.error("Error listing Terminals: ", e);
        }

        // Liste ausgeben
        logger.info("List of PC/SC Readers connected:");
        ListIterator<CardTerminal> i = l.listIterator();
        while (i.hasNext()) {
            logger.info("Reader: " + i.next().getName());
        }

        CardTerminal c = ct.getTerminal(terminalName);

        if (c != null) {
            logger.info("Terminal '" + c.getName() + "' fetched");
        } else {
            logger.error("Error - Terminal not avaialble; exit");
            return;
        }
        // Endlosschleife zum Karten lesen
        try {

            // Warten bis eine Karte gefunden ist.
            while (c.isCardPresent()) {

                // Kuzrse Pause wegen Endlosloop
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {

                }

                // Hier Protokoll definieren
                try {

                    // Versuchen mit T=1;
                    card = c.connect(T_1);
                    logger.info("Terminal connected mit T=1");
                } catch (CardException e) {
                    logger.error("Terminal NOT onnected via T=1 ",e);

                    try {

                        // Versuchen mit T=0;
                        card = c.connect(T_0);
                        logger.info("Terminal connected mit T=0");
                    } catch (CardException ex) {
                        logger.error("Terminal NOT onnected via T=0 ",ex);
                        return;
                    }

                }

                if (card != null) {
                    logger.info("Card okay");
                } else {
                    logger.error("Card is NULL!");
                    return;
                }

                // ATR holen
                logger.info("ATR: " + arrayToHex(((ATR) card.getATR()).getBytes()));

                // Basic Channle holen (= ein virtueller Kanal zur CarteE)
                CardChannel ch = card.getBasicChannel();

                // Root Files selektieren
                if (check9000andPrint(ch.transmit(SELECT_APDU))) {
                    logger.info("SELECT OKAY");
                } else {
                    logger.error("SELECT NOT OKAY");
                    return;
                }

                // Reading machen
                ResponseAPDU rp = ch.transmit(READ_BINARY_APDU);

                if (check9000andPrint(rp)) {
                    logger.info("READ BINARY OKAY");
                    logger.info("Out: " + new String(rp.getBytes()));

                } else {
                    logger.error("READ BINARY NOT OKAY");
                    return;
                }

                break;

            }// while
        }// try
        catch (CardException e) {
            logger.info("Error isCardPresent()", e);
        }

        logger.info("exit");
    }

    // wenn eine Reponse auf 0x9000 Endet, ist alles gut.
    public static boolean check9000(ResponseAPDU ra) {
        byte[] response = ra.getBytes();
        return (response[response.length - 2] == (byte) 0x90 && response[response.length - 1] == (byte) 0x00);
    }

    // wenn eine Reponse auf 0x9000 Endet, ist alles gut.
    public static boolean check9000andPrint(ResponseAPDU ra) {
        byte[] response = ra.getBytes();
        logger.info(arrayToHex(response));
        return (response[response.length - 2] == (byte) 0x90 && response[response.length - 1] == (byte) 0x00);
    }

    private static String arrayToHex(byte[] data) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < data.length; i++) {
            String bs = Integer.toHexString(data[i] & 0xFF);
            if (bs.length() == 1) {
                sb.append(0);
            }
            sb.append(bs);
        }
        return sb.toString();
    }
}
