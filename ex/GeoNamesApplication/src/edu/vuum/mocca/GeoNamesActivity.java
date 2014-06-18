package edu.vuum.mocca;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * This is the main activity that the program uses to start the
 * GeoNamesApplication, which provides access to the GeoNames
 * geographical database via its RESTful Web services API, as
 * described at http://en.wikipedia.org/wiki/GeoNames.  It allows the
 * user to input a URL containing a request for one of its various
 * services, such as direct and reverse geocoding, finding places
 * through postal codes, finding places next to a given place, and
 * finding Wikipedia articles about neighbouring places.
 *
 * The GeoNamesActivity uses Bound Services to perform the actual
 * interactions with the GeoNames Web services in background Threads
 * running in a separate process.  There are two types of Bound
 * Services shown in this example: synchronous and asynchronous.  The
 * Activity starts the desired Service using bindService().  After the
 * Service is started, its onBind() hook method returns an
 * implementation of an AIDL interface to the Activity by
 * asynchronously calling the onServiceConnected() hook method in the
 * Activity.  The AIDL interface object that's returned can then be
 * used to interact with the Service either synchronously or
 * asynchronously, depending on the type of AIDL interface requested.
 * 
 * Starting Bound Services to run synchronously in background Threads
 * from the asynchronous UI Thread is an example of the
 * Half-Sync/Half-Async Pattern.  Starting Bound Services using
 * Intents is an example of the Activator and Command Processor
 * patterns.  The GeoNamesActivity plays the role of the Creator and
 * creates a Command in the form of an Intent.  The Intent is received
 * by the service process, which plays the role of the Executor.
 * 
 * The use of AIDL interfaces to pass information between two
 * different processes is an example of the Broker Pattern, in which
 * all communication-related functionality is encapsulated in the AIDL
 * interface and the underlying Android Binder framework, shielding
 * applications from tedious and error-prone aspects of inter-process
 * communication.
 */
public class GeoNamesActivity extends GeoNamesBase {
    /**
     * Used for debugging.
     */
    private final String TAG = this.getClass().getSimpleName(); 
    
    /**
     * The AIDL Interface that's used to make twoway calls to the
     * GeoNamesServiceSync Service.  This object plays the role of
     * Requestor in the Broker Pattern.  If it's null then there's no
     * connection to the Service.
     */
    GeoNamesCall mGeoNamesCall = null;
     
    /**
     * The AIDL Interface that we will use to make oneway calls to the
     * GeoNamesServiceAsync Service.  This plays the role of Requestor
     * in the Broker Pattern.  If it's null then there's no connection
     * to the Service.
     */
    GeoNamesRequest mGeoNamesRequest = null;
     
    /** 
     * This ServiceConnection is used to receive the GeoNamesCall
     * proxy after binding to the GeoNamesServiceSync Service using
     * bindService().
     */
    ServiceConnection mServiceConnectionSync = new ServiceConnection() {
            /**
             * Cast the returned IBinder object to the GeoNamesCall
             * AIDL Interface and store it for later use in
             * mGeoNamesCall.
             */
            @Override
            public void onServiceConnected(ComponentName name,
                                           IBinder service) {
            	Log.d(TAG, "ComponentName: " + name);
                mGeoNamesCall = GeoNamesCall.Stub.asInterface(service);
            }

            /**
             * Called if the remote service crashes and is no longer
             * available.  The ServiceConnection will remain bound,
             * but the service will not respond to any requests.
             */
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mGeoNamesCall = null;
            }
    	 
        };
     
    /** 
     * This ServiceConnection is used to receive the GeoNamesRequest
     * proxy after binding to the GeoNamesServiceAsync Service using
     * bindService().
     */
    ServiceConnection mServiceConnectionAsync = new ServiceConnection() {
            /**
             * Called after the KeyGeneratorService is connected to
             * convey the result returned from onBind().
             */
            @Override
            public void onServiceConnected(ComponentName name,
                                           IBinder service) {
            	Log.d(TAG, "ComponentName: " + name);
                // Cast the returned IBinder object to the
                // GeoNamesRequest AIDL Interface and store it for
                // later use in mGeoNamesRequest.
                mGeoNamesRequest = GeoNamesRequest.Stub.asInterface(service);
            }

            /**
             * Called if the Service crashes and is no longer
             * available.  The ServiceConnection will remain bound,
             * but the service will not respond to any requests.
             */
            @Override
		public void onServiceDisconnected(ComponentName name) {
                mGeoNamesRequest = null;
            }
    	 
        };
     
    /**
     * The implementation of the GeoNamesCallback AIDL
     * Interface. Should be passed to the GeoNamesServiceAsync
     * Service using the GeoNamesRequest.downloadImage() method.
     * 
     * This implementation of GeoNamesCallback.Stub plays the role of
     * Invoker in the Broker Pattern.
     */
    GeoNamesCallback.Stub mGeoNamesCallback = new GeoNamesCallback.Stub() {
            /**
             * Called when the GeoNamesServiceAsync finishes obtaining
             * the results from the GeoNames Web service.  Use the
             * provided String to display the results in a TextView.
             */
            @Override
            public void sendResults(final String results) throws RemoteException {
                Log.d(TAG, "Receiving oneway sendResults() callback");

                // Since this method is not run in the Main UI Thread,
                // but in a thread allocated from the Binder thread
                // pool, we need to call displayResults() to post a
                // Runnable on the UI Thread via runOnUiThread().
                runOnUiThread(new Runnable () {
                        public void run () {
                            displayResults(results);
                        }
                    });
            }
        };
     
    /**
     * This method is called when a user presses a button (see
     * res/layout/GeoNamesActivity.xml)
     */
    public void runService(View view) {
        Uri uri = Uri.parse(getUrlString());

        hideKeyboard();

    	switch (view.getId()) {
        case R.id.bound_sync_button:
            if (mGeoNamesCall != null)
                try {
                    Log.d(TAG,
                          "Calling twoway GeoNamesServiceSync.getWebServiceResults()");

                    String results =
                        // Invoke the twoway call, which blocks until
                        // it gets a reply.
                        mGeoNamesCall.getWebServiceResults(uri);
                    displayResults(results);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                
            break;

        case R.id.bound_async_button:
            if (mGeoNamesRequest != null) 
                try {
                    Log.d(TAG, "Calling oneway GeoNamesServiceAsync.callWebService()");

                    // Invoke the oneway call, which doesn't block.
                    mGeoNamesRequest.callWebService(uri,
                                                    mGeoNamesCallback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            break;
        }
    }

    /**
     * Hook method called when the GeoNamesActivity becomes visible to
     * bind the Activity to the Services.
     */
    @Override
    public void onStart () {
    	super.onStart();
    	
        // Launch the designated Bound Service if they aren't already
    	// running via a call to bindService() Bind this activity to
    	// the GeoNamesService* Services if they aren't already bound.

    	if (mGeoNamesCall == null) 
            bindService(GeoNamesServiceSync.makeIntent(this), 
                        mServiceConnectionSync,
                        BIND_AUTO_CREATE);

    	if (mGeoNamesRequest == null)
            bindService(GeoNamesServiceAsync.makeIntent(this), 
                        mServiceConnectionAsync, 
                        BIND_AUTO_CREATE);
    }
    
    /**
     * Hook method called when the GeoNamesActivity becomes completely
     * hidden to unbind the Activity from the Services.
     */
    @Override
    public void onStop () {
    	super.onStop();
    	
    	// Unbind the Sync/Async Services if they are connected.
    	if (mGeoNamesCall != null) 
            unbindService(mServiceConnectionSync);

    	if (mGeoNamesRequest != null) 
            unbindService(mServiceConnectionAsync);
    }
}
