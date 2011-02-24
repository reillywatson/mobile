package com.vasken.movie.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.vasken.movie.R;
import com.vasken.util.UserTask;

public class UpdateManager{
	private static final String DB_VERSION = "version";
	
	private static String DB_URL;
	private static String DB_VERSION_URL;
	private static int currentDbVersion;

    private DatabaseManager dbManager;
    private LoadListener loadListener;
    private String dbPath;

	private int remoteDbVersion;
    
	public UpdateManager(Activity context) {		
		dbManager = new DatabaseManager(context);
        dbPath = dbManager.getDbPath();
        
        DB_URL = context.getString(R.string.db_location);
        DB_VERSION_URL = context.getString(R.string.db_version_location);
        currentDbVersion = context.getPreferences(Activity.MODE_PRIVATE).getInt(DB_VERSION, 0);
	}

	public void setOnDoneListener(LoadListener loadListener) {
		this.loadListener = loadListener;
	}

	public class LoadListener {
		protected void onDone() {}
		protected void onError() {}
	}
	
	public void loadCatalog(Activity context) {
		new UpdateDatabaseTask().execute(context);
	}

	class UpdateDatabaseTask extends UserTask<Activity, List<String>, Void> {
		private boolean loadedSuccessfuly;

		@Override
		public Void doInBackground(Activity... contexts) {
			SQLiteDatabase db = dbManager.getReadableDatabase();
			db.close();
	    	
			try {
	            InputStream reader = new URL(DB_URL).openStream();
	            
	            File dir = new File(dbPath);
	            boolean dirReady = dir.exists();
	            if (!dirReady) {
	            	dirReady = dir.mkdirs();
	            }
	            
	            if (!dirReady) {
	            	System.err.println("Couldn't create " + dbPath);
		        	loadedSuccessfuly = false;
		        	return null;
	            }
	            
	            Log.d(getClass().toString(), "Putting new database in path: " + dbPath);
				OutputStream writer = new FileOutputStream(dbPath + DatabaseManager.DB_NAME);

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

				// Save current database version
				SharedPreferences prefs = contexts[0].getPreferences(Activity.MODE_PRIVATE);
				prefs.edit().putInt(DB_VERSION, remoteDbVersion).commit();
				
	        	loadedSuccessfuly = true;
			} catch (MalformedURLException e) {
	        	System.err.println("URL is wrong: " + DB_URL);
	        	e.printStackTrace();
	        	loadedSuccessfuly = false;
	        } catch (IOException e) {
				System.err.println("Couldn't update movie catalog: ");
				e.printStackTrace();
	        	loadedSuccessfuly = false;
	        }
	        
			return null;
		}
		
		@Override
		public void onPostExecute(Void result) {
			super.onPostExecute(result);

			// Notify Listeners
    		if (null != loadListener) {
    			if ( loadedSuccessfuly ) {
    				loadListener.onDone();
    			} else {
    				loadListener.onError();
    			}
    		}
		}
	}

	public boolean databaseIsOkay() {
		boolean existsLocally = dbExistsLocally();
		boolean isLatest = dbIsLatest();

		return existsLocally && isLatest;
	}

	private boolean dbIsLatest() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(DB_VERSION_URL).openStream()));
			StringBuilder result = new StringBuilder();
			String line = reader.readLine();
			while(line != null) {
				result.append(line);
				line = reader.readLine();
			}
			reader.close();
			
			JSONObject object = new JSONObject(result.toString());
			remoteDbVersion = object.getInt(DB_VERSION);
			Log.d(this.getClass().toString(), "Current DB Version: " + currentDbVersion);
			Log.d(this.getClass().toString(), "Remote DB Version: " + remoteDbVersion);
			return remoteDbVersion == currentDbVersion;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	private boolean dbExistsLocally() {
		SQLiteDatabase checkDB = null;
		 
    	try{
    		checkDB = SQLiteDatabase.openDatabase(dbPath + DatabaseManager.DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
    	}catch(SQLiteException e){
    		//database does't exist yet.
    	}
 
    	if(checkDB != null){
    		checkDB.close();
    	}
 
    	return checkDB != null ? true : false;
	}
}
