package at.ac.tuwien.mnsa.nokiaspi;

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
		if (card == null) {
			card = new NokiaCard();
		}
		return card;
	}

	@Override
	public boolean isCardPresent() throws CardException {
		//TODO: Implement
		return true;
	}

	@Override
	public boolean waitForCardPresent(long l) throws CardException {
		//TODO: Implement
		return true;
	}

	@Override
	public boolean waitForCardAbsent(long l) throws CardException {
		return false;
	}
}
