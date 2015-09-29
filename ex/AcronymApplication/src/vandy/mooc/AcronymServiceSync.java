package vandy.mooc;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * @class AcronymServiceSync
 * 
 * @brief This class uses synchronous AIDL interactions to expand
 *        acronyms via an Acronym Web service.  The AcronymActivity
 *        that binds to this Service will receive an IBinder that's an
 *        instance of AcronymCall, which extends IBinder.  The
 *        Activity can then interact with this Service by making
 *        two-way method calls on the AcronymCall object asking this
 *        Service to lookup the meaning of the Acronym string.  After
 *        the lookup is finished, this Service sends the Acronym
 *        results back to the Activity by returning a List of
 *        AcronymData.
 * 
 *        AIDL is an example of the Broker Pattern, in which all
 *        interprocess communication details are hidden behind the
 *        AIDL interfaces.
 */
@SuppressWarnings("deprecation")
public class AcronymServiceSync extends Service {
    /**
     * Logging tag.
     */
    private final static String TAG =
        AcronymServiceSync.class.getCanonicalName();

    /**
     * Object that can invoke HTTP GET requests on URLs.
     */
    private final static AndroidHttpClient mClient =
        AndroidHttpClient.newInstance("");

    /**
     * Called when a client (e.g., AcronymActivity) calls
     * bindService() with the proper Intent.  Returns the
     * implementation of AcronymCall, which is implicitly cast as an
     * IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAcronymCallImpl;
    }

    /**
     * Factory method that makes an Intent used to start the
     * AcronymServiceSync when passed to bindService().
     * 
     * @param context
     *            The context of the calling component.
     */
    public static Intent makeIntent(Context context) {
        return new Intent(context,
                          AcronymServiceSync.class);
    }

    /**
     * The concrete implementation of the AIDL Interface AcronymCall,
     * which extends the Stub class that implements AcronymCall,
     * thereby allowing Android to handle calls across process
     * boundaries.  This method runs in a separate Thread as part of
     * the Android Binder framework.
     * 
     * This implementation plays the role of Invoker in the Broker
     * Pattern.
     */
    AcronymCall.Stub mAcronymCallImpl = new AcronymCall.Stub() {
            /**
             * Implement the AIDL AcronymCall expandAcronym() method,
             * which forwards to DownloadUtils getResults() to obtain
             * the results from the Acronym Web service and then
             * returns the results back to the Activity.
             */
            @Override
            public List<AcronymData> expandAcronym(String acronym)
                throws RemoteException {

                // Call the Acronym Web service to get the list of
                // possible expansions of the designated acronym.
                List<AcronymData> acronymResults = 
                    AcronymDownloadUtils.getResults(mClient,
                                                    acronym);

                Log.d(TAG, "" + acronymResults.size() + " results for acronym: " + acronym);

                // Return the list of acronym expansions back to the
                // AcronymActivity.
                return acronymResults;
            }
	};

    public void onDestroy() {
        mClient.close();
        super.onDestroy();
    }
}
