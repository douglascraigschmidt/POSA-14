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

import java.io.File;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.vuum.mocca.R;
import edu.vuum.mocca.storage.MoocResolver;
import edu.vuum.mocca.storage.StoryCreator;
import edu.vuum.mocca.storage.StoryData;

/**
 * This activity lets a user view a story in full screen mode.
 */
public class ViewStoryActivity extends StoryActivityBase {

	// A tag used for debugging with Logcat
	private static final String LOG_TAG = ViewStoryActivity.class	// Line 77
			.getCanonicalName();
	
	// The StoryData we're displaying
	StoryData storyData;

	// The UI elements we'll be using
	TextView loginIdTV;
	TextView storyIdTV;
	TextView titleTV;
	TextView bodyTV;
	TextView audioLinkTV;
	TextView videoLinkTV;
	TextView imageNameTV;

	TextView tagsTV;
	TextView creationTimeTV;
	TextView storyTimeTV;
	TextView latitudeTV;
	TextView longitudeTV;

	ImageView imageMetaDataView;

	// buttons for edit and delete
	Button editButton;
	Button deleteButton;

	// Helps us retrieve data from the database
	private MoocResolver resolver;
	
	// Used to pass around the row ID of stories
	public final static String rowIdentifyerTAG = "index";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set up the UI
		setContentView(R.layout.view_story_activity);
		
		// Create a resolver to help us retrieve data from the database
		resolver = new MoocResolver(this);	// Line 118
		
		// Get actual references to the instantiated UI objects
		loginIdTV = (TextView) findViewById(R.id.story_view_value_login_id);
		storyIdTV = (TextView) findViewById(R.id.story_view_value_story_id);
		titleTV = (TextView) findViewById(R.id.story_view_value_title);
		bodyTV = (TextView) findViewById(R.id.story_view_value_body);
		audioLinkTV = (TextView) findViewById(R.id.story_view_value_audio_link);
		videoLinkTV = (TextView) findViewById(R.id.story_view_value_video_link);
		imageNameTV = (TextView) findViewById(R.id.story_view_value_image_name);

		tagsTV = (TextView) findViewById(R.id.story_view_value_tags);
		creationTimeTV = (TextView) findViewById(R.id.story_view_value_creation_time);
		storyTimeTV = (TextView) findViewById(R.id.story_view_value_story_time);
		latitudeTV = (TextView) findViewById(R.id.story_view_value_latitude);
		longitudeTV = (TextView) findViewById(R.id.story_view_value_longitude);

		imageMetaDataView = (ImageView) findViewById(R.id.story_view_value_image_meta_data);

		// Set the default values
		loginIdTV.setText("" + 0);
		storyIdTV.setText("" + 0);
		titleTV.setText("" + "");
		bodyTV.setText("" + "");
		audioLinkTV.setText("" + "");
		videoLinkTV.setText("" + "");
		imageNameTV.setText("" + "");
		tagsTV.setText("" + "");

		latitudeTV.setText("" + 0);
		longitudeTV.setText("" + 0);

		editButton = (Button) findViewById(R.id.button_story_view_to_edit);
		deleteButton = (Button) findViewById(R.id.button_story_view_to_delete);

		try {
			// Fill out all the UI elements with data from our StoryData
			setUiToStoryData(getUniqueKey());
		} catch (RemoteException e) {
			Toast.makeText(this,
					"Error retrieving information from local data store.",
					Toast.LENGTH_LONG).show();
			Log.e(LOG_TAG, "Error getting Story data from C.P.");
			// e.printStackTrace();
		}
		
	}
	
	// Fills out the UI elements with data from a StoryData in the database specified by a unique key
	public void setUiToStoryData(long getUniqueKey) throws RemoteException {
		Log.d(LOG_TAG, "setUiToStoryData");	// Line 168
		
		// Get the StoryData from the database
		storyData = resolver.getStoryDataViaRowID(getUniqueKey);
		
		if (storyData != null) 
		{
			Log.d(LOG_TAG, "setUiToStoryData + storyData:" + storyData.toString());
			
			// Fill in the appropriate UI elements
			loginIdTV.setText(Long.valueOf(storyData.loginId).toString());
			storyIdTV.setText(Long.valueOf(storyData.storyId).toString());
			titleTV.setText(String.valueOf(storyData.title).toString());
			bodyTV.setText(String.valueOf(storyData.body).toString());
			audioLinkTV.setText(String.valueOf(storyData.audioLink).toString());
			videoLinkTV.setText(String.valueOf(storyData.videoLink).toString());
			imageNameTV.setText(String.valueOf(storyData.imageName).toString());
			tagsTV.setText(String.valueOf(storyData.tags).toString());
			creationTimeTV.setText(StoryCreator.getStringDate(storyData.creationTime));
			storyTimeTV.setText(StoryCreator.getStringDate(storyData.storyTime));

			latitudeTV.setText(Double.valueOf(storyData.latitude).toString());
			longitudeTV.setText(Double.valueOf(storyData.longitude).toString());

			Log.d(LOG_TAG, "image file path: " + storyData.imageLink);
			
			// Set up image
			imageNameTV.setVisibility(View.GONE);
			imageMetaDataView.setVisibility(View.GONE);

			if (storyData.imageLink != null
					&& storyData.imageLink.equals("") == false
					&& storyData.imageLink.equals("null") == false) {
				Log.d(LOG_TAG, "image link is valid" + storyData.imageLink);
				Uri uri = Uri.parse(storyData.imageLink);
				File image = new File(uri.getPath());
				
				if (image != null && image.exists()) {
					Log.d(LOG_TAG, "image link is valid");
					imageNameTV.setVisibility(View.VISIBLE);
					imageNameTV.setText("Image");

					Bitmap bmp = BitmapFactory.decodeFile(image
							.getAbsolutePath());
					imageMetaDataView.setVisibility(View.VISIBLE);
					imageMetaDataView.setImageBitmap(bmp);
				}
			}

		}
	}
	
	// Action to be performed when the edit button is pressed
	private void editButtonPressed() {
		openEditStoryActivity(storyData.KEY_ID);
	}

	// Action to be performed when the delete button is pressed
	private void deleteButtonPressed() {
		String message;

		message = getResources().getString(
				R.string.story_view_deletion_dialog_message);	// Line 230

		// Show a dialog confirming that the user wants to delete this story
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.story_view_deletion_dialog_title)
				.setMessage(message)
				.setPositiveButton(R.string.story_view_deletion_dialog_yes,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									resolver.deleteAllStoryWithRowID(storyData.KEY_ID);
								} catch (RemoteException e) {
									Log.e(LOG_TAG, "RemoteException Caught => "
											+ e.getMessage());
									e.printStackTrace();
								}
						
								finish();
								
							}

						})
				.setNegativeButton(R.string.story_view_deletion_dialog_no, null)
				.show();
	}

	// Get the unique key associated with the StoryData we're displaying
	public long getUniqueKey() {
		return getIntent().getLongExtra(rowIdentifyerTAG, 0);	// Line 261
	}
	
	// A clickListener that forwards clicks to the appropriate function
	public void clickListener(View view) {
		switch (view.getId()) {
		case R.id.button_story_view_to_delete:
			deleteButtonPressed();
			break;
		case R.id.button_story_view_to_edit:
			editButtonPressed();
			break;
		default:
			break;
		}
	}
}
