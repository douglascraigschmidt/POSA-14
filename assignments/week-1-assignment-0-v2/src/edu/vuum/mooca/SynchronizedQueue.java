package edu.vuum.mooca;
import java.util.concurrent.*;

/**
 * @class SynchronizedQueue
 * 
 * @brief This class tests the use of Java Threads and several
 *        implementations of the Java BlockingQueue interface.  It
 *        plays the role of the Abstract Class in the Template Method
 *        pattern.
 */
public abstract class SynchronizedQueue {
    /**
     * Keep track of the number of times the producer test iterates.
     */
    static volatile int mProducerCounter = 0;

    /**
     * Keep track of the number of times the consumer test iterates.
     */
    static volatile int mConsumerCounter = 0;

    /**
     * Maximum timeout.
     */
    static final int TIMEOUT_SECONDS = 5;

    /**
     * Error value for a timeout.
     */
    static final int TIMEOUT_OCCURRED = -1;

    /**
     * Error value for a failure.
     */
    static final int FAILURE_OCCURRED = -2;

    /**
     * @class SynchronizedQueueResult
     *
     * @brief Enumerated type for return values of testing logic, has
     *       String for easy output.
     */
   public enum SynchronizedQueueResult {
        RAN_PROPERLY("Threads Ran Properly."), 
        JOIN_NEVER_CALLED("Join() never called."),
        THREADS_NEVER_RAN("Threads never ran."),
        THREADS_NEVER_INTERUPTED("Threads never interrupted."),
        THREADS_THREW_EXCEPTION("Thread threw an exception."),
        THREADS_NEVER_CREATED("Threads never created."),
        TESTING_LOGIC_THREW_EXCEPTION("Testing Logic threw Exception."),
        THREADS_TIMEDOUT("Threads Timed-out, Interupt likely not called."), 
        INCORRECT_COUNT("The size of mQueue is not consistent with the number of puts() and takes() performed.");

        /**
         * String value for the enumerated type.
         */
        private String mValue = null;

        /**
         * Initialize the mValue string.
         */
        private SynchronizedQueueResult(String value) {
            mValue = value;
        }

        /**
         * Return the mValue string.
         */
        public String getString() {
            return mValue;
        }
    }

    /**
     * @class QueueAdapter
     * 
     * @brief Applies a variant of the GoF Adapter pattern that
     *        enables us to test several implementations of the
     *        BlockingQueue interface.
     */
    public static class QueueAdapter<E> {
        /**
         * Stores the queue that we're adapting.
         */
        private BlockingQueue<E> mQueue;

        /**
         * Store the queue that we're adapting.
         */
        public QueueAdapter(BlockingQueue<E> queue) {
            mQueue = queue;
        }

        /**
         * Returns the number of elements in this queue.
         */
        int size() {
            return mQueue.size();
        }

        /**
         * Insert msg at the tail of the queue.
         * 
         * @throws TimeoutException and InterruptedException
         */
        public void put(E msg) throws InterruptedException, TimeoutException {
            // Keep track of how many times we're called.
            boolean timeoutValue = mQueue.offer(msg,
                                                TIMEOUT_SECONDS,
                                                TimeUnit.SECONDS);
            if (timeoutValue == false)
                throw new TimeoutException();
            
            mProducerCounter++;
        }

        /**
         * Remove msg from the head of the queue.
         * 
         * @throws TimeoutException
         *             , InterruptedException
         */
        public E take() throws InterruptedException, TimeoutException {
            // Keep track of how many times we're called.
            E rValue = mQueue.poll(TIMEOUT_SECONDS,
                                   TimeUnit.SECONDS);

            if (rValue == null)
                throw new TimeoutException();

            mConsumerCounter++;
            
            return rValue;            
        }
    }

    /**
     * Adapter object used to test different BlockingQueue
     * implementations.
     */
    private static QueueAdapter<Integer> mQueue = null;

    /**
     * This runnable loops for mMaxIterations and calls put() on
     * mQueue to insert the iteration number into the queue.
     */
    protected static Runnable mProducerRunnable = new Runnable() {
            public void run() {
                for (int i = 0; i < mMaxIterations; i++)
                    try {
                        mQueue.put(i);
                        if (Thread.interrupted())
                            throw new InterruptedException();
                    } catch (InterruptedException e) {
                    	if (diagnosticsEnabled) 
                            System.out.println("Thread " 
                                               + Thread.currentThread().getId()
                                               + " in test "
                                               + mTestName 
                                               + " properly interrupted by "
                                               + e.toString() + " in producerRunnable");
                        // This isn't an error - it just means that
                        // we've been interrupted by the main Thread.
                        return;
                    } catch (TimeoutException e) {
                    	if (diagnosticsEnabled) 
                            System.out.println("Thread "
                                               + Thread.currentThread().getId()
                                               + " in test "
                                               + mTestName 
                                               + " Exception " 
                                               + e.toString()
                                               + " occurred in producerRunnable");
                        // Indicate a timeout.
                        mProducerCounter = TIMEOUT_OCCURRED;
                        return;
                    } catch (Exception e) {
                    	if (diagnosticsEnabled) 
                            System.out.println("Thread "
                                               + Thread.currentThread().getId()
                                               + " in test "
                                               + mTestName 
                                               + " Exception " 
                                               + e.toString()
                                               + " occurred in producerRunnable");
                        // Indicate a failure.
                        mProducerCounter = FAILURE_OCCURRED;
                        return;
                    }
            }
	};

    /**
     * This runnable loops for mMaxIterations and calls take() on mQueue to
     * remove the iteration from the queue.
     */
    protected static Runnable mConsumerRunnable = new Runnable() {
            public void run() {
                for (int i = 0; i < mMaxIterations; i++)
                    try {
                        if (Thread.interrupted()) {
                            throw new InterruptedException();
                        }
                        Integer result = (Integer) mQueue.take();

                        if (diagnosticsEnabled)
                        	System.out.println("iteration = " + result);
                    } catch (InterruptedException e) {
                    	if (diagnosticsEnabled) 
                    		System.out.println("Thread " 
                                           + Thread.currentThread().getId()
                                           + " in test "
                                           + mTestName 
                                           + " properly interrupted by "
                                           + e.toString() + " in consumerRunnable");
                        // This isn't an error - it just means that
                        // we've been interrupted by the main Thread.
                        return;
                    } catch (TimeoutException e) {
                    	if (diagnosticsEnabled) 
                    		System.out.println("Thread "
                                           + Thread.currentThread().getId()
                                           + " in test "
                                           + mTestName 
                                           + " Exception " 
                                           + e.toString()
                                           + " occurred in consumerRunnable");
                        // Indicate a timeout.
                        mConsumerCounter = TIMEOUT_OCCURRED;
                        return;
                    } catch (Exception e) {
                    	if (diagnosticsEnabled)  
                    		System.out.println("Thread "
                                           + Thread.currentThread().getId()
                                           + " in test "
                                           + mTestName 
                                           + " Exception " 
                                           + e.toString()
                                           + " occurred in consumerRunnable");
                        // Indicate a failure.
                        mConsumerCounter = FAILURE_OCCURRED;
                        return;
                    }
            }
	};

    protected SynchronizedQueueResult checkResults() {
        int numberOfRemainingItemsInQueue = 
            mProducerCounter - mConsumerCounter;

        // Do some sanity checking to see if the Threads work as
        // expected.
        if (mConsumer == null 
            || mProducer == null)
            return SynchronizedQueueResult.THREADS_NEVER_CREATED;
        else if (mConsumer.isAlive() 
                 || mProducer.isAlive())
            return SynchronizedQueueResult.JOIN_NEVER_CALLED;
        else if (mConsumerCounter == 0 
                 || mProducerCounter == 0)
            return SynchronizedQueueResult.THREADS_NEVER_RAN;
        else if (mConsumerCounter == mMaxIterations
                 || mProducerCounter == mMaxIterations) 
            return SynchronizedQueueResult.THREADS_NEVER_INTERUPTED;
        else if (mConsumerCounter == FAILURE_OCCURRED
                 || mProducerCounter == FAILURE_OCCURRED) 
            return SynchronizedQueueResult.THREADS_THREW_EXCEPTION;
        else if (mConsumerCounter == TIMEOUT_OCCURRED
                 || mProducerCounter == TIMEOUT_OCCURRED) 
            return SynchronizedQueueResult.THREADS_TIMEDOUT;
        else if (mQueue.size() != numberOfRemainingItemsInQueue)
            return SynchronizedQueueResult.INCORRECT_COUNT;
        else
            return SynchronizedQueueResult.RAN_PROPERLY;
    }

    /**
     * Number of iterations to test (the actual test shouldn't run
     * this many iterations since the Threads ought to be interrupted
     * long before it gets this far).
     */
    public static int mMaxIterations = 1000000;

    /**
     * The Java Threads that are used to produce and consume messages
     * on the queue.
     */
    protected Thread mConsumer = null;
    protected Thread mProducer = null;

    /**
     * The name of the test that's being run, e.g., 
     */
    protected static String mTestName = null;
    
    /**
     * If this is set to true in SynchronizedQueueImpl.java then lots
     * of debugging output will be generated.
     */
    public static boolean diagnosticsEnabled;

    /**
     * These are hook methods that play the role of "primitive
     * operations" in the Template Method pattern.  They must be
     * defined in SynchronizedQueueImpl.java by adding code after the
     * "TODO" comments.
     */
    protected abstract void createThreads();
    protected abstract void startThreads();
    protected abstract void interruptThreads();    
    protected abstract void joinThreads() throws InterruptedException;    

    /**
     * This template method runs the test on the queue parameter.  It
     * decouples the test code from the user-defined code using the
     * Template Method pattern.
     */
    public SynchronizedQueueResult testQueue(QueueAdapter<Integer> queue,
                                             String testName) {
        try {
            mQueue = queue;
            mTestName = testName;
            mProducerCounter = 0;
            mConsumerCounter = 0;
            
            // Invoke the various hook methods, which are "primitive
            // operations" in the Template Method pattern.
            createThreads();
            startThreads();

            // Give the Threads a chance to run before interrupting
            // them. (disabling console output makes them run really
            // fast).
            if (diagnosticsEnabled)
            	Thread.sleep(1000);
            else
            	Thread.sleep(100);
            
            interruptThreads();
            joinThreads();

            return checkResults();
        } catch (Exception e) {
            return SynchronizedQueueResult.TESTING_LOGIC_THREW_EXCEPTION;
        }
    }
}
