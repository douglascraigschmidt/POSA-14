package edu.vuum.mocca.test;

/**
 * @class Options
 * 
 * @brief Holds global constants for the testing suite. More
 *        convenient than grabbing resources from /res and trying to
 *        finagle a working Context out of the test classes every time
 *        we want to use TEST_URI.
 *
 */
public class Options {
	
    /**
     * The online URL of the image we're using for testing.
     */
    static final String TEST_URI = "https://d396qusza40orc.cloudfront.net/posa/dougs-xsmall.jpg";

    /**
     * Whatever image we're testing, store it in assets/ so
     * we can compare it to what's downloaded.
     */
    static final String TEST_IMAGE = "dougs.jpg";
	
    /**
     * Time we should wait for things to instantiate.
     */
    static final long SHORT_WAIT_TIME = 10000;
	
    /**
     * Time we should wait for things to download.
     */
    static final long LONG_WAIT_TIME = 25000;
}
