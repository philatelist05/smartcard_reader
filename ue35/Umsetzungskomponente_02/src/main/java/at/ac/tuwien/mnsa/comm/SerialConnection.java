package at.ac.tuwien.mnsa.comm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class SerialConnection implements Closeable {

	private static class SerialConnectionProperties {

		private final int baudrate;
		private final int parity;
		private final int stopbits;
		private final int databits;
		private final String portString;

		SerialConnectionProperties() throws ConfigurationException {
			PropertiesConfiguration configuration
					= new PropertiesConfiguration("serialConnection.properties");
			portString = configuration.getString("port", "COM1");
			databits = configuration.getInt("databits", 8);
			stopbits = configuration.getInt("stopbits", 1);
			parity = configuration.getInt("parity", 0);
			baudrate = configuration.getInt("baudrate", 9600);
		}

	}

	private SerialPort port;

	private SerialConnection(SerialConnectionProperties properties) throws SerialConnectionException {
		try {
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(properties.portString);
			port = (SerialPort) portIdentifier.open(this.getClass().getName(), 1000);
			port.setSerialPortParams(properties.baudrate, properties.databits, properties.stopbits, properties.parity);
		} catch (NoSuchPortException ex) {
			throw new SerialConnectionException("Port " + properties.portString + " does not exist", ex);
		} catch (PortInUseException ex) {
			throw new SerialConnectionException("Port " + properties.portString + " is already in use", ex);
		} catch (UnsupportedCommOperationException ex) {
			throw new SerialConnectionException("Can not set connection parameters", ex);
		}
	}

	public static SerialConnection open() throws SerialConnectionException {
		try {
			return new SerialConnection(new SerialConnectionProperties());
		} catch (ConfigurationException e) {
			throw new SerialConnectionException("Failed to load properties ", e);
		}
	}

	public InputStream getInputStream() throws IOException {
		return new BufferedInputStream(port.getInputStream());
	}

	public OutputStream getOutputStream() throws IOException {
		return new BufferedOutputStream(port.getOutputStream());
	}

	public void close() throws IOException {
		port.close();
	}
}
