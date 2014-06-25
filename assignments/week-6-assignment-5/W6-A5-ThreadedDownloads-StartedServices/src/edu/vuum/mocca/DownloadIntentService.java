package edu.vuum.mocca;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Messenger;

/**
 * @class DownloadIntentService
 *
 * @brief This class extends the IntentService, which provides a
 *        framework that simplifies programming and processing Android
 *        Started Services concurrently.
 * 
 *        DownloadIntentService receives an Intent containing a URL
 *        (which is a type of URI) and a Messenger (which is an IPC
 *        mechanism). It downloads the file at the URL, stores it on
 *        the file system, then returns the path name to the caller
 *        using the supplied Messenger.
 * 
 *        The IntentService class implements the CommandProcessor
 *        pattern and the Template Method Pattern.  The Messenger is
 *        used as part of the Active Object pattern.
 */
public class DownloadIntentService extends IntentService {
    /**
     * The default constructor for this service. Simply forwards
     * construction to IntentService, passing in a name for the Thread
     * that the service runs in.
     */
    public DownloadIntentService() { 
        super("IntentService Worker Thread"); 
    }

    /**
     * Optionally allow the instantiator to specify the name of the
     * thread this service runs in.
     */
    public DownloadIntentService(String name) {
        super(name);
    }

    /**
     * Make an intent that will start this service if supplied to
     * startService() as a parameter.
     * 
     * @param context		The context of the calling component.
     * @param handler		The handler that the service should
     *                          use to respond with a result  
     * @param uri               The web URL of a file to download
     * 
     * This method utilizes the Factory Method makeMessengerIntent()
     * from the DownloadUtils class.  The returned intent is a Command
     * in the Command Processor Pattern. The intent contains a
     * messenger, which plays the role of Proxy in the Active Object
     * Pattern.
     */
    public static Intent makeIntent(Context context,
                                    Handler handler,
                                    String uri) {
    	// TODO - You fill in here to replace null with a call to the
    	// factory method in DownloadUtils that makes a Messenger
    	// Intent with the appropriate parameters.

        return null;
    }

    /**
     * Hook method called when a component calls startService() with
     * the proper intent.  This method serves as the Executor in the
     * Command Processor Pattern. It receives an Intent, which serves
     * as the Command, and executes some action based on that intent
     * in the context of this service.
     * 
     * This method is also a Hook Method in the Template Method
     * Pattern. The Template class has an overall design goal and
     * strategy, but it allows subclasses to how some steps in the
     * strategy are implemented. For example, IntentService handles
     * the creation and lifecycle of a started service, but allows a
     * user to define what happens when an Intent is actually handled.
     */
    @Override
    protected void onHandleIntent (Intent intent) {
        // TODO - You fill in here with a call the appropriate helper
        // method from the DownloadUtils class that downloads the uri
        // in the intent and returns the file's pathname using a
        // Messenger who's Bundle key is defined by DownloadUtils.MESSENGER_KEY
    }
}
