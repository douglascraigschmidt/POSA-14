package edu.vuum.mocca;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import edu.vuum.mocca.R;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

/**
 * @class DownloadUtils
 *
 * @brief This class encapsulates several static methods so that all
 *        Services can access them without redefining them in each
 *        Service.
 */
public class DownloadUtils {
    /**
     * Used for debugging.
     */
    static final String TAG = "DownloadActivity";
    
    /**
    * If you have access to a stable Internet connection for testing
    * purposes, feel free to change this variable to false so it
    * actually downloads the image from a remote server.
    */
    // TODO - You can change this to the appropriate setting for your
    // environment.
    static final boolean DOWNLOAD_OFFLINE = true;
    
    /**
     * The resource that we write to the file system in offline
     * mode. Note that this must be the same image that the testing
     * project expects. (found in res/drawable-nodpi and Options.java)
     */
    static final int OFFLINE_TEST_IMAGE = R.raw.dougs;
	
    /**
     * The file name that we should use to store the image in offline mode
     */
    static final String OFFLINE_FILENAME = "dougs.jpg";
    
    /**
     * Download the file located at the provided internet url using
     * the URL class, store it on the android file system using
     * openFileOutput(), and return the path to the file on disk.
     *
     * @param context	the context in which to write the file
     * @param uri       the web url
     * 
     * @return          the path to the downloaded file on the file system
     */
    public static String downloadFile (Context context,
                                       Uri uri) {
    	
    	try {
    	
            // If we're offline, write the image in our resources to
            // disk, then return that pathname.
            if (DOWNLOAD_OFFLINE) {
	        	
                // Store the image on the file system. We can store it
                // as private since the test project runs in the same
                // process as the target project
                FileOutputStream out =
                    context.openFileOutput(OFFLINE_FILENAME, 0);
	        	
                // Get a stream from the image resource
                InputStream in =
                    context.getResources().openRawResource(OFFLINE_TEST_IMAGE);
	        	
                // Write the resource to disk.
                copy(in, out);
                in.close();
                out.close();
	        	
                return context.getFilesDir().toString() + File.separator + OFFLINE_FILENAME;
            }
    	
            // Otherwise, go ahead and download the file
            else {
                // Create a temp file.
                final File file = getTemporaryFile(context,
                                                   uri.toString());
                Log.d(TAG, "    downloading to " + file);
	
                // Download the contents at the URL, which should
                // reference an image.
                final InputStream in = (InputStream)
                    new URL(uri.toString()).getContent();
                final OutputStream os =
                    new FileOutputStream(file);
	
                // Copy the contents of the downloaded image to the
                // temp file.
                copy(in, os);
                in.close();
                os.close();
	
                // Return the pathname of the temp file.
                return file.getAbsolutePath();
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception while downloading. Returning null.");
            Log.e(TAG, e.toString());
            e.printStackTrace();
            return null;
        }
    }
        
    /**
     * Create a temp file to store the result of a download.
     * 
     * @param context
     * @param url
     * @return
     * @throws IOException
     */
    static private File getTemporaryFile(final Context context,
                                         final String url) throws IOException {
        
        // This is what you'd normally call to get a unique temporary
        // file, but for testing purposes we always name the file the
        // same to avoid filling up student phones with numerous
        // files!
        // return context.getFileStreamPath(Base64.encodeToString(url.getBytes(),
        //                                  Base64.NO_WRAP)
        //                                  + System.currentTimeMillis());

        return context.getFileStreamPath(Base64.encodeToString(url.getBytes(),
                                                               Base64.NO_WRAP));
    }

    /**
     * Copy the contents of an InputStream into an OutputStream.
     * 
     * @param in
     * @param out
     * @return
     * @throws IOException
     */
    static public int copy(final InputStream in,
                           final OutputStream out) throws IOException {
        final int BUFFER_LENGTH = 1024;
        final byte[] buffer = new byte[BUFFER_LENGTH];
        int totalRead = 0;
        int read = 0;

        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
            totalRead += read;			
        }

        return totalRead;
    }
}
