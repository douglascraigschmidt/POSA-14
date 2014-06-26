package edu.vuum.mocca.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.RemoteException;
import android.test.ActivityInstrumentationTestCase2;
import android.view.WindowManager;
import android.widget.EditText;

import com.robotium.solo.Solo;

import edu.vuum.mocca.DownloadActivity;
import edu.vuum.mocca.DownloadCall;
import edu.vuum.mocca.DownloadCallback;
import edu.vuum.mocca.DownloadRequest;
import edu.vuum.mocca.DownloadUtils;

/**
 * @class DownloadActivityTests
 *
 * @brief Tests the functionality of DownloadActivity. Current tests
 *        only press buttons and check that the correct image was downloaded.
 */
public class DownloadActivityTests extends ActivityInstrumentationTestCase2<DownloadActivity> {
    /**
     * Constructor initializes the superclass
     */
    public DownloadActivityTests() {
        super(DownloadActivity.class);
    }
	
    // This is the handle to the Robotium framework, which lets us interact with the UI 
    Solo mSolo;

    /**
     * The context of this project, not the target project.
     */
    Context mContext;

    // Store the bitmap we expect to be downloaded
    Bitmap mExpected;
    
    // The pathname that we received from DownloadCallback
    static String mReceivedPathname = null;
	
    // The latch used to synchronize DownloadCallback and the JUnit thread.
    static CountDownLatch mLatch = null;
    
    /**
     * This is the implementation of DownloadCallback we'll use to test mDownloadRequest
     */
    DownloadCallback.Stub mDownloadCallback = new DownloadCallback.Stub() {
            /**
             * Set the received pathname in this activity then notify the JUnit thread that
             * we've gotten a result.
             */
    		@Override
            public void sendPath(final String imagePathname) throws RemoteException {
    			mReceivedPathname = imagePathname;
    			
    			if (mLatch != null)
    				mLatch.countDown();
    		}
        };
    
    // This is called once before each test is run.
    public void setUp() throws Exception{
        super.setUp();

        // Setup Robotium and get the EditText View
        mSolo = new Solo(getInstrumentation(), getActivity());
		
        mContext = getInstrumentation().getContext();
		
        EditText edit_ = (EditText) mSolo.getView(edu.vuum.mocca.R.id.url);
        mSolo.clearEditText(edit_);		
        mSolo.enterText(edit_, Options.TEST_URI);
		
        mExpected = Utilities.getTestBitmap(mContext); 
		
        getInstrumentation().callActivityOnStart(getActivity());
        getInstrumentation().callActivityOnResume(getActivity());
    
        // Let us dismiss the lockscreen
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            }
        });
        
        // Wait for things to settle
        Thread.sleep(Options.SHORT_WAIT_TIME);
        
    }
	
    /**
     * Check that we're bound to the DownloadBoundServiceSync
     */
    public void testBoundToSync () throws Exception {
    	assertTrue(getActivity().isBoundToSync());
    }
    
    /**
     * Check that we're bound to the DownloadBoundServiceSync
     */
    public void testBoundToAsync () throws Exception {
    	assertTrue(getActivity().isBoundToAsync());
    }
    
    /**
     * Check that we correctly received mDownloadCall
     */
    public void testDownloadCall () throws Exception {
    	
    	// Get the DownloadCall object
    	DownloadCall downloadCall = getActivity().getDownloadCall();
    	
    	// Make sure it's not null
    	assertNotNull(downloadCall);
    	
    	// Ask downloadCall to download a file
    	String fileName = downloadCall.downloadImage(Uri.parse(Options.TEST_URI));
    	
    	// Then check if it's there.
    	assertTrue(Utilities.checkDownloadedImage(mContext, fileName));
    }
    
    /**
     * Check that we correctly received mDownloadRequest
     */
    public void testDownloadRequest () throws Exception {
    	
    	// Get the DownloadCall object
    	DownloadRequest downloadRequest = getActivity().getDownloadRequest();
    	
    	// Make sure it's not null
    	assertNotNull(downloadRequest);
    	
    	// Setup synchronization stuff
    	mLatch = new CountDownLatch(1);
    	
    	// Ask downloadCall to download a file.
    	downloadRequest.downloadImage(Uri.parse(Options.TEST_URI), mDownloadCallback);
    	
    	// Wait for a result
    	mLatch.await(Options.LONG_WAIT_TIME, TimeUnit.MILLISECONDS);
    
    	// Check that we actually got a result
    	assertNotNull(mReceivedPathname);
    	
    	// Check that the the file is correctly downloaded
    	assertTrue(Utilities.checkDownloadedImage(mContext, mReceivedPathname));
    }

    /**
     * Call the target activity's mDownloadCallback and see if the correct image is displayed.
     */
    public void testDownloadCallback() throws Exception {
        		
        // Make sure the current image isn't the one we're looking for
        assertFalse(mExpected.sameAs(getActivity().mCurrentBitmap));
		
        // Download the file we want displayed
        String pathName = DownloadUtils.downloadFile(getActivity(), Uri.parse(Options.TEST_URI));
        
        // Send the pathname to mDownloadCallback
        getActivity().getDownloadCallback().sendPath(pathName);
        
        // Wait for the image to display
        Thread.sleep(Options.SHORT_WAIT_TIME);
		
        // Check if we displayed the correct image
        assertTrue(mExpected.sameAs(getActivity().mCurrentBitmap));
    }

    
    /**
     * Push the button and see if the correct image is displayed.
     */
    public void testBoundAsyncButton() throws Exception {
        		
        // Make sure the current image isn't the one we're looking for
        assertFalse(mExpected.sameAs(getActivity().mCurrentBitmap));
		
        // Click on the "Start ThreadPool Button"
        mSolo.clickOnView(mSolo.getView(edu.vuum.mocca.R.id.bound_async_button));
		
        // Wait for the image to download
        Thread.sleep(Options.LONG_WAIT_TIME);
		
        // Check if we displayed the correct image
        assertTrue(mExpected.sameAs(getActivity().mCurrentBitmap));
    }

    /**
     * Push the button and see if the correct image is displayed
     */
    public void testBoundSyncButton() throws Exception {
        
        // Make sure the current image isn't the one we're looking for
        assertFalse(mExpected.sameAs(getActivity().mCurrentBitmap));
		
        // Click on the "Start ThreadPool Button"
        mSolo.clickOnView(mSolo.getView(edu.vuum.mocca.R.id.bound_sync_button));
		
        // Wait for the image to download
        Thread.sleep(Options.LONG_WAIT_TIME);
		
        // Check if we displayed the correct image
        assertTrue(mExpected.sameAs(getActivity().mCurrentBitmap));
    }
}
