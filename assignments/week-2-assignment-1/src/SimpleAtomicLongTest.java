// Import the necessary Java synchronization and scheduling classes.

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * @class SimpleAtomicLongTest
 *
 * @brief This class tests our implementation of SimpleAtomicLong to ensure
 *        it works properly.
 */
class SimpleAtomicLongTest
{
    /**
     * Number of iterations to run the test.
     */
    final static long mMaxIterations = 10000000;

    /**
     * Barrier synchronizer that controls when the two threads start
     * running the test.
     */
    final static CyclicBarrier mStartBarrier = new CyclicBarrier(2);

    /**
     * Barrier synchronizer that controls when the main thread can
     * return.
     */
    final static CountDownLatch mStopLatch = new CountDownLatch(2);

    /**
     * An instance of our implementation of SimpleAtomicLong.
     */
    final static SimpleAtomicLong mCounter = new SimpleAtomicLong(0);

    /**
     * @brief This class runs the test by invoking a command each time
     *        through the loop.
     */
    static class RunTest implements Runnable
    { 
        /**
         * A Command that determines what operation is done within the
         * loop.
         */
        private Runnable mCommand;

        /**
         * Store the command in a data member field.
         */
        RunTest(Runnable command) {
            mCommand = command;
        }
        
        /**
         * Run the command within a loop.
         */
        public void run() {
            try
                {
                    /**
                     * Wait for both Threads to start running before
                     * beginning the loop.
                     */
                    mStartBarrier.await();

                    for (int i = 0; i < mMaxIterations; ++i)
                        mCommand.run();

                    /**
                     * Inform the main thread that we're done.
                     */
                    mStopLatch.countDown();
                }
            catch (Exception e) {
                System.out.println("problem here");
            }
        }
    }

    /**
     * Main entry point method that runs the test.
     */
    public static void main(String[] args) {
        try {
            System.out.println("Starting SimpleAtomicLongTest");

            /**
             * A Runnable command that decrements the mCounter.
             */
            final Runnable decrementCommand =
                new Runnable() { public void run() { mCounter.getAndDecrement(); } };
            
            /**
             * A Runnable command that decrements the mCounter.
             */
            final Runnable incrementCommand =
                new Runnable() { public void run() { mCounter.getAndIncrement(); } };

            /**
             * Start a Thread whose Runnable command decrements the
             * SimpleAtomicLong mMaxIterations number of times.
             */
            new Thread(new RunTest(decrementCommand)).start();

            /**
             * Start a Thread whose Runnable command increments the
             * SimpleAtomicLong mMaxIterations number of times.
             */
            new Thread(new RunTest(incrementCommand)).start();

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

            System.out.println("Finishing SimpleAtomicLongTest");
        }
        catch (Exception e) { }
    }
}
