package edu.vuum.mocca.test;

/**
 * @class Options
 * 
 * @brief Holds global constants for the testing suite. More convenient than
 *        grabbing resources from /res and trying to finagle a working Context
 *        out of the test classes every time we want to use TEST_URI.
 * 
 */
public class TestOptions {
    /**
     * Time we should wait for things to run.
     */
    static final long ACCEPTABLE_STARTUP_LENGTH = 1000;

    /**
     * Time we should wait for things to run.
     */
    static final long ACCEPTABLE_RUNTIME_LENGTH = 2000;

    /**
     * Time we should wait for things to download.
     */
    static final long LONG_WAIT_TIME = 5000;

    /**
     * Various test strings.
     */
    static final String JAVA_CONSOLE_UNIX = "Ready...Set...Go!\nping  (1)\n_pong (1)\nping  (2)\n_pong (2)\nping  (3)\n_pong (3)\nping  (4)\n_pong (4)\nping  (5)\n_pong (5)\nping  (6)\n_pong (6)\nping  (7)\n_pong (7)\nping  (8)\n_pong (8)\nping  (9)\n_pong (9)\nping  (10)\n_pong (10)\nDone!\n";
    static final String PING_ALL_FIRST_UNIX = "Ready...Set...Go!\nping  (1)\nping  (2)\nping  (3)\nping  (4)\nping  (5)\nping  (6)\nping  (7)\nping  (8)\nping  (9)\nping  (10)\n_pong (1)\n_pong (2)\n_pong (3)\n_pong (4)\n_pong (5)\n_pong (6)\n_pong (7)\n_pong (8)\n_pong (9)\n_pong (10)\nDone!\n";
    static final String PING_ALL_FIRST_WINDOWS = "Ready...Set...Go!\r\nping  (1)\r\nping  (2)\r\nping  (3)\r\nping  (4)\r\nping  (5)\r\nping  (6)\r\nping  (7)\r\nping  (8)\r\nping  (9)\r\nping  (10)\r\n_pong (1)\r\n_pong (2)\r\n_pong (3)\r\n_pong (4)\r\n_pong (5)\r\n_pong (6)\r\n_pong (7)\r\n_pong (8)\r\n_pong (9)\r\n_pong (10)\r\nDone!\r\n";
    static final String PONG_ALL_FIRST_WINDOWS = "Ready...Set...Go!\r\n_pong (1)\r\n_pong (2)\r\n_pong (3)\r\n_pong (4)\r\n_pong (5)\r\n_pong (6)\r\n_pong (7)\r\n_pong (8)\r\n_pong (9)\r\n_pong (10)\r\nping  (1)\r\nping  (2)\r\nping  (3)\r\nping  (4)\r\nping  (5)\r\nping  (6)\r\nping  (7)\r\nping  (8)\r\nping  (9)\r\nping  (10)\r\nDone!";
    static final String PONG_ALL_FIRST_UNIX = "Ready...Set...Go!\n_pong (1)\n_pong (2)\n_pong (3)\n_pong (4)\n_pong (5)\n_pong (6)\n_pong (7)\n_pong (8)\n_pong (9)\n_pong (10)\nping  (1)\nping  (2)\nping  (3)\nping  (4)\nping  (5)\nping  (6)\nping  (7)\nping  (8)\nping  (9)\nping  (10)\nDone!";
    static final String JAVA_CONSOLE_WINDOWS = "Ready...Set...Go!\r\nping  (1)\r\n_pong (1)\r\nping  (2)\r\n_pong (2)\r\nping  (3)\r\n_pong (3)\r\nping  (4)\r\n_pong (4)\r\nping  (5)\r\n_pong (5)\r\nping  (6)\r\n_pong (6)\r\nping  (7)\r\n_pong (7)\r\nping  (8)\r\n_pong (8)\r\nping  (9)\r\n_pong (9)\r\nping  (10)\r\n_pong (10)\r\nDone!";
    static final String ANDROID_TEXTVIEW = "Ready...Set...Go!\nping    (1)\n_pong (1)\nping    (2)\n_pong (2)\nping    (3)\n_pong (3)\nping    (4)\n_pong (4)\nping    (5)\n_pong (5)\nping    (6)\n_pong (6)\nping    (7)\n_pong (7)\nping    (8)\n_pong (8)\nping    (9)\n_pong (9)\nping    (10)\n_pong (10)\nDone!\n";

}
