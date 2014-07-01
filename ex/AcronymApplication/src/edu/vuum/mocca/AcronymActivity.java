package edu.vuum.mocca;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

/**
 * The main Activity that launches the screen users will see.
 */
public class AcronymActivity extends Activity {
    /**
     * Used for logging purposes.
     */
    static private String TAG = AcronymActivity.class.getCanonicalName();

    /**
     * The ListView that will display the results to the user.
     */
     private ListView mListView;

    /**
     * A custom ArrayAdapter used to display the list of AcronymData objects.
     */
    private AcronymDataArrayAdapter adapter;
    
    /**
     * Acronym entered by the usre.
     */
    private EditText mEditText;

    /**
     * The implementation of the AcronymResults AIDL Interface.
     * Should be passed to the WebService using the
     * AcronymRequest.expandAcronym() method.
     * 
     * This implementation of AcronymResults.Stub plays the role of
     * Invoker in the Broker Pattern.
     */
    private AcronymResults.Stub mAcronymResults = new AcronymResults.Stub() {
            /**
             * This method is called back by the Service to return the
             * results.
             */
            @Override
		public void sendResults(final List<AcronymData> acronymDataList)
                throws RemoteException {
                // This method runs in a separate Thread as per the
                // behavior of the Android Binder framework, so we
                // need to explicitly post a runnable containing the
                // results back to the UI Thread.
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
        new GenericServiceConnection<AcronymRequest>
        (new GenericServiceConnection.InterfaceFactory<AcronymRequest>() {
            public AcronymRequest asInterface(IBinder service) { 
                return AcronymRequest.Stub.asInterface(service); 
            }});

    /**
     * This GenericServiceConnection is used to receive results after
     * binding to the AcronymServiceSync Service using bindService().
     */
    private GenericServiceConnection<AcronymCall> mServiceConnectionSync =
        new GenericServiceConnection<AcronymCall>
        (new GenericServiceConnection.InterfaceFactory<AcronymCall>() {
            public AcronymCall asInterface(IBinder service) { 
                return AcronymCall.Stub.asInterface(service); 
            }});

    /**
     * Called when the activity is starting - this is where most
     * initialization should go.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get references to the UI components.
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.editText1);
        mListView = (ListView) findViewById(R.id.listView1);
    }

    /*
     * Initiate the asynchronous acronym lookup.
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
     * Initiate the synchronous acronym lookup.
     */
    public void expandAcronymSync(View v) {
        final AcronymCall acronymCall = 
            mServiceConnectionSync.getInterface();

        if (acronymCall != null) {
            // Get the acronym entered by the user.
            final String acronym = mEditText.getText().toString();

            hideKeyboard();

            // Use mAcronymCall to download the Acronym data in a
            // separate Thread and then display it in the UI Thread.
            // We use a separate Thread to avoid blocking the UI
            // Thread.
            new Thread(new Runnable() {
                    public void run () {
                        try {
                            Log.d(TAG,
                                  "Calling twoway AcronymServiceSync.expandAcronym()");

                            // Download the expanded acronym via a
                            // synchronous two-way method call.
                            final List<AcronymData> acronymDataList = 
                                acronymCall.expandAcronym(acronym);

                            // Display the results in the UI Thread.
                            runOnUiThread(new Runnable() {
                                    public void run() {
                                        displayResults(acronymDataList);
                                    }
                                });
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }).start();
        } else {
            Log.d(TAG, "mAcronymCall was null.");
        }
    }

    /**
     * Display the results to the screen.
     * 
     * @param results
     *            List of Resultes to be displayed.
     */
    protected void displayResults(List<AcronymData> results) {
        // Create custom ListView Adapter and fill it with our data.
        if (adapter == null) {
            // Create a local instance of our custom Adapter for our
            // ListView.
            adapter = new AcronymDataArrayAdapter(this, results);
        } else {
            // If adapter already existed, then change data set.
            adapter.clear();
            adapter.addAll(results);
            adapter.notifyDataSetChanged();
        }

        // Set the adapter to the ListView.
        mListView.setAdapter(adapter);
    }

    /**
     * Hook method called when the MainActivity becomes visible to
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
     * Hook method called when the MainActivity becomes completely hidden to
     * unbind the Activity from the Services.
     */
    @Override
    public void onStop() {
        super.onStop();

        // Unbind the Async Service if it is connected.
        if (mServiceConnectionAsync.getInterface() != null)
            unbindService(mServiceConnectionAsync);

        // Unbind the Sync Service if it is connected.
        if (mServiceConnectionSync.getInterface() != null)
            unbindService(mServiceConnectionSync);
    }

    /**
     * Hide the keyboard after a user has finished typing the url.
     */
    private void hideKeyboard() {
        InputMethodManager mgr =
            (InputMethodManager) getSystemService
            (Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(mEditText.getWindowToken(),
                                    0);
    }
}
