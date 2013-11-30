package at.ac.tuwien.nokiaspi;

import at.ac.tuwien.mnsa.ClassUtils;
import at.ac.tuwien.mnsa.converter.BooleanByteConverter;
import at.ac.tuwien.mnsa.converter.ByteByteConverter;
import at.ac.tuwien.mnsa.message.factory.ATRMessageFactory;
import at.ac.tuwien.mnsa.message.factory.CardPresentMessageFactory;
import at.ac.tuwien.mnsa.message.type.Message;
import at.ac.tuwien.mnsa.message.exception.MessageCreationException;
import at.ac.tuwien.mnsa.message.factory.MessageFactory;
import at.ac.tuwien.mnsa.message.type.ATRMessage;
import at.ac.tuwien.mnsa.message.type.CardPresentMessage;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestMessageFactory {

	@Test
	public void testATRMessage() throws Exception {
		MessageFactory<byte[]> factory = new ATRMessageFactory(new ByteByteConverter());
		byte[] expected = new byte[]{0, 1, 0};
		byte serial = ClassUtils.lookupSerial(ATRMessage.class);
		Message<byte[]> message = factory.create(serial, expected);
		byte[] actual = message.getPayload();
		assertArrayEquals(expected, actual);
	}

	@Test(expected = MessageCreationException.class)
	public void testWithCastException() throws Exception {
		MessageFactory<byte[]> factory = new ATRMessageFactory(new ByteByteConverter());
		byte serial = ClassUtils.lookupSerial(Boolean.class);
		factory.create(serial, new byte[]{});
	}

	@Test
	public void testTrueCardPresetMessage() throws Exception {
		MessageFactory<Boolean> factory = new CardPresentMessageFactory(new BooleanByteConverter());
		byte[] expected = new byte[]{1};
		byte serial = ClassUtils.lookupSerial(CardPresentMessage.class);
		Message<Boolean> message = factory.create(serial, expected);
		boolean actual = message.getPayload();
		assertTrue(actual);
	}

	@Test
	public void testFalseCardPresetMessage() throws Exception {
		MessageFactory<Boolean> factory = new CardPresentMessageFactory(new BooleanByteConverter());
		byte[] expected = new byte[]{0};
		byte serial = ClassUtils.lookupSerial(CardPresentMessage.class);
		Message<Boolean> message = factory.create(serial, expected);
		boolean actual = message.getPayload();
		assertFalse(actual);
	}
}
