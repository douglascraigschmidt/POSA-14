import java.util.ArrayList;
import java.util.*;
import java.util.concurrent.*;

/**
 * @class SynchronizedQueueTest
 *
 * @brief This program tests the use of Java Threads and several
 *        implementations of the Java BlockingQueue interface.
 */
public class SynchronizedQueueTest
{ 
    /**
     * Keep track of the number of times the producer test iterates.
     */
    static volatile int mProducerCounter = 0;

    /**
     * Keep track of the number of times the consumer test iterates.
     */
    static volatile int mConsumerCounter = 0;

    /**
     * @class QueueAdapter
     *
     * @brief Applies a variant of the GoF Adapter pattern that
     *        enables us to test several implementations of the
     *        BlockingQueue interface.
     */
    static class QueueAdapter<E>
    {
        /**
         * Stores the queue that we're adapting.
         */
        private BlockingQueue<E> mQueue;

        /**
         * Store the queue that we're adapting.
         */
        QueueAdapter(BlockingQueue<E> queue) {
            mQueue = queue;
        }

        /**
         * Insert msg at the tail of the queue.
         */
        public void put(E msg) throws InterruptedException {
            // Keep track of how many times we're called.
            mProducerCounter++;
            mQueue.put(msg); 
        }

        /**
         * Remove msg from the head of the queue.
         */
        public E take() throws InterruptedException {
            // Keep track of how many times we're called.
            mConsumerCounter++;
            return mQueue.take(); 
        }
    }

    /**
     * @class BuggyBlockingQueue
     *
     * @brief Defines a buggy version of the BlockingQueue interface
     *        that doesn't implement any synchronization mechanisms,
     *        so of course it will fail horribly (which is the intent).
     */
    static class BuggyBlockingQueue<E> implements BlockingQueue<E> {
        /**
         * ArrayList doesn't provide any synchronization, so it will
         * not work correctly when called from multiple Java Threads.
         */
        private ArrayList<E> mList = null;

        /**
         * Constructor just creates an ArrayList of the appropriate
         * size.
         */
        public BuggyBlockingQueue(int initialSize) {
            mList = new ArrayList<E>(initialSize);
        }
        
        /**
         * Insert msg at the tail of the queue, but doesn't block if
         * the queue is full.
         */
        public void put(E msg) throws InterruptedException {
            mList.add(msg); 
        }

        /**
         * Remove msg from the head of the queue, but doesn't block if
         * the queue is empty.
         */
        public E take() throws InterruptedException {
            return mList.remove(0); 
        }

        /**
         * All these methods are inherited from the BlockingQueue
         * interface.  They are defined as no-ops to ensure the
         * "Buggyness" of this class ;-)
         */
        public int drainTo(Collection<? super E> c) { return 0; }
        public int drainTo(Collection<? super E> c, int maxElements) { return 0; }
        public boolean contains(Object o) { return false; }
        public boolean remove(Object o) { return false; }
        public int remainingCapacity() { return 0; }
        public E poll() { return null; }
        public E poll(long timeout, TimeUnit unit) throws InterruptedException { return null; }
        public E peek() { return null; }
        public boolean offer(E e) { return false; }
        public boolean offer(E e, long timeout, TimeUnit unit){ return false; }
        public boolean add(E e) { return false; }
        public E element() { return null; }
        public E remove() { return null; }
        public void clear() { }
        public boolean retainAll(Collection<?> collection) { return false; }
        public boolean removeAll(Collection<?> collection) { return false; }
        public boolean addAll(Collection<? extends E> collection) { return false; }
        public boolean containsAll(Collection<?> collection) { return false; }
        public Object[] toArray() { return null; }
        public <T> T[] toArray(T[] array) { return null ;}
        public Iterator<E> iterator() { return null; }
        public boolean isEmpty() { return false; }
        public int size() { return 0; }
    }

    /**
     * Adapter object used to test different BlockingQueue
     * implementations.
     */
    static QueueAdapter<Integer> mQueue = null;

    /**
     * This runnable loops for mMaxIterations and calls put() on
     * mQueue to insert the iteration number into the queue.
     */
    static Runnable producerRunnable = new Runnable() {
            public void run() {
                for(int i = 0; i < mMaxIterations; i++)
                    try {
                        mQueue.put(i);
                        if (Thread.interrupted())
                        	throw new InterruptedException();
                    }
                    catch (InterruptedException e) {
                        System.out.println("Thread properly interrupted by "
                                           + e.toString() 
                                           + " in producerRunnable");
                        // This isn't an error - it just means that
                        // we've been interrupted.
                        return;
                    }
                    catch (Exception e) {
                        System.out.println("Exception " 
                                           + e.toString() 
                                           + " occurred in producerRunnable");
                        // Indicate a failure.
                        mProducerCounter = -1;
                        // mProducerCounter.set(mMaxIterations);
                        return;
                    }
            }};

    /**
     * This runnable loops for mMaxIterations and calls take() on
     * mQueue to remove the iteration from the queue.
     */
    static Runnable consumerRunnable = new Runnable() {
            public void run() {
                for(int i = 0; i < mMaxIterations; i++)
                    try {
                    	if (Thread.interrupted())
                        	throw new InterruptedException();
                        Integer result = (Integer) mQueue.take();

                        System.out.println("iteration = " 
                                           + result);
                    }
                    catch (InterruptedException e) {
                        System.out.println("Thread properly interrupted by "
                                           + e.toString() 
                                           + " in consumerRunnable");
                        // This isn't an error - it just means that
                        // we've been interrupted.
                        return;
                    }
                    catch (Exception e) {
                        System.out.println("Exception " 
                                           + e.toString() 
                                           + " occurred in consumerRunnable");
                        // Indicate a failure.
                        mConsumerCounter = -1;
                        return;
                    }
            }};

    /**
     * Number of iterations to test (the actual test shouldn't run
     * this many iterations since the Threads ought to be interrupted
     * long before it gets this far).
     */
    static int mMaxIterations = 1000000;

    /**
     * Run the test for the queue parameter.
     */
    static void testQueue(String testName, QueueAdapter<Integer> queue) {
        try {
            // TODO - you fill in here to replace the null
            // initialization below to create two Java Threads, one
            // that's passed the producerRunnable and the other that's
            // passed the consumerRunnable.
            Thread consumer = null;
            Thread producer = null;

            // TODO - you fill in here to start the threads.  More
            // interesting results will occur if you start the
            // consumer first.
        
            // Give the Threads a chance to run before interrupting
            // them.
            Thread.sleep(100);

            // TODO - you fill in here to interrupt the threads.

            // TODO - you fill in here to wait for the threads to
            // exit.

            String result = " passed";

            // Do some sanity checking to see if the Threads work as
            // expected.
            if (consumer.isAlive()
                || producer.isAlive())
                result = " failed because join() never called";
            else if (mConsumerCounter == 0
                     || mProducerCounter == 0)
                result = " failed because Threads never ran";
            else if (mConsumerCounter == mMaxIterations 
                     || mProducerCounter == mMaxIterations)
                result = " failed because Threads never interrupted";
            else if (mConsumerCounter == -1
                     || mProducerCounter == -1)
                result = " failed because Threads threw an exception";

            System.out.println("test " + testName + result);
        }
        catch (Exception e) { 
        }
    }

    /**
     * Main entry point method into the test program.
     */
    public static void main(String argv[]) {
    	System.out.println("Starting SynchronizedQueueTest");
        // Indicate how big the queue should be, which should be
        // smaller than the number of iterations to induce blocking
        // behavior.
        int queueSize = mMaxIterations / 10;

        // Test the ArrayBlockingQueue, which should pass the test.
        mQueue = new QueueAdapter<Integer>(new ArrayBlockingQueue<Integer>(queueSize));
        testQueue("ArrayBlockingQueue", mQueue);

        // Test the BuggyBlockingQueue, which should fail the test.
        mQueue = new QueueAdapter<Integer>(new BuggyBlockingQueue<Integer>(queueSize));
        testQueue("BuggyBlockingQueue", mQueue);
        
        System.out.println("Finishing SynchronizedQueueTest");
    }
}

