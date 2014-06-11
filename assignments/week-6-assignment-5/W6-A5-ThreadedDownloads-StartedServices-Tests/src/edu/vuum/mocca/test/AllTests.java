package edu.vuum.mocca.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @class AllTests
 *
 * @brief Combine all the unit tests into one suite, so that you can run them together with one click.
 */
public class AllTests {
    public static Test suite() {
        TestSuite suite = new TestSuite(AllTests.class.getName());
        //$JUnit-BEGIN$
        suite.addTestSuite(DownloadIntentServiceTests.class);
        suite.addTestSuite(DownloadUtilsTests.class);
        suite.addTestSuite(ThreadPoolDownloadServiceTests.class);
        suite.addTestSuite(DownloadActivityTests.class);
        //$JUnit-END$
        return suite;
    }
}
