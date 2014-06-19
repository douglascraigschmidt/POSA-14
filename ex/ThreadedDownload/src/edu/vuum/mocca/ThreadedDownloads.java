package edu.vuum.mocca;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @class ThreadedDownloads
 * 
 * @brief A class that allows a user to download a bitmap image using
 *        any of the following concurrency models from the
 *        . HaMeR framework 
 *          . Handlers and Runnables
 *          . Handlers and Messages
 *        . AsyncTask framework
 */
public class ThreadedDownloads extends Activity {
    /**
     * Default URL to download
     */
    private final static String mDefaultURL = 
        "http://www.dre.vanderbilt.edu/~schmidt/ka.png";

    /**
     * User's selection of URL to download
     */
    private EditText mUrlEditText;

    /**
     * Image that's been downloaded
     */
    private ImageView mImageView;

    /**
     * Display progress of download
     */
    private ProgressDialog mProgressDialog;

    /**
     * Debug Tag for logging debug output to LogCat
     */
    private final static String TAG =
        ThreadedDownloads.class.getSimpleName();

    /**
     * Method that initializes the Activity when it is first created.
     * 
     * @param savedInstanceState
     *            Activity's previously frozen state, if there was one.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Sets the content view specified in the main.xml file.
         */
        setContentView(R.layout.main);

        /**
         * Caches references to the EditText and ImageView objects in
         * data members to optimize subsequent access.
         */
        mUrlEditText = (EditText) findViewById(R.id.mUrlEditText);
        mImageView = (ImageView) findViewById(R.id.mImageView);
    }

    /**
     * Show a toast, notifying a user of an error when retrieving a
     * bitmap.
     */
    void showErrorToast(String errorString) {
        Toast.makeText(this,
                       errorString,
                       Toast.LENGTH_LONG).show();
    }

    /**
     * Display a downloaded bitmap image if it's non-null; otherwise,
     * it reports an error via a Toast.
     * 
     * @param image
     *            The bitmap image
     */
    void displayImage(Bitmap image)
    {   
        if (mImageView == null)
            showErrorToast("Problem with Application,"
                           + " please contact the Developer.");
        else if (image != null)
            mImageView.setImageBitmap(image);
        else
            showErrorToast("image is corrupted,"
                           + " please check the requested URL.");
    }

    /**
     * Download a bitmap image from the URL provided by the user.
     * 
     * @param url
     *            The url where a bitmap image is located
     * @return the image bitmap or null if there was an error
     */
    private Bitmap downloadImage(String url) {
        /**
         * Use the default URL if the user doesn't supply one.
         */
        if (url.equals(""))
            url = mDefaultURL;

        try {
            /**
             * Connect to a remote server, download the contents of
             * the image, and provide access to it via an Input
             * Stream. */
            InputStream is =
                (InputStream) new URL(url).getContent();

            /**
             * Decode an InputStream into a Bitmap.
             */
            Bitmap image = BitmapFactory.decodeStream(is);
            return image;
        } catch (Exception e) {
            /** 
             * Post error reports to the UI Thread.
             */
            this.runOnUiThread(new Runnable() {
                    public void run() {
                        /**
                         * Use a Toast to inform user that something
                         * has gone wrong.
                         */
                        showErrorToast("Error downloading image,"
                                       + " please check the requested URL.");
                    }
                });
            Log.e(TAG, "Error downloading image");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Called when a user clicks a button to download an image with
     * Runnables and Handlers.
     * 
     * @param view
     *            The "Run Runnable" button
     */
    public void runRunnable(View view) {
        /**
         * Obtain the requested URL from the user input.
         */
        String url = getUrlString();

        hideKeyboard();

        /**
         * Inform the user that the download is starting.
         */
        
        showDialog("downloading via Runnables and Handlers");
        
        /**
         * Create and start a new Thread to download an image in the
         * background via a Runnable. The downloaded image is then
         * diplayed in the UI Thread by posting another Runnable via
         * the Activity's runOnUiThread() method, which uses an
         * internal Handler.
         */
        new Thread(new RunnableWithHandlers(url)).start();
    }

    /**
     * @class RunnablesWithHandler
     *
     * @brief This class downloads a bitmap image in the background
     *        using Runnables and Handlers.
     */
    private class RunnableWithHandlers implements Runnable {
        /*
         * The URL to download. 
         */ 
        String mUrl;

        /**
         * Class constructor caches the url of the bitmap image.
         * 
         * @param url
         *            The bitmap image url
         */
        RunnableWithHandlers(String url) {
            mUrl = url;
        }

        /**
         * Download a bitmap image in the background. It also sets the
         * image to an image view and dismisses the progress dialog.
         */
        public void run() {
            final Bitmap image = downloadImage(mUrl);

            ThreadedDownloads.this.runOnUiThread(new Runnable() {
                    public void run() {
                        /**
                         * Dismiss the progress dialog.
                         */
                        mProgressDialog.dismiss();
                    
                        /**
                         * Display the downloaded image to the user.
                         */
                        displayImage(image);
                    }
                });
        }
    }

    /**
     * Called when a user clicks a button to download an image with
     * Handlers and Messages.
     * 
     * @param view
     *            The "Run Messages" button
     */
    public void runMessages(View view) {
        /**
         * Obtain the requested URL from the user input.
         */
        String url = getUrlString();

        hideKeyboard();

        /**
         * Create and start a new Thread to download an image in the
         * background and then use Messages and MessageHandler to
         * cause it to be displayed in the UI Thread.
         */
        new Thread(new RunnableWithMessages(url)).start();
    }

    /**
     * Display the Dialog to the User.
     * 
     * @param message 
     *          The String to display what download method was used.
     */
    public void showDialog(String message) {
        mProgressDialog =
            ProgressDialog.show(this,"Download",message);
    }
    
    /**
     * Dismiss the Dialog
     */
    public void dismissDialog() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }
    
    /**
     * @class MessageHandler
     *
     * @brief A static inner class that inherits from Handler and uses
     *        its handleMessage() hook method to process Messages sent
     *        to it from a background Thread. Since it's static its
     *        instances do not hold implicit references to their outer
     *        classes.
     */
    private static class MessageHandler extends Handler {
        /**
         * Types of Messages that can be passed from a background
         * Thread to the UI Thread to specify which processing to
         * perform.
         */
        static final int SHOW_DIALOG = 1;
        static final int DISMISS_DIALOG = 2;
        static final int DISPLAY_IMAGE = 3;

        /**
         * Allows Activity to be garbage collected properly.
         */
        private WeakReference<ThreadedDownloads> mActivity;

        /**
         * Class constructor constructs mActivity as weak reference
         * to the activity
         * 
         * @param activity
         *            The corresponding activity
         */
        public MessageHandler(ThreadedDownloads activity) {
            mActivity = new WeakReference<ThreadedDownloads>(activity);
        }
        /**
         * Process the specified Messages passed to MessageHandler in
         * the UI Thread. These Messages instruct the Handler to start
         * showing the progress dialog, dismiss it, or display the
         * designated bitmap image via the ImageView.
         */
        public void handleMessage(Message msg) {

            /*
             * Check to see if the activity still exists and return if
             * not.
             */
            ThreadedDownloads activity = mActivity.get();
            if (activity == null) 
                return;                

            switch (msg.what) {

            case SHOW_DIALOG:
                activity.showDialog("downloading via Handlers and Messages");
                break;

            case DISMISS_DIALOG:
                /**
                 * Dismiss the progress dialog.
                 */
                activity.dismissDialog();
                break;

            case DISPLAY_IMAGE:
                /**
                 * Display the downloaded image to the user.
                 */
                activity.displayImage((Bitmap) msg.obj);
                break;
            }
        }
    }

    /**
     * Instance of MessageHandler.
     */
    MessageHandler messageHandler = new MessageHandler(this);

    /**
     * @class RunnableWithMessages
     *
     * @brief This class downloads a bitmap image in the background
     *        using Handlers and Messages.
     */
    private class RunnableWithMessages implements Runnable {
        /*
         * The URL to download. 
         */ 
        String mUrl;

        /**
         * Class constructor caches the url of a bitmap image and a
         * handler.
         * 
         * @param url
         *            The bitmap image url
         * @param h
         *            The handler that handles Messages
         */
        RunnableWithMessages(String url) {
            this.mUrl = url;
        }

        /**
         * Download a bitmap image in a background Thread. It sends
         * various messages to the mHandler running in the UI Thread.
         */
        public void run() {
            /**
             * Store a copy of the reference to the MessageHandler.
             */
            final MessageHandler mHandler =
                ThreadedDownloads.this.messageHandler;

            /**
             * Factory creates a Message that instructs the
             * MessageHandler to begin showing the progress dialog to
             * the user.
             */
            Message msg = 
                mHandler.obtainMessage(MessageHandler.SHOW_DIALOG,
                                       mProgressDialog);

            /**
             * Send the Message to initiate the ProgressDialog.
             */
            mHandler.sendMessage(msg);

            /**
             * Download the image.
             */
            final Bitmap image = downloadImage(mUrl);

            /**
             * Factory creates a Message that instructs the
             * MessageHandler to dismiss the progress dialog.
             */
            msg = mHandler.obtainMessage(MessageHandler.DISMISS_DIALOG,
                                         mProgressDialog);
            /**
             * Send the Message to dismiss the ProgressDialog.
             */
            mHandler.sendMessage(msg);

            /**
             * Factory creates a Message that instructs the
             * MessageHandler to display the image to the user.
             */
            msg = mHandler.obtainMessage(MessageHandler.DISPLAY_IMAGE, 
                                         image);
            /**
             * Send the Message to instruct the UI Thread to display
             * the image.
             */
            mHandler.sendMessage(msg);
        }
    }

    /**
     * Called when a user clicks a button to download an image with
     * AsyncTask.
     * 
     * @param view
     *            The "Run Async" button
     */
    public void runAsyncTask(View view) {

        String url = getUrlString();

        hideKeyboard();

        // Execute the download using a Thread in the pool of Threads.
        new DownloadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
    }

    /**
     * @class DownloadTask
     *
     * @brief This class downloads a bitmap image in the background
     *        using AsyncTask.
     */
    private class DownloadTask extends AsyncTask<String, Integer, Bitmap> {
         /**
         * Called by the AsyncTask framework in the UI Thread to
         * perform initialization actions.
         */
        protected void onPreExecute() {
            /**
             * Show the progress dialog before starting the download
             * in a Background Thread.
             */
            showDialog("downloading via AsyncTask");
        }

        /**
         * Downloads bitmap in an AsyncTask background thread.
         * 
         * @param params
         *            The url of a bitmap image
         */
        protected Bitmap doInBackground(String... urls) {
            return downloadImage(urls[0]);
        }

        /**
         * Called after an operation executing in the background is
         * completed. It sets the bitmap image to an image view and
         * dismisses the progress dialog.
         * 
         * @param image
         *            The bitmap image
         */
        protected void onPostExecute(Bitmap image) {
            /**
             * Dismiss the progress dialog.
             */
            dismissDialog();

            /**
             * Display the downloaded image to the user.
             */
            displayImage(image);
        }
    }

    /**
     * Called when a user clicks a button to reset an image to
     * default.
     * 
     * @param view
     *            The "Reset Image" button
     */
    public void resetImage(View view) {
        mImageView.setImageResource(R.drawable.default_image);
    }

    /**
     * Hide the keyboard after a user has finished typing the url.
     */
    private void hideKeyboard() {
        InputMethodManager mgr =
            (InputMethodManager) getSystemService
            (Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(mUrlEditText.getWindowToken(),
                                    0);
    }

    /**
     * Show a collection of menu items for the ThreadedDownloads
     * activity.
     * 
     * @return true
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }
    
    /**
     * Read the URL EditText and return the String it contains.
     * 
     * @return String value in mUrlEditText
     */
    String getUrlString() {
        return mUrlEditText.getText().toString();
    }

}
