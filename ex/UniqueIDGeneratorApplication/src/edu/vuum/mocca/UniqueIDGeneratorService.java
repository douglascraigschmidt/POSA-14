package edu.vuum.mocca;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
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
    static final int MAX_THREADS = 4;
	
    /**
     * The ExecutorService that references a ThreadPool.
     */
    ExecutorService mExecutor;

    /**
     * Hook method called when the Service is created.
     */
    @Override
    public void onCreate() {
        // Create a FixedThreadPool Executor that's configured to use
        // MAX_THREADS.
        mExecutor = Executors.newFixedThreadPool(MAX_THREADS);
    }

    /**
     * A set of unique keys.
     */
    private final Set<UUID> keys = new HashSet<UUID>();

    /**
     * Extracts the encapsulated unique ID from the Message.
     */
    public static String uniqueID(Message message) {
        return message.getData().getString("ID");
    }

    /**
     * Implementation a Messenger that encapsulates the RequestHandler
     * used to handle request Messages sent from the
     * UniqueIDGeneratorActivity.
     */
    private final Messenger mMessengerImpl =
            new Messenger(new RequestHandler());

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
            Runnable keyGeneratorRunnable = new Runnable() {
                    public void run () {
                        UUID id;

                        // We need to synchronize this block of code
                        // since it's accessed by multiple threads in
                        // the pool.
                        synchronized (keys) {
                            do {
                                id = UUID.randomUUID();
                            } while (keys.contains(id));
                            keys.add(id);
                        }

                        final String key = id.toString();

                        // Create a Message that's used to send the
                        // unique ID back to the UniqueIDGeneratorActivity.
                        Message reply = Message.obtain();
                        Bundle data = new Bundle();
                        data.putString("ID", key);
                        reply.setData(data);

                        try {
                            // Send the reply back to the
                            // UniqueIDGeneratorActivity.
                            if (replyMessenger == null)
                                Log.d(TAG, "replyMessenger is null");
                            else {
                                Log.d(TAG, "sending key" + key);
                                replyMessenger.send(reply);
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                };

            // Put the runnable in the thread pool for subsequent
            // concurrent processing.
            mExecutor.execute(keyGeneratorRunnable);
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
        return mMessengerImpl.getBinder();
    }
}
    
