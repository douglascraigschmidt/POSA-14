package edu.vuum.mocca;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

/**
 * This is the main activity that the program uses to start the
 * ThreadedDownloads application.  It allows the user to input the URL
 * of an image and download that image using one of two different
 * Android Service implementations.  When the service is done
 * downloading the image, it stores it on the Android file system,
 * then notifies this activity using the Messenger IPC mechanism
 * discussed in the class.
 * 
 * Starting services to run synchronously from the asynchronous UI
 * Thread is an example of the Half-Sync/Half-Async Pattern.  Starting
 * services using Intents is an example of the Command Processor
 * Pattern. This activity, the Creator, creates a Command in the form
 * of an Intent. The Intent is received by the service process, which
 * plays the role of the Executor.
 * 
 * Returning a result using Messages and Handlers is an example of the
 * Active Object Pattern. The Service must invoke a method to update
 * the UI. However, the service thread is not allowed to interact with
 * the UI. To decouple the invocation of this method from execution,
 * the Service encapsulates the request in a Message, which plays the
 * role of Active Object. The message is then passed to the UI
 * thread's handler, which eventually executes the request on the UI
 * Thread.
 * 
 * Note: all UI functionality has been factored out into
 * DownloadBase. If you wish to display an image, use
 * displayBitmap(). If you want to get the URL from the EditText
 * object, use getUrlString().
 */
public class DownloadActivity extends DownloadBase {
    /**
     * This is the handler used for handling messages sent by a
     * Messenger.  It receives a message containing a pathname to an
     * image and displays that image in the ImageView.
     *
     * The handler plays several roles in the Active Object pattern,
     * including Proxy, Future, and Servant.
     * 
     * Please use displayBitmap() defined in DownloadBase
     */
    static class MessengerHandler extends Handler {
	    
    	// A weak reference to the enclosing class
    	WeakReference<DownloadActivity> outerClass;
    	
    	/**
    	 * A constructor that gets a weak reference to the enclosing class.
    	 * We do this to avoid memory leaks during Java Garbage Collection.
    	 * 
    	 * @see https://groups.google.com/forum/#!msg/android-developers/1aPZXZG6kWk/lIYDavGYn5UJ
    	 */
    	public MessengerHandler(DownloadActivity outer) {
            outerClass = new WeakReference<DownloadActivity>(outer);
    	}
    	
    	// Handle any messages that get sent to this Handler
    	public void handleMessage(Message msg) {
    		
            // Get an actual reference to the DownloadActivity
            // from the WeakReference.
            final DownloadActivity activity = outerClass.get();
    		
            // If DownloadActivity hasn't been garbage collected
            // (closed by user), display the sent image.
            if (activity != null) {
                // TODO - You fill in here to display the image
                // bitmap that's been downloaded and returned to
                // the DownloadActivity as a pathname that's named
                // "PATHNAME".
            }
    	}
    }

    /**
     * Instantiate the MessengerHandler, passing in the
     * DownloadActivity to help with garbage collection.
     */
    MessengerHandler handler = new MessengerHandler(this);
    
    /**
     * This method is called when a user presses a button (see
     * res/layout/activity_download.xml)
     * 
     * Start a service using startService() or make a call to a Bound
     * Service using an AIDL interface.  The action performed depends
     * on the button pressed.
     * 
     * To get the URL from the EditText, please use getUrlString()
     * defined in DownloadBase.
     */
    public void runService(View view) {
    	String which = "";

    	switch (view.getId()) {
        case R.id.intent_service_button:
            // TODO - You fill in here to start the
            // DownloadIntentService with the appropriate Intent
            // returned from the makeIntent() factory method.

            which = "Starting IntentService";
            break;
        
        case R.id.thread_pool_button:
            // TODO - You fill in here to start the
            // ThreadPoolDownloadService with the appropriate Intent
            // returned from the makeIntent() factory method.

            which = "Starting ThreadPoolDownloadService";
            break;
        
        }

    	// Display a short pop-up notification telling the user which
    	// service was started.
    	Toast.makeText(this,
                       which,
                       Toast.LENGTH_SHORT).show();
    }

    /** Called when this activity becomes visible after onStart().
     * 	Also called when the activity is un-paused.
     */
    @Override
        public void onResume() {
        // Attach handler to looper.
    	super.onResume();
    }

    /**
     * Called when this activity becomes partially hidden.
     */
    @Override
        public void onPause() {
        // Remove handler from looper.
    	super.onPause();    	
    }
}
