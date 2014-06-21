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

package edu.vuum.mocca.provider;

import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * A schema is defined as an overall method of organization for some set of data.
 * 
 * This class contains many class constants that help organize data storage
 * and database access.
 */
public class MoocSchema {

	/**
	 * Project Related Constants
	 */

	public static final String ORGANIZATIONAL_NAME = "edu.vanderbilt";
	public static final String PROJECT_NAME = "mooc";

	/**
	 * ConentProvider Related Constants
	 */
	public static final String AUTHORITY = ORGANIZATIONAL_NAME + "."
			+ PROJECT_NAME + ".moocprovider";
	private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

	// Define a static class that represents description of stored content
	// entity.
	public static class Story {
		// define a URI paths to access entity
		// BASE_URI/story - for list of story(s)
		// BASE_URI/story/* - retrieve specific story by id
		public static final String PATH = "story";
		public static final int PATH_TOKEN = 110;

		public static final String PATH_FOR_ID = "story/*";
		public static final int PATH_FOR_ID_TOKEN = 120;

		// URI for all content stored as story entity
		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();

		private final static String MIME_TYPE_END = "story";

		// define the MIME type of data in the content provider
		public static final String CONTENT_TYPE_DIR = ORGANIZATIONAL_NAME
				+ ".cursor.dir/" + ORGANIZATIONAL_NAME + "." + MIME_TYPE_END;
		public static final String CONTENT_ITEM_TYPE = ORGANIZATIONAL_NAME
				+ ".cursor.item/" + ORGANIZATIONAL_NAME + "." + MIME_TYPE_END;

		// the names and order of ALL columns, including internal use ones
		public static final String[] ALL_COLUMN_NAMES = { Cols.ID,
				Cols.LOGIN_ID, Cols.STORY_ID, Cols.TITLE, Cols.BODY,
				Cols.AUDIO_LINK, Cols.VIDEO_LINK, Cols.IMAGE_NAME,
				Cols.IMAGE_LINK, Cols.TAGS, Cols.CREATION_TIME,
				Cols.STORY_TIME, Cols.LATITUDE, Cols.LONGITUDE };

		// the names and order of ALL column types, including internal use ones (for use with SQLite)
		public static final String[] ALL_COLUMN_TYPES = { "integer",
				"integer", "integer", "text", "text",
				"text", "text", "text",
				"text", "text", "integer",
				"integer", "integer", "integer" };
	
		public static ContentValues initializeWithDefault(
				final ContentValues assignedValues) {
			// final Long now = Long.valueOf(System.currentTimeMillis());
			final ContentValues setValues = (assignedValues == null) ? new ContentValues()
					: assignedValues;
			if (!setValues.containsKey(Cols.LOGIN_ID)) {
				setValues.put(Cols.LOGIN_ID, 0);
			}
			if (!setValues.containsKey(Cols.STORY_ID)) {
				setValues.put(Cols.STORY_ID, 0);
			}
			if (!setValues.containsKey(Cols.TITLE)) {
				setValues.put(Cols.TITLE, "");
			}
			if (!setValues.containsKey(Cols.BODY)) {
				setValues.put(Cols.BODY, "");
			}
			if (!setValues.containsKey(Cols.AUDIO_LINK)) {
				setValues.put(Cols.AUDIO_LINK, "");
			}
			if (!setValues.containsKey(Cols.VIDEO_LINK)) {
				setValues.put(Cols.VIDEO_LINK, "");
			}
			if (!setValues.containsKey(Cols.IMAGE_NAME)) {
				setValues.put(Cols.IMAGE_NAME, "");
			}
			if (!setValues.containsKey(Cols.IMAGE_LINK)) {
				setValues.put(Cols.IMAGE_LINK, "");
			}
			if (!setValues.containsKey(Cols.TAGS)) {
				setValues.put(Cols.TAGS, "");
			}
			if (!setValues.containsKey(Cols.CREATION_TIME)) {
				setValues.put(Cols.CREATION_TIME, 0);
			}
			if (!setValues.containsKey(Cols.STORY_TIME)) {
				setValues.put(Cols.STORY_TIME, 0);
			}
			if (!setValues.containsKey(Cols.LATITUDE)) {
				setValues.put(Cols.LATITUDE, 0);
			}
			if (!setValues.containsKey(Cols.LONGITUDE)) {
				setValues.put(Cols.LONGITUDE, 0);
			}
			return setValues;
		}

		// a static class to store columns in entity
		public static class Cols {
			public static final String ID = BaseColumns._ID; // convention
			// The name and column index of each column in your database
			public static final String LOGIN_ID = "LOGIN_ID";
			public static final String STORY_ID = "STORY_ID";
			public static final String TITLE = "TITLE";
			public static final String BODY = "BODY";
			public static final String AUDIO_LINK = "AUDIO_LINK";
			public static final String VIDEO_LINK = "VIDEO_LINK";
			public static final String IMAGE_NAME = "IMAGE_NAME";
			public static final String IMAGE_LINK = "IMAGE_LINK";
			public static final String TAGS = "TAGS";
			public static final String CREATION_TIME = "CREATION_TIME";
			public static final String STORY_TIME = "STORY_TIME";
			public static final String LATITUDE = "LATITUDE";
			public static final String LONGITUDE = "LONGITUDE";
		}
	}

}
