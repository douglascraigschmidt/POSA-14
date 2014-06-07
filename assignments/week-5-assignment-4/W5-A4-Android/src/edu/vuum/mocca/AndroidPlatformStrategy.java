package edu.vuum.mocca;

import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;

import android.app.Activity;
import android.widget.TextView;
import android.util.Log;

/**
 * @class AndroidPlatformStrategy
 * 
 * @brief Provides methods that define a platform-independent API for
 *        output data to Android UI thread and synchronizing on thread
 *        completion in the ping/pong game.  It plays the role of the
 *        "Concrete Strategy" in the Strategy pattern.
 */
public class AndroidPlatformStrategy extends PlatformStrategy
{	
    /** TextViewVariable. */
    private TextView mTextViewOutput;
	
    /** Activity variable finds gui widgets by view. */
    private WeakReference<Activity> mActivity;

    public AndroidPlatformStrategy(Object output,
                                   final Object activityParam)
    {
        /**
         * A textview output which displays calculations and
         * expression trees.
         */
        mTextViewOutput = (TextView) output;

        /** The current activity window (succinct or verbose). */
        mActivity = new WeakReference<Activity>((Activity) activityParam);
    }

    /**
     * Latch to decrement each time a thread exits to control when the
     * play() method returns.
     */
    private static CountDownLatch mLatch = null;

    /** Do any initialization needed to start a new game. */
    public void begin()
    {
        /** Reset the CountDownLatch. */
        // TODO - You fill in here.
    }

    /** Print the outputString to the display. */
    public void print(final String outputString)
    {
        /** 
         * Create a Runnable that's posted to the UI looper thread
         * and appends the outputString to a TextView. 
         */
        // TODO - You fill in here.
    }

    /** Indicate that a game thread has finished running. */
    public void done()
    {	
        // TODO - You fill in here.
    }

    /** Barrier that waits for all the game threads to finish. */
    public void awaitDone()
    {
        // TODO - You fill in here.
    }

    /** Returns the platform name in a String. */
    public String platformName() 
    {
        return System.getProperty("java.specification.vendor");
    }

    /** 
     * Error log formats the message and displays it for the
     * debugging purposes.
     */
    public void errorLog(String javaFile, String errorMessage) 
    {
       Log.e(javaFile, errorMessage);
    }
}
