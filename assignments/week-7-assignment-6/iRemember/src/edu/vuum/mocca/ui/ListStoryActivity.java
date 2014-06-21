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

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import edu.vuum.mocca.R;
import edu.vuum.mocca.provider.MoocSchema;
import edu.vuum.mocca.storage.MoocResolver;
import edu.vuum.mocca.storage.StoryData;

/**
 * This activity lists all the stories currently stored in the database
 */
public class ListStoryActivity extends StoryActivityBase {
	
	// A tag used for debugging with Logcat
    private static final String LOG_TAG = ListStoryActivity.class
            .getCanonicalName();
    
    // A resolver that helps us store/retrieve data from the database
	MoocResolver resolver;
	
	// Used as a native container for the stories we retrieve from the database
	ArrayList<StoryData> mStoryData;
	
	// An adapter that lets the ListView correctly display the data in our ArrayList. 
	private StoryDataArrayAdaptor aa;

	// The EditText used to filter the stories listed based on the tags they have
	EditText filterET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        
        // Set up the UI
        setContentView(R.layout.list_story_activity);	// Line 92
        
        // Instantiate the resolver and the ArrayList
        resolver = new MoocResolver(this);
		mStoryData = new ArrayList<StoryData>();
		
		// Get the ListView that will be displayed
		ListView lv = (ListView) findViewById(android.R.id.list);

		filterET = (EditText) findViewById(R.id.story_listview_tags_filter);

		// customize the ListView in whatever desired ways.
		lv.setBackgroundColor(Color.GRAY);	// Line 104
		
		// Instantiate the adapter using our local StoryData ArrayList.
		aa = new StoryDataArrayAdaptor(this,
				R.layout.story_listview_custom_row, mStoryData);	// Line 108

		// Update our StoryData ArrayList with data from the database
		updateStoryData();

		// Tell the ListView which adapter to use to display the data.
		lv.setAdapter(aa);
		
		// Set the click listener for the list view
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View v, int position, long id){
		    	Log.d(LOG_TAG, "onListItemClick");
				Log.d(LOG_TAG,
						"position: " + position + "id = "
								+ (mStoryData.get(position)).KEY_ID);
				
				// When an item is clicked, open the ViewStoryActivity so the user can view it in full screen
				openViewStoryActivity((mStoryData.get(position)).KEY_ID);
		    }
		});
    }

    // When the refresh button is clicked, update our local StoryData ArrayList with data from the database
    public void refreshButtonClicked (View v) {
    	updateStoryData();
    }
    
    // When the Create button is clicked, open the CreateStoryActivity
    public void createButtonClicked(View v) {
    	openCreateStoryActivity();
    }
    
    // When the Login button is clicked, open the LoginActivity
    public void loginButtonClicked(View v) {
    	openLoginActivity();	// Line 142
    }
    
    // Update mStoryData with the data currently in the database using MoocResolver
    public void updateStoryData() {
		Log.d(LOG_TAG, "updateStoryData");
		try {
			// Clear our local cache of StoryData
			mStoryData.clear();

			String filterWord = filterET.getText().toString();	// Line 152

			// create String that will match with 'like' in query
			filterWord = "%" + filterWord + "%";

			// Get all the StoryData in the database
			ArrayList<StoryData> currentList2 = resolver.queryStoryData(null,
					MoocSchema.Story.Cols.TAGS + " LIKE ? ",
					new String[] { filterWord }, null);

			// Add all of them to our local ArrayList
			mStoryData.addAll(currentList2);		// Line 163
			
			// Let the ArrayAdaptor know that we changed the data in its array.
			aa.notifyDataSetChanged();
		} catch (Exception e) {
			Log.e(LOG_TAG,
					"Error connecting to Content Provider" + e.getMessage());
			e.printStackTrace();
		}
	}
}