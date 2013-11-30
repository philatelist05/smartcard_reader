package at.ac.tuwien.mnsa.converter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BooleanByteConverter implements ByteConverter<Boolean> {

	public Boolean convert(byte[] bytes) {
		try {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
			DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
			return dataInputStream.readBoolean();
		} catch (IOException ex) {
			throw new AssertionError(ex);
		}
	}

	public byte[] convert(Boolean b) {
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
			dataOutputStream.writeBoolean(b);
			dataOutputStream.flush();
			return byteArrayOutputStream.toByteArray();
		} catch (IOException ex) {
			throw new AssertionError(ex);
		}
	}

}
