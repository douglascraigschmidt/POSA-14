package edu.vuum.mocca;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

/**
 * @brief SimpleAtomicLongSingleThreadedTest
 *
 * @class Evalutes the logic of the SimpleAtomicLong class by testing
 *        every method with each of the values in mTestValues.
 */
public class SimpleAtomicLongSingleThreadedTest {
    /*
     * Test possible edge cases at 0, and a large negative and
     * positive.
     */
    final static long[] mTestValues = { -100, -1, 0, 1, 100 };

    /**
     * Test Constructor.
     */
    @Test
    public void constructorTest() {
        for (long testValue : mTestValues) {
            SimpleAtomicLong counter =
                new SimpleAtomicLong(testValue);
            Assert.assertNotNull(counter);
            assertEquals(testValue, counter.get());
        }
    }

    /**
     * Test get()
     */
    @Test
    public void getTest() {
        for (long testValue : mTestValues) {
            SimpleAtomicLong counter = new SimpleAtomicLong(testValue);
            assertEquals(testValue,
                         counter.get());
        }
    }

    /**
     * test decrementAndGet()
     */
    @Test
    public void decrementAndGetTest() {
        for (long testValue : mTestValues) {
            final SimpleAtomicLong counter =
                new SimpleAtomicLong(testValue);
            decrementAndGetTestLogic(counter,
                                     testValue,
                                     testValue - 1,
                                     testValue - 1);
        }
    }

    /**
     * test getAndDecrement()
     */
    @Test
    public void getAndDecrementTest() {
        for (long testValue : mTestValues) {
            final SimpleAtomicLong counter =
                new SimpleAtomicLong(testValue);
            getAndDecrementTestLogic(counter,
                                     testValue,
                                     testValue,
                                     testValue - 1);
        }
    }

    /**
     * test incrementAndGet()
     */
    @Test
	public void incrementAndGetTestTest() {
        for (long testValue : mTestValues) {
            final SimpleAtomicLong counter =
                new SimpleAtomicLong(testValue);
            incrementAndGetTestLogic(counter,
                                     testValue,
                                     testValue + 1,
                                     testValue + 1);
        }
    }

    /**
     * test getAndIncrement()
     */
    @Test
    public void getAndIncrementTest() {
        for (long testValue : mTestValues) {
            final SimpleAtomicLong counter =
                new SimpleAtomicLong(testValue);
            getAndIncrementTestLogic(counter,
                                     testValue,
                                     testValue,
                                     testValue + 1);
        }
    }

    /**
     * Compares the values expected with the values produced by each test logic.
     * 
     * @param pre
     *           The 'pre' number produced by the test
     * @param preValue
     *           The 'pre' number expected
     * @param result
     *           The 'result' number produced by the test
     * @param resultValue
     *           The 'result' expected
     * @param post
     *           The 'post' number produced by the test
     * @param postValue
     *           The 'post' expected
     */
    private void compareResults(long pre, long preValue, long result,
                                long resultValue, long post, long postValue) {
        assertEquals(pre, preValue);
        assertEquals(result, resultValue);
        assertEquals(post, postValue);
    }

    /**
     * The Logic of testing decrementAndGet
     * 
     * @param simpleAtomicLong
     *           The SimpleAtomicLong to be tested
     * @param preValue
     *           The expected 'pre' value
     * @param resultValue
     *           The expected 'result' value
     * @param postValue
     *           The expected 'post' value
     */
    public void decrementAndGetTestLogic(SimpleAtomicLong simpleAtomicLong,
                                         long preValue, long resultValue, long postValue) {
        long pre = simpleAtomicLong.get();
        long result = simpleAtomicLong.decrementAndGet();
        long post = simpleAtomicLong.get();
        assertEquals(pre - 1, result);
        assertEquals(result, post);
        compareResults(pre,
                       preValue,
                       result,
                       resultValue,
                       post,
                       postValue);
    }

    /**
     * The Logic of testing getAndDecrement
     * 
     * @param simpleAtomicLong
     *           The SimpleAtomicLong to be tested
     * @param preValue
     *           The expected 'pre' value
     * @param resultValue
     *           The expected 'result' value
     * @param postValue
     *           The expected 'post' value
     */
    public void getAndDecrementTestLogic(SimpleAtomicLong simpleAtomicLong,
                                         long preValue, long resultValue, long postValue) {
        long pre = simpleAtomicLong.get();
        long result = simpleAtomicLong.getAndDecrement();
        long post = simpleAtomicLong.get();
        assertEquals(pre, result);
        assertEquals(pre - 1, post);
        compareResults(pre,
                       preValue,
                       result,
                       resultValue,
                       post,
                       postValue);
    }

    /**
     * The Logic of testing incrementAndGet
     * 
     * @param simpleAtomicLong
     *           The SimpleAtomicLong to be tested
     * @param preValue
     *           The expected 'pre' value
     * @param resultValue
     *           The expected 'result' value
     * @param postValue
     *           The expected 'post' value
     */
    public void incrementAndGetTestLogic(SimpleAtomicLong simpleAtomicLong,
                                         long preValue, long resultValue, long postValue) {
        long pre = simpleAtomicLong.get();
        long result = simpleAtomicLong.incrementAndGet();
        long post = simpleAtomicLong.get();
        assertEquals(pre + 1, result);
        assertEquals(result, post);
        compareResults(pre,
                       preValue,
                       result,
                       resultValue,
                       post,
                       postValue);
    }

    /**
     * The Logic of testing getAndIncrement
     * 
     * @param simpleAtomicLong
     *           The SimpleAtomicLong to be tested
     * @param preValue
     *           The expected 'pre' value
     * @param resultValue
     *           The expected 'result' value
     * @param postValue
     *           The expected 'post' value
     */
    public void getAndIncrementTestLogic(SimpleAtomicLong simpleAtomicLong,
                                         long preValue, long resultValue, long postValue) {
        long pre = simpleAtomicLong.get();
        long result = simpleAtomicLong.getAndIncrement();
        long post = simpleAtomicLong.get();
        assertEquals(pre, result);
        assertEquals(pre + 1, post);
        compareResults(pre,
                       preValue,
                       result,
                       resultValue,
                       post, postValue);
    }
}
