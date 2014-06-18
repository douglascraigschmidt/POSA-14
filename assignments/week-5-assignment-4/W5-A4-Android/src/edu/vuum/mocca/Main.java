package edu.vuum.mocca;

/**
 * @class Main
 *
 * @brief This class is the main entry point for the command-line
 *        version of the PlayPingPong app.
 */
public class Main 
{
    /**
     * The Java virtual machine requires the instantiation of a main
     * method to run the console version of the PlayPingPong app.
     */
    public static void main(String[] args)
    {
        /** 
         * Initializes the Platform singleton with the appropriate
         * PlatformStrategy, which in this case will be the
         * ConsolePlatform.
         */
        PlatformStrategy.instance
            (new PlatformStrategyFactory(System.out,
                                         null).makePlatformStrategy());

        /** Initializes the Options singleton. */
        Options.instance().parseArgs(args);

        /**
         * Create a PlayPingPong object to run the designated number of
         * iterations.
         */
        PlayPingPong pingPong =
            new PlayPingPong(PlatformStrategy.instance(),
                             Options.instance().maxIterations(),
                             Options.instance().maxTurns(),
                             Options.instance().syncMechanism());

        /**
         * Start a thread to play ping-pong.
         */
        new Thread (pingPong).start();
    }
}
