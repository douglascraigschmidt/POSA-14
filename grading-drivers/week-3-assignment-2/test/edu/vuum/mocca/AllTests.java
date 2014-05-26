package edu.vuum.mocca;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({PalantirManagerUnitTest.class,
               SimpleAtomicLongUnitTest.class,
               SimpleSemaphoreUnitTest.class})
public class AllTests {
}
