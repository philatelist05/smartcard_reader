package at.ac.tuwien.mnsa.nokiaspi;

import at.ac.tuwien.mnsa.comm.MessageReader;
import at.ac.tuwien.mnsa.comm.MessageWriter;
import at.ac.tuwien.mnsa.comm.SerialConnection;
import at.ac.tuwien.mnsa.comm.SerialConnectionException;
import java.io.IOException;
import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

public class NokiaCard extends Card {

	private static final String T0_PROTOCOL = "T=0";
	private final NokiaChannel basicChannel;
	private final MessageWriter messageWriter;
	private final MessageReader messageReader;

	public NokiaCard(SerialConnection connection) throws SerialConnectionException {
		try {
			basicChannel = new NokiaChannel(this, 0, connection);
			messageWriter = new MessageWriter(connection.getOutputStream());
			messageReader = new MessageReader(connection.getInputStream());
		} catch (IOException ex) {
			throw new SerialConnectionException("Can not get Outputstream from connection", ex);
		}
	}

	@Override
	public ATR getATR() {
		byte[] atrData = messageReader.readATR();
		return new ATR(atrData);
	}

	@Override
	public String getProtocol() {
		return T0_PROTOCOL;
	}

	@Override
	public CardChannel getBasicChannel() {
		return basicChannel;
	}

	@Override
	public CardChannel openLogicalChannel() throws CardException {
		return basicChannel;
	}

	@Override
	public void beginExclusive() throws CardException {
	}

	@Override
	public void endExclusive() throws CardException {
	}

	@Override
	public byte[] transmitControlCommand(int i, byte[] bytes)
			throws CardException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void disconnect(boolean bln) throws CardException {
	}

	public ResponseAPDU transmitCommand(CommandAPDU capdu) {
		byte[] requestData = capdu.getBytes();
		messageWriter.writeAPDU(requestData);
		byte[] responseData = messageReader.readAPDU();
		return new ResponseAPDU(responseData);
	}

	public boolean isPresent() {
		return messageReader.isCardPresent();
	}
}
