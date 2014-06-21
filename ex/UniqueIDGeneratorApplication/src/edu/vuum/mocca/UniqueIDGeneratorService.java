package edu.vuum.mocca;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * @class UniqueIDGeneratorService
 *
 * @brief This Service generates unique IDs within a pool of Threads.
 *        When it is created, it creates a ThreadPoolExecutor using
 *        the newFixedThreadPool() method of the Executors class.
 * 
 *        This class implements the Synchronous Service layer of the
 *        Half-Sync/Half-Async pattern.  It also implements a variant
 *        of the Factory Method pattern.
 */
public class UniqueIDGeneratorService extends Service {
    /**
     * Used for debugging.
     */
    private final String TAG = getClass().getName();

    /**
     * A class constant that determines the maximum number of threads
     * used to service download requests.
     */
    private final int MAX_THREADS = 4;
	
    /**
     * The ExecutorService that references a ThreadPool.
     */
    private ExecutorService mExecutor;

    /**
     * A persistent collection of unique IDs.  For simplicity we use a
     * SharedPreference, which isn't optimized for performance but is
     * easy to use.
     */
    private SharedPreferences uniqueIDs = null;

    /**
     * A Messenger that encapsulates the RequestHandler used to handle
     * request Messages sent from the UniqueIDGeneratorActivity.
     */
    private Messenger mReqMessenger = null;

    /**
     * Hook method called when the Service is created.
     */
    @Override
    public void onCreate() {
        // A Messenger that encapsulates the RequestHandler used to
        // handle request Messages sent from the
        // UniqueIDGeneratorActivity.
        mReqMessenger =
            new Messenger(new RequestHandler());

        // Get a SharedPreferences instance that points to the default
        // file used by the preference framework in this Service.
        uniqueIDs = PreferenceManager.getDefaultSharedPreferences(this);

        // Create a FixedThreadPool Executor that's configured to use
        // MAX_THREADS.
        mExecutor = Executors.newFixedThreadPool(MAX_THREADS);
    }

    /**
     * Factory method to make the desired Intent.
     */
    public static Intent makeIntent(Context context) {
        // Create the Intent that's associated to the
        // UniqueIDGeneratorService class.
        return new Intent(context, 
        				  UniqueIDGeneratorService.class);
    }

    /**
     * Extracts the encapsulated unique ID from the Message.
     */
    public static String uniqueID(Message message) {
        return message.getData().getString("ID");
    }

    /**
     * @class RequestHandler
     *
     * @brief This class handles messages sent by the
     *        UniqueIDGeneratorActivity.
     */
    private class RequestHandler extends Handler {

        // Hook method called back when a request Message arrives from
        // the UniqueIDGeneratorActivity.  The message it receives contains
        // the Messenger used to reply to the Activity.
        public void handleMessage(Message request) {

            // Store the reply Messenger so it doesn't change out from
            // underneath us.
            final Messenger replyMessenger = request.replyTo;

            // Runnable that's used to generate a unique ID in the
            // thread pool.
            Runnable idGeneratorRunnable = new Runnable() {
                    public void run () {
                        String uniqueID;

                        // We need to synchronize this block of code
                        // since it's accessed by multiple threads in
                        // the pool.
                        synchronized (this) {
                            // This look keep generating a random UUID
                            // if it's not unique (i.e., is not
                            // currently found in the persistent
                            // collection of SharedPreferences).  The
                            // likelihood of a non-unique UUID is low,
                            // but we're being extra paranoid for the
                            // sake of this example ;-)
                            for (;;) {
                                uniqueID = UUID.randomUUID().toString();

                                if (uniqueIDs.getInt(uniqueID,
                                                      0) == 1)
                                    Log.d(TAG, uniqueID + " already in use");
                                else {
                                    Log.d(TAG, uniqueID + " not in use");
                                    break;
                                }
                            }

                            // We found a unique ID, so add it as the
                            // "key" to the persistent collection of
                            // SharedPreferences, with a value of 1 to
                            // indicate this ID is already "used".
                            SharedPreferences.Editor editor = uniqueIDs.edit();
                            editor.putInt(uniqueID, 1);
                            editor.commit();
                        }

                        // Create a Message that's used to send the
                        // unique ID back to the UniqueIDGeneratorActivity.
                        Message reply = Message.obtain();
                        Bundle data = new Bundle();
                        data.putString("ID", uniqueID);
                        reply.setData(data);

                        try {
                            // Send the reply back to the
                            // UniqueIDGeneratorActivity.
                            if (replyMessenger == null)
                                Log.d(TAG, "replyMessenger is null");
                            else {
                                Log.d(TAG, "sending unique ID" + uniqueID);
                                replyMessenger.send(reply);
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                };

            // Put the runnable in the thread pool for subsequent
            // concurrent processing.
            mExecutor.execute(idGeneratorRunnable);
        }
    }

    /**
     * Called when the service is destroyed, which is the last call
     * the Service receives informing it to clean up any resources it
     * holds.
     */
    @Override
    public void onDestroy() {
    	// Ensure that the threads used by the ThreadPoolExecutor
    	// complete and are reclaimed by the system.

        mExecutor.shutdown();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mReqMessenger.getBinder();
    }
}
    
