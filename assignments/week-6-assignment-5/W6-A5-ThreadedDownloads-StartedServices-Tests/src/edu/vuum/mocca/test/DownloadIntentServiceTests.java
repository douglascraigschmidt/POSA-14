package edu.vuum.mocca.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.test.ServiceTestCase;
import edu.vuum.mocca.DownloadIntentService;
import edu.vuum.mocca.DownloadUtils;

/**
 * @class DownloadIntentServiceTests
 *
 * @brief Tests the functionality of the DownloadIntentService.
 */
public class DownloadIntentServiceTests 
             extends
             ServiceTestCase<DownloadIntentService> {
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
     * Store the intent from makeIntent() to test proper creations.
     */
    Intent mIntent;
    
    /**
     * The context of THIS project, not the target project
     */
    Context mContext;
	
    /**
     * Constructor initializes the superclass.
     */
    public DownloadIntentServiceTests() {
        super(DownloadIntentService.class);
    }
	
    /**
     * @class MessageHandler
     *
     * @brief Make a handler to catch Messages sent from the service.
     */
    static class MessageHandler extends Handler {
        /**
         * Allows us to explicitly specify which thread's Looper to use.
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
     * This method is called before each test is called to perform
     * initialization activities.
     */
    public void setUp() throws Exception{
        super.setUp();
		
        // Make an intent with the handler using the factory method.
        mIntent = DownloadIntentService.makeIntent(getContext(), 
                                                   new Handler(), 
                                                   Options.TEST_URI);
        
        // Get the context for THIS project, not the target project.
        mContext = getContext().createPackageContext(this.getClass().getPackage().getName(),
                                                            Context.CONTEXT_IGNORE_SECURITY);
        
    }
	
    /**
     * Check that the intent starts the correct service.
     */
    public void test_makeIntent_Class () {
        assertTrue(Utilities.checkClass(mIntent, DownloadIntentService.class));
    }
	
    /**
     * Check that the intent has the correct Uri.
     */
    public void test_makeIntent_Uri () {
        assertTrue(Utilities.checkUri(mIntent));
    }
	
    /**
     * Check that the intent has a Messenger attached.
     */
    public void test_makeIntent_Messenger () {
        assertTrue(Utilities.checkMessenger(mIntent));
    }
	
    /**
     * Try starting the service
     */
    public void test_startService () throws Exception {
    	
    	mLatch = new CountDownLatch(1);
		
        // Start a thread to handle the message when it's sent.
        new Thread(new Runnable() {
                public void run() {
                    Looper.prepare();
                    mHandler = new MessageHandler(Looper.myLooper());
                    Looper.loop();
                }
            }).start();
		
        // Wait for the handler to get instantiated
        Thread.sleep(Options.SHORT_WAIT_TIME);	

        //Create an Intent to start the service
        mIntent = DownloadIntentService.makeIntent(getContext(), 
                mHandler, 
                Options.TEST_URI);
        
        // Start the service
        startService(mIntent);
		
        assertNotNull(getService());
		
        // Wait for it to send us a Message (or time out)
        mLatch.await(Options.LONG_WAIT_TIME, TimeUnit.MILLISECONDS);
        
        // Check if we got a Message or timed out
        assertNotNull(mReceivedUri);
		
        // Check that the image actually downloaded.
        assertTrue(Utilities.checkDownloadedImage(mContext, mReceivedUri));
    }
}
