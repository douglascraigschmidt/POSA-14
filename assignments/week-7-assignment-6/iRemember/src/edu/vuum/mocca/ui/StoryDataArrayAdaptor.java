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

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import edu.vuum.mocca.R;
import edu.vuum.mocca.storage.StoryCreator;
import edu.vuum.mocca.storage.StoryData;

/**
 * This is an ArrayAdapter for an array of StoryData. It is used by the ListView
 * present in ListStoryActivity to display each StoryData object in the array as
 * a row in a GUI list.
 * 
 * This is an example of the Adapter pattern. In this case, this class wraps some plain
 * data (StoryData) into an interface that can be recognized and used by the ListView
 * Object.
 */
public class StoryDataArrayAdaptor extends ArrayAdapter<StoryData> {

	// A tag used for debugging with Logcat
	private static final String LOG_TAG = StoryDataArrayAdaptor.class
			.getCanonicalName();

	// The resource ID of a Layout used for instantiating Views
	int resource;

	/**
	 * Constructs this adapter
	 * @param _context	A context to create the Views in
	 * @param _resource	The resource ID of a TextView to use when instantiating Views
	 * @param _items	The actual objects we're representing
	 */
	public StoryDataArrayAdaptor(Context _context, int _resource,
			List<StoryData> _items) {
		super(_context, _resource, _items);
		Log.v(LOG_TAG, "constructor()");
		resource = _resource;
	}

	/**
	 * Called when a component using this adapter (like a ListView) needs to get a View
	 * that represents an object in the ArrayList.
	 * 
	 * @param position		The position in the ArrayList of the object we want a representation of
	 * @param convertView	A view that has already been instantiated but is destined to be garbage collected. 
	 * 							Use this for recycling Views and faster performance.
	 * @param parent		The ViewGroup that the returned view will be a child of.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Log.v(LOG_TAG, "getView()");
		
		// The view we need to fill out with data
		LinearLayout todoView = null;
		
		try {
			// Get the data from the ArrayList
			StoryData item = getItem(position);

			// The ID of the data in the database
			long KEY_ID = item.KEY_ID;
			// The title of the story
			String title = item.title;
			// The time of the story
			long storyDate = item.storyTime;

			// If there's no View to be recycled, instantiate a new View
			if (convertView == null) {
				todoView = new LinearLayout(getContext());
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater vi = (LayoutInflater) getContext()
						.getSystemService(inflater);
				vi.inflate(resource, todoView, true);
			} else {
				// Otherwise, use the View that's already been instantiated
				todoView = (LinearLayout) convertView;
			}

			// Get references to the children of the View we're going to return
			TextView KEY_IDTV = (TextView) todoView
					.findViewById(R.id.story_listview_custom_row_KEY_ID_textView);
			TextView tagsTV = (TextView) todoView
					.findViewById(R.id.story_listview_custom_row_tags_textView);
			TextView titleTV = (TextView) todoView
					.findViewById(R.id.story_listview_custom_row_title_textView);
			TextView creationTimeTV = (TextView) todoView
					.findViewById(R.id.story_listview_custom_row_creation_time_textView);

			// Set the appropriate values
			KEY_IDTV.setText("Key ID: " + KEY_ID);
			tagsTV.setText("" + item.tags.toString());
			titleTV.setText("" + title);
			creationTimeTV.setText("" + StoryCreator.getStringDate(storyDate));

		} catch (Exception e) {
			Toast.makeText(getContext(),
					"exception in ArrayAdpter: " + e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
		
		return todoView;
	}


}