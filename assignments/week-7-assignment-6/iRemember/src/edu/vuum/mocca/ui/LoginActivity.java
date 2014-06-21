package edu.vuum.mocca.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import edu.vuum.mocca.R;
import edu.vuum.mocca.storage.StorageUtilities;

/**
 * The activity that allows the user to provide login information.
 */
public class LoginActivity extends StoryActivityBase{

	// A tag used for debugging with Logcat
	static final String LOG_TAG = LoginActivity.class.getCanonicalName();
	
	// The edit texts used
	EditText mLoginId;
	EditText mPassword;
	
	// Make sure we use maximum security to store login credentials
	static final int MAX_SECURITY = Integer.MAX_VALUE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Setup the UI
		setContentView(R.layout.login_activity);	// Line 36
		
		//Find the edit texts
		mLoginId = (EditText) findViewById(R.id.username);
		mPassword = (EditText) findViewById(R.id.password);
		
	}

	/**
	 * Get the file used for storing login credentials
	 */
	public static File getLoginFile (Context context) {
		return StorageUtilities.getOutputMediaFile(context, 	// Line 48
				StorageUtilities.MEDIA_TYPE_TEXT, 
				MAX_SECURITY, 
				"login.txt");
	}
	
	/**
	 * Returns the last LoginId input into this activity, or 0 if none is set.
	 */
	public static long getLoginId(Context context) {
		// Get the output file for the login information
		File loginFile = getLoginFile(context);		// Line 59
		
		String out = null;
		
		// If it already exists, read the login ID and return it
		if (loginFile != null && loginFile.exists()) {
			try {
				Scanner sc = new Scanner(loginFile);
				out = sc.nextLine();
				sc.close();
				return Long.parseLong(out);
			} catch (Exception e) {
				// This should never really happen
				Log.e(LOG_TAG, "Unable to get LoginID from file");	// Line 72
			}
		}

		return 0;
	}

	/**
	 * Returns the last password input into this activity, or null if one has not been set
	 */
	public static String getPassword(Context context) {
		// Get the output file for the login information
		File loginFile = getLoginFile(context);
		
		String out = null;
		
		// If it already exists, read the login information from the file and display it
		if (loginFile != null && loginFile.exists()) {
			try {
				Scanner sc = new Scanner(loginFile);	// Line 91
				sc.nextLine();
				out = sc.nextLine();
				sc.close();
				return out;
			} catch (Exception e) {
				// This should never really happen
				Log.e(LOG_TAG, "Unable to get password from file.");
			}
		}

		return out;
	}

	
	/**
	 * The login button was clicked.
	 */
	public void loginClicked(View v){
		// Save the input login information in a file so that the rest of the app can access it.
		File loginFile = getLoginFile(this);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(loginFile));	// Line 113
			writer.write(mLoginId.getText().toString());
			writer.newLine();
			writer.write(mPassword.getText().toString());
			writer.newLine();
			writer.close();
		} catch (Exception e) {
			Log.e(LOG_TAG, "Problem in loginClicked");
		}
		finally {
			openListStoryActivity();	// Line 123
		}
	}
}
