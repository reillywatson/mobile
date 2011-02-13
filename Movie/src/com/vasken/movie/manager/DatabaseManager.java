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
import com.vasken.movie.model.NominatedPerson;
import com.vasken.movie.model.Movie;
import com.vasken.movie.model.Quote;

public class DatabaseManager  extends SQLiteOpenHelper{
	protected static final String DB_URL = "http://dl.dropbox.com/u/3091257/Vasken/oscars/oscars.sqlite";
	protected static final String DB_PATH = "/Android/data/com.vasken.movie/files/";
	protected static final String DB_NAME = "oscars.sqlite";
	
	private static final String BEST_ACTRESS = "Best Actress";
	private static final String BEST_ACTOR = "Best Actor";
	private static final String BEST_PICTURE = "Best Picture";
	private static final String BEST_SUPPORTING_ACTOR = "Best Supporting Actor";
	private static final String BEST_SUPPORTING_ACTRESS = "Best Supporting Actress";
	private static final String BEST_DIRECTOR = "Best Director";

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

	public List<Actor> getBestSupportingActorEntries(int randomYear) {
		List<Actor> result = new ArrayList<Actor>();
		result = getActorFields(BEST_SUPPORTING_ACTOR, randomYear);
		return result;
	}

	public List<Actor> getBestSupportingActressesEntries(int randomYear) {
		List<Actor> result = new ArrayList<Actor>();
		result = getActorFields(BEST_SUPPORTING_ACTRESS, randomYear);
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

	public Quote getRandomQuote() {
		Quote result = null;
		
		Cursor cursor = theDataBase.query(
				"Quote"
				, new String[]{"Film", "Quoute"}	// Columns
				, null								// Selection (where)
				, new String[]{}					// Selection args
				, null								// Group By
				, null								// Having
				, "RANDOM()"						// Order By
				, "1");								// Limit
		
		if (cursor.moveToFirst()) {
			result = new Quote(cursor.getString(0), cursor.getString(1));
		} 
		cursor.close();
		
		return result;
	}

	public List<Movie> getMoviesFromSameYear(String filmName) {
		List<Movie> result = new ArrayList<Movie>();
		
		Cursor cursor = theDataBase.rawQuery(
				"SELECT AwardName, Year, Film, IsWinner, Actors " +
				"FROM Oscar " +
				"WHERE year IN ( SELECT year FROM oscar WHERE film LIKE '?')" +
				"	AND awardName like 'Best Picture'" , new String[]{filmName});
		
		while (cursor.moveToNext()) {
   			Movie movie = new Movie( 
   					cursor.getString(0)		// award
   					, cursor.getInt(1)		// year
   					, cursor.getString(2)	// film
   					,(cursor.getInt(3) == 1)// isWinner
   					, cursor.getString(4)	// actors
   			);
   			result.add(movie);
   		}
		cursor.close();
		
		return result;
	}

	public List<NominatedPerson> getBestDirectorEntries(int randomYear) {
		List<NominatedPerson> result = new ArrayList<NominatedPerson>();
		
		String award = BEST_DIRECTOR;
		String year = String.valueOf(randomYear);
		Cursor cursor = theDataBase.query(
				"Oscar"
			, new String[]{"AwardName", "Year", "Film", "Nominee", "IsWinner" }	// Columns
			, "AwardName=? AND Year=?"								// Selection (where)
			, new String[]{award, year}								// Selection args
			, null													// Group By
			, null													// Having
			, null													// Order By
			, null);												// Limit
		
   		while (cursor.moveToNext()) {
   			NominatedPerson person = new NominatedPerson( 
   					award					 // award
   					, randomYear 			 // year
   					, cursor.getString(2)	 // film
   					, cursor.getString(3)	 // nominee
   					,(cursor.getInt(4) == 1) // isWinner
   			);
   			result.add(person);
   		}
   		cursor.close();
   		return result;
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
		
   		while (cursor.moveToNext()) {
   			Movie movie = new Movie( 
   					award					 // award
   					, yr		 			 // year
   					, cursor.getString(2)	 // film
   					,(cursor.getInt(3) == 1) // isWinner
   					, cursor.getString(4)	 // actors
   			);
   			result.add(movie);
   		}
   		cursor.close();
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
    	cursor.close();
    	
		return result;
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
