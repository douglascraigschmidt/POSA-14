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
    int mQueueSize;
	
    /**
     * Run the test for the queue parameter.
     *
     * @return result. If SynchronizedQueue test ran properly, returns
     * null. If not, returns error message.
     */
    static SynchronizedQueueResult runQueueTest(String qName,
                                                QueueAdapter<Integer> queue) {
    	if (SynchronizedQueue.diagnosticsEnabled) {	    
            System.out.println("Starting " 
                               + qName 
                               + " test...");
            if (qName == "BuggyBlockingQueue") 
                System.out.println("An exception may be thrown since "
                                   + qName
                                   + " is intentially BUGGY.");
    	}		

    	/**
         * We have to instantiate this object because Java doesn't
    	 * like things being abstract AND static, which makes
    	 * implementing the Template Pattern statically more painful
    	 * than it should be.
         */
        SynchronizedQueueResult result =
            new SynchronizedQueueImpl().testQueue(queue, qName);

        if (SynchronizedQueue.diagnosticsEnabled) {
            System.out.println("End " + qName + " test.\n");
            System.out.println("See JUnit view for results -- \n" 
                               + "Green check-marks denote program correctness. \n" 
                               + "Blue x-marks indicate a problem with your implementation. \n");
        }
        
        if (result != SynchronizedQueueResult.RAN_PROPERLY)
            return result;
        else
            return null;
    }
	
    /**
     * Runs before each test. Sets mQueueSize.
     * @throws Exception
     */
    @Before
        public void setUp() throws Exception {
        // Indicate how big the queue should be, which should be
        // smaller than the number of iterations to induce blocking
        // behavior.
        mQueueSize = SynchronizedQueue.mMaxIterations / 10;
    }

    /**
     * Tests the ArrayBlockingQueue, which should pass without error.
     */
    @Test
    public void arrayBlockingQueueTest() {
        // Make the appropriate QueueAdapter for the
        // ArrayBlockingQueue.
        QueueAdapter<Integer> queueAdapter =
            new QueueAdapter<Integer>(new ArrayBlockingQueue<Integer>(mQueueSize));

        // Run a test on the ArrayBlockingQueue.
        SynchronizedQueueResult errors =
            runQueueTest("ArrayBlockingQueue", queueAdapter);

        String errorMessage = "";
        
        if (errors != null) 
            errorMessage = errors.getString();

        assertNull("Error occurred: " + 
                   errorMessage,
                   errors);
    }
	
    /**
     * Tests the BuggyBlockingQueue, an intentionally flawed class.
     * The buggyBlockingQueueTest() will succeed if the testQueue
     * method fails, i.e., this test succeeds if our queue causes
     * errors (which is what we expect)!
     */
    @Test
    public void buggyBlockingQueueTest() {
        // Make the appropriate QueueAdapter for the
        // BuggyBlockingQueue.

        QueueAdapter<Integer> queueAdapter =
            new QueueAdapter<Integer>(new BuggyBlockingQueue<Integer>(mQueueSize));

        // Run a test on the BuggyBlockingQueue.
        SynchronizedQueueResult errors =
            runQueueTest("BuggyBlockingQueue", queueAdapter);
        
        assertNotNull("Test should not complete without errors. " +
                      "BuggyBlockingQueue is intended to function incorrectly.",
                      errors);
    }
}
