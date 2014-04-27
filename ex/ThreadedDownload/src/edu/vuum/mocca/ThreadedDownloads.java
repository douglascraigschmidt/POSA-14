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
 *        any of the following concurrency models: Handlers and
 *        Runnables, Handlers and Messages, and AsyncTask.
 * 
 */
public class ThreadedDownloads extends Activity 
{
    /**
     * Default URL to download
     */
    private final String mDefaultURL = "http://www.dre.vanderbilt.edu/~schmidt/ka.png";

    /**
     * User's selection of URL to download
     */
    private EditText mUrlEditText;

    /**
     * Image that's been downloaded
     */
    private static ImageView mImageView;

    /**
     * Display progress of download
     */
    private static ProgressDialog mProgressDialog;

    /**
     * Debug Tag for logging debug output to LogCat
     */
    private String TAG = getClass().getSimpleName();
    
    /**
     * This Activity's context.
     */
    static private Context mThisActivityContext;

    /**
     * Method that initializes the Activity when it is first created.
     * 
     * @param savedInstanceState
     *            Activity's previously frozen state, if there was one.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /** 
         * Store the Activity Context for later use by the Toast.
         */
        
        mThisActivityContext = getApplication().getApplicationContext();

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
    private static void showErrorToast(String errorString) {
        Toast.makeText(mThisActivityContext,
                       errorString,
                       Toast.LENGTH_LONG).show();
    }

    /**
     * Display a downloaded bitmap image if it's non-null; otherwise,
     * it reports an error via a Toast.
     * 
     * @param imageBitmap
     *            The bitmap image
     */
    private static void displayImage(Bitmap imageBitmap)
    {
        if (imageBitmap != null)
            mImageView.setImageBitmap(imageBitmap);
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
            InputStream is = (InputStream) new URL(url).getContent();

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
        String url = mUrlEditText.getText().toString();

        hideKeyboard();

        /**
         * Inform the user that the download is starting via a
         * progress dialog.
         */
        mProgressDialog =
            ProgressDialog.show(ThreadedDownloads.this,
                                "Download",
                                "downloading via Runnables and Handlers");

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
            this.mUrl = url;
        }

        /**
         * Download a bitmap image in the background. It also sets the
         * image to an image view and dismisses the progress dialog.
         */
        public void run() {
            final Bitmap imageBitmap = downloadImage(mUrl);

            ThreadedDownloads.this.runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Dismiss the progress dialog.
                     */
                    mProgressDialog.dismiss();
                    
                    /**
                     * Display the downloaded image to the user.
                     */
                    displayImage(imageBitmap);
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
        String url = mUrlEditText.getText().toString();

        hideKeyboard();

        /**
         * Create and start a new Thread to download an image in the
         * background via Messages and display it in the UI Thread
         * via the messageHandler.
         */
        new Thread(new RunnableWithMessages(url)).start();
    }

    /**
     * @class MyHandler
     *
     * @brief A static inner class that inherits from Handler. Since
     *        it's static its instances do not hold implicit
     *        references to their outer classes.
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
        WeakReference<ThreadedDownloads> mActivity;

        /**
         * Class constructor constructs mActivity as weak reference
         * to the activity
         * 
         * @param activity
         *            The corresponding activity
         */
        MessageHandler(ThreadedDownloads activity) {
            mActivity = new WeakReference<ThreadedDownloads>(activity);
        }

        /**
         * Process the specified Messages passed to MessageHandler in
         * the UI Thread. These Messages instruct the Handler to start
         * showing the progress dialog, dismiss it, or display the
         * designated bitmap image via the ImageView.
         */
        public void handleMessage(Message msg) {

            switch (msg.what) {

            case SHOW_DIALOG:
                mProgressDialog =
                    ProgressDialog.show(mActivity.get(),
                                        "Download",
                                        "downloading via Runnables and Messages");
                break;

            case DISMISS_DIALOG:
                /**
                 * Dismiss the progress dialog.
                 */
                mProgressDialog.dismiss();
                break;

            case DISPLAY_IMAGE:
                /**
                 * Display the downloaded image to the user.
                 */
                displayImage((Bitmap) msg.obj);
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
         * Class constructor caches the url of a bitmap image and a handler.
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
            final Bitmap imageBitmap = downloadImage(mUrl);

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
                                         imageBitmap);
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

        String url = mUrlEditText.getText().toString();

        hideKeyboard();

        new DownloadTask().execute(url);
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
         * Show the progress dialog before starting the download in a
         * Background Thread.
         */
            mProgressDialog =
                ProgressDialog.show(ThreadedDownloads.this,
                                    "Download",
                                    "downloading via AsyncTask");
        }

        /**
         * Downloads bitmap in an AsyncTask background thread.
         * 
         * @param params
         *            The url of a bitmap image
         */
        protected Bitmap doInBackground(String... params) {
            return downloadImage(params[0]);
        }

        /**
         * Called after an operation executing in the background is
         * completed. It sets the bitmap image to an image view and
         * dismisses the progress dialog.
         * 
         * @param imageBitmap
         *            The bitmap image
         */
        protected void onPostExecute(Bitmap imageBitmap) {
            /**
             * Dismiss the progress dialog.
             */
            mProgressDialog.dismiss();

            /**
             * Display the downloaded image to the user.
             */
            displayImage(imageBitmap);
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
            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(mUrlEditText.getWindowToken(), 0);
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

}
