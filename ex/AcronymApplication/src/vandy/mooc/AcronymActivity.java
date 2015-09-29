package vandy.mooc;

import java.util.List;

import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

/**
 * @class AcronymActivity
 *
 * @brief The main Activity that handles communicate with the
 *        AcronymService(s).
 */
public class AcronymActivity extends AcronymActivityBase {
    /**
     * The implementation of the AcronymResults AIDL Interface, which
     * will be passed to the Acronym Web service using the
     * AcronymRequest.expandAcronym() method.
     * 
     * This implementation of AcronymResults.Stub plays the role of
     * Invoker in the Broker Pattern since it dispatches the upcall to
     * sendResults().
     */
    private AcronymResults.Stub mAcronymResults = new AcronymResults.Stub() {
            /**
             * This method is invoked by the AcronymServiceAsync to
             * return the results back to the DownloadActivity.
             */
            @Override
            public void sendResults(final List<AcronymData> acronymDataList)
                throws RemoteException {
                // Since the Android Binder framework dispatches this
                // method in a separate Thread we need to explicitly
                // post a runnable containing the results to the UI
                // Thread, where it's displayed.
                runOnUiThread(new Runnable() {
                        public void run() {
                            displayResults(acronymDataList);
                        }
                    });
            }
	};

    /**
     * This GenericServiceConnection is used to receive results after
     * binding to the AcronymServiceAsync Service using bindService().
     */
    private GenericServiceConnection<AcronymRequest> mServiceConnectionAsync =
        new GenericServiceConnection<AcronymRequest>(AcronymRequest.class);

    /**
     * This GenericServiceConnection is used to receive results after
     * binding to the AcronymServiceSync Service using bindService().
     */
    private GenericServiceConnection<AcronymCall> mServiceConnectionSync =
        new GenericServiceConnection<AcronymCall>(AcronymCall.class);

    /*
     * Initiate the asynchronous acronym lookup when the user presses
     * the "Look Up Async" button.
     */
    public void expandAcronymAsync(View v) {
        AcronymRequest acronymRequest = 
            mServiceConnectionAsync.getInterface();

        if (acronymRequest != null) {
            // Get the acronym entered by the user.
            final String acronym = mEditText.getText().toString();

            hideKeyboard();

            try {
                // Invoke a one-way AIDL call, which does not block
                // the client.  The results are returned via the
                // sendResults() method of the mAcronymResults
                // callback object, which runs in a Thread from the
                // Thread pool managed by the Binder framework.
                acronymRequest.expandAcronym(mAcronymResults,
                                             acronym);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException:" + e.getMessage());
            }
        } else {
            Log.d(TAG, "acronymRequest was null.");
        }
    }

    /*
     * Initiate the synchronous acronym lookup when the user presses
     * the "Look Up Sync" button.
     */
    public void expandAcronymSync(View v) {
        final AcronymCall acronymCall = 
            mServiceConnectionSync.getInterface();

        if (acronymCall != null) {
            // Get the acronym entered by the user.
            final String acronym = mEditText.getText().toString();

            hideKeyboard();

            // Use an AsyncTask to download the Acronym data in a
            // separate thread and then display it in the UI thread.
            new AsyncTask<String, Void, List<AcronymData>> () {

                // Download the expanded acronym via a synchronous
                // two-way method call, which runs in a background
                // thread to avoid blocking the UI thread.
                protected List<AcronymData> doInBackground(String... acronyms) {
                    try {
                        return acronymCall.expandAcronym(acronyms[0]);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                // Display the results in the UI Thread.
                protected void onPostExecute(List<AcronymData> acronymDataList) {
                    displayResults(acronymDataList);
                }
            }.execute(acronym);
        } else {
            Log.d(TAG, "mAcronymCall was null.");
        }
    }

    /**
     * Hook method called when the AcronymActivity becomes visible to
     * bind the Activity to the Services.
     */
    @Override
    public void onStart() {
        super.onStart();

        // Launch the Bound Services if they aren't already running
        // via a call to bindService(), which binds this activity to
        // the Acronym* Services if they aren't already bound.
        if (mServiceConnectionAsync.getInterface() == null) 
            bindService(AcronymServiceAsync.makeIntent(this),
                        mServiceConnectionAsync,
                        BIND_AUTO_CREATE);
        
        if (mServiceConnectionSync.getInterface() == null) 
            bindService(AcronymServiceSync.makeIntent(this),
                        mServiceConnectionSync,
                        BIND_AUTO_CREATE);
    }

    /**
     * Hook method called when the AcronymActivity becomes completely
     * hidden to unbind the Activity from the Services.
     */
    @Override
    public void onStop() {
        // Unbind the Async Service if it is connected.
        if (mServiceConnectionAsync.getInterface() != null)
            unbindService(mServiceConnectionAsync);

        // Unbind the Sync Service if it is connected.
        if (mServiceConnectionSync.getInterface() != null)
            unbindService(mServiceConnectionSync);

        super.onStop();
    }
}
