package edu.vuum.mocca.test;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;

/**
 * @class Utilities
 *
 * @brief A few checks are repeated several times, such as checking
 *        whether the correct class is set for an Intent, or if the proper
 *        Uri is present in getData().
 */
public class Utilities {
    // Check that the proper Uri is set for the provided Intent
    static boolean checkUri(Intent intent) {
        return Options.TEST_URI.equals(intent.getData().toString());
    }
	
    // Check that the provided Intent will start the expected class.	
    static boolean checkClass(Intent intent, Class<?> class_) {
        return intent.getComponent().getClassName().equals(class_.getName());
    }
	
    
    /**
     * Check if the downloaded Bitmap is equivalent to the expected
     * bitmap.
     * @throws IOException 
     */
    static boolean checkDownloadedImage(Context test_context, String result) throws IOException {
        Bitmap downloaded = BitmapFactory.decodeFile(result);
		
        Bitmap expected = getTestBitmap(test_context);
        return downloaded.sameAs(expected);
    }
    
    /**
     * Get the Bitmap that we're expecting to be downloaded from the assets folder.
     * 
     * @param  context		The context of THIS (the test) project
     * @throws IOException 
     */
    public static Bitmap getTestBitmap(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        istr = assetManager.open(Options.TEST_IMAGE);
        bitmap = BitmapFactory.decodeStream(istr);
   

        return bitmap;
    }

    // Search for the pathname that should be bundled in the provided Message. (Returns the last String extra found)
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
