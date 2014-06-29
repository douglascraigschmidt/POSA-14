
package edu.vuum.mocca;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * @class DownloadBoundServiceSync
 *
 * @brief This class handles downloads using synchronous AIDL
 *        interactions.
 * 
 *        The component that binds to this service should receive an
 *        IBinder. This IBinder should be an instance of DownloadCall,
 *        which extends IBinder. The component can then interact with
 *        this service by making normal calls on the DownloadCall
 *        object. Specifically, the component can ask this service to
 *        download an image by calling downloadImage() on the
 *        DownloadCall object, which will run synchronously in this
 *        service until it finishes downloading and returns the file
 *        name of the downloaded file as a String.
 *  
 *        AIDL is an example of the Broker Pattern, in which all
 *        interprocess communication details are hidden behind the
 *        AIDL interfaces.
 */
public class DownloadBoundServiceSync extends Service {
    /**
     * An implementation of the AIDL Interface DownloadCall.  We
     * extend the Stub class, which implements DownloadCall, so that
     * Android can properly handle calls across process boundaries.
     * 
     * This implementation plays the role of Invoker in the Broker
     * Pattern
     */
    DownloadCall.Stub mDownloadCallImpl = new DownloadCall.Stub() {
            /**
             * Download the image at the given Uri and return a
             * pathname to the file on the Android file system.
             * 
             * Use the methods defined in DownloadUtils for code
             * brevity.
             */
            @Override
            public String downloadImage(Uri uri) throws RemoteException {
                // TODO You fill in here to replace the null and
                // download the file using the appropriate helper
                // method in DownloadUtils and then return the
                // pathname back to the client.
                return null;
            }
	};
	
    /**
     * Called when a component calls bindService() with the proper
     * intent.  Return the concrete implementation of DownloadCall
     * cast as an IBinder.
     */
    @Override
	public IBinder onBind(Intent intent) {
        return mDownloadCallImpl;
    }
	
    /**
     * Make an Intent that will start this service when passed to
     * bindService().
     *
     * @param context		The context of the calling component.
     */
    public static Intent makeIntent(Context context) {
        // TODO - replace the null to create the appropriate Intent
        // and return it to the caller.
        return null;
    }
}
