package edu.vuum.mocca;

import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @class PingPongRightTest
 * 
 * @brief This JUnit test checks the PingPong program to make sure it's working
 *        properly.
 */
public class PingPongRightTest {
    /*
     * These data members are used to capture [
     * System.out.println() ] for testing.
     */
    private final ByteArrayOutputStream outContent =
        new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent =
        new ByteArrayOutputStream();

    /*
     * Setup the output Capturing.
     */
    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    /*
     * Tear-down the output Capturing.
     */
    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    /*
     * Test the process(...) logic/accuracy
     * 
     * Gives some helpful error outputs for some simple error states.
     */
    @Test(timeout = 3000)
    // This test will only run for 3000ms => 3 seconds, otherwise if fails
    public void test() throws InterruptedException, IOException {
        for (int i = 0; i < 10; i++) {
            PingPongRight.process("start", "a", "b", "end", 10);
            String outResults = outContent.toString();

            if (outResults == null || outResults.length() == 0) {
                fail("No output at all.");
            }

            //We need to split things up for Windows and UNIX due to
            //differences in the way that newlines are handled.
            boolean windowsTrue = outResults.equals(testResultWindows);
            boolean unixTrue = outResults.equals(testResultUnix);
            boolean pingAllFirstTrue = outResults.equals(pingAllFirst);
            boolean pongAllFirstTrue = outResults.equals(pongAllFirst);

            if (errContent.toString().length() != 0) 
                fail("There was error text.");

            if (pingAllFirstTrue) 
                fail("Ping Thread completed before Pong started.");

            if (pongAllFirstTrue) 
                fail("Pong Thread completed before Ping started.");

            if (!(windowsTrue || unixTrue)) 
                fail("Output was wrong.\n" 
                     + "--- Received output ---\n" 
                     + outResults 
                     + "--- Expected output ---\n" 
                     + testResultWindows);

            outContent.reset();
        }
    }

    // This is what should be output \n was replaced for visible
    // endlines for inclusion into single line.
    String testResultUnix =
        "start\na(1)\nb(1)\na(2)\nb(2)\na(3)\nb(3)\na(4)\nb(4)\na(5)\nb(5)\na(6)\nb(6)\na(7)\nb(7)\na(8)\nb(8)\na(9)\nb(9)\na(10)\nb(10)\nend\n";
    String testResultWindows =
        "start\r\na(1)\r\nb(1)\r\na(2)\r\nb(2)\r\na(3)\r\nb(3)\r\na(4)\r\nb(4)\r\na(5)\r\nb(5)\r\na(6)\r\nb(6)\r\na(7)\r\nb(7)\r\na(8)\r\nb(8)\r\na(9)\r\nb(9)\r\na(10)\r\nb(10)\r\nend\r\n";
    String pingAllFirst =
        "start\r\na(1)\r\nb(1)\r\na(2)\r\na(3)\r\na(4)\r\na(5)\r\na(6)\r\na(7)\r\na(8)\r\na(9)\r\na(10)\r\nb(2)\r\nb(3)\r\nb(4)\r\nb(5)\r\nb(6)\r\nb(8)\r\nb(7)\r\nb(9)\r\nb(10)\r\nend\r\n";
    String pongAllFirst =
        "start\r\nb(1)\r\nb(2)\r\nb(3)\r\nb(4)\r\nb(5)\r\nb(6)\r\nb(8)\r\nb(7)\r\nb(9)\r\nb(10)\r\na(1)\r\na(2)\r\na(3)\r\na(4)\r\na(5)\r\na(6)\r\na(7)\r\na(8)\r\na(9)\r\na(10)\r\nend\r\n";
}
