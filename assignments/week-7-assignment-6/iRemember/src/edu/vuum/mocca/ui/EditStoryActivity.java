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
import edu.vuum.mocca.storage.StoryCreator;
import edu.vuum.mocca.storage.StoryData;

/**
 * This activity allows users to edit some parts of a previously posted story
 */
public class EditStoryActivity extends StoryActivityBase {
	
	// The tag used for debugging with Logcat
	final static public String LOG_TAG = EditStoryActivity.class // Line 72 
			.getCanonicalName();

	// variable for passing around row index
	final static public String rowIdentifyerTAG = "index";
	
	// The TextViews and EditTexts we use
	TextView loginIdET;
	TextView storyIdET;
	EditText titleET;
	EditText bodyET;
	EditText imageNameET;
	EditText imageMetaDataET;
	EditText audioLinkET;
	EditText tagsET;
	TextView creationTimeTV;
	EditText latitudeET;
	EditText longitudeET;
	
	DatePicker storyDate;
	

	// Button(s) used
	Button saveButton;
	Button resetButton;
	Button cancelButton;

	// custom ContentResolver wrapper.
	MoocResolver resolver;

	StoryData mData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Setup the UI
		setContentView(R.layout.edit_story_activity);
		
		// Start the MoocResolver to help us get/set data in the database
		resolver = new MoocResolver(this);
		
		// Get the Buttons
		saveButton = (Button) findViewById(R.id.story_edit_button_save);
		resetButton = (Button) findViewById(R.id.story_edit_button_reset);
		cancelButton = (Button) findViewById(R.id.story_edit_button_cancel);

		// Get the EditTexts
		loginIdET = (TextView) findViewById(R.id.story_edit_login_id);
		storyIdET = (TextView) findViewById(R.id.story_edit_story_id);
		titleET = (EditText) findViewById(R.id.story_edit_title);
		bodyET = (EditText) findViewById(R.id.story_edit_body);
		audioLinkET = (EditText) findViewById(R.id.story_edit_audio_link);
		imageNameET = (EditText) findViewById(R.id.story_edit_image_name);
		imageMetaDataET = (EditText) findViewById(R.id.story_edit_image_meta_data);
		tagsET = (EditText) findViewById(R.id.story_edit_tags);
		creationTimeTV = (TextView) findViewById(R.id.story_edit_creation_time);
		storyDate = (DatePicker) findViewById(R.id.story_edit_value_story_time_date_picker);
		latitudeET = (EditText) findViewById(R.id.story_edit_latitude);
		longitudeET = (EditText) findViewById(R.id.story_edit_longitude);
		
		// set the EditTexts to this Story's Values
		setValuesToDefault();  // Line 134

	}

	// The listener for the various buttons
	public void clickListener(View v) {
		switch (v.getId()) {
		case R.id.story_edit_button_save:
			doSaveButtonClick();	// Line 142
			break;
		case R.id.story_edit_button_reset:
			doResetButtonClick();
			break;
		case R.id.story_edit_button_cancel:
			doCancelButtonClick();
			break;
		default:
		}
	}
	
	public void doResetButtonClick() {
		setValuesToDefault();
	}

	// Update the provided values in the database
	public void doSaveButtonClick() {
		Toast.makeText(this, "Updated.", Toast.LENGTH_SHORT).show();
		
		// Make the story data from the UI
		StoryData story = makeStoryDataFromUI();
		
		// If we succeeded, go ahead and update the data in the database
		if (story != null) {
			try {
				resolver.updateStoryWithID(story);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		} else {
			return;
		}
		
		finish(); // same as hitting 'back' button
		
	}

	// Use the data the user input into the UI to construct a StoryData object
	public StoryData makeStoryDataFromUI() {

		// Get the editables from the UI
		Editable titleEditable = titleET.getText();
		Editable bodyEditable = bodyET.getText();
		Editable imageNameEditable = imageMetaDataET.getText();
		Editable tagsEditable = tagsET.getText();
		Editable latitudeEditable = latitudeET.getText();
		Editable longitudeEditable = longitudeET.getText();

		// Pull values from Editables
		String title = titleEditable.toString();
		String body = bodyEditable.toString();
		String imageName = imageNameEditable.toString();

		String tags = tagsEditable.toString();
		Double latitude = Double.valueOf(latitudeEditable.toString());		// Line 199
		Double longitude = Double.valueOf(longitudeEditable.toString());
		
		long storyTime = StoryCreator.componentTimeToTimestamp(storyDate.getYear(),
				storyDate.getMonth(), storyDate.getDayOfMonth(), 0, 0);

		// Construct the Story Data Object
		StoryData rValue = new StoryData(getUniqueKey(), mData.loginId,
				mData.storyId, title, body, mData.audioLink, mData.videoLink,
				imageName, mData.imageLink, tags, mData.creationTime,
				storyTime, latitude, longitude);

		// Make sure the new StoryData has the same key as the old one so that it will
		// replace the old one in the database.
		rValue.key = mData.key;		// Line 213

		// return StoryData object with new values
		return rValue;

	}

	public void doCancelButtonClick() {
		finish(); // same as hitting 'back' button
	}
	
	/**
	 * Sets all the UI elements to their original values
	 * @return
	 */
	public boolean setValuesToDefault() {

		StoryData storyData;
		try {
			storyData = resolver.getStoryDataViaRowID(getUniqueKey());
		} catch (RemoteException e) {
			Log.d(LOG_TAG, "" + e.getMessage());
			e.printStackTrace();
			return false;
		}

		if (storyData != null) {
			mData = storyData;
			Log.d(LOG_TAG, "setValuesToDefualt :" + storyData.toString());
			
			// set the EditTexts to the current values
			loginIdET.setText(Long.valueOf(storyData.loginId).toString());  
			storyIdET.setText(Long.valueOf(storyData.storyId).toString());
			titleET.setText(storyData.title);
			bodyET.setText(storyData.body);
			imageNameET.setText(storyData.imageName);
			imageMetaDataET.setText(storyData.imageLink);	// Line 249
			tagsET.setText(storyData.tags);
			creationTimeTV.setText(Long.valueOf(storyData.creationTime)
					.toString());
			latitudeET.setText(Double.valueOf(storyData.latitude).toString());
			longitudeET.setText(Double.valueOf(storyData.longitude).toString());
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the unique identifier of this StoryData in the database
	 */
	public long getUniqueKey() {
		return getIntent().getLongExtra(rowIdentifyerTAG, 0);
	}
}
