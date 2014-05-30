package edu.vuum.mooca;

import static org.junit.Assert.assertEquals;
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
 * @brief This JUnit test checks the PingPong program to make sure
 *        it's working properly.
 */
public class PingPongRightTest {
    /*
     * This is for capturing [ System.out.println() ] for testing
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
    @Test(timeout = 3000) // This test will only run for 2000ms => 2 seconds, otherwise if fails
    public void test() throws InterruptedException, IOException {
        for (int i = 0; i < 10; i++) {
            PingPongRight.process("start", "a", "b", "end", 10);
            String outResults = outContent.toString();
            if (outResults == null) 
                fail("No output at all.");
            else if (errContent.toString() == null
                       || errContent.toString().length() != 0) 
                fail("There was error text.");
            else if (outResults.equals(pingAllFirst)) 
                fail("Ping Thread completed before Pong started.");
            else if (outResults.equals(pongAllFirst))
                fail("Pong Thread completed before Ping started.");

            assertEquals(testResult,
                         outContent.toString());
            outContent.reset();
        }
    }

    // This is what should be output
    // \n was replaced for visible endlines for inclusion into single line
    String testResult =
        "start\r\na(1)\r\nb(1)\r\na(2)\r\nb(2)\r\na(3)\r\nb(3)\r\na(4)\r\nb(4)\r\na(5)\r\nb(5)\r\na(6)\r\nb(6)\r\na(7)\r\nb(7)\r\na(8)\r\nb(8)\r\na(9)\r\nb(9)\r\na(10)\r\nb(10)\r\nend\r\n";
    String pingAllFirst =
        "start\r\na(1)\r\nb(1)\r\na(2)\r\na(3)\r\na(4)\r\na(5)\r\na(6)\r\na(7)\r\na(8)\r\na(9)\r\na(10)\r\nb(2)\r\nb(3)\r\nb(4)\r\nb(5)\r\nb(6)\r\nb(8)\r\nb(7)\r\nb(9)\r\nb(10)\r\nend\r\n";
    String pongAllFirst =
        "start\r\nb(1)\r\nb(2)\r\nb(3)\r\nb(4)\r\nb(5)\r\nb(6)\r\nb(8)\r\nb(7)\r\nb(9)\r\nb(10)\r\na(1)\r\na(2)\r\na(3)\r\na(4)\r\na(5)\r\na(6)\r\na(7)\r\na(8)\r\na(9)\r\na(10)\r\nend\r\n";
}
