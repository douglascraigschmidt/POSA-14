package edu.vuum.mocca.test;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.test.ServiceTestCase;
import edu.vuum.mocca.DownloadBoundServiceSync;
import edu.vuum.mocca.DownloadCall;

/**
 * @class
 *
 * @brief Test the functionality of the Bound Synchronous Service.
 */
public class DownloadBoundServiceSyncTests extends ServiceTestCase<DownloadBoundServiceSync>{
	// Store the intent from makeIntent() to ensure proper creation
    Intent mIntent;
		
    /**
     * Constructor initializes the superclass
     */
    public DownloadBoundServiceSyncTests() {
        super(DownloadBoundServiceSync.class);
    }
	
    // This is called before each test is run.
    public void setUp() throws Exception{
        super.setUp();
		
        // Create an intent using the factory method
        mIntent = DownloadBoundServiceSync.makeIntent(getContext());
    }

    // Make sure the intent will start the correct service		
    public void test_makeIntent_Class () {
        // Make sure the intent will start the correct service
        assertTrue(Utilities.checkClass(mIntent, DownloadBoundServiceSync.class));
    }

    // Call bindService and see if the service correctly downloads the image and responds.	
    public void test_bindService () throws Exception {
        // Bind to the service and get the AIDL interface
        DownloadCall downloadCall = DownloadCall.Stub.asInterface(bindService(mIntent));
		
        // Check that the service is alive
        assertNotNull(getService());
		
        // Download the image synchronously
        String result = downloadCall.downloadImage(Uri.parse(Options.TEST_URI));
		
        // Get the context of THIS project, not the target project
        Context context = getContext().createPackageContext(this.getClass().getPackage().getName(),
                                                            Context.CONTEXT_IGNORE_SECURITY);
		
        // Check that the image actually downloaded
        assertTrue(Utilities.checkDownloadedImage(context, result));
    }
}
