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

package edu.vuum.mocca.storage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.format.DateFormat;
import edu.vuum.mocca.provider.MoocSchema;

/**
 * StoryCreator is a helper class that does convenience functions for converting
 * between the Custom ORM objects (such as StoryData), ContentValues, and Cursors.
 * 
 * @author Michael A. Walker
 * 
 */
public class StoryCreator {

	/**
	 * Create a ContentValues from a provided StoryData.
	 * 
	 * @param data
	 *            StoryData to be converted.
	 * @return ContentValues that is created from the StoryData object
	 */
	public static ContentValues getCVfromStory(final StoryData data) {
		ContentValues rValue = new ContentValues();
		rValue.put(MoocSchema.Story.Cols.LOGIN_ID, data.loginId);
		rValue.put(MoocSchema.Story.Cols.STORY_ID, data.storyId);
		rValue.put(MoocSchema.Story.Cols.TITLE, data.title);
		rValue.put(MoocSchema.Story.Cols.BODY, data.body);
		rValue.put(MoocSchema.Story.Cols.AUDIO_LINK, data.audioLink);
		rValue.put(MoocSchema.Story.Cols.VIDEO_LINK, data.videoLink);
		rValue.put(MoocSchema.Story.Cols.IMAGE_NAME, data.imageName);
		rValue.put(MoocSchema.Story.Cols.IMAGE_LINK, data.imageLink);
		rValue.put(MoocSchema.Story.Cols.TAGS, data.tags);
		rValue.put(MoocSchema.Story.Cols.CREATION_TIME, data.creationTime);
		rValue.put(MoocSchema.Story.Cols.STORY_TIME, data.storyTime);
		rValue.put(MoocSchema.Story.Cols.LATITUDE, data.latitude);
		rValue.put(MoocSchema.Story.Cols.LONGITUDE, data.longitude);
		return rValue;
	}

	/**
	 * Get all of the StoryData from the passed in cursor.
	 * 
	 * @param cursor
	 *            passed in cursor to get StoryData(s) of.
	 * @return ArrayList<StoryData\> The set of StoryData
	 */
	public static ArrayList<StoryData> getStoryDataArrayListFromCursor(
			Cursor cursor) {
		ArrayList<StoryData> rValue = new ArrayList<StoryData>();
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					rValue.add(getStoryDataFromCursor(cursor));
				} while (cursor.moveToNext() == true);
			}
		}
		return rValue;
	}

	/**
	 * Get the first StoryData from the passed in cursor.
	 * 
	 * @param cursor
	 *            passed in cursor
	 * @return StoryData object
	 */
	public static StoryData getStoryDataFromCursor(Cursor cursor) {

		long rowID = cursor.getLong(cursor
				.getColumnIndex(MoocSchema.Story.Cols.ID));
		long loginId = cursor.getLong(cursor
				.getColumnIndex(MoocSchema.Story.Cols.LOGIN_ID));
		long storyId = cursor.getLong(cursor
				.getColumnIndex(MoocSchema.Story.Cols.STORY_ID));
		String title = cursor.getString(cursor
				.getColumnIndex(MoocSchema.Story.Cols.TITLE));
		String body = cursor.getString(cursor
				.getColumnIndex(MoocSchema.Story.Cols.BODY));
		String audioLink = cursor.getString(cursor
				.getColumnIndex(MoocSchema.Story.Cols.AUDIO_LINK));
		String videoLink = cursor.getString(cursor
				.getColumnIndex(MoocSchema.Story.Cols.VIDEO_LINK));
		String imageName = cursor.getString(cursor
				.getColumnIndex(MoocSchema.Story.Cols.IMAGE_NAME));
		String imageMetaData = cursor.getString(cursor
				.getColumnIndex(MoocSchema.Story.Cols.IMAGE_LINK));
		String tags = cursor.getString(cursor
				.getColumnIndex(MoocSchema.Story.Cols.TAGS));
		long creationTime = cursor.getLong(cursor
				.getColumnIndex(MoocSchema.Story.Cols.CREATION_TIME));
		long storyTime = cursor.getLong(cursor
				.getColumnIndex(MoocSchema.Story.Cols.STORY_TIME));
		double latitude = cursor.getDouble(cursor
				.getColumnIndex(MoocSchema.Story.Cols.LATITUDE));
		double longitude = cursor.getDouble(cursor
				.getColumnIndex(MoocSchema.Story.Cols.LONGITUDE));

		// construct the returned object
		StoryData rValue = new StoryData(rowID, loginId, storyId, title, body,
				audioLink, videoLink, imageName, imageMetaData, tags,
				creationTime, storyTime, latitude, longitude);

		return rValue;
	}
	
	/**
	 * A convenience function for converting a date expressed in minutes, days, and hours into
	 * a millisecond time stamp.
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static long componentTimeToTimestamp(int year, int month, int day, int hour,
			int minute) {

		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c.getTimeInMillis();
	}
	
	/**
	 * A convenience function for converting a millisecond time stamp into a String format
	 * @param timestamp
	 * @return
	 */
	public static String getStringDate(long timestamp) {
		Calendar cal = Calendar.getInstance(Locale.ENGLISH);
		cal.setTimeInMillis(timestamp);
		String date = DateFormat.format("dd-MM-yyyy", cal).toString();
		return date;
	}
}
