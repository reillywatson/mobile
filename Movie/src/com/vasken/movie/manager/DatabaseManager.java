package com.vasken.movie.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.vasken.movie.model.Actor;
import com.vasken.movie.model.Movie;

public class DatabaseManager  extends SQLiteOpenHelper{
	protected static final String DB_URL = "http://dl.dropbox.com/u/3091257/Vasken/oscars/oscars.sqlite";
	protected static final String DB_PATH = "/Android/data/com.vasken.movie/files/";
	protected static final String DB_NAME = "oscars.sqlite";
	
	private static final String BEST_ACTRESS = "Best Actress";
	private static final String BEST_ACTOR = "Best Actor";
	private static final String BEST_PICTURE = "Best Picture";

    private SQLiteDatabase theDataBase;
    private String dbPath;
    
	public DatabaseManager(Context context) {
		super(context, DB_NAME, null, 1);

		dbPath = Environment.getExternalStorageDirectory().getAbsolutePath() + DatabaseManager.DB_PATH;
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
        	Log.d(getClass().toString(), "SD card not available: ");
        	dbPath = Environment.getDataDirectory().getAbsolutePath() + DatabaseManager.DB_PATH;
        }
	}
	 
    public void openDataBase() throws SQLException{
    	theDataBase = SQLiteDatabase.openDatabase(getDbPath() + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
    }
	
	@Override
	public synchronized void close() {
    	if(theDataBase != null) {
    		theDataBase.close();
    	}
    	super.close();
	}
	
	public List<Movie> getBestPictureEntries(int yr) {
		List<Movie> result = new ArrayList<Movie>();
		
		String award = BEST_PICTURE;
		String year = String.valueOf(yr);
		Cursor cursor = theDataBase.query(
				"Oscar"
			, new String[]{"AwardName", "Year", "Film", "IsWinner", "Actors" }	// Columns
			, "AwardName=? AND Year=?"										// Selection (where)
			, new String[]{award, year}								// Selection args
			, null													// Group By
			, null													// Having
			, null													// Order By
			, null);												// Limit
		
		if (cursor.moveToFirst()) {
    		while (cursor.moveToNext()) {
    			Movie actor = new Movie( 
    					award					 // award
    					, yr		 			 // year
    					, cursor.getString(2)	 // film
    					,(cursor.getInt(3) == 1) // isWinner
    					, cursor.getString(4)	 // actors
    			);
    			result.add(actor);
    		}
    	}
		return result;
	}
	
	public List<Actor> getBestActressEntries(int year) {
		List<Actor> result = new ArrayList<Actor>();
		result = getActorFields(BEST_ACTRESS, year);
		return result;
	}
	
	public List<Actor> getBestActorEntries(int year) {
		List<Actor> result = new ArrayList<Actor>();
		result = getActorFields(BEST_ACTOR, year);
		return result;
	}

	private List<Actor> getActorFields(String award, int yr) {
		List<Actor> result = new ArrayList<Actor>();
		
		String year = String.valueOf(yr);
		Cursor cursor = theDataBase.query(
				"Oscar"
			, new String[]{"AwardName", "Year", "Film", "Nominee", "Role", "IsWinner" }	// Columns
			, "AwardName=? AND Year=?"						// Selection (where)
			, new String[]{award, year}								// Selection args
			, null													// Group By
			, null													// Having
			, null													// Order By
			, null);												// Limit
		
		if (cursor.moveToFirst()) {
    		while (cursor.moveToNext()) {
    			Actor actor = new Actor( 
    					award					 // award
    					, yr		 			 // year
    					, cursor.getString(2)	 // film
    					, cursor.getString(3)	 // name
    					, cursor.getString(4)	 // role
    					,(cursor.getInt(5) == 1) // isWinner
    			);
    			result.add(actor);
    		}
    	}
		return result;
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

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

	public String getDbPath() {        
        return dbPath;
	}
}
