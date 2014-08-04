package edu.vuum.mocca;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * @class EarthQuakeService
 *
 * @brief This class handles downloads using synchronous AIDL
 *        interactions.  The component that binds to this Service
 *        (which in this example application is EarthQuakeActivity)
 *        should receive an IBinder proxy that references an instance
 *        of EarthQuakeCall, which extends IBinder.  The component can
 *        then interact with this service by making twoway method
 *        calls on the EarthQuakeCall proxy. Specifically, the component
 *        can ask this Service to call the EarthQuake Web service, which
 *        will run synchronously until it finishes getting the results
 *        from the the web service and returning them as a String.
 *  
 *        AIDL and the Binder framework are an example of the Broker
 *        Pattern, in which all interprocess communication details are
 *        hidden behind the AIDL interfaces.
 */
public class EarthQuakeService extends Service {
    /**
     * Used for debugging.
     */
    private final String TAG = this.getClass().getSimpleName(); 

    /**
     * Object that can invoke HTTP GET requests on URLs.
     */
    private final static AndroidHttpClient mClient =
        AndroidHttpClient.newInstance("");

    /**
     * The concrete implementation of the AIDL Interface EarthQuakeCall.
     * We extend the Stub class, which implements the EarthQuakeCall
     * interface, so that Android can properly handle calls across
     * process boundaries.
     * 
     * This implementation plays the role of Invoker in the Broker
     * Pattern.
     */
    EarthQuake.Stub mEarthQuakeImpl = new EarthQuake.Stub() {
            /**
             * Invoke a call to the EarthQuake webservice at the
             * provided Internet uri.  The Service uses the
             * EarthQuakeback parameter to return a string
             * containing the results back to the Activity.
             * 
             * Use the method defined in EarthQuakeUtils for code
             * brevity.

             * EarthQuake the image at the given Uri and return a
             * pathname to the file on the Android file system.
             */
            @Override
            public List<EarthQuakeData> getEarthQuakeData(Uri uri) 
                throws RemoteException {
            	Log.d(TAG, "Calling getWebServiceResults()");
                // Return the results to the client, which blocks
                // synchronously.
                return EarthQuakeUtils.getEarthQuakeData(mClient,
                                                         uri);
            }
	};

    /**
     * Make an Intent that will start this service when passed to
     * bindService().
     *
     * @param context		The context of the calling component.
     */
    public static Intent makeIntent(Context context) {
        return new Intent(context,
                          EarthQuakeService.class);		
    }

    /**
     * Called when a component calls bindService() with the proper
     * intent.  Return the concrete implementation of EarthQuakeCall
     * cast as an IBinder.
     */
    @Override
	public IBinder onBind(Intent intent) {
        Log.d(TAG, "calling onBind()");
        return mEarthQuakeImpl;
    }
	
    public void onDestroy() {
        mClient.close();
        super.onDestroy();
    }
}
