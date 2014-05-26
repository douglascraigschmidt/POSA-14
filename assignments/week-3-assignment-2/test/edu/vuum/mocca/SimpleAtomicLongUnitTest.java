package edu.vuum.mocca;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * @class SimpleAtomicLongUnitTest
 *
 * @brief Simple unit test for the SimpleAtomicLong clas that ensures
 *        the version submitted for this assignment works correctly.
 */
public class SimpleAtomicLongUnitTest {
    @Test
    public void testSimpleAtomicLong() {
        SimpleAtomicLong testLong = new SimpleAtomicLong(0);
        assertNotNull(testLong);
    }

    @Test
    public void testGet() {
        SimpleAtomicLong testLong = new SimpleAtomicLong(0);
        assertEquals(testLong.get(), 0);
		
        SimpleAtomicLong testLong2 = new SimpleAtomicLong(100);
        assertEquals(testLong2.get(), 100);
		
        SimpleAtomicLong testLong3 = new SimpleAtomicLong(-100);
        assertEquals(testLong3.get(), -100);
    }

    @Test
    public void testDecrementAndGet() {
        SimpleAtomicLong testLong = new SimpleAtomicLong(0);
        assertEquals(testLong.decrementAndGet(), -1);
        assertEquals(testLong.get(), -1);
		
        SimpleAtomicLong testLong2 = new SimpleAtomicLong(100);
        assertEquals(testLong2.decrementAndGet(), 99);
        assertEquals(testLong2.get(), 99);
		
        SimpleAtomicLong testLong3 = new SimpleAtomicLong(-100);
        assertEquals(testLong3.decrementAndGet(), -101);
        assertEquals(testLong3.get(), -101);
    }

    @Test
    public void testIncrementAndGet() {
        SimpleAtomicLong testLong = new SimpleAtomicLong(0);
        assertEquals(testLong.incrementAndGet(), 1);
        assertEquals(testLong.get(), 1);
		
        SimpleAtomicLong testLong2 = new SimpleAtomicLong(100);
        assertEquals(testLong2.incrementAndGet(), 101);
        assertEquals(testLong2.get(), 101);
		
        SimpleAtomicLong testLong3 = new SimpleAtomicLong(-100);
        assertEquals(testLong3.incrementAndGet(), -99);
        assertEquals(testLong3.get(), -99);
    }
	
    @Test
    public void testGetAndIncrement() {
        SimpleAtomicLong testLong = new SimpleAtomicLong(0);
        assertEquals(testLong.getAndIncrement(), 0);
        assertEquals(testLong.get(), 1);
		
        SimpleAtomicLong testLong2 = new SimpleAtomicLong(100);
        assertEquals(testLong2.getAndIncrement(), 100);
        assertEquals(testLong2.get(), 101);
		
        SimpleAtomicLong testLong3 = new SimpleAtomicLong(-100);
        assertEquals(testLong3.getAndIncrement(), -100);
        assertEquals(testLong3.get(), -99);
    }

    @Test
    public void testGetAndDecrement() {
        SimpleAtomicLong testLong = new SimpleAtomicLong(0);
        assertEquals(testLong.getAndDecrement(), 0);
        assertEquals(testLong.get(), -1);
		
        SimpleAtomicLong testLong2 = new SimpleAtomicLong(100);
        assertEquals(testLong2.getAndDecrement(), 100);
        assertEquals(testLong2.get(), 99);
		
        SimpleAtomicLong testLong3 = new SimpleAtomicLong(-100);
        assertEquals(testLong3.getAndDecrement(), -100);
        assertEquals(testLong3.get(), -101);
    }
	
}
