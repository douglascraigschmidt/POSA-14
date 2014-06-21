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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.RemoteException;
import android.util.Log;
import edu.vuum.mocca.provider.MoocSchema;

/**
 * This used to be a wrapper for a ContentResolver to a single URI, which would be used to interface
 * with a ContentProvider which communicates with a server on the web.
 * 
 * However, to simplify this assignment and remove any dependencies on the database server being online,
 * we removed the ContentResolver and instead are storing StoryData in the default SQLite Database that 
 * is hosted by the device. 
 * 
 */
public class MoocResolver {

	// A private instance of our locally defined mSQLiteOpenHelper
	private mSQLiteOpenHelper helper;

	// An extension of SQLiteOpenHelper which helps us create and manage
	// an SQLiteDatabase
	static class mSQLiteOpenHelper extends SQLiteOpenHelper {
		
		// Simply forward construction to the super class
		public mSQLiteOpenHelper (Context context) {
			super(context, "iRememberSecurityDatabase", null, 1);
		}
		
		// When the database is created, create a table to store our story data, if it does not exist.
		@Override
		public void onCreate(SQLiteDatabase db) {
			
			StringBuilder createTable = new StringBuilder();
			createTable.append("create table if not exists " + tableName + " (");
			createTable.append(MoocSchema.Story.Cols.ID + " integer primary key autoincrement ");
			
			String [] names = MoocSchema.Story.ALL_COLUMN_NAMES;
			String [] types = MoocSchema.Story.ALL_COLUMN_TYPES;
			
			for (int i = 1; i < names.length; ++i) {
				createTable.append(", " + names[i] + " " +types[i]);
			}
			
			createTable.append(");");
			
			Log.d("MoocResolver", "onCreate() called: " + createTable.toString());
			
			try {
				db.execSQL(createTable.toString());
			}
			catch (SQLException e) {
				Log.e("MoocResolver", e.getMessage());
			}
		}

		// We won't worry about upgrading our database in this simple version.
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
		
	}
	
	// The name of the table that will store our story data in the database
	private static final String tableName = "iRememberTable";
	
	/**
	 * Constructor
	 * 
	 * @param context  The context in which to create the database.
	 */
	public MoocResolver(Context context) {
		// Create a new instance of mSQLiteOpenHelper to manage the database
		helper = new mSQLiteOpenHelper(context);
	}
	
	/**
	 * When the user of this class is done with it, make sure to close the database.
	 */
	@Override
	public void finalize() {
		if (helper != null)
			helper.close();
	}
	
	/*
	 * Delete for each ORM Data Type
	 */
	/**
	 * Delete all StoryData(s) from the database that match the
	 * selectionArgs
	 * 
	 * @param selection
	 * @param selectionArgs
	 * @return number of StoryData rows deleted
	 * @throws RemoteException
	 */
	public int deleteStoryData(final String selection,
			final String[] selectionArgs) throws RemoteException {
		SQLiteDatabase db = helper.getWritableDatabase();
		int res = db.delete(tableName, selection, selectionArgs);
		return res;
	}

	/**
	 * Insert a new StoryData object into the database
	 * 
	 * @param storyObject
	 *            object to be inserted
	 * @return row ID of inserted StoryData in the ContentProvider
	 * @throws RemoteException
	 */
	public long insert(final StoryData storyObject) throws RemoteException {
		ContentValues tempCV = storyObject.getCV();
		tempCV.remove(MoocSchema.Story.Cols.ID);
		SQLiteDatabase db = helper.getWritableDatabase();
		long res = db.insert(tableName, null, tempCV);
		db.close();
		return res;
	}

	/*
	 * Query for each ORM Data Type
	 */

	/**
	 * Query the database for StoryData conforming to certain specifications.
	 * 
	 * @param projection
	 * @param selection
	 * @param selectionArgs
	 * @param sortOrder
	 * @return an ArrayList of StoryData objects
	 * @throws RemoteException
	 */
	public ArrayList<StoryData> queryStoryData(final String[] projection,
			final String selection, final String[] selectionArgs,
			final String sortOrder) throws RemoteException {
		// query the database
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor result = db.query(tableName, projection, selection,
				selectionArgs, null, null, sortOrder);
		// make return object
		ArrayList<StoryData> rValue = new ArrayList<StoryData>();
		// convert cursor to reutrn object
		rValue.addAll(StoryCreator.getStoryDataArrayListFromCursor(result));
		result.close();
		
		//close the database
		db.close();
		
		// return 'return object'
		return rValue;
	}

	/*
	 * Update for each ORM Data Type
	 */

	/**
	 * Update the specified StoryData with new values.
	 * 
	 * @param values
	 * @param selection
	 * @param selectionArgs
	 * @return number of rows changed
	 * @throws RemoteException
	 */
	public int updateStoryData(final StoryData values, final String selection,
			final String[] selectionArgs) throws RemoteException {
		
		SQLiteDatabase db = helper.getWritableDatabase();
		int res = db.update(tableName, values.getCV(), selection, selectionArgs);
		db.close();
		return res;
	}

	/*
	 * Sample extensions of above for customized additional methods for classes
	 * that extend this one
	 */

	/**
	 * Get all the StoryData objects current stored in the Content Provider
	 * 
	 * @return an ArrayList containing all the StoryData objects
	 * @throws RemoteException
	 */
	public ArrayList<StoryData> getAllStoryData() throws RemoteException {
		return queryStoryData(null, null, null, null);
	}

	/**
	 * Get a StoryData from the data stored at the given rowID
	 * 
	 * @param rowID
	 * @return StoryData at the given rowID
	 * @throws RemoteException
	 */
	public StoryData getStoryDataViaRowID(final long rowID)
			throws RemoteException {
		String[] selectionArgs = { String.valueOf(rowID) };
		ArrayList<StoryData> results = queryStoryData(null,
				MoocSchema.Story.Cols.ID + "= ?", selectionArgs, null);
		if (results.size() > 0) {
			return results.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Delete All rows, from AllStory table, that have the given rowID. (Should
	 * only be 1 row, but Content Providers/SQLite3 deletes all rows with
	 * provided rowID)
	 * 
	 * @param rowID
	 * @return number of rows deleted
	 * @throws RemoteException
	 */
	public int deleteAllStoryWithRowID(long rowID) throws RemoteException {
		String[] args = { String.valueOf(rowID) };
		return deleteStoryData(MoocSchema.Story.Cols.ID + " = ? ", args);
	}

	/**
	 * Updates all StoryData stored with the provided StoryData's 'KEY_ID'
	 * (should only be 1 row of data in the content provider, but content
	 * provider implementation will update EVERY row that matches.)
	 * 
	 * @param data
	 * @return number of rows altered
	 * @throws RemoteException
	 */
	public int updateStoryWithID(StoryData data) throws RemoteException {
		String selection = "_id = ?";
		String[] selectionArgs = { String.valueOf(data.KEY_ID) };
		return updateStoryData(data, selection, selectionArgs);
	}

	
}