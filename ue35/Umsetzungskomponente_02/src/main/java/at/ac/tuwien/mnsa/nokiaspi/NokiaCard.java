package at.ac.tuwien.mnsa.nokiaspi;

import at.ac.tuwien.mnsa.comm.SerialConnection;
import at.ac.tuwien.mnsa.comm.SerialConnectionException;
import at.ac.tuwien.mnsa.message.Message;
import at.ac.tuwien.mnsa.message.MessageException;
import at.ac.tuwien.mnsa.message.MessageReader;
import at.ac.tuwien.mnsa.message.MessageWriter;
import org.apache.log4j.Logger;

import javax.smartcardio.*;
import java.io.IOException;

public class NokiaCard extends Card {

	private static final String T0_PROTOCOL = "T=0";
	private static final String DEFAULT_ATR = "3BFA1800008131FE454A434F5033315632333298";
	private final NokiaChannel basicChannel;
	private final MessageWriter messageWriter;
	private final MessageReader messageReader;
	private final Logger logger = Logger.getLogger(NokiaCard.class);

	public NokiaCard(SerialConnection connection) throws SerialConnectionException, MessageException {
		try {
			basicChannel = new NokiaChannel(this, 0, connection);
			messageWriter = new MessageWriter(connection.getOutputStream());
			messageReader = new MessageReader(connection.getInputStream());
			connect();
		} catch (IOException ex) {
			throw new SerialConnectionException("Can not get Outputstream from connection", ex);
		}
	}

	private void connect() throws MessageException {
		messageWriter.write(new Message(Message.MessageType.CONNECT));
		messageReader.read();
	}

	@Override
	public ATR getATR() {
		try {
			messageWriter.write(new Message(Message.MessageType.ATR));
			Message message = messageReader.read();
			return new ATR(message.getPayload());
		} catch (MessageException e) {
			logger.error("Can not get ATR message", e);
			return new ATR(DEFAULT_ATR.getBytes());
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
		try {
			messageWriter.write(new Message(Message.MessageType.CLOSE));
			messageReader.read();
		} catch (MessageException e) {
			throw new CardException(e);
		}
	}

	public ResponseAPDU transmitCommand(CommandAPDU capdu) throws CardException {
		try {
			byte[] requestData = capdu.getBytes();
			messageWriter.write(new Message(Message.MessageType.APDU, requestData));
			Message message = messageReader.read();
			return new ResponseAPDU(message.getPayload());
		} catch (MessageException e) {
			throw new CardException(e);
		}
	}

	public boolean isPresent() throws CardException{
		try {
			messageWriter.write(new Message(Message.MessageType.CARD_PRESENT));
			Message message = messageReader.read();
			byte[] payload = message.getPayload();
			return payload.length == 1 && payload[0] == 1;
		} catch (MessageException e) {
			throw new CardException(e);
		}
	}
}
