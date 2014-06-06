package edu.vuum.mocca.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.vuum.mocca.Options;
import edu.vuum.mocca.PlatformStrategy;
import edu.vuum.mocca.PlatformStrategyFactory;
import edu.vuum.mocca.PlayPingPong;

public class PingPongFinalTest extends TestCase {

	/*
	 * These data members are used to capture [ System.out.println() ] for
	 * testing.
	 */
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

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
	public void testSemaphore() throws InterruptedException, IOException {
		tester("SEMA", new String[]{});
	}

	@Test(timeout = 3000)
	public void testConditional() throws InterruptedException, IOException {
		tester("COND", new String[]{"-s", "COND"});
	}

	public void tester(String method, String[] args)
			throws InterruptedException, IOException {
		for (int i = 0; i < 10; i++) {

			/** Initializes the Options singleton. */
			Options.instance().parseArgs(args);

			PlatformStrategy.instance(new PlatformStrategyFactory(
					new PrintStream(outContent), null).makePlatformStrategy());

			PlayPingPong pingPong = new PlayPingPong(
					PlatformStrategy.instance(), Options.instance()
							.maxIterations(), Options.instance().maxTurns(),
					method);

			pingPong.run();

			String outResults = outContent.toString();

			if (outResults == null || outResults.length() == 0) {
				fail("No output at all.");
			}

			// We need to split things up for Windows and UNIX due to
			// differences in the way that newlines are handled.
			boolean windowsTrue = outResults
					.equals(TestOptions.JAVA_CONSOLE_WINDOWS);
			boolean unixTrue = outResults.equals(TestOptions.JAVA_CONSOLE_UNIX);
			boolean pingAllFirstTrue = outResults
					.equals(TestOptions.PING_ALL_FIRST_UNIX)
					|| outResults.equals(TestOptions.PING_ALL_FIRST_WINDOWS);

			boolean pongAllFirstTrue = outResults
					.equals(TestOptions.PONG_ALL_FIRST_UNIX)
					|| outResults.equals(TestOptions.PONG_ALL_FIRST_WINDOWS);

			if (errContent.toString().length() != 0)
				fail("There was error text.");

			if (pingAllFirstTrue)
				fail("Ping Thread completed before Pong started.");

			if (pongAllFirstTrue)
				fail("Pong Thread completed before Ping started.");

			if (!(windowsTrue || unixTrue))
				fail("Output was wrong.\n" + "--- Received output ---\n"
						+ outResults + "--- Expected output ---\n"
						+ edu.vuum.mocca.test.TestOptions.JAVA_CONSOLE_UNIX
						+ " or Windows Endline, if on Windows.");

			outContent.reset();
		}
	}

}
