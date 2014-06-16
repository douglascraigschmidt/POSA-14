package edu.vuum.mocca.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import edu.vuum.mocca.DownloadUtils;

/**
 * @class Utilities
 *
 * @brief A few checks are repeated several times, such as checking
 *        whether a Messenger is present in an Intent, or if the proper
 *        Uri is set.
 */
public class Utilities {
    /**
     * Check that the proper Uri is set for the provided Intent.
     */
    static boolean checkUri(Intent intent) {
        return Options.TEST_URI.equals(intent.getData().toString());
    }
	
    /**
     * Check that the provided Intent contains a Messenger extra.
     */
    static boolean checkMessenger(Intent intent) {
        Bundle extras = intent.getExtras();
		
        // We don't know what tag they'll use for the Messenger, so we
        // have to search for it.
        Messenger messenger = null;

        for (String key : extras.keySet()) {
            if (extras.get(key) instanceof Messenger) 
                messenger = (Messenger) extras.get(key);
        }
		
        return (messenger != null);
    }

    /**
     * Check that the provided Intent will start the expected class.
     */
    static boolean checkClass(Intent intent, Class<?> class_) {
        return intent.getComponent().getClassName().equals(class_.getName());
    }
	
    
    /**
     * Check if the downloaded Bitmap is equivalent to the expected
     * bitmap.
     */
    static boolean checkDownloadedImage(Context test_context, String result) {
        Bitmap downloaded = BitmapFactory.decodeFile(result);
		
        Bitmap expected = BitmapFactory.decodeResource(test_context.getResources(),
                                                       Options.TEST_IMAGE);
        return downloaded.sameAs(expected);
    }
    
    /**
     * Search for the pathname that should be bundled in the provided
     * Message. (Returns the last String extra found).
     */
    static String searchForPath (Message msg) {
        Bundle msg_extras = msg.getData();
        
        String path = null;
        for (String key : msg_extras.keySet()) {
            if (msg_extras.get(key) instanceof String) 
                path = (String) msg_extras.get(key);
        }
        
        return path;
    }
    
}
