package edu.vuum.mocca.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @class AllTests
 *
 * @brief Combine all the unit tests into one suite so that we can run them all with one click.
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(DownloadBoundServiceAsyncTests.class);
		suite.addTestSuite(DownloadBoundServiceSyncTests.class);
		suite.addTestSuite(DownloadUtilsTests.class);
		suite.addTestSuite(DownloadActivityTests.class);
		//$JUnit-END$
		return suite;
	}

}
