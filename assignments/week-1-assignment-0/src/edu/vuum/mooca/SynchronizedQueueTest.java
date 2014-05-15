package edu.vuum.mooca;
import static org.junit.Assert.*;

import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Before;
import org.junit.Test;

import edu.vuum.mooca.SynchronizedQueue.*;

/**
 * @class SynchronizedQueueTest
 *
 * @brief This class tests queues for proper functionality by 
 * 	  using the testQueue method defined in SynchronizedQueue.java
 */
public class SynchronizedQueueTest {
    /**
     * Indicates how big the queue should be.
     */
    int queueSize;
	
    /**
     * Run the test for the queue parameter.
     *
     * @return result. If SynchronizedQueue test ran properly, returns
     * null. If not, returns error message.
     */
    static String runQueueTest(String qName, QueueAdapter<Integer> queue) {
    	System.out.println("Starting " + qName + " test...");

        SynchronizedQueueResult result =
            SynchronizedQueue.testQueue(queue);

        System.out.println("End " + qName + " test.\n");
        System.out.println("See JUnit view for results -- \n" +
                           "Green check-marks denote program correctness. \n" +
                           "Blue x-marks indicate a problem with your implementation. \n");
        
        if (result != SynchronizedQueueResult.RAN_PROPERLY)
            return result.getString();
		
        return null;
    }
	
    /**
     * Runs before each test. Sets queueSize.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        // Indicate how big the queue should be, which should be
        // smaller than the number of iterations to induce blocking
        // behavior.
        queueSize = SynchronizedQueue.mMaxIterations / 10;
    }

    /**
     * Tests the ArrayBlockingQueue, which should pass without error.
     */
    @Test
    public void arrayBlockingQueueTest() {
        QueueAdapter<Integer> queueAdapter =
            new QueueAdapter<Integer>(new ArrayBlockingQueue<Integer>(queueSize));
        String errors = runQueueTest("ArrayBlockingQueue", queueAdapter);

        assertNull("Error occurred: " + 
                   errors,
                   errors);
    }
}
