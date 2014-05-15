package edu.vuum.mooca;

/**
 * @class SynchronizedQueueImpl
 *
 * @brief This is where you put your implementation code to to (1)
 *        create, (2) start, (3) interrupt, and (4) wait for the
 *        completion of multiple Java Threads.  This class plays the
 *        role of the "Concrete Class" in the Template Method pattern
 *        and isolates the code written by students from the
 *        underlying SynchronizedQueue test infrastructure.
 *
 *        Make sure to keep all the "TODO" comments in the code below
 *        to make it easy for peer reviewers to find them.
 */
public class SynchronizedQueueImpl extends SynchronizedQueue {
    // TODO - change this to true if you want to see diagnostic
    // output on the console as the test runs.
    static {
        diagnosticsEnabled = false;
    }
	
    protected void createThreads() {
        // TODO - replace the "null" assignments below to create two
        // Java Threads, one that's passed the mProducerRunnable and
        // the other that's passed the mConsumerRunnable.
        mConsumer = null;
        mProducer = null;
    }
    
    protected void startThreads() {
        // TODO - you fill in here to start the threads. More
        // interesting results will occur if you start the
        // consumer first.
    }

    protected void interruptThreads() {
        // TODO - you fill in here to interrupt the threads.
    }

    protected void joinThreads() throws InterruptedException {
        // TODO - you fill in here to wait for the threads to
        // exit.
    }
}            
