package edu.vuum.mocca;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * @class BuggyLongTest
 *
 * @brief This class shows what happens if concurrent access to a long
 *        isn't properly serialized.
 */
class BuggyLongTest
{
    /**
     * Number of iterations to run the test.
     */
    static final long mMaxIterations = 100000000;

    /**
     * Barrier synchronizer that controls when the two threads start
     * running the test.
     */
    static CyclicBarrier mStartBarrier = new CyclicBarrier(2);

    /**
     * Barrier synchronizer that controls when the main thread can
     * return.
     */
    static CountDownLatch mStopLatch = new CountDownLatch(2);

    /**
     * An instance of long that's not protected by a lock.
     */
    static long mCounter = 0;

    /**
     * Main entry point method that runs the test.
     */
    public static void main(String[] args) {
        try {
            System.out.println("Starting BuggyLongTest");

            /**
             * Start a Thread whose Runnable decrements the SimpleAtomicLong
             * mMaxIterations number of times.
             */
            new Thread(new Runnable()
                { public void run() {
                    try
                        {
                            /**
                             * Wait for both Threads to start running
                             * before beginning the loop.
                             */
                            mStartBarrier.await();

                            for (int i = 0; i < mMaxIterations; ++i)
                                mCounter--;

                            /**
                             * Inform the main thread that we're done.
                             */
                            mStopLatch.countDown();
                        }
                    catch (Exception e)
                        {
                            System.out.println("problem here");
                        }
                }
                }).start();

            /**
             * Start a Thread whose Runnable increments the SimpleAtomicLong
             * mMaxIterations number of times.
             */
            new Thread(new Runnable()
                { public void run() {
                    try {
                        /**
                         * Wait for both Threads to start running
                         * before beginning the loop.
                         */
                        mStartBarrier.await();

                        for (int i = 0; i < mMaxIterations; ++i)
                            mCounter++;

                        /**
                         * Inform the main thread that we're done.
                         */
                        mStopLatch.countDown();
                    }
                    catch (Exception e) { }
                }
                }).start();

            /**
             * Barrier synchronizer that waits for both worker threads
             * to exit before continuing.
             */
            mStopLatch.await();

            long result = mCounter;
            /**
             * Check to ensure the test worked, i.e., mCounter's value
             * should be 0.
             */
            if (result == 0)
                System.out.println("test worked");
            else
                System.out.println("test failed: mCounter = " + result);

            System.out.println("Finishing BuggyLongTest");
        }
        catch (Exception e) { }
    }
}
