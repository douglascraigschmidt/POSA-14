package edu.vuum.mocca.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import edu.vuum.mocca.DownloadActivity;

/**
 * @class DownloadActivityTests
 *
 * @brief Tests the functionality of DownloadActivity. Current tests
 *        only press buttons and check that the correct image was downloaded.
 */
public class DownloadActivityTests 
             extends 
             ActivityInstrumentationTestCase2<DownloadActivity> {
    /**
     * Constructor initializes the superclass
     */
    public DownloadActivityTests() {
        super(DownloadActivity.class);
    }
	
    /**
     * This is the handle for Robotium, which allows us to interact
     * with the UI.
    */
    Solo mSolo;

    /**
     * The context of this project, not the target project.
     */
    Context mContext;
    
    /**
     * The activity we're testing
     */
    DownloadActivity mActivity;    
    
    /**
     * Store the bitmap that we expect to be downloaded.
     */
    Bitmap mExpected;
	
    /**
     * Called before each test is run to perform the initialization.
     */
    public void setUp() throws Exception{
        super.setUp();
        
        mActivity = getActivity();
        
        // Setup Robotium and get the EditText View.
        mSolo = new Solo(getInstrumentation(), mActivity);
		
        mContext = getInstrumentation().getContext();
		
        EditText edit_ = (EditText) mSolo.getView(edu.vuum.mocca.R.id.url);
        mSolo.clearEditText(edit_);		
        mSolo.enterText(edit_, Options.TEST_URI);
		
        mExpected = BitmapFactory.decodeResource(mContext.getResources(),
                                                 R.drawable.dougs);
        
        getInstrumentation().callActivityOnStart(mActivity);
        getInstrumentation().callActivityOnResume(mActivity);
 
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
     * Push the button and see if the correct image is displayed.
     */
    public void testThreadPoolButton() throws Exception {
        
        // Make sure the current image isn't the one we're looking for
        assertFalse(mExpected.sameAs(mActivity.mCurrentBitmap));
		
        // Click on the "Start ThreadPool Button"
        mSolo.clickOnView(mSolo.getView(edu.vuum.mocca.R.id.thread_pool_button));
        
        // Wait for the image to download
        Thread.sleep(Options.LONG_WAIT_TIME);
		
        // Check if we displayed the correct image
        assertTrue(mExpected.sameAs(mActivity.mCurrentBitmap));
    }
	
    /**
     * Push the button and see if the correct image is displayed.
     */
    public void testIntentServiceButton() throws Exception {
     
        // Make sure the current image isn't the one we're looking for
        assertFalse(mExpected.sameAs(mActivity.mCurrentBitmap));
		
        // Click on the "Start ThreadPool Button"
        mSolo.clickOnView(mSolo.getView(edu.vuum.mocca.R.id.intent_service_button));
        
        // Wait for the image to download
        Thread.sleep(Options.LONG_WAIT_TIME);
		
        // Check if we displayed the correct image
        assertTrue(mExpected.sameAs(mActivity.mCurrentBitmap));
    }
}
