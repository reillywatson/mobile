package com.vasken.movie.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

public class DataManager {
	private static final String DB_URL = "link/to/my/dropbox/oscars.sqlite";
	private static final String DB_PATH = "oscars.sqlite";
	private LoadListener loadListener;
	
	public void loadCatalog() {
		try {
            URL url = new URL(DB_URL);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(DB_PATH)));
            
            // Copy the database file from the web to the Android
            String line = null;
            while ((line=reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }

            reader.close();
            writer.close();
        } catch (MalformedURLException e) {
        	System.err.println("URL is wrong: " + DB_URL);
        	e.printStackTrace();
        } catch (IOException e) {
			System.err.println("Couldn't update movie catalog: ");
			e.printStackTrace();
        }
        
		// Notify Listeners
		if (null != this.loadListener) {
			this.loadListener.onDone();
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
		// TODO Auto-generated method stub
		return false;
	}

}
