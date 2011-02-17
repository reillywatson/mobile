package com.vasken.movie.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class DataManager{
    
	private LoadListener loadListener;
    DatabaseManager dbManager;
    String dbPath;
    
	public DataManager(Context context) {		
		dbManager = new DatabaseManager(context);
        dbPath = dbManager.getDbPath();
	}
	
	public void loadCatalog() {
		SQLiteDatabase db = dbManager.getReadableDatabase();
		db.close();
    	
		try {
            URL url = new URL(DatabaseManager.DB_URL);
            InputStream reader = url.openStream();
            
            File dir = new File(dbPath);
            if (!dir.exists()) {
            	dir.mkdirs();
            }
            
            Log.d(getClass().toString(), "Database path: " + dbPath);
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

			// Notify Listeners
			if (null != this.loadListener) {
				this.loadListener.onDone();
			}

		} catch (MalformedURLException e) {
        	System.err.println("URL is wrong: " + DatabaseManager.DB_URL);
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

	public boolean databaseIsOkay() {
		boolean existsLocally = foundDatabase();
		boolean isNewest = true; // TODO: Check the dbVersion with the server

		return existsLocally && isNewest;
	}

	public void setOnDoneListener(LoadListener loadListener) {
		this.loadListener = loadListener;
	}

	private boolean foundDatabase() {
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
