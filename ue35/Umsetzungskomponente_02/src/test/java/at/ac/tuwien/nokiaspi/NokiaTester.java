package at.ac.tuwien.nokiaspi;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.util.List;
import java.util.ListIterator;
import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.TerminalFactory;

import org.junit.Test;

public class NokiaTester {

	@Test
	public void testProviderList() {
		Provider[] x = Security.getProviders();

		for (int i = 0; i < x.length; i++) {
			System.out.println("Provider: " + x[i].getName());
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
