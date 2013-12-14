package at.ac.tuwien.nokiaspi;

import org.junit.Test;

import javax.comm.CommPortIdentifier;
import java.util.Enumeration;

import static org.junit.Assert.assertTrue;


public class TestComm {

    /**
     * This is the port on which a serial device
     * is expected.
     */
    private static final String comPort = "COM3";

    @Test
    public void testAtLeastOneCommPortOpen() {
        Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();
        assertTrue(portIdentifiers.hasMoreElements());
    }

    @Test
    public void testConnection() {
        Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();
        boolean hasSerialPort = false;
        while (portIdentifiers.hasMoreElements()) {
            CommPortIdentifier portIdentifier = (CommPortIdentifier) portIdentifiers.nextElement();
            if (portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL &&
                    portIdentifier.getName().equals(comPort)) {
                hasSerialPort = true;
            }
        }
        assertTrue(hasSerialPort);
    }
}
