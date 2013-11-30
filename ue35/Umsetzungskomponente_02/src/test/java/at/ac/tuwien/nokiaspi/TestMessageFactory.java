package at.ac.tuwien.nokiaspi;

import at.ac.tuwien.mnsa.message.ATRMessageFactory;
import at.ac.tuwien.mnsa.message.CardPresentMessageFactory;
import at.ac.tuwien.mnsa.message.Message;
import at.ac.tuwien.mnsa.message.MessageCreationException;
import at.ac.tuwien.mnsa.message.MessageFactory;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestMessageFactory {

	@Test
	public void testATRMessage() throws Exception {
		MessageFactory<byte[]> factory = new ATRMessageFactory();
		byte[] expected = new byte[]{0, 1, 0};
		Message<byte[]> message = factory.create(1, expected);
		byte[] actual = message.getPayload();
		assertArrayEquals(expected, actual);
	}

	@Test(expected = MessageCreationException.class)
	public void testWithCastException() throws Exception {
		MessageFactory<byte[]> factory = new ATRMessageFactory();
		factory.create(2, new byte[]{});
	}

	@Test
	public void testTrueCardPresetMessage() throws Exception {
		MessageFactory<Boolean> factory = new CardPresentMessageFactory();
		byte[] expected = new byte[]{1};
		Message<Boolean> message = factory.create(3, expected);
		boolean actual = message.getPayload();
		assertTrue(actual);
	}

	@Test
	public void testFalseCardPresetMessage() throws Exception {
		MessageFactory<Boolean> factory = new CardPresentMessageFactory();
		byte[] expected = new byte[]{0};
		Message<Boolean> message = factory.create(3, expected);
		boolean actual = message.getPayload();
		assertFalse(actual);
	}
}
