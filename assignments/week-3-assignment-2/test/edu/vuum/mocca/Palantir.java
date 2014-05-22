package edu.vuum.mocca;
/**
 * @class Palantir
 *
 * @brief Provides an interface for gazing into a Palantir.
 *        Essentially plays the role of a "command" in the Command
 *        pattern.
 */

interface Palantir {
    /**
     * Gaze into the Palantir (and go into a tranc ;-)).
     */
    public void gaze();

    /**
     * Return the name of the Palantir.
     */
    public String name();
}

