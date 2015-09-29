package edu.vuum.mocca;

import java.util.concurrent.Semaphore;

/**
 * This class implements a Java program that creates two threads,
 * "ping" and "pong", that use a pair of Java Semaphores to
 * alternately print "Ping" and "Pong", respectively, on the display.
 */
public class PlayPingPong implements Runnable {
    /**
     * Number of iterations to ping/pong.
     */
    private final int mMaxIterations;

    /**
     * This class uses semaphores to implement the acquire()
     * and release() hook methods that schedule the ping/pong
     * algorithm.
     */
    static class PingPongThread extends Thread {
        /**
         * Number of iterations to ping/pong.
         */
        private final int mMaxIterations;

        /**
         * Data member that indicates the string to print (typically a
         * "ping" or a "pong").
         */
        private final String mStringToPrint;

        /**
         * Semaphores that schedule the ping/pong algorithm
         */
        private final Semaphore mFirstSema;
        private final Semaphore mSecondSema;

        /** 
         * Constructor initializes the various fields.
         */
        public PingPongThread(int maxIterations,
                              String stringToPrint,
                              Semaphore firstSema,
                              Semaphore secondSema) {
            mMaxIterations = maxIterations;
            mStringToPrint = stringToPrint;
            mFirstSema = firstSema;
            mSecondSema = secondSema;
        }

        /**
         * Hook method for ping/pong acquire.
         */
        void acquire() {
            mFirstSema.acquireUninterruptibly();
        }

        /**
         * Hook method for ping/pong release.
         */
        void release() {
            mSecondSema.release();
        }

        /**
         * This method runs in a separate thread of control and
         * implements the core ping/pong algorithm.
         */
        public void run() {
            // Loop repeatedly performing the protocol for printing a
            // "ping" or a "pong" on the display.
            for (int loopsDone = 1;
                 loopsDone <= mMaxIterations;
                 ++loopsDone) {
                acquire();
                System.out.println(mStringToPrint 
                		           + "(" + loopsDone + ")");
                release();
            }
            // Exit the thread when the loop is done.
        }
    }

    /**
     * Constructor stores the PlatformStrategy and the number of
     * iterations to play ping/pong.
     */
    public PlayPingPong (int maxIterations) {
        // Number of iterations to perform pings and pongs.
        mMaxIterations = maxIterations;
    }

    /**
     * Start running the ping/pong code, which can be called from a
     * main() function in a Java class, an Android Activity, etc.
     */ 
    public void run() {
        // Let the user know we're starting.
        System.out.println("Ready...Set...Go!");

        // Create the semaphores that schedule threads printing "ping"
        // and "pong" in the correct alternating order.
        final Semaphore pingSema =
            new Semaphore(1); // Starts out unlocked.
        final Semaphore pongSema =
            new Semaphore(0);
                
        // Create the ping and pong threads.
        final PingPongThread pingThread =
            new PingPongThread(mMaxIterations,
                               "ping",
                               pingSema,
                               pongSema);
        final PingPongThread pongThread = 
            new PingPongThread(mMaxIterations,
                               "pong", 
                               pongSema,
                               pingSema);

        // Start ping and pong threads, which calls their run()
        // methods.
        pingThread.start();
        pongThread.start();

        try {
            // Barrier synchronization to wait for all work to be done
            // before exiting play().
            pingThread.join();
            pongThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Let the user know we're done. 
        System.out.println("Done!");
    }
}
