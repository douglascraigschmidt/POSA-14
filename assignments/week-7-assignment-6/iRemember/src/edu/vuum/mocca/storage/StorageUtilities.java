package edu.vuum.mocca.storage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/**
 * This utility class provides several options for storing temporary and permanent files on
 * the file system with varying degrees of security.
 */
public class StorageUtilities {

	// Log tag used for debugging with Logcat
	public static final String LOG_TAG = StorageUtilities.class.getCanonicalName();
	
	// Constant that denote whether a file should be stored publicly or privately
	public static final int SECURITY_PUBLIC = 0; // Line 24
	public static final int SECURITY_PRIVATE = 1;
	
	// Constant that denotes what media type a file should be stored as.
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_AUDIO = 2;
	public static final int MEDIA_TYPE_TEXT = 3;


	/**
	 * Creates an output file to store some kind of media (images, audio, text).
	 * 
	 * The directory the file is created in depends both on the media type and the security level.
	 * If the security is private, we store it in app-specific private memory. If it is public, we
	 * store the file on external storage. Android has different directories in external storage for
	 * each media type, so we choose the directory depending on the media type parameter.
	 * 
	 * If the provided filename is null, we generate a filename based on the current time and media type.
	 * 
	 * @param context	The context of the calling component
	 * @param type		The media type that's being stored (determines file location and name
	 * 						if not specified)
	 * @param security	How securely we should store the temporary files. 
	 * 						We can store it on the SD card or in private app memory.
	 * @param name		The name of the file to be created. If null, we generate a name based on
	 * 						the current time and media type.
	 * @return			A File reference to a newly created temporary file
	 */
	public static File getOutputMediaFile(Context context, int type, int security, String name) {
		Log.d(LOG_TAG, "getOutputMediaFile() type:" + type);
		
		// Get the current time stamp
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US) // Line 56
		.format(new Date());

		// The directory where we'll store the file
		File storageDir = null;		// Line 59
		
		// The name of the file we'll return
		File outputFile = null;
		
		// Make sure external storage is mounted.
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Toast.makeText(context, "External storage not mounted. Can't write/read file.", Toast.LENGTH_LONG).show();
			return null;
		}
		
		// If security is private, store it in the app's private directory.
		if (security == SECURITY_PRIVATE) {
			storageDir = context.getFilesDir();
		}
		// Otherwise, store the file in a public directory depending on its media type.
		else {	// Line 76
			switch (type) {
				case MEDIA_TYPE_IMAGE:
					storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
					break;
				case MEDIA_TYPE_AUDIO:
					storageDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
					break;
				case MEDIA_TYPE_TEXT:
					storageDir = context.getExternalFilesDir(null);
					break;
			}
		}
		
		// If a name was specified, use that filename.
		if (name != null && storageDir != null) {
			outputFile = new File(storageDir.getPath() + File.separator + name);	// Line 92
		}
		// Otherwise, determine filename based on media type.
		else if (storageDir != null){
			switch (type) {
			case MEDIA_TYPE_IMAGE:
				outputFile = new File(storageDir.getPath() + File.separator
							+ "IMG_" + timeStamp);
				break;
			case MEDIA_TYPE_AUDIO:
				outputFile = new File(storageDir.getPath() + File.separator +
						"SND_" + timeStamp);
				break;
			case MEDIA_TYPE_TEXT:
				outputFile = new File(storageDir.getPath() + File.separator
						+ "TXT_" + timeStamp);
				break;
			}
		}
		
		return outputFile;
	}

	/**
	 * A convenience function for getting a URI to an output file instead of a File reference.
	 * 
	 * @param context	The context of the calling component
	 * @param type		The media type that's being stored (determines file name and location)
	 * @param security	How securely we should store the temporary files. 
	 * 						We can store it on the SD card or in private app memory.
	 * @param name		The name of the file to be created (optional)
	 * @return			A Uri to a newly created temporary file
	 */
	public static Uri getOutputMediaFileUri(Context context, int type, int security, String name){
		File outFile = getOutputMediaFile(context, type, security, name);
		if (outFile != null)
			return Uri.fromFile(outFile);	// Line 128
		
		return null;
	}

	
}
