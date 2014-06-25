package edu.vuum.mocca.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.test.ServiceTestCase;
import edu.vuum.mocca.DownloadBoundServiceAsync;
import edu.vuum.mocca.DownloadCallback;
import edu.vuum.mocca.DownloadRequest;

/**
 * @class DownloadBoundServiceAsyncTests
 *
 * @brief Tests the functionality of the Asynchronous Bound Service. 
 */
public class DownloadBoundServiceAsyncTests extends ServiceTestCase<DownloadBoundServiceAsync>{
    // Store the intent from makeIntent() to ensure proper creation
    Intent mIntent;

    // Use this latch to wait for messages to be received by the Handler's thread 
    static CountDownLatch mLatch;

    // Stores the Uri received by the Handler
    static String mReceivedUri = null;

    // Create a callback to allow the service to send us a result
    DownloadCallback.Stub callback_ = new DownloadCallback.Stub () {
            // Store the received Uri and notify the JUnit thread
            public void sendPath(String path) {
                mReceivedUri = path;
			
                // Let the unit test thread know we've gotten a
                // message.
                mLatch.countDown();
            }
	};

    /**
     * Constructor initializes the superclass
     */
    public DownloadBoundServiceAsyncTests() {
        super(DownloadBoundServiceAsync.class);
    }

    // This is called before each test is run
    public void setUp() throws Exception{
        super.setUp();
		
        // Create an intent using the factory method
        mIntent = DownloadBoundServiceAsync.makeIntent(getContext());
    }
	
    /**
     * Check that the intent will start the correct class
     */
    public void test_makeMIntentClass () {
        assertTrue(Utilities.checkClass(mIntent, DownloadBoundServiceAsync.class));
    }
	
    /**
     * Test bindService functionality
     */
    public void test_bindService () throws Exception{
        mLatch = new CountDownLatch(1);
		
        // Bind to the service and get the AIDL interface
        DownloadRequest request = DownloadRequest.Stub.asInterface(bindService(mIntent));
		
        // Make sure the service is running
        assertNotNull(getService());
		
        // Download the image
        request.downloadImage(Uri.parse(Options.TEST_URI), callback_);
		
        // Wait for the callback
        mLatch.await(Options.LONG_WAIT_TIME, TimeUnit.MILLISECONDS);
		
        // Make sure the callback was called
        assertNotNull(mReceivedUri);
		
        // Get the context for THIS project, not the target project
        Context context = getContext().createPackageContext(this.getClass().getPackage().getName(),
                                                            Context.CONTEXT_IGNORE_SECURITY);
		
        // Make sure the file actually downloaded
        assertTrue(Utilities.checkDownloadedImage(context, mReceivedUri));
    }
}
