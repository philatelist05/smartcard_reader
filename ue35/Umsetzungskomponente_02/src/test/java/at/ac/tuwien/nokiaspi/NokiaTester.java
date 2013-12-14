package at.ac.tuwien.nokiaspi;

import org.junit.Test;

import javax.smartcardio.*;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.util.List;
import java.util.ListIterator;

public class NokiaTester {

    @Test
    public void testProviderList() {
        Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            System.out.println("Provider: " + provider.getName());
        }
    }

    @Test
    public void testNokiaProvider() throws NoSuchAlgorithmException,
            NoSuchProviderException, CardException {

        List<CardTerminal> l = null;
        Card card = null;

        TerminalFactory tf = TerminalFactory.getInstance("NokiaProvider", null);

        CardTerminals cts = tf.terminals();
        List<CardTerminal> ctl = cts.list();

        System.out.println("List of NokiaTerminals connected:");
        ListIterator<CardTerminal> i = ctl.listIterator();

        while (i.hasNext()) {
            CardTerminal ct = i.next();

            System.out.println("Reader: " + (ct).getName());

            // don't care about the protocol (either T=0 or T=1)
            card = ct.connect("*");
        }
        System.out.println("ATR: " + card.getATR());

    }
}
