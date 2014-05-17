package edu.vuum.mocca;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ SimpleAtomicLongMultithreadedTest.class,
		SimpleAtomicLongSingleThreadedTest.class })
/**
 * @class SimpleAtomicLongTest
 *
 * @brief Entry point for running all the regression tests for the
 *        SimpleAtomicLong implementation.
 */
public class SimpleAtomicLongTests {
}
