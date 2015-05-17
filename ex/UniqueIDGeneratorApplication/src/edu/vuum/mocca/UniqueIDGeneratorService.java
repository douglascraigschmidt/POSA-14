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

/**
 * @class UniqueIDGeneratorService
 *
 * @brief This Service generates unique IDs via a Thread pool and
 *        returns the IDs to the UniqueIDGeneratorActivity.
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
     * String used as a key for the unique ID stored in the reply
     * Message.
     */
    final static String ID = "ID";

    /**
     * A RequestHandler that processes Messages from the
     * UniqueIDGeneratorService within a pool of Threads. 
     */ 
    private RequestHandler mRequestHandler = null; 

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
        // The Messenger encapsulates the RequestHandler 
        // used to handle request Messages sent from 
        // UniqueIDGeneratorActivity.
    	mRequestHandler = new RequestHandler();
        mReqMessenger = new Messenger(mRequestHandler);
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
     * Extracts the encapsulated unique ID from the reply Message.
     */
    public static String uniqueID(Message replyMessage) {
        return replyMessage.getData().getString(ID);
    }

    /**
     * @class RequestHandler
     *
     * @brief This class generates unique IDs via a pool of Threads
     *        and sends them back to UniqueIDGeneratorActivity. When
     *        it's created, it creates a ThreadPoolExecutor using the
     *        newFixedThreadPool() method of the Executors class.
     */
    private class RequestHandler extends Handler {
        /**
         * A class constant that determines the maximum number of
         * threads used to service download requests.
         */
        private final int MAX_THREADS = 4;
	
        /**
         * The ExecutorService implementation that references a
         * ThreadPool.
         */
        private ExecutorService mExecutor;

        /**
         * A collection of unique IDs implemented internally using a
         * persistent Java HashMap.
         */
        private SharedPreferences uniqueIDs = null;

        /**
         * Initialize RequestHandler to generate IDs concurrently.
         */
        public RequestHandler() {
            // Get a SharedPreferences instance that points to the
            // default file used by the preference framework in this
            // Service.
            uniqueIDs = 
                PreferenceManager.getDefaultSharedPreferences
                (UniqueIDGeneratorService.this);

            // Create a FixedThreadPool Executor that's configured to
            // use MAX_THREADS.
            mExecutor = 
                Executors.newFixedThreadPool(MAX_THREADS);
        }

    	// Ensure threads used by the ThreadPoolExecutor complete and
    	// are reclaimed by the system.
        public void shutdown() {
            mExecutor.shutdown();
        }

        /**
         * Return a Message containing an ID that's unique
         * system-wide.
         */
        private Message generateUniqueID() {
            String uniqueID;

            // Protect critical section to ensure the IDs are unique.
            synchronized (this) {
                // This loop keeps generating a random UUID if it's
                // not unique (i.e., is not currently found in the
                // persistent collection of SharedPreferences).  The
                // likelihood of a non-unique UUID is low, but we're
                // being extra paranoid for the sake of this example
                // ;-)
                do {
                    uniqueID = UUID.randomUUID().toString();
                } while (uniqueIDs.getInt(uniqueID, 0) == 1);

                // We found a unique ID, so add it as the "key" to the
                // persistent collection of SharedPreferences, with a
                // value of 1 to indicate this ID is already "used".
                SharedPreferences.Editor editor = uniqueIDs.edit();
                editor.putInt(uniqueID, 1);
                editor.commit();
            }

            // Create a Message that's used to send the unique ID back
            // to the UniqueIDGeneratorActivity.
            Message reply = Message.obtain();
            Bundle data = new Bundle();
            data.putString(ID, uniqueID);
            reply.setData(data);
            return reply;
        }

        // Hook method called back when a request Message arrives from
        // the UniqueIDGeneratorActivity.  The message it receives
        // contains the Messenger used to reply to the Activity.
        public void handleMessage(Message request) {

            // Store the reply Messenger so it doesn't change out from
            // underneath us.
            final Messenger replyMessenger = request.replyTo;

            // Log.d(TAG, "replyMessenger = " + replyMessenger.hashCode());

            // Put a runnable that generates a unique ID into the
            // thread pool for subsequent concurrent processing.
            mExecutor.execute(new Runnable() {
                    public void run () {
                        Message reply =
                            generateUniqueID();
                        
                        try {
                            // Send the reply back to the
                            // UniqueIDGeneratorActivity.
                            // Log.d(TAG, "replyMessenger = " + replyMessenger.hashCode());
                            // try { Thread.sleep (10000); } catch (InterruptedException e) {}
                            replyMessenger.send(reply);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
        }
    }

    /**
     * Called when the service is destroyed, which is the last call
     * the Service receives informing it to clean up any resources it
     * holds.
     */
    @Override
    public void onDestroy() {
    	// Ensure threads used by the ThreadPoolExecutor complete and
    	// are reclaimed by the system.
        mRequestHandler.shutdown();
    }

    /**
     * Factory method that returns the underlying IBinder associated
     * with the Request Messenger.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mReqMessenger.getBinder();
    }
}
    
