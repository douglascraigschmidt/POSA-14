package edu.vuum.mocca;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * This class is used the base class for the DownloadActivity.  It
 * instantiates the UI and handles displaying images and getting text
 * from the EditText object by making displayBitmap() and
 * getUrlString() available to subclasses.  This design separates
 * concerns by having DownloadBase handle UI functionality while
 * subclasses (such as DownloadActivity) handle any Service-related
 * communication with the GeoNames Web service.
 * 
 * GeoNamesBase is an example of the Template Method pattern since it
 * extends Activity and overrides its onCreate() hook method.  More
 * generally, any object that extends Activity and overrides its hook
 * methods, such as onStart() or onPause(), is also an example of the
 * Template Method pattern.
 */
public class DownloadBase extends Activity {
    /**
     * Used for debugging.
     */
    private final String TAG = this.getClass().getSimpleName(); 

    /**
     * This is the reference to the text box that allows the user to
     * input a URL to an image for downloading.
     */
    private EditText mUrlEditText;

    /**
     * This is a reference to the container of an image in the UI.
     * When we finish downloading, we update this to display the image
     * we just stored on the file system.
     */
    private ImageView mImageView;

    /**
     * The original bitmap (used for reseting the image).
     */
    private Bitmap mDefaultBitmap;
    
    /**
     * Store the current bitmap for testing purposes.
     */
    public Bitmap mCurrentBitmap;
    
    /**
     * Hide the keyboard after a user has finished typing the url.
     */
    protected void hideKeyboard() {
        InputMethodManager mgr =
            (InputMethodManager) getSystemService
            (Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(mUrlEditText.getWindowToken(),
                                    0);
    }

    /**
     * Display the given file in the ImageView. Use
     * BitmapFactory.decodeFile().  Store the bitmap used to update
     * the file to make testing easier.
     */
    void displayBitmap (String pathname) {
    	mCurrentBitmap = BitmapFactory.decodeFile(pathname);
    	
    	mImageView.setImageBitmap(mCurrentBitmap);
    }
    
    /**
     * Gets the URL from the EditText
     */
    String getUrlString () {
    	return mUrlEditText.getText().toString();
    }
    
    /**
     * Resets image to the default image stored with the program.
     */
    public void resetImage(View view) {
    	mImageView.setImageBitmap(mDefaultBitmap);
    	mCurrentBitmap = mDefaultBitmap;
        Log.d(TAG, "reset Image");
    }
    
    /**
     * This is called when the Activity is initially created. This is
     * where we setup the UI for the activity and initialize any
     * objects that need to exist while the activity exists.
     */
    @Override
        public void onCreate(Bundle savedInstanceState) {
        Log.d(getClass().getSimpleName(), "onCreate");
        super.onCreate(savedInstanceState);
        
        // Use the Android framework to create a User Interface for
        // this activity.  The interface that should be created is
        // defined in activity_download.xml in the res/layout folder.
        setContentView(R.layout.activity_download);
        
        // Once the UI is created, get a reference to the instantiated
        // EditText and ImageView objects by providing their ids to
        // the Android framework.
        mUrlEditText = (EditText) findViewById(R.id.url);
        mImageView = (ImageView) findViewById(R.id.imageView1);
        
        // Store whatever image is originally displayed in the
        // ImageView as a local Bitmap object so that we can quickly
        // reset the image when a button is pressed.
        mCurrentBitmap = ((BitmapDrawable)(mImageView.getDrawable())).getBitmap();
        mDefaultBitmap = mCurrentBitmap;
        
        /**
         * Turn off strict mode. 
         * 
         * Normally, if you try to do any networking from the main UI
         * thread, the Android framework will throw an exception and
         * stop working. However, part of this application uses a
         * synchronous AIDL interface to demonstrate how to execute
         * functions in services synchronously. For this purpose, we
         * turn off strict mode so that the Android framework will
         * work without complaining.
         * 
         *  Please note that this is for demonstration purposes ONLY,
         *  and you should NEVER, EVER turn off strict mode in
         *  production code. You should also not do networking
         *  operations on the main thread, because it might cause your
         *  application to crash.
         */
        StrictMode.ThreadPolicy policy =
            new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
