// Import the necessary Java synchronization and scheduling classes.

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * @class AtomicLongTest
 *
 * @brief This class tests our implementation of AtomicLong to ensure
 *        it works properly.
 */
class AtomicLongTest
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
     * An instance of our implementation of AtomicLong.
     */
    static AtomicLong mCounter = new AtomicLong(0);

    /**
     * Main entry point method that runs the test.
     */
    public static void main(String[] args) {
        try {
            System.out.println("Starting AtomicLongTest");

            /**
             * Start a Thread whose Runnable decrements the AtomicLong
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
                                mCounter.getAndDecrement();

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
             * Start a Thread whose Runnable increments the AtomicLong
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
                            mCounter.getAndIncrement();

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

            long result = mCounter.get();
            /**
             * Check to ensure the test worked, i.e., mCounter's value
             * should be 0.
             */
            if (result == 0)
                System.out.println("test worked");
            else
                System.out.println("test failed: mCounter = " + result);

            System.out.println("Finishing AtomicLongTest");
        }
        catch (Exception e) { }
    }
}
