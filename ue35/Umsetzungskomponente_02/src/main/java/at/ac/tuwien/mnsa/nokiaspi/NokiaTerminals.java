package at.ac.tuwien.mnsa.nokiaspi;

import java.util.ArrayList;
import java.util.List;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;

public class NokiaTerminals extends CardTerminals {

	/**
	 * Returns only one terminal with state ALL|CARD_PRESENT|CARD_INSERTION, in
	 * other case returns empty list.
	 */
	public List<CardTerminal> list(State state) throws CardException {
		List<CardTerminal> terminals = new ArrayList<CardTerminal>();
		switch (state) {
			case ALL:
			case CARD_PRESENT:
			case CARD_INSERTION:
				terminals.add(new NokiaTerminal());
				break;

		}
		return terminals;
	}

	/**
	 * Immediately returns true
	 */
	public boolean waitForChange(long l) throws CardException {
		return true;
	}
}
