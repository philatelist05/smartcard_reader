package at.ac.tuwien.mnsa;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectStreamClass;
import java.io.Serializable;

public class ClassUtils {

	public static byte lookupSerial(Class<? extends Serializable> clazz) {
		ObjectStreamClass objectStreamClass = ObjectStreamClass.lookup(clazz);
		long serialVersionUID = objectStreamClass.getSerialVersionUID();
		return calculateLRC(toByteArray(serialVersionUID));
	}

	/**
	 * Calculates the checksum in compliance with the ISO 1155 standard More
	 * info: http://en.wikipedia.org/wiki/Longitudinal_redundancy_check
	 *
	 * @param data array to calculate checksum for
	 * @return returns the calculated checksum in byte format
	 */
	private static byte calculateLRC(byte[] data) {
		byte checksum = 0;
		for (int i = 0; i < data.length; i++) {
			checksum = (byte) ((checksum + data[i]) & 0xFF);
		}
		checksum = (byte) (((checksum ^ 0xFF) + 1) & 0xFF);
		return checksum;
	}

	private static byte[] toByteArray(long value) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeLong(value);
			dos.flush();
			return baos.toByteArray();
		} catch (IOException ex) {
			throw new AssertionError();
		}
	}
}
