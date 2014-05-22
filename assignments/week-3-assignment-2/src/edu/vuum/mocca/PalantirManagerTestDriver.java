package edu.vuum.mocca;

import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;

/**
 * @class PalantirManagerTest
 *
 * @brief This program tests student implementations of
 *        SimpleSimpleAtomicLong and SimpleSemaphore, which are used
 *        to correctly implement a resource manager that
 *        limits the number of Beings from Middle-Earth who
 *        can concurrently gaze into a Palantir (if you're not a Lord of the
 *        Ring's fan see http://en.wikipedia.org/wiki/Palantir for more
 *        information on Palantir).
 */
class PalantirManagerTestDriver 
{
    /**
     * Total number of times each Palantir user gets to gaze into a
     * Palantir.
     */
    final static int mMaxPalantirSessions = 5;

    static volatile long mMaxActiveThreads = 0;

    static SimpleAtomicLong mActiveThreads = new SimpleAtomicLong(0);

    /**
     * Resource Manager that controls access to the available
     * Palantiri.
     */
    static PalantirManager mPalantirManager = null;

    /**
     * Object that attempts to check whether the Semaphore
     * implementation is "fair".
     */
    static FairnessChecker mFairnessChecker = null;
    
    /* Runnable passed to each Thread that uses a Palantir. */
    static Runnable usePalantir = new Runnable() {
            /**
             * This is the main loop run by each Being of Middle-Earth
             * who wants to gaze into a Palantir.
             */
            public void run() {
                // Bound the total number of times that each user can
                // gaze into a Palantir.
                for (int i = 0; i < mMaxPalantirSessions; ++i) {
                    System.out.println(Thread.currentThread().getName() 
                                       + " is acquiring the palantir");

                    // Used to check for Semaphore fairness.
                    mFairnessChecker.addNewThread(Thread.currentThread().getName());

                    // Get access to a Palantir, which will block if
                    // all the available Palantiri are in use.
                    Palantir palantir = mPalantirManager.acquirePalantir();

                    // There's a race condition here since it's
                    // possible for one thread to call
                    // mFairnessChecker.addNewThread() and then yield
                    // to another thread which again calls
                    // mFairnessChecker.addNewThread() and then goes
                    // on without interruption to call
                    // mPalantirManager.acquirePalantir(), which will
                    // fool the fairness checker into wrongly thinking
                    // the acquisition wasn't fair. we'll just give a
                    // warning (rather than an error) if it looks like
                    // the semaphore acquire() method isn't "fair".
                    if (!mFairnessChecker.checkOrder(Thread.currentThread().getName()))
                        System.out.println(Thread.currentThread().getName() 
                                           + ": warning, semaphore acquire may not be fair");

                    // Ensure that the Semaphore implementation is
                    // correctly limiting the number of Palantir
                    // gazers.
                    long activeThreads = mActiveThreads.getAndIncrement();
                    if (mMaxActiveThreads < activeThreads)
                        {
                            System.out.println("too many threads = " 
                                               + activeThreads);
                            throw new RuntimeException();
                        }
                    System.out.println(Thread.currentThread().getName() 
                                       + " is starting to gaze at the "
                                       + palantir.name() 
                                       + " palantir");

                    // Gaze at the Palantir for the time alloted in
                    // the command.
                    palantir.gaze();

                    // Indicate this Being is no longer using the
                    // Palantir.
                    mActiveThreads.decrementAndGet();

                    System.out.println(Thread.currentThread().getName() 
                                       + " is finished gazing at the "
                                       + palantir.name() 
                                       + " palantir");

                    // Return the Palantir back to the shared pool so
                    // other Beings can gaze at it.
                    mPalantirManager.releasePalantir(palantir);

                    System.out.println(Thread.currentThread().getName()
                                       + " is releasing the "
                                       + palantir.name() + 
                                       " palantir");                    
                }
                    
            }
        };

    /**
     * This factory creates a list of Palantiri.
     */
    static List<Palantir> makePalantiri() {
        List<Palantir> palantiri = new ArrayList<Palantir>();

        // MinasTirith Palantir
        palantiri.add (new Palantir() { 
                public void gaze() { 
                    try {
                        Thread.sleep(10); 
                    }
                    catch (InterruptedException e) {}
                }
                public String name() {return "MinasTirith";}
            });
        // Orthanc Palantir
        palantiri.add (new Palantir() {
                public void gaze() {
                    try {
                        Thread.sleep(150);
                    }
                    catch (InterruptedException e) {}
                }

                public String name() {return "Orthanc";}
            });
        // Barad-dur Palantir
        palantiri.add (new Palantir() {
                public void gaze() 
                { 
                    try {
                        // The unblinking eye gazes for a long time
                        // ;-)
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e) {}
                }
                public String name() {return "Barad-dur";}
            });

        return palantiri;
    }
    /**
     * Main entry point method that runs the test.
     */
    public static void main(String[] args) {
        try {
            System.out.println("Starting PalantirManagerTest");

            // Get the list of available Palantiri.
            List<Palantir> palantiri = makePalantiri();

            // Limit the number of users (threads) that can gaze into
            // the available Palantiri.
            mMaxActiveThreads = palantiri.size();

            // Create a resource manager that control access to the
            // available Palantiri.
            mPalantirManager = new PalantirManager(palantiri);

            // Create a list of Middle-Earth Beings who want to use
            // the Palantir.
            List<Thread> palantirUsers = new ArrayList<Thread>();
            palantirUsers.add(new Thread(usePalantir, "Pippen"));
            palantirUsers.add(new Thread(usePalantir, "Aragorn"));
            palantirUsers.add(new Thread(usePalantir, "Denathor"));
            palantirUsers.add(new Thread(usePalantir, "Sauron"));
            palantirUsers.add(new Thread(usePalantir, "Saruman"));

            // Create an object that attempts to check whether the
            // Semaphore implementation is "fair".
            mFairnessChecker = new FairnessChecker(palantirUsers.size());

            // Start all the Threads that Middle-Earth Beings use to
            // gaze into the Palantir.
            for (ListIterator<Thread> iterator = palantirUsers.listIterator();
                 iterator.hasNext();
                 )
                iterator.next().start();

            // Barrier synchronization that waits for all the Threads
            // to exit.
            for (ListIterator<Thread> iterator = palantirUsers.listIterator();
                 iterator.hasNext();
                 )
                iterator.next().join();

            System.out.println("Finishing PalantirManagerTest");
        }
        catch (Exception e) { 
            System.out.println("Something went wrong");
        }
    }
    
}
