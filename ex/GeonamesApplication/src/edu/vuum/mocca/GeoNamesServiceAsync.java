package edu.vuum.mocca;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * @class GeoNamesServiceAsync
 *
 * @brief This Service performs calls to the GeoNames Web service
 *        using asynchronous AIDL interactions.  The component (which
 *        in this example application is GeoNamesActivity) that binds
 *        to this service should receive an IBinder Proxy that
 *        references an instance of GeoNamesRequest, which extends
 *        IBinder.  The component can then interact with this Service
 *        by invoking oneway method calls via the GeoNamesRequest
 *        proxy. Specifically, the component can ask this Service to
 *        call the GeoNames Web service, passing in a GeoNamesCallback
 *        object.  Once the Web service call is finished, this Service
 *        will send the results back to the calling component by
 *        invoking sendResults() on the GeoNamesCallback object.
 *  
 *        AIDL and the Binder framework are an example of the Broker
 *        Pattern, in which all interprocess communication details are
 *        hidden behind the AIDL interfaces.
 */
public class GeoNamesServiceAsync extends Service{
    /**
     * Used for debugging.
     */
    private final String TAG = this.getClass().getSimpleName(); 

    /**
     * The concrete implementation of the AIDL Interface
     * GeoNamesRequest.  We extend the Stub class, which implements
     * the GeoNamesRequest interface, so that Android can properly
     * handle calls across process boundaries.
     * 
     * This implementation plays the role of Invoker in the Broker
     * Pattern.
     */
    GeoNamesRequest.Stub mGeoNamesRequestImpl = new GeoNamesRequest.Stub() {
            /**
             * Invoke a call to the GeoNames webservice at the
             * provided Internet uri.  The Service uses the
             * GeoNamesCallback parameter to return a string
             * containing the results back to the Activity.
             * 
             * Use the method defined in GeoNamesUtils for code
             * brevity.
             */
            @Override
            public void callWebService(Uri uri,
                                       GeoNamesCallback callback)
                throws RemoteException {
            	Log.d(TAG, "Calling getWebServiceResults()");
                // Invoke a oneway method to return the results to the
                // client, which doesn't block synchronously.
                callback.sendResults(GeoNamesUtils.getWebServiceResults(uri));
            }
		
	};
	
    /**
     * Called when a component calls bindService() with the proper
     * intent.  Return the concrete implementation of GeoNamesRequest
     * cast as an IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "calling onBind()");
        return mGeoNamesRequestImpl;
    }

    /**
     * Make an Intent that will start this service when passed to
     * bindService().
     *
     * @param context		The context of the calling component.
     */
    public static Intent makeIntent(Context context) {
        return new Intent(context,
                          GeoNamesServiceAsync.class);		
    }
}
