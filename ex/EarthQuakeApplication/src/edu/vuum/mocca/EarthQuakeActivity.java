package edu.vuum.mocca;

import java.util.List;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

/**
 * This is the main activity that the program uses to start the
 * EarthQuakeApplication, which provides access to the EarthQuake
 * geographical database via its RESTful Web services API, as
 * described at http://en.wikipedia.org/wiki/EarthQuake.  It allows the
 * user to input a URL containing a request for one of its various
 * services, such as direct and reverse geocoding, finding places
 * through postal codes, finding places next to a given place, and
 * finding Wikipedia articles about neighbouring places.
 *
 * The EarthQuakeActivity uses Bound Services to perform the actual
 * interactions with the EarthQuake Web services in background Threads
 * running in a separate process.  The Activity starts the desired
 * Service using bindService().  After the Service is started, its
 * onBind() hook method returns an implementation of an AIDL interface
 * to the Activity by asynchronously calling the onServiceConnected()
 * hook method in the Activity.  The AIDL interface object that's
 * returned can then be used to interact with the Service either
 * synchronously.
 * 
 * Starting Bound Services to run synchronously in background Threads
 * from the asynchronous UI Thread is an example of the
 * Half-Sync/Half-Async Pattern.  Starting Bound Services using
 * Intents is an example of the Activator and Command Processor
 * patterns.  The EarthQuakeActivity plays the role of the Creator and
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
public class EarthQuakeActivity extends EarthQuakeBaseActivity {
    /**
     * Used for debugging.
     */
    private final String TAG = this.getClass().getSimpleName(); 
    
    /**
     * URL for the EarthQuake Web service that returns earthquake results.
     */
    private final static String UNAME = "aporter";
    private final static String URL =
        "http://api.geonames.org/earthquakesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&username="
        + UNAME;

    /**
     * The AIDL Interface that's used to make twoway calls to the
     * EarthQuakeService Service.  This object plays the role of
     * Requestor in the Broker Pattern.  If it's null then there's no
     * connection to the Service.
     */
    EarthQuake mEarthQuake = null;
     
    /** 
     * This ServiceConnection is used to receive the EarthQuake
     * proxy after binding to the EarthQuakeService Service using
     * bindService().
     */
    ServiceConnection mServiceConnection = new ServiceConnection() {
            /**
             * Cast the returned IBinder object to the EarthQuake
             * AIDL Interface and store it for later use in
             * mEarthQuake.
             */
            @Override
            public void onServiceConnected(ComponentName name,
                                           IBinder service) {
            	Log.d(TAG, "ComponentName: " + name);
                mEarthQuake = EarthQuake.Stub.asInterface(service);
            }

            /**
             * Called if the remote service crashes and is no longer
             * available.  The ServiceConnection will remain bound,
             * but the service will not respond to any requests.
             */
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mEarthQuake = null;
            }
    	 
        };
     
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
        // defined in EarthQuakeActivity.xml in the res/layout folder.
        setContentView(R.layout.earthquake_activity);

        // Run the 
        mapEarthQuakes(null);
    }        

    /**
     * This method is called to map the earthquakes.
     */
    public void mapEarthQuakes(View view) {
        Uri uri = Uri.parse(URL);

        if (mEarthQuake != null) {
            // Use an AsyncTask to download the Acronym data in a
            // separate thread and then display it in the UI thread.
            new AsyncTask<Uri, Void, List<EarthQuakeData>> () {

                // Download the expanded acronym via a synchronous
                // two-way method call, which runs in a background
                // thread to avoid blocking the UI thread.
                protected List<EarthQuakeData> doInBackground(Uri... uris) {
                    try {
                        return mEarthQuake.getEarthQuakeData(uris[0]);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                // Display the results in the UI Thread.
                protected void onPostExecute(List<EarthQuakeData> earthQuakeDataList) {
                    displayResults(earthQuakeDataList);
                }
            }.execute(uri);
        }
    }

    /**
     * Hook method called when the EarthQuakeActivity becomes visible to
     * bind the Activity to the EarthQuakeService.
     */
    @Override
    public void onStart () {
    	super.onStart();
    	
        // Launch the designated Bound Service if they aren't already
    	// running via a call to bindService() Bind this activity to
    	// the EarthQuakeService* Services if they aren't already bound.

    	if (mEarthQuake == null) 
            bindService(EarthQuakeService.makeIntent(this), 
                        mServiceConnection,
                        BIND_AUTO_CREATE);
    }
    
    /**
     * Hook method called when the EarthQuakeActivity becomes completely
     * hidden to unbind the Activity from the Service.
     */
    @Override
    public void onStop () {
    	super.onStop();
    	
    	// Unbind the EarthQuakeService if it is connected.
    	if (mEarthQuake != null) 
            unbindService(mServiceConnection);
    }

}
