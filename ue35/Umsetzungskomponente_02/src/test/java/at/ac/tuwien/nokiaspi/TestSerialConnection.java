package at.ac.tuwien.nokiaspi;

import at.ac.tuwien.mnsa.comm.SerialConnection;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import static org.junit.Assert.assertFalse;

public class TestSerialConnection {

    @Test(timeout = 5000L)
    public void testCommunication() throws Exception {
        SerialConnection connection = SerialConnection.open();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = bufferedReader.readLine();
        assertFalse(line.isEmpty());

        Thread.sleep(3000);
        PrintWriter printWriter = new PrintWriter(connection.getOutputStream(), true);
        printWriter.println("Hi!");
    }
}
