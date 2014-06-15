package edu.vuum.mocca;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * This class is used as a base for the two ThreadedDownloads
 * Assignment Activities.  It instantiates the UI and handles
 * displaying images and getting text from the EditText object by
 * making displayBitmap() and getUrlString() available to subclasses.
 * 
 * This design creates a separation of concerns, where this class
 * handles UI functionality, while subclasses handle any
 * service-related functionality that downloads images.
 * 
 * DownloadBase, which extends Activity and overrides its primitive
 * operation onCreate(), is an example of the Template Method
 * Pattern. More generically, any object that extends Activity and
 * overrides its primitive methods such as onStart() or onPause() is
 * also an example of the Template Method Pattern.
 */
public class DownloadBase extends Activity {
    /**
     * Used for debugging.
     */
    private final String TAG = this.getClass().getSimpleName(); 

    /**
     * This object is the reference to the text box that allows the user to
     * input a URL to an image for downloading.
     */
    private EditText mEditText;

    /**
     * This object is a reference to the container of an image in the
     * UI.  When we finish downloading, we update this to display the
     * image we just stored on the file system.
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
     * Display the given file in the ImageView.  Use
     * BitmapFactory.decodeFile().  Store the bitmap used to update
     * the file to make testing easier.
     */
    void displayBitmap (String pathname) {
    	mCurrentBitmap = BitmapFactory.decodeFile(pathname);
    	
    	mImageView.setImageBitmap(mCurrentBitmap);
    }
    
    /**
     * Gets the URL from the EditText.
     */
    String getUrlString () {
    	return mEditText.getText().toString();
    }
    
    /**
     * Resets image to the default image stored with the program and
     * reset the image default URL.
     */
    public void resetImage(View view) {
    	mImageView.setImageBitmap(mDefaultBitmap);
    	mCurrentBitmap = mDefaultBitmap;
        mEditText.setText(getResources().getString(R.string.default_url));
        Log.d(TAG, "reset Image");
    }
    
    /**
     * This hook method is called when the Activity is initially
     * created.  It's where we setup the UI for the activity and
     * initialize any objects that need to exist while the activity
     * exists.
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
        mEditText = (EditText) findViewById(R.id.url);
        mImageView = (ImageView) findViewById(R.id.imageView1);
        
        // Store whatever image is originally displayed in the
        // ImageView as a local Bitmap object so that we can quickly
        // reset the image when a button is pressed.
        mCurrentBitmap =
            ((BitmapDrawable)(mImageView.getDrawable())).getBitmap();
        mDefaultBitmap = mCurrentBitmap;
    }
}
