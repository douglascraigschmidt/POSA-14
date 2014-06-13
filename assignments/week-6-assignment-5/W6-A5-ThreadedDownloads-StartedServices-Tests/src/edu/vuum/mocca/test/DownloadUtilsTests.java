package edu.vuum.mocca.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.test.ActivityInstrumentationTestCase2;
import edu.vuum.mocca.DownloadActivity;
import edu.vuum.mocca.DownloadUtils;

/**
 * @class DownloadUtilsTests
 *
 * @brief Test the functionality of the DownloadUtils class
 */
public class DownloadUtilsTests 
             extends
             ActivityInstrumentationTestCase2<DownloadActivity> {
    /**
     * Constructor initializes the superclass.
     */
    public DownloadUtilsTests () {
        super (DownloadActivity.class);
    }
	
    // The intent returned by makeMessengerIntent()
    Intent mIntent;
    
    // The bundle that is part of the intent
    Bundle mExtras;

    /**
     * Use this latch to wait for messages to be received by the
     * Handler's thread.
     */
    static CountDownLatch mLatch;

    /**
     * Stores the Uri received by the Handler.
     */
    static String mReceivedUri = null;

    /**
     * The Handler that receives a Message reply from the
     * IntentService.
     */
    static MessageHandler mHandler;
	
    /**
     * @class MessageHandler
     *
     * @brief Define a handler to catch messages from sendPath()
     */
    static class MessageHandler extends Handler {
        /**
         * Constructor initializes the superclass
         */
        public MessageHandler(Looper myLooper) {
            super(myLooper);
        }

        /**
         * Handle return messages from the IntentService. Store the
         * received Uri and notify the JUnit thread.
         */
        public void handleMessage(Message msg) {
			
            // We don't know what tag they'll use for the path, so we
            // have to search for it.
        	mReceivedUri = Utilities.searchForPath(msg);
			
            // Let the unit test thread know we've gotten a message.
            mLatch.countDown();
        }
    }
	
    /**
     * This method is called before each test is run to perform
     * initialization activities.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
		
        // Make an arbitrary Messenger intent.
        mIntent = DownloadUtils.makeMessengerIntent(getActivity(), 
                                                    DownloadUtilsTests.class, 
                                                    new MessageHandler(Looper.myLooper()), 
                                                    Options.TEST_URI);
        mExtras = mIntent.getExtras();
    }
	
    /**
     * Try downloading a file.
     */
    public void test_downloadFile () {
        Context context = getInstrumentation().getContext();
        String result = DownloadUtils.downloadFile(getActivity(), 
                                                   Uri.parse(Options.TEST_URI));
	
        assertTrue(Utilities.checkDownloadedImage(context, result));
    }
	
    /**
     * Check that the intent has a Messenger attached.
     */
    public void test_makeMessengerIntent_Messenger_Extra () {
        assertTrue(Utilities.checkMessenger(mIntent));
    }
	
    /**
     * Check that the intent has the proper Uri attached
     */
    public void test_makeMessengerIntent_Uri_Extra () {
        assertTrue(Utilities.checkUri(mIntent));		
    }
	
    /**
     * Check that the intent will start the class we told it to
     */
    public void test_makeMessengerIntent_Class () {
        assertTrue(Utilities.checkClass(mIntent, DownloadUtilsTests.class));
    }
	
    /**
     * Try sending a message using sendPath().
     */
    public void test_sendPath () throws Exception {
        mLatch = new CountDownLatch(1);
		
        // Start a thread to catch the message from sendPath()
        new Thread ( new Runnable () {
                public void run() {
                    Looper.prepare();
                    mHandler = new MessageHandler(Looper.myLooper());
                    Looper.loop();
                }
            }).start();
		
        // Wait for the handler to instantiate
        Thread.sleep(Options.SHORT_WAIT_TIME);
		
        // Send a message to ourselves
        DownloadUtils.sendPath(Options.TEST_URI, new Messenger(mHandler));
		
        // Wait for it to get here
        mLatch.await(Options.LONG_WAIT_TIME, TimeUnit.MILLISECONDS);
		
        // See if we got the message or timed out.
        assertNotNull(mReceivedUri);
		
        // Check that the Uri is correct
        assertTrue(mReceivedUri.equals(Options.TEST_URI));
		
        // Other tests use this
        mReceivedUri = null;
    }

    /**
     * Test that the downloadAndRespond method properly uses the other
     * two methods in DownloadUtils.
     */
    public void test_downloadAndRespond() throws Exception {
        mLatch = new CountDownLatch(1);
		
        // Start a thread to handle messages we send to ourselves
        new Thread ( new Runnable () {
                public void run() {
                    Looper.prepare();
                    mHandler = new MessageHandler(Looper.myLooper());
                    Looper.loop();
                }
            }).start();
		
        // Wait for mHandler to instantiate
        Thread.sleep(Options.SHORT_WAIT_TIME);
		
        // Download the image and send a message to ourselves
        DownloadUtils.downloadAndRespond(getActivity(), 
                                         Uri.parse(Options.TEST_URI), 
                                         new Messenger(mHandler));
		
        // Wait for it to get here.
        mLatch.await(Options.LONG_WAIT_TIME, TimeUnit.MILLISECONDS);
		
        // Check if we timed out or got the message
        assertNotNull(mReceivedUri);
		
        // Make sure the image downloaded correctly.
        assertTrue(Utilities.checkDownloadedImage(getInstrumentation().getContext(), mReceivedUri));
		
        // Other tests use this
        mReceivedUri = null;
    }
}
