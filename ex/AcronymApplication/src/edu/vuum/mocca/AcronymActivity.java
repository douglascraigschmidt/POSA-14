package edu.vuum.mocca;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
     * Reference to the Acronym Service (after it's bound).
     */
    AcronymRequest mAcronymRequest;

    /**
     * The ListView that will display the results to the user.
     */
    ListView mListView;

    /**
     * A custom ArrayAdapter used to display the list of AcronymData objects.
     */
    AcronymDataArrayAdapter adapter;

    /**
     * The implementation of the AcronymCallback AIDL
     * Interface. Should be passed to the WebService using the
     * AcronymRequest.callAcronymRequest() method.
     * 
     * This implementation of AcronymCallback.Stub plays the role of
     * Invoker in the Broker Pattern.
     */
    AcronymCallback.Stub mAcronymCallback = new AcronymCallback.Stub() {
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
     * This ServiceConnection is used to receive results after binding
     * to the AcronymServiceAsync Service using bindService().
     */
    ServiceConnection mServiceConnectionAsync = new ServiceConnection() {
            /**
             * Cast the returned IBinder object to the AcronymRequest
             * AIDL Interface and store it for later use in
             * mAcronymRequest.
             */
            @Override
		public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected(), ComponentName: " + name);
                mAcronymRequest = AcronymRequest.Stub.asInterface(service);
            }

            /**
             * Called if the remote service crashes and is no longer
             * available. The ServiceConnection will remain bound, but
             * the service will not respond to any requests.
             */
            @Override
		public void onServiceDisconnected(ComponentName name) {
                mAcronymRequest = null;
            }

	};

    /**
     * Called when the activity is starting. This is where most initialization
     * should go.
     */
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get references to the UI components.
        setContentView(R.layout.activity_main);
        final Button mButton = (Button) findViewById(R.id.button1);
        final EditText mEditText = (EditText) findViewById(R.id.editText1);
        mListView = (ListView) findViewById(R.id.listView1);

        /*
         * Requirement to have Android ignore what it thinks is a long
         * running process (Inernet lookup of data) on the UI thread.
         * 
         * However we are making use of the Asynchronous callback pattern.
         * 
         * This eliminates the need for us to manually call the
         * service from within a separate thread from the UI thread,
         * as synchronous service calls would require us to do.
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
            .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Setup the button click, to start the Asynchronous lookup.
        mButton.setOnClickListener(new OnClickListener() {
                /*
                 * Start the Asynchronous lookup.
                 */
                @Override
                    public void onClick(View v) {
                    if (mAcronymRequest != null) {
                        String acronym = mEditText.getText().toString();
                        try {
                            // Invoke a one-way AIDL call.
                            mAcronymRequest.callAcronymRequest(mAcronymCallback,
                                                               acronym);
                        } catch (RemoteException e) {
                            Log.e(TAG, "RemoteException:" + e.getMessage());
                        }
                    } else {
                        Log.d(TAG, "mAcronymRequest was null.");
                    }
                } // onClick
            }); // mButton.setOnClickListener ending
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
     * Hook method called when the MainActivity becomes visible to bind the
     * Activity to the Services.
     */
    @Override
    public void onStart() {
        super.onStart();

        // Launch the designated Bound Service(s) if they aren't
        // already running via a call to bindService(), which binds
        // this activity to the Acronym* Services if they aren't
        // already bound.
        if (mAcronymRequest == null) {
            bindService(AcronymServiceAsync.makeIntent(this),
                        mServiceConnectionAsync, BIND_AUTO_CREATE);
        }
    }

    /**
     * Hook method called when the MainActivity becomes completely hidden to
     * unbind the Activity from the Services.
     */
    @Override
	public void onStop() {
        super.onStop();

        // Unbind the Async Service if it is connected.
        if (mAcronymRequest != null) {
            unbindService(mServiceConnectionAsync);
        }
    }
}
