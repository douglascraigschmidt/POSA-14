package edu.vuum.mocca;

import java.util.List;

/**
 * @class PalantirManager
 *
 * @brief Uses a "fair" Semaphore to control access to the
 *        available Palantiri.  Implements the "Pooling" pattern
 *        in POSA3.
 */
public class PalantirManager {
    /**
     * Max number of Palantiri available.
     */
    private int mMaxPalantiri = 0;

    /**
     * Simple implementation of a Semaphore that can be configured
     * to use the "fair" policy.
     */
    private SimpleSemaphore mAvailable = null;

    /**
     * List of the available Palantiri.
     */
    protected List<Palantir> mPalantiri = null;

    /**
     * Keeps track of the Palantiri that are available for use.
     * The indices in this array mirror the list of mPanatiri.
     */
    protected boolean[] used = null;

    /**
     * Create a resource manager for the palantiri passed as a
     * parameter.
     */
    PalantirManager(final List<Palantir> palantiri) {
        mMaxPalantiri = palantiri.size();
        mPalantiri = palantiri;
        used = new boolean[palantiri.size()];

        /**
         * Use the "fair" policy.
         */
        mAvailable = new SimpleSemaphore(mMaxPalantiri, true);
    }

    /**
     * Get the next available Palantir from the resource pool,
     * blocking until one is available.
     */
    public Palantir acquirePalantir() {
        mAvailable.acquireUninterruptibly();
        return getNextAvailablePalantir();
    }

    /**
     * Returns the designated @code palantir so that it's
     * available for others to use.
     */
    public void releasePalantir(final Palantir palantir) {
        if (markAsUnused(palantir))
            mAvailable.release();
    }

    /**
     * Get the next available Palantir from the resource pool.
     */
    protected synchronized Palantir getNextAvailablePalantir() {
        // Linear search is fine for this simple demo.
        for (int i = 0; i < mMaxPalantiri; ++i) {
            if (!used[i]) {
                used[i] = true;
                return mPalantiri.get(i);
            }
        }
        // Not reached unless something really weird happens..
        return null; 
    }

    /**
     * Return the @code palantir back to the resource pool.
     */
    protected synchronized boolean markAsUnused(final Palantir palantir) {
        // Linear search is fine for this simple demo.
        for (int i = 0; i < mMaxPalantiri; ++i) {
            if (palantir == mPalantiri.get(i)) {
                if (used[i]) {
                    used[i] = false;
                    return true;
                } else
                    return false;
            }
        }
        return false;
    }
}

