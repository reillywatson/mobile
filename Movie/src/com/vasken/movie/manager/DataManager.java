package com.vasken.movie.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DataManager extends SQLiteOpenHelper{

	private static final String DB_URL = "http://dl.dropbox.com/u/3091257/Vasken/oscars/oscars.sqlite";
	private static final String DB_PATH = "/Android/data/com.vasken.movie/files/";
	private static final String DB_NAME = "oscars.sqlite";

    private SQLiteDatabase theDataBase;
    private String dbPath;
    
	public DataManager(Context context) {
		super(context, DB_NAME, null, 1);
	}
	private LoadListener loadListener;
	
	public void loadCatalog() {
    	this.getReadableDatabase();
    	
		try {
            URL url = new URL(DB_URL);
            InputStream reader = url.openStream();
            
            dbPath = Environment.getExternalStorageDirectory().getAbsolutePath() + DB_PATH;
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            	Log.d(getClass().toString(), "SD card not available: ");
            	dbPath = Environment.getDataDirectory().getAbsolutePath() + DB_PATH;
            }
            File dir = new File(dbPath);
            if (!dir.exists()) {
            	dir.mkdirs();
            }
            
            Log.d(getClass().toString(), "Database path: " + dbPath);
			OutputStream writer = new FileOutputStream(dbPath + DB_NAME);

			// Copy the database file from the web to the Android
			byte[] buffer = new byte[1024];
			int length;
			while ((length = reader.read(buffer)) > 0) {
				writer.write(buffer, 0, length);
			}

			// Close the streams
			writer.flush();
			writer.close();
			reader.close();

			// Notify Listeners
			if (null != this.loadListener) {
				this.loadListener.onDone();
			}

		} catch (MalformedURLException e) {
        	System.err.println("URL is wrong: " + DB_URL);
        	e.printStackTrace();
        	// Notify Listeners
    		if (null != this.loadListener) {
    			this.loadListener.onError(e);
    		}
        } catch (IOException e) {
			System.err.println("Couldn't update movie catalog: ");
			e.printStackTrace();
			// Notify Listeners
    		if (null != this.loadListener) {
    			this.loadListener.onError(e);
    		}
        }
	}

	// The database is okay if:
	//	- it exists locally
	//	- it's as new as the one on the server
	public boolean databaseIsOkay() {
		return foundDatabase();
	}

	public void setOnDoneListener(LoadListener loadListener) {
		this.loadListener = loadListener;
	}

	private boolean foundDatabase() {
		SQLiteDatabase checkDB = null;
		 
    	try{
    		checkDB = SQLiteDatabase.openDatabase(dbPath+DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
    	}catch(SQLiteException e){
    		//database does't exist yet.
    	}
 
    	if(checkDB != null){
    		checkDB.close();
    	}
 
    	return checkDB != null ? true : false;
	}
	 
    public void openDataBase() throws SQLException{
    	theDataBase = SQLiteDatabase.openDatabase(dbPath + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
    }
    
    public void getEntry() {
//    	Cursor cursor = theDataBase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
//    	if (cursor.moveToFirst()) {
//    		cursor.
//    	}
    }
	
    @Override
	public synchronized void close() {
    	if(theDataBase != null) {
    		theDataBase.close();
    	}
    	super.close();
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public void test() {
    	Cursor cursor = theDataBase.query(
    				"Oscar"
    			, new String[]{"AwardName", "Year", "Film", "Nominee", "Role", "IsWinner", "Actors" }	// Columns
    			, null													// Selection (where)
    			, new String[]{}										// Selection args
    			, null													// Group By
    			, null													// Having
    			, null													// Order By
    			, null);												// Limit
    	Log.d("-------", String.valueOf(cursor.getCount()));
    	if (cursor.moveToFirst()) {
    		while (cursor.moveToNext()) {
    			Log.d("-------", 
    					cursor.getString(0) + " " +
    					cursor.getString(1) + " " +
    					cursor.getString(2) + " " +
    					cursor.getString(3) + " " +
    					cursor.getString(4) + " " +
    					(cursor.getInt(5) == 1? "WON" : "LOSER") + " " +
    					cursor.getString(6));
    		}
    	}
	}

}
