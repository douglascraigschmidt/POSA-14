package edu.vuum.mocca;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * @class GeoNamesServiceSync
 *
 * @brief This class handles downloads using synchronous AIDL
 *        interactions.  The component that binds to this Service
 *        (which in this example application is GeoNamesActivity)
 *        should receive an IBinder proxy that references an instance
 *        of GeoNamesCall, which extends IBinder.  The component can
 *        then interact with this service by making twoway method
 *        calls on the GeoNamesCall proxy. Specifically, the component
 *        can ask this Service to call the GeoNames Web service, which
 *        will run synchronously until it finishes getting the results
 *        from the the web service and returning them as a String.
 *  
 *        AIDL and the Binder framework are an example of the Broker
 *        Pattern, in which all interprocess communication details are
 *        hidden behind the AIDL interfaces.
 */
public class GeoNamesServiceSync extends Service {
    /**
     * Used for debugging.
     */
    private final String TAG = this.getClass().getSimpleName(); 

    /**
     * The concrete implementation of the AIDL Interface GeoNamesCall.
     * We extend the Stub class, which implements the GeoNamesCall
     * interface, so that Android can properly handle calls across
     * process boundaries.
     * 
     * This implementation plays the role of Invoker in the Broker
     * Pattern.
     */
    GeoNamesCall.Stub mGeoNamesCallImpl = new GeoNamesCall.Stub() {
            /**
             * Invoke a call to the GeoNames webservice at the
             * provided Internet uri.  The Service uses the
             * GeoNamesCallback parameter to return a string
             * containing the results back to the Activity.
             * 
             * Use the method defined in GeoNamesUtils for code
             * brevity.

             * GeoNames the image at the given Uri and return a
             * pathname to the file on the Android file system.
             */
            @Override
            public String getWebServiceResults(Uri uri) 
                throws RemoteException {
            	Log.d(TAG, "Calling getWebServiceResults()");
                // Return the results to the client, which blocks
                // synchronously.
                return GeoNamesUtils.getWebServiceResults(uri);
            }
	};
	
    /**
     * Called when a component calls bindService() with the proper
     * intent.  Return the concrete implementation of GeoNamesCall
     * cast as an IBinder.
     */
    @Override
	public IBinder onBind(Intent intent) {
        Log.d(TAG, "calling onBind()");
        return mGeoNamesCallImpl;
    }
	
    /**
     * Make an Intent that will start this service when passed to
     * bindService().
     *
     * @param context		The context of the calling component.
     */
    public static Intent makeIntent(Context context) {
        return new Intent(context,
                          GeoNamesServiceSync.class);		
    }
}
