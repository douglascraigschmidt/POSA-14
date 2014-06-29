package edu.vuum.mocca;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * @class DownloadBoundServiceAsync
 
 * @brief This class handles downloads using asynchronous AIDL
 *        interactions.  The component that binds to this service
 *        should receive an IBinder. This IBinder should be an
 *        instance of DownloadRequest, which extends IBinder. The
 *        component can then interact with this service by making
 *        normal calls on the DownloadRequest object. Specifically,
 *        the component can ask this service to download an image,
 *        passing in a DownloadCallback object.  Once the download is
 *        finished, this service should send the pathname of the
 *        downloaded file back to the calling component by calling
 *        sendPath() on the DownloadCallback object.
 *  
 *        AIDL is an example of the Broker Pattern, in which all
 *        interprocess communication details are hidden behind the
 *        AIDL interfaces.
 */
public class DownloadBoundServiceAsync extends Service{
    /**
     * The concrete implementation of the AIDL Interface
     * DownloadRequest.  We extend the Stub class, which implements
     * DownloadRequest, so that Android can properly handle calls
     * across process boundaries.
     * 
     * This implementation plays the role of Invoker in the Broker
     * Pattern.
     */
    DownloadRequest.Stub mDownloadRequestImpl = new DownloadRequest.Stub() {
            /**
             * Download the image at the given Uri and return a
             * pathname to the file on the Android file system by
             * calling the sendPath() method on the provided callback
             * 
             * Use the methods defined in DownloadUtils for code brevity.
             */
            @Override
            public void downloadImage(Uri uri,
                                      DownloadCallback callback)
                throws RemoteException {
                // TODO You fill in here to download the file using
                // the appropriate helper method in DownloadUtils and
                // then send the pathname back to the client via the
                // callback object.
            }
		
	};
	
    /**
     * Called when a component calls bindService() with the proper
     * intent.  Return the concrete implementation of DownloadRequest
     * cast as an IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mDownloadRequestImpl;
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
