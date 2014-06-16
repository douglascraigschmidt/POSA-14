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
import edu.vuum.mocca.ThreadPoolDownloadService;

/**
 * @class ThreadPoolDownloadServiceTests
 * 
 * @brief Tests the functionality of the ThreadPoolDownloadService.
 */
public class ThreadPoolDownloadServiceTests 
             extends
             ServiceTestCase<ThreadPoolDownloadService> {
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
    public ThreadPoolDownloadServiceTests() {
        super(ThreadPoolDownloadService.class);
    }
	
    /**
     * @class MessageHandler
     * 
     * @brief Define a private handler to catch messages from the service.
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
     * This is called once before each test is run.
     */
    public void setUp() throws Exception{
        super.setUp();
		
        // Make an intent using the factory method
        mIntent = ThreadPoolDownloadService.makeIntent(getContext(), 
                                                       new Handler(), 
                                                       Options.TEST_URI);
        
        // Get the context for THIS project, not the target project.
        mContext = getContext().createPackageContext(this.getClass().getPackage().getName(),
                                                            Context.CONTEXT_IGNORE_SECURITY);
        
    }
	
    /**
     * Check that the intent will start the correct service.
     */
    public void test_makeIntent_Class () {
        assertTrue(Utilities.checkClass(mIntent, ThreadPoolDownloadService.class));
    }
	
    /**
     * Check that the intent has the correct uri.
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
     * Test starting the service.
     */
    public void test_startService () throws Exception{
    	
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
		
        // Check if the service actually started
        assertNotNull(getService());
		
        // Wait for the service to download and send us a message
        mLatch.await(Options.LONG_WAIT_TIME, TimeUnit.MILLISECONDS);
		
        // See if we timed out or actually got a message
        assertNotNull(mReceivedUri);
		
        // Get the context for THIS project, not the target project
        Context context = getContext().createPackageContext(this.getClass().getPackage().getName(),
                                                            Context.CONTEXT_IGNORE_SECURITY);
		
        // Check that the file downloaded correctly
        assertTrue(Utilities.checkDownloadedImage(context, mReceivedUri));
    }
}


