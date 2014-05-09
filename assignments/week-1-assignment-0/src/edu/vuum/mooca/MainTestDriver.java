package edu.vuum.mooca;

import java.util.concurrent.ArrayBlockingQueue;

import edu.vuum.mooca.SynchronizedQueue.QueueAdapter;
import edu.vuum.mooca.SynchronizedQueue.SynchronizedQueueResult;

public class MainTestDriver {
    /**
     * Run the test for the queue parameter.
     */
    static void testQueue(String testName,
                          QueueAdapter<Integer> queue) {
        // Ask the SynchronizedQueue to test itself using the queue
        // parameter.
        SynchronizedQueueResult result = SynchronizedQueue.testQueue(queue);

        String resultString = "*** Test " + testName + " ";
        if (result != SynchronizedQueueResult.RAN_PROPERLY) 
            resultString += "Failed because: " + result.getString();
        else 
            resultString += "Passed.";

        System.out.println(resultString);
    }

    /**
     * Main entry point method into the test program.
     */
    public static void main(String argv[]) {
        System.out.println("Starting SynchronizedQueueTest");
        // Indicate how big the queue should be, which should be
        // smaller than the number of iterations to induce blocking
        // behavior.
        int queueSize = SynchronizedQueue.mMaxIterations / 10;

        // Test the ArrayBlockingQueue, which should pass the test.
        SynchronizedQueue.mQueue = new QueueAdapter<Integer>(
                                                             new ArrayBlockingQueue<Integer>(queueSize));
        testQueue("ArrayBlockingQueue", SynchronizedQueue.mQueue);

        // Test the BuggyBlockingQueue, which should fail the test.
        SynchronizedQueue.mQueue = new QueueAdapter<Integer>(
                                                             new BuggyBlockingQueue<Integer>(queueSize));
        testQueue("BuggyBlockingQueue", SynchronizedQueue.mQueue);

        System.out.println("Finishing SynchronizedQueueTest");
    }

}
