import static org.junit.Assert.fail;

import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Test;

import edu.vuum.mooca.SynchronizedQueue;
import edu.vuum.mooca.SynchronizedQueue.SynchronizedQueueResult;

public class SynchronizedQueueTest {

	@Test
	public void test() {
		int queueSize = SynchronizedQueue.mMaxIterations / 10;

		// Test the ArrayBlockingQueue, which should pass the test.
		SynchronizedQueue.mQueue = new SynchronizedQueue.QueueAdapter<Integer>(
				new ArrayBlockingQueue<Integer>(queueSize));
		// testQueue("ArrayBlockingQueue", SynchronizedQueue.mQueue);

		SynchronizedQueueResult result = SynchronizedQueue
				.testQueue(SynchronizedQueue.mQueue);

		if (result != SynchronizedQueueResult.RAN_PROPERLY) {
			fail("" + result.getString());
		}
	}

}
