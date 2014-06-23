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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

/**
 * Base class for all StoryData UI activities.
 * 
 * This class provides convenience functions for switching between the various activities
 * in the application.
 * 
 */
@SuppressLint("Registered")
public class StoryActivityBase extends Activity {

	// A tag used for debugging with Logcat
	private static final String LOG_TAG = StoryActivityBase.class
			.getCanonicalName();

	/**
	 * Open the LoginActivity
	 */
	public void openLoginActivity() {
		Intent intent = newLoginActivityIntent(this); // Line 74
		startActivity(intent);
	}
	
	/**
	 *  Open the ViewStoryActivity
	 * @param index		The index in the database of the StoryData to display
	 */
	public void openViewStoryActivity(long index) {
		Log.d(LOG_TAG, "openStoryViewActivity(" + index + ")");
		Intent intent = newStoryViewIntent(this, index);
		startActivity(intent);
	}

	/**
	 *  Open the EditStoryActivity
	 * @param index		The index in the database of the StoryData to edit
	 */
	public void openEditStoryActivity(final long index) {
		Log.d(LOG_TAG, "openEditStoryActivity(" + index + ")");
		Intent intent = newEditStoryIntent(this, index);
		startActivity(intent);
	}

	/**
	 * Open the CreateStoryActivity
	 */
	public void openCreateStoryActivity() {
		Log.d(LOG_TAG, "openCreateStoryActivity");
		Intent intent = newCreateStoryIntent(this);
		startActivity(intent);
	}

	/**
	 * Open the ListStoryActivity
	 */
	public void openListStoryActivity() {
		Log.d(LOG_TAG, "openCreateStoryActivity");
		Intent intent = newListStoryIntent(this);
		startActivity(intent);
	}

	/*************************************************************************/
	/*
	 * Create Intents for the various Activities
	 */
	/*************************************************************************/

	public static Intent newLoginActivityIntent(Activity activity) {
		return new Intent(activity, LoginActivity.class);
	}
	
	public static Intent newStoryViewIntent(Activity activity, long index) {
		Intent intent = new Intent();
		intent.setClass(activity, ViewStoryActivity.class);
		intent.putExtra(ViewStoryActivity.rowIdentifyerTAG, index);	// Line 129
		return intent;
	}

	public static Intent newEditStoryIntent(Activity activity, long index) {
		Intent intent = new Intent();
		intent.setClass(activity, EditStoryActivity.class);
		intent.putExtra(EditStoryActivity.rowIdentifyerTAG, index);
		return intent;
	}

	public static Intent newListStoryIntent(Activity activity) {
		Intent intent = new Intent();
		intent.setClass(activity, ListStoryActivity.class); // Line 142
		return intent;
	}

	public static Intent newCreateStoryIntent(Activity activity) {
		Intent intent = new Intent();
		intent.setClass(activity, CreateStoryActivity.class);
		return intent;
	}
}
