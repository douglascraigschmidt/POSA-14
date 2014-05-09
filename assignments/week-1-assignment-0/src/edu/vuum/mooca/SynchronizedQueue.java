package edu.vuum.mooca;
import java.util.*;
import java.util.concurrent.*;

/**
 * @class SynchronizedQueueTest
 * 
 * @brief This program tests the use of Java Threads and several implementations
 *        of the Java BlockingQueue interface.
 */
public class SynchronizedQueue {
	/**
	 * Keep track of the number of times the producer test iterates.
	 */
	static volatile int mProducerCounter = 0;

	/**
	 * Keep track of the number of times the consumer test iterates.
	 */
	static volatile int mConsumerCounter = 0;

	static final int TIMEOUT_SECONDS = 5;

	/**
	 * enum for return values of testing logic, has String for easy output.
	 */
	public enum SynchronizedQueueResult {

		RAN_PROPERLY("Threads Ran Properly."), JOIN_NEVER_CALLED(
				"Join() never called."), THREADS_NEVER_RAN("Threads never ran."), THREADS_NEVER_INTERUPTED(
				"Threads never interrupted."), THREADS_THREW_EXCEPTION(
				"Thread threw an exception."), THREADS_NEVER_CREATED(
				"Threads never created."), TESTING_LOGIC_THREW_EXCEPTION(
				"Testing Logic threw Exception."), THREADS_TIMEDOUT(
				"Threads Timed-out, Interupt likely not called.");

		private String val = null;

		private SynchronizedQueueResult(String val) {
			this.val = val;
		}

		public String getString() {
			return val;
		}
	}

	/**
	 * @class QueueAdapter
	 * 
	 * @brief Applies a variant of the GoF Adapter pattern that enables us to
	 *        test several implementations of the BlockingQueue interface.
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
		 * Insert msg at the tail of the queue.
		 * 
		 * @throws TimeoutException
		 *             , InterruptedException
		 */
		public void put(E msg) throws InterruptedException, TimeoutException {
			// Keep track of how many times we're called.
			mProducerCounter++;
			boolean timeoutValue = mQueue.offer(msg, TIMEOUT_SECONDS,
					TimeUnit.SECONDS);
			if (timeoutValue == false) {
				throw new TimeoutException();
			}
		}

		/**
		 * Remove msg from the head of the queue.
		 * 
		 * @throws TimeoutException
		 *             , InterruptedException
		 */
		public E take() throws InterruptedException, TimeoutException {
			// Keep track of how many times we're called.
			mConsumerCounter++;
			E rValue = mQueue.poll(TIMEOUT_SECONDS, TimeUnit.SECONDS);
			if (rValue == null) {
				throw new TimeoutException();
			}
			return rValue;
		}
	}

	/**
	 * Adapter object used to test different BlockingQueue implementations.
	 */
	public static QueueAdapter<Integer> mQueue = null;

	/**
	 * This runnable loops for mMaxIterations and calls put() on mQueue to
	 * insert the iteration number into the queue.
	 */
	static Runnable producerRunnable = new Runnable() {
		public void run() {
			for (int i = 0; i < mMaxIterations; i++)
				try {
					mQueue.put(i);
					if (Thread.interrupted())
						throw new InterruptedException();
				} catch (InterruptedException e) {
					System.out.println("Thread properly interrupted by "
							+ e.toString() + " in producerRunnable");
					// This isn't an error - it just means that
					// we've been interrupted.
					return;
				} catch (TimeoutException e) {
					System.out.println("Exception " + e.toString()
							+ " occurred in producerRunnable");
					// Indicate a timeout.
					mProducerCounter = -2;
					return;
				} catch (Exception e) {
					System.out.println("Exception " + e.toString()
							+ " occurred in producerRunnable");
					// Indicate a failure.
					mProducerCounter = -1;
					return;
				}
		}
	};

	/**
	 * This runnable loops for mMaxIterations and calls take() on mQueue to
	 * remove the iteration from the queue.
	 */
	static Runnable consumerRunnable = new Runnable() {
		public void run() {
			for (int i = 0; i < mMaxIterations; i++)
				try {
					if (Thread.interrupted()) {
						throw new InterruptedException();
					}
					Integer result = (Integer) mQueue.take();

					System.out.println("iteration = " + result);
				} catch (InterruptedException e) {
					System.out.println("Thread properly interrupted by "
							+ e.toString() + " in consumerRunnable");
					// This isn't an error - it just means that
					// we've been interrupted.
					return;
				} catch (TimeoutException e) {
					System.out.println("Exception " + e.toString()
							+ " occurred in consumerRunnable");
					// Indicate a timeout.
					mProducerCounter = -2;
					return;
				} catch (Exception e) {
					System.out.println("Exception " + e.toString()
							+ " occurred in consumerRunnable");
					// Indicate a failure.
					mConsumerCounter = -1;
					return;
				}
		}
	};

	/**
	 * Number of iterations to test (the actual test shouldn't run this many
	 * iterations since the Threads ought to be interrupted long before it gets
	 * this far).
	 */
	public static int mMaxIterations = 1000000;

	/**
	 * Run the test for the queue parameter.
	 */
	@SuppressWarnings("unused")
	public static SynchronizedQueueResult testQueue(QueueAdapter<Integer> queue) {
		try {
			// TODO - you fill in here to replace the null
			// initialization below to create two Java Threads, one
			// that's passed the producerRunnable and the other that's
			// passed the consumerRunnable.
			Thread consumer = null;
			Thread producer = null;

			// TODO - you fill in here to start the threads. More
			// interesting results will occur if you start the
			// consumer first.

			// Give the Threads a chance to run before interrupting
			// them.
			Thread.sleep(100);

			// TODO - you fill in here to interrupt the threads.

			// TODO - you fill in here to wait for the threads to
			// exit.

			// Do some sanity checking to see if the Threads work as
			// expected.
			if (consumer == null || producer == null) {
				return SynchronizedQueueResult.THREADS_NEVER_CREATED;
			} else if (consumer.isAlive() || producer.isAlive()) {
				return SynchronizedQueueResult.JOIN_NEVER_CALLED;
			} else if (mConsumerCounter == 0 || mProducerCounter == 0) {
				return SynchronizedQueueResult.THREADS_NEVER_RAN;
			} else if (mConsumerCounter == mMaxIterations
					|| mProducerCounter == mMaxIterations) {
				return SynchronizedQueueResult.THREADS_NEVER_INTERUPTED;
			} else if (mConsumerCounter == -1 || mProducerCounter == -1) {
				return SynchronizedQueueResult.THREADS_THREW_EXCEPTION;
			} else if (mConsumerCounter == -2 || mProducerCounter == -2) {
				return SynchronizedQueueResult.THREADS_TIMEDOUT;
			} else {
				return SynchronizedQueueResult.RAN_PROPERLY;
			}
		} catch (Exception e) {
			return SynchronizedQueueResult.TESTING_LOGIC_THREW_EXCEPTION;
		}

	}

}
