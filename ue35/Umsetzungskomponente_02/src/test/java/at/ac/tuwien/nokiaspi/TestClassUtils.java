package at.ac.tuwien.nokiaspi;

import at.ac.tuwien.mnsa.ClassUtils;
import at.ac.tuwien.mnsa.message.type.APDUMessage;
import at.ac.tuwien.mnsa.message.type.ATRMessage;
import at.ac.tuwien.mnsa.message.type.CardPresentMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

public class TestClassUtils {

	@Test
	public void testUnequality1() throws Exception {
		byte serial1 = ClassUtils.lookupSerial(APDUMessage.class);
		byte serial2 = ClassUtils.lookupSerial(ATRMessage.class);
		assertNotEquals(serial1, serial2);
	}

	@Test
	public void testUnequality2() throws Exception {
		byte serial1 = ClassUtils.lookupSerial(APDUMessage.class);
		byte serial2 = ClassUtils.lookupSerial(CardPresentMessage.class);
		assertNotEquals(serial1, serial2);
	}

	@Test
	public void testUnequality3() throws Exception {
		byte serial1 = ClassUtils.lookupSerial(CardPresentMessage.class);
		byte serial2 = ClassUtils.lookupSerial(ATRMessage.class);
		assertNotEquals(serial1, serial2);
	}
	
	@Test
	public void testEquality1() throws Exception {
		byte serial1 = ClassUtils.lookupSerial(ATRMessage.class);
		byte serial2 = ClassUtils.lookupSerial(ATRMessage.class);
		assertEquals(serial1, serial2);
	}

	@Test
	public void testEquality2() throws Exception {
		byte serial1 = ClassUtils.lookupSerial(APDUMessage.class);
		byte serial2 = ClassUtils.lookupSerial(APDUMessage.class);
		assertEquals(serial1, serial2);
	}

	@Test
	public void testEquality3() throws Exception {
		byte serial1 = ClassUtils.lookupSerial(CardPresentMessage.class);
		byte serial2 = ClassUtils.lookupSerial(CardPresentMessage.class);
		assertEquals(serial1, serial2);
	}
}
