package edu.vuum.mocca;

/**
 * This class is the main entry point for the command-line version of
 * the PlayPingPong app.
 */
public class Main {
    /**
     * The Java virtual machine requires the instantiation of a main
     * method to run the console version of the PlayPingPong app.
     */
    public static void main(String[] args) {
        // Initializes the Options singleton. 
        Options.instance().parseArgs(args);

        // Create a PlayPingPong object to run the designated number
        // of iterations.
        final PlayPingPong pingPong =
            new PlayPingPong(Options.instance().maxIterations());

        // Start a thread to play ping-pong.
        new Thread (pingPong).start();
    }
}
