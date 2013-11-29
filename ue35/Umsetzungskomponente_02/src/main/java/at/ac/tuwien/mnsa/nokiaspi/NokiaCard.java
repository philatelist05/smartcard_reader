package at.ac.tuwien.mnsa.nokiaspi;

import at.ac.tuwien.mnsa.message.MessageReader;
import at.ac.tuwien.mnsa.message.MessageWriter;
import at.ac.tuwien.mnsa.comm.SerialConnection;
import at.ac.tuwien.mnsa.comm.SerialConnectionException;
import at.ac.tuwien.mnsa.message.APDUMessage;
import at.ac.tuwien.mnsa.message.CardPresentMessage;
import at.ac.tuwien.mnsa.message.Message;
import at.ac.tuwien.mnsa.message.MessageFactory;
import java.io.IOException;
import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import org.apache.log4j.Logger;

public class NokiaCard extends Card {

	private static final String T0_PROTOCOL = "T=0";
	private final NokiaChannel basicChannel;
	private final MessageWriter messageWriter;
	private final MessageReader messageReader;
	private final Logger logger = Logger.getLogger(NokiaCard.class);

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
		Message<byte[]> message;
		try {
			message = messageReader.read(new MessageFactory());
			return new ATR(message.getPayload());
		} catch (IOException ex) {
			logger.error("Unable to transmit getATR() command", ex);
			return new ATR(new byte[]{});
		}
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

	public ResponseAPDU transmitCommand(CommandAPDU capdu) throws CardException {
		try {
			byte[] requestData = capdu.getBytes();
			messageWriter.write(new APDUMessage(requestData));
			Message<byte[]> message = messageReader.read(new MessageFactory());
			return new ResponseAPDU(message.getPayload());
		} catch (IOException ex) {
			throw new CardException("Unable to transmit command " + capdu, ex);
		}
	}

	public boolean isPresent() {
		try {
			messageWriter.write(new CardPresentMessage(true));
			Message<Boolean> message = messageReader.read(new MessageFactory());
			return message.getPayload();
		} catch (IOException ex) {
			logger.error("Unable to send isPresent message.", ex);
			return false;
		}
	}
}
