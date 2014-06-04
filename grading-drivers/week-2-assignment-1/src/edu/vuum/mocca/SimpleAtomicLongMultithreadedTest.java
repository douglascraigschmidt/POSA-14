package edu.vuum.mocca;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @class SimpleAtomicLongMultithreadedTest
 *
 * @brief Test the logic and multithreaded implementation of the
 *        SimpleAtomicLong class by having concurrent threads call the
 *        SimpleAtomicLong instance for various methods.
 */
public class SimpleAtomicLongMultithreadedTest {

    /**
     * Our start points.
     */
    final static long INITIAL_VALUE = 0;
    
    /**
     * Number of iterations to run the commands.
     */
    final static long mMaxIterations = 1000000;
    
    /**
     * Barrier synchronizer that controls when the threads start
     * running the test.
     */
    static CyclicBarrier mStartBarrier;

    /**
     * Barrier synchronizer that controls when the main thread can
     * return.
     */
    static CountDownLatch mStopLatch;

    /**
     * An instance of our implementation of SimpleAtomicLong, which is
     * defined as "volatile" to ensure proper visibility of its fields
     * after construction.
     */
    static volatile SimpleAtomicLong mCounter;
    
    /**
     * Runnable commands that use the mCounter methods
     * get()
     * incrementAndGet()
     * getAndIncrement()
     * decrementAndGet()
     * getAndDecrement()
     */
    static Runnable getCommand;
    static Runnable incrementGetCommand;
    static Runnable getIncrementCommand;
    static Runnable decrementGetCommand;
    static Runnable getDecrementCommand;
    
    /**
     * The value of mCounter prior to any changes made by testing.
     */
    long preTestValue;

    /**
     * The number of duplicate threads to run when testing each
     * individual command.
     */
    final int numThreads = 5;
	
    /**
     * @class RunTest
     *
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
         * An integer which keeps track of the number of times the
         * command has been called. Initially equal to zero.
         */
        private long iterations = 0;
        
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
                     * Wait for all Threads to start running before
                     * beginning the loop.
                     */
                    mStartBarrier.await();
                
                    for (; iterations < mMaxIterations; ++iterations) {
                        mCommand.run();
                    }
                    /**
                     * Inform the main thread that we're done.
                     */
                    mStopLatch.countDown();
                } catch (Exception e) {
                fail("Runnable failed.");
            }
        }
        
        /**
         * Returns the number of times this command has been performed.
         * @return iterations
         */
        public long getIterations() {
            return iterations;
        }
    }	
    
    /**
     * Runs prior to all tests. Creates a static instance of
     * SimpleAtomicLong and all runnable commands.
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        /**
         * Instance of SimpleAtomicLong class
         */
        mCounter = new SimpleAtomicLong(INITIAL_VALUE);
		
        /**
         * Runnable commands that execute get(), incrementAndGet(),
         * getAndIncrement(), decrementAndGet(), getAndDecrement(),
         * respectively, on the SimpleAtomicLong instance
         */
        getCommand = new Runnable() { public void run() { mCounter.get(); } };
        incrementGetCommand = new Runnable() { public void run() { mCounter.incrementAndGet(); } };
        getIncrementCommand = new Runnable() {  public void run() { mCounter.getAndIncrement(); } };
        decrementGetCommand = new Runnable() { public void run() { mCounter.decrementAndGet(); } };
        getDecrementCommand = new Runnable() { public void run() { mCounter.getAndDecrement(); } };
    }
	
    /**
     * Runs prior to each test. Stores the pre-test value of the mCounter.
     */
    @Before
    public void setUp() throws Exception {
        preTestValue = mCounter.get();
    }
	
    /**
     * Tests for proper concurrency and functionality of {@code get()}.
     */
    @Test
    public void multiGetTest() {
        /** 
         * run multiple threads calling mCounter.get().
         */
        runThreads(getCommand);
        /**
         *  The expected post-test value is no change in the pre-test
         *  value.
         */
        assertEquals(preTestValue,
                     mCounter.get());
    }
	
    /**
     * Tests for proper concurrency and functionality of {@code
     * incrementAndGet()}.
     */
    @Test
	public void multiIncrementAndGetTest() {
        runThreads(incrementGetCommand);
        /**
         * expected value after threads are run should be the number
         * of maximum iterations times the number of threads plus the
         * pre-test value
         */
        assertEquals(preTestValue 
                     + mMaxIterations*numThreads,
                     mCounter.get());
    }

    /**
     * Tests for proper concurrency and functionality of {@code getAndIncrement()}.
     */
    @Test
    public void multiGetAndIncrementTest() {
        runThreads(getIncrementCommand);
        assertEquals(preTestValue 
                     + mMaxIterations*numThreads,
                     mCounter.get());
    }
	
    /**
     * Tests for proper concurrency and functionality of {@code
     * decrementAndGet()}.
     */
    @Test
	public void multiDecrementAndGetTest() {
        runThreads(decrementGetCommand);
        /** 
         * Expected value of mCounter after threads have completed
         * running is the pre-test value minus the maximum iterations
         * times the number of threads that were run.
         */
        assertEquals(preTestValue - 
                     mMaxIterations*numThreads, 
                     mCounter.get());
    }
	
    /**
     * Tests for proper concurrency and functionality of {@code getAndIncrement()}.
     */
    @Test
    public void multiGetAndDecrementTest() {
        runThreads(getDecrementCommand); 
        /** 
         * expected value of mCounter after threads have completed running 
         * is the pre-test value minus the maximum iterations times the number
         * of threads that were run
         */
        assertEquals(preTestValue -
                     mMaxIterations*numThreads,
                     mCounter.get());
    }
	
    /**
     * Tests concurrent running of threads performing a variety of
     * operations on mCounter (e.g. {@code get()}, {@code
     * getAndIncrement()}, {@code getAndDecrement()},{@code
     * incrementAndGet()}, and {@code decrementAndGet()}).
     */
    @Test
    public void multiThreadedTest() {
        /**
         * Run five threads concurrently, each performing a different 
         * method on the SimpleAtomicLong instance
         */
        runThreads(null);
        /**
         * Check to ensure the pre-test and post-test values are
         * equal.  This indicates the test was successful.
         */
        assertEquals(preTestValue,
                     mCounter.get());
    }
	
    /**
     * Runs numThreads concurrent threads executing the same command.
     * Has a CyclicBarrier and CountDownLatch to facilitate
     * concurrency.
     * @param command
     */
    private void runThreads(Runnable command) {
        mStartBarrier = new CyclicBarrier(numThreads);
        mStopLatch = new CountDownLatch(numThreads);
        try { 
            /**
             * Create an array of RunTests whose Runnable commands
             * execute on the SimpleAtomicLong mMaxIterations number
             * of times.  If given a command, each thread should run
             * duplicates.  If command is null, then run one of each
             * type of command.
             */
            RunTest[] runTests;
            if(command == null) {
                runTests = new RunTest[5];
                runTests[0] = new RunTest(getCommand);
                runTests[1] = new RunTest(incrementGetCommand);
                runTests[2] = new RunTest(decrementGetCommand);
                runTests[3] = new RunTest(getDecrementCommand);
                runTests[4] = new RunTest(getIncrementCommand);
            } 
            else {
                runTests = new RunTest[numThreads];
                for(int i = 0; i < runTests.length; i++)
                    runTests[i] = new RunTest(command);
            }
			
            /**
             * Start threads whose Runnable commands execute on the
             * SimpleAtomicLong mMaxIterations number of times.
             */
            for(int i = 0; i < runTests.length; i++) 
                new Thread(runTests[i]).start();
			
            /**
             * Barrier synchronizer that waits for all worker threads
             * to exit before continuing.
             */
            mStopLatch.await();
	        
            /**
             * Check to ensure threads have run.
             */
            for(int i = 0; i < runTests.length; i++)
                assertEquals("Threads have not executed.", 
                             mMaxIterations,
                             runTests[i].getIterations());
        } catch (Exception e) { 
            fail("Exception thrown."); 
        }			
    }
	
}
