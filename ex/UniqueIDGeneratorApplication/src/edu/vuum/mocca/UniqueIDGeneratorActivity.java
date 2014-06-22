package edu.vuum.mocca;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * @class UniqueIDGeneratorActivity
 *
 * @brief A class that interacts with a user to invoke a Bound Service
 *        that generates a system-wide Unique ID.
 */
public class UniqueIDGeneratorActivity extends Activity {
    /**
     * Used for debugging.
     */
    private final String TAG = getClass().getName();

    /**
     * Location where the unique ID is displayed.
     */
    private TextView mOutput;

    /**
     * Reference to the Messenger that's implemented in the
     * UniqueIDGeneratorService.
     */
    private Messenger mReqMessengerRef = null;

    /**
     * @class ReplyHandler
     *
     * @brief Receives the reply from the UniqueIDGeneratorService, which
     * contains the unique ID.
     */
    class ReplyHandler extends Handler {
        /**
         * Callback to handle the reply from the UniqueIDGeneratorService.
         */
        public void handleMessage(Message reply) {
            // Get the unique ID encapsulated in reply Message.
            String uniqueID = UniqueIDGeneratorService.uniqueID(reply);

            Log.d(TAG, "received unique ID " + uniqueID);

            // Display the unique ID.
            mOutput.setText(uniqueID);
        }
    }

    /** 
     * This ServiceConnection is used to receive a Messenger reference
     * after binding to the UniqueIDGeneratorService using bindService().
     */
    private ServiceConnection mSvcConn = new ServiceConnection() {
            /**
             * Called after the UniqueIDGeneratorService is connected to
             * convey the result returned from onBind().
             */
            public void onServiceConnected(ComponentName className,
                                           IBinder binder) {
                Log.d(TAG, "ComponentName:" + className);

                // Create a new Messenger that encapsulates the
                // returned IBinder object and store it for later use
                // in mReqMessengerRef.
                mReqMessengerRef = new Messenger(binder);
            }

            /**
             * Called if the Service crashes and is no longer
             * available.  The ServiceConnection will remain bound,
             * but the Service will not respond to any requests.
             */
            public void onServiceDisconnected(ComponentName className) {
                mReqMessengerRef = null;
            }
	};

    /**
     * Method that initializes the Activity when it is first created.
     * 
     * @param savedInstanceState
     *            Activity's previously frozen state, if there was one.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mOutput = (TextView) findViewById(R.id.output);
    }

    /**
     * Called by Android when the user presses the "Generate Unique
     * ID" button to request a new unique ID from the
     * UniqueIDGeneratorService.
     */
    public void generateUniqueID(View view) {
        // Create a request Message that indicates the Service should
        // send the reply back to ReplyHandler encapsulated by the
        // Messenger.
        Message request = Message.obtain();
        request.replyTo = new Messenger(new ReplyHandler());
        
        try {
            if (mReqMessengerRef != null) {
                Log.d(TAG, "sending message");
                // Send the request Message to the
                // UniqueIDGeneratorService.
                mReqMessengerRef.send(request);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hook method called by Android when this Activity becomes
     * visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");

        Log.d(TAG, "calling bindService()");
        if (mReqMessengerRef == null)
            // Bind to the UniqueIDGeneratorService associated with this
            // Intent.
            bindService(UniqueIDGeneratorService.makeIntent(this),
                        mSvcConn,
                        Context.BIND_AUTO_CREATE);
    }

    /**
     * Hook method called by Android when this Activity becomes
     * invisible.
     */
    @Override
    protected void onStop() {
        // Unbind from the Service.
        unbindService(mSvcConn);
        super.onStop();
    }
}
