package at.ac.tuwien.nokiaspi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.comm.CommPort;
import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import org.junit.Test;

import static org.junit.Assert.*;


public class TestComm {
	
	/**
	 * This is the port on which a serial device
	 * is expected.
	 */
	private static final String comPort = "COM3";
	
	@Test
	public void testAtLeastOneCommPortOpen(){
		Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();
		assertTrue(portIdentifiers.hasMoreElements());
	}
	
	@Test
	public void testConnection(){
		Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();
		boolean hasSerialPort = false;
		while(portIdentifiers.hasMoreElements()){
			CommPortIdentifier portIdentifier = (CommPortIdentifier) portIdentifiers.nextElement();
			if(portIdentifier.getPortType()== CommPortIdentifier.PORT_SERIAL &&
					portIdentifier.getName().equals(comPort)){
				hasSerialPort = true;
			}
		}
		assertTrue(hasSerialPort);
	}
	
	@Test
	public void testCommunication() throws Exception{
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(comPort);
		CommPort port = portIdentifier.open("Junit", 3000);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(port.getInputStream()));
		String line = bufferedReader.readLine();
		assertFalse(line.isEmpty());
		
		Thread.sleep(3000);
		PrintWriter printWriter = new PrintWriter(port.getOutputStream(), true);
		printWriter.println("Hi!");
	}
}
