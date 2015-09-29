package edu.vuum.mocca;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This class is used the base class for the GeoNamesActivity.  It
 * instantiates the UI and handles displaying images and getting text
 * from the EditText object by making displayBitmap() and
 * getUrlString() available to subclasses.  This design separates
 * concerns by having GeoNamesBase handle UI functionality while
 * subclasses (such as GeoNamesActivity) handle any Service-related
 * communication with the GeoNames Web service.
 * 
 * GeoNamesBase is an example of the Template Method pattern since it
 * extends Activity and overrides its onCreate() hook method.  More
 * generally, any object that extends Activity and overrides its hook
 * methods, such as onStart() or onPause(), is also an example of the
 * Template Method pattern.
 */
public class GeoNamesBase extends Activity {
    /**
     * Used for debugging.
     */
    private final String TAG = this.getClass().getSimpleName(); 

    /**
     * This is the reference to the text box that allows the user to
     * input a URL to a Web service request.
     */
    private EditText mUrlEditText;

    /**
     * This is a reference to the container of text in the UI.  When
     * we finish getting the results of the Web service call, we
     * update this to display the results.
     */
    private TextView mTextView = null;

    /**
     * Display the given result string in the TextView.
     */
    protected void displayResults(String result) {
    	mTextView.setText(result);
    }
    
    /**
     * Gets the URL from the EditText.
     */
    protected String getUrlString () {
        return mUrlEditText.getText().toString();
    }

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
     * Resets the textView to be "".
     */
    public void resetOutput(View view) {
    	mTextView.setText("");
        Log.d(TAG, "reset Image");
    }
    
    /**
     * This is called when the Activity is initially created. This is
     * where we setup the UI for the activity and initialize any
     * objects that need to exist while the activity exists.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        
        // Use the Android framework to create a User Interface for
        // this activity.  The interface that should be created is
        // defined in GeoNamesActivity.xml in the res/layout folder.
        setContentView(R.layout.geo_names_activity);
        
        // Once the UI is created, get a reference to the instantiated
        // EditText and ImageView objects by providing their ids to
        // the Android framework.
        mUrlEditText = (EditText) findViewById(R.id.url);
        mTextView = (TextView) findViewById(R.id.textView1);
        
        // Turn off strict mode. 
        //
        // Normally, if you try to do any networking from the main UI
        // thread, the Android framework will throw an exception and
        // stop working. However, part of this application uses a
        // synchronous AIDL interface to demonstrate how to execute
        // functions in services synchronously. For this purpose, we
        // turn off strict mode so that the Android framework will
        // work without complaining.
        // 
        // Please note that this is for demonstration purposes ONLY,
        // and you should NEVER, EVER turn off strict mode in
        // production code. You should also not do networking
        // operations on the main thread, because it might cause your
        // application to crash.
        StrictMode.ThreadPolicy policy =
            new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
