package at.ac.tuwien.nokiaspi;

import at.ac.tuwien.mnsa.message.Message;
import at.ac.tuwien.mnsa.message.MessageFactory;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestMessageFactory {

	@Test
	public void testATRMessage() {
		MessageFactory factory = new MessageFactory();
		byte[] expected = new byte[]{0, 1, 0};
		Message<byte[]> message = factory.create(1, expected);
		byte[] actual = message.getPayload();
		assertArrayEquals(expected, actual);
	}

	@Test(expected = ClassCastException.class)
	public void testWithCastException() {
		MessageFactory factory = new MessageFactory();
		byte[] expected = new byte[]{1};
		Message<Boolean> message = factory.create(1, expected);
		Boolean actual = message.getPayload();
	}

	@Test
	public void testTrueCardPresetMessage() {
		MessageFactory factory = new MessageFactory();
		byte[] expected = new byte[]{1};
		Message<Boolean> message = factory.create(3, expected);
		boolean actual = message.getPayload();
		assertTrue(actual);
	}
	
	@Test
	public void testFalseCardPresetMessage() {
		MessageFactory factory = new MessageFactory();
		byte[] expected = new byte[]{0};
		Message<Boolean> message = factory.create(3, expected);
		boolean actual = message.getPayload();
		assertFalse(actual);
	}
}
