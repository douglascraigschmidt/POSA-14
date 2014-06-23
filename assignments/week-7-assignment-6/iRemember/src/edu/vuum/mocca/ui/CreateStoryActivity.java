/*
The iRemember source code (henceforth referred to as "iRemember") is
copyrighted by Mike Walker, Adam Porter, Doug Schmidt, and Jules White
at Vanderbilt University and the University of Maryland, Copyright (c)
2014, all rights reserved.  Since iRemember is open-source, freely
available software, you are free to use, modify, copy, and
distribute--perpetually and irrevocably--the source code and object code
produced from the source, as well as copy and distribute modified
versions of this software. You must, however, include this copyright
statement along with any code built using iRemember that you release. No
copyright statement needs to be provided if you just ship binary
executables of your software products.

You can use iRemember software in commercial and/or binary software
releases and are under no obligation to redistribute any of your source
code that is built using the software. Note, however, that you may not
misappropriate the iRemember code, such as copyrighting it yourself or
claiming authorship of the iRemember software code, in a way that will
prevent the software from being distributed freely using an open-source
development model. You needn't inform anyone that you're using iRemember
software in your software, though we encourage you to let us know so we
can promote your project in our success stories.

iRemember is provided as is with no warranties of any kind, including
the warranties of design, merchantability, and fitness for a particular
purpose, noninfringement, or arising from a course of dealing, usage or
trade practice.  Vanderbilt University and University of Maryland, their
employees, and students shall have no liability with respect to the
infringement of copyrights, trade secrets or any patents by DOC software
or any part thereof.  Moreover, in no event will Vanderbilt University,
University of Maryland, their employees, or students be liable for any
lost revenue or profits or other special, indirect and consequential
damages.

iRemember is provided with no support and without any obligation on the
part of Vanderbilt University and University of Maryland, their
employees, or students to assist in its use, correction, modification,
or enhancement.

The names Vanderbilt University and University of Maryland may not be
used to endorse or promote products or services derived from this source
without express written permission from Vanderbilt University or
University of Maryland. This license grants no permission to call
products or services derived from the iRemember source, nor does it
grant permission for the name Vanderbilt University or
University of Maryland to appear in their names.
 */

package edu.vuum.mocca.ui;

import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import edu.vuum.mocca.R;
import edu.vuum.mocca.storage.MoocResolver;
import edu.vuum.mocca.storage.StorageUtilities;
import edu.vuum.mocca.storage.StoryCreator;
import edu.vuum.mocca.storage.StoryData;

/**
 * The activity that allows a user to create and save a story.
 */
public class CreateStoryActivity extends StoryActivityBase {

	// Tag used for debugging with Logcat
	private final static String LOG_TAG = CreateStoryActivity.class
			.getCanonicalName();

	// Used as the request codes in startActivityForResult().
	static final int CAMERA_PIC_REQUEST = 1;
	static final int MIC_SOUND_REQUEST = 3;

	// The various UI elements we use
	TextView loginIdTV;
	EditText storyIdET;
	EditText titleET;
	EditText bodyET;
	Button audioCaptureButton;
	Button videoCaptureButton;
	EditText imageNameET;
	Button imageCaptureButton;
	EditText tagsET;
	EditText creationTimeET;
	EditText storyTimeET;
	Button locationButton;

	TextView imageLocation;
	TextView videoLocation;
	TextView audioLocation;

	Button buttonCreate;
	Button buttonClear;
	Button buttonCancel;

	TextView latitudeValue;
	TextView longitudeValue;

	DatePicker storyDate;

	static Uri imagePath;	// Making this static keeps it from getting GC'd when we take pictures
	Uri fileUri;
	String audioPath;
	Location loc;

	MoocResolver resolver;

	Uri imagePathFinal = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Setup the UI
		setContentView(R.layout.create_story_activity);
		
		// Start a resolver to help us store/retrieve data from a database
		resolver = new MoocResolver(this);
		
		// Get references to all the UI elements
		loginIdTV = (TextView) findViewById(R.id.story_create_value_login_id);
		storyIdET = (EditText) findViewById(R.id.story_create_value_story_id);
		titleET = (EditText) findViewById(R.id.story_create_value_title);
		bodyET = (EditText) findViewById(R.id.story_create_value_body);
		audioCaptureButton = (Button) findViewById(R.id.story_create_value_audio_link);
		videoCaptureButton = (Button) findViewById(R.id.story_create_value_video_button);
		imageNameET = (EditText) findViewById(R.id.story_create_value_image_name);
		imageCaptureButton = (Button) findViewById(R.id.story_create_value_image_button);
		tagsET = (EditText) findViewById(R.id.story_create_value_tags);
		creationTimeET = (EditText) findViewById(R.id.story_create_value_creation_time);
		locationButton = (Button) findViewById(R.id.story_create_value_location_button);

		imageLocation = (TextView) findViewById(R.id.story_create_value_image_location);
		videoLocation = (TextView) findViewById(R.id.story_create_value_video_location);
		audioLocation = (TextView) findViewById(R.id.story_create_value_audio_location);

		latitudeValue = (TextView) findViewById(R.id.story_create_value_latitude);
		longitudeValue = (TextView) findViewById(R.id.story_create_value_longitude);

		buttonClear = (Button) findViewById(R.id.story_create_button_reset);
		buttonCancel = (Button) findViewById(R.id.story_create_button_cancel);
		buttonCreate = (Button) findViewById(R.id.story_create_button_save);

		storyDate = (DatePicker) findViewById(R.id.story_create_value_story_time_date_picker);
		
		//Set the login ID, if it's been set
		loginIdTV.setText(String.valueOf(LoginActivity.getLoginId(this)));

	}

	// Reset all the fields to their default values
	public void buttonClearClicked (View v) {
		storyIdET.setText("" + 0);
		titleET.setText("" + "");
		bodyET.setText("" + "");
		imageNameET.setText("" + "");
		tagsET.setText("" + "");
		creationTimeET.setText("" + 0);
	}
	
	// Close this activity if the cancel button is clicked
	public void buttonCancelClicked (View v) {
		finish(); // same as hitting 'back' button
	}
	
	// Create a StoryData object from the input data and store it using the resolver
	public void buttonCreateClicked (View v) {
		Log.d(LOG_TAG, "create button pressed, creation time="
				+ creationTimeET.getText());

		// local Editables
		Editable titleCreateable = titleET.getText();
		Editable bodyCreateable = bodyET.getText();
		Editable imageNameCreateable = imageNameET.getText();
		Editable tagsCreateable = tagsET.getText();
		Editable creationTimeCreateable = creationTimeET.getText();
		
		Calendar cal = Calendar.getInstance();
		cal.getTimeInMillis();

		long loginId = 0;
		long storyId = 0;
		String title = "";
		String body = "";
		String audioLink = "";
		String videoLink = "";
		String imageName = "";
		String imageData = "";
		String tags = "";
		long creationTime = 0;
		long storyTime = 0;
		double latitude = 0;
		double longitude = 0;

		// pull values from Editables
		loginId = LoginActivity.getLoginId(this);
		storyId = 1; // TODO Pull this from somewhere. 
		title = String.valueOf(titleCreateable.toString());
		body = String.valueOf(bodyCreateable.toString());
		if (audioPath != null) {
			audioLink = audioPath;
		}
		if (fileUri != null) {
			videoLink = fileUri.toString();
		}
		imageName = String.valueOf(imageNameCreateable.toString());
		if (imagePathFinal != null) {
			imageData = imagePathFinal.toString();
		}
		tags = String.valueOf(tagsCreateable.toString());
		if (loc != null) {
			latitude = loc.getLatitude();
			longitude = loc.getLongitude();
		}
		Log.d(LOG_TAG, "creation time object:" + creationTimeCreateable);
		Log.d(LOG_TAG, "creation time as string"
				+ creationTimeCreateable.toString());
		Calendar cal2 = Calendar.getInstance(Locale.ENGLISH);
		
		creationTime = cal2.getTimeInMillis();
				
		storyTime = StoryCreator.componentTimeToTimestamp(storyDate.getYear(),
				storyDate.getMonth(), storyDate.getDayOfMonth(), 0, 0);

		// new StoryData object with above info
		StoryData newData = new StoryData(
				-1,
				// -1 row index, because there is no way to know which
				// row it will go into
				loginId, storyId, title, body, audioLink, videoLink,
				imageName, imageData, tags, creationTime, storyTime,
				latitude, longitude);
		Log.d(LOG_TAG, "imageName"
				+ imageNameET.getText());

		Log.d(LOG_TAG,
				"newStoryData:" + newData);

		// insert it through Resolver to be put into the database
		try {
			resolver.insert(newData);
		} catch (RemoteException e) {
			Log.e(LOG_TAG,
					"Caught RemoteException => " + e.getMessage());
			e.printStackTrace();
		}
		
		finish(); // same as hitting 'back' button
		
	}
	
	
	/**
	 * Method to be called when Audio Clicked button is pressed.
	 */
	public void addAudioClicked(View aView) {
		Log.v(LOG_TAG, "addAudioClicked(View) called.");
		
		//Create an intent to start the SoundRecordActivity
		Intent soundIntent = new Intent(this, SoundRecordActivity.class);	// Line 275
		
		// Tell the sound activity where to store the recorded audio.
		String fileName = StorageUtilities.getOutputMediaFile(this, // Line 278
				StorageUtilities.MEDIA_TYPE_AUDIO, 
				StorageUtilities.SECURITY_PUBLIC,
				null).getAbsolutePath();
        
		if (fileName == null)
			return;
		
        soundIntent.putExtra("FILENAME", fileName);	
        
        // Start the SoundRecordActivity
		startActivityForResult(soundIntent, MIC_SOUND_REQUEST);
	
	}

	/**
	 * Method to be called when the Add Photo button is pressed.
	 */
	public void addPhotoClicked(View aView) {
		Log.v(LOG_TAG, "addPhotoClicked(View) called.");	// Line 297
		
		// Create an intent that asks for an image to be captured
		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		
		// Tell the capturing activity where to store the image
		Uri uriPath = StorageUtilities.getOutputMediaFileUri(this, // Line 304
				StorageUtilities.MEDIA_TYPE_IMAGE, 
				StorageUtilities.SECURITY_PUBLIC,
				null);
		
		if (uriPath == null)
			return;
		
		imagePath = uriPath;
		cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
				imagePath);

		// Start the activity
		startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST); // Line 317
	}

	/**
	 * Method to be called when the user clicks the "Get Location" button
	 * @param aView
	 */
	public void getLocationClicked(View aView) {
		
		// Acquire a reference to the system Location Manager
		final LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location
				// provider.

				Toast.makeText(getApplicationContext(),
						"New Location obtained.", Toast.LENGTH_LONG).show(); // Line 337
				setLocation(location);
				locationManager.removeUpdates(this);

			}

			// We must define these to implement the interface, but we don't do anything when they're triggered.
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}
			public void onProviderEnabled(String provider) {
			}
			public void onProviderDisabled(String provider) {
			}
		};

		// Register the listener with the Location Manager to receive location
		// updates
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Log.d(LOG_TAG, "locationManager.isProviderEnabled = true/gps");
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, locationListener);
			Location location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				setLocation(location);
			} else {
				Toast.makeText(getApplicationContext(),
						"GPS has yet to calculate location.", Toast.LENGTH_LONG)
						.show();
			}

		} else {
			Toast.makeText(getApplicationContext(), "GPS is not enabled.",
					Toast.LENGTH_LONG).show();
		}
		
	}

	/**
	 * Update the UI with a new location.
	 * @param location
	 */
	public void setLocation(Location location) {
		
		Log.d(LOG_TAG, "setLocation =" + location);		// Line 382
		
		loc = location;
		double latitude = loc.getLatitude();
		double longitude = loc.getLongitude();

		latitudeValue.setText("" + latitude);
		longitudeValue.setText("" + longitude);
	}

	/**
	 * This is called when an activity that we've started returns a result.
	 * 
	 * In our case, we're looking for the results returned from the SoundRecordActivity
	 * or the Camera Activity
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		Log.d(LOG_TAG, "CreateFragment onActivtyResult called. requestCode: "
				+ requestCode + " resultCode:" + resultCode + "data:" + data);
		
		if (requestCode == CreateStoryActivity.CAMERA_PIC_REQUEST) {
			if (resultCode == CreateStoryActivity.RESULT_OK) {
				// Image captured and saved to fileUri specified in the Intent
				imagePathFinal = imagePath;
				imageLocation.setText(imagePathFinal.toString());
			} else if (resultCode == CreateStoryActivity.RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
				Toast.makeText(getApplicationContext(),
						"Image capture failed.", Toast.LENGTH_LONG)
						.show();
			}
		} 
		else if (requestCode == CreateStoryActivity.MIC_SOUND_REQUEST) {
			// If we successfully recorded sound, grab the results.
			if (resultCode == SoundRecordActivity.RESULT_OK) {
				audioPath = (String) data.getExtras().get("data");  // Line 421
				audioLocation.setText(audioPath.toString());
			}
			// If not, let the user know.
			else {
				Toast.makeText(this, "Sound capture failed.", Toast.LENGTH_LONG).show();
			}
		}
	}
}
