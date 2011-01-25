package com.vasken.music.server.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vasken.music.server.model.Song;

public class MusicCatalogManager {
	private static MusicCatalogManager INSTANCE = null;

	public static MusicCatalogManager sharedInstace() {
		if (INSTANCE == null) {
			INSTANCE = new MusicCatalogManager();
		}
		return INSTANCE;
	}

	public List<Song> getSongCatalog(String genre) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
	    
		Query catalogQuery = pm.newQuery(Song.class);
		catalogQuery.setRange(0, 300);
		catalogQuery.setFilter("genre == genreParam");
		catalogQuery.declareParameters("String genreParam");
		
		@SuppressWarnings("unchecked")
		List<Song> catalog = (List<Song>) catalogQuery.execute(genre);
		catalogQuery.closeAll();
	    
		return catalog;
	}
	
	public StringBuffer updateCatalog() throws JSONException {
		StringBuffer result = new  StringBuffer();
		for (int i=21; i<22; i++) {
			String genre = String.valueOf(i);
			
			// Make genre request
			StringBuilder response = makeGenreRequest(genre);
			
			// Parse response
			List<Song> songs = parseResponse(response, genre);
			
			// Save list of songs
			saveSongCatalog(songs);
			
			result.append(genre).append(" - ").append(songs.size()).append("\n");
		}
		
		return result;
	}

	private StringBuilder makeGenreRequest(String genre) {
		StringBuilder result = new StringBuilder();
		final String urlString = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/" 
			+ "sf=143441/" 				// Some magic shit
			+ "limit=300/" 				// Number of results 
			+ "genre=" + genre + "/"  	// Type of music
			+ "explicit=true/"  		// Naughty songs
			+ "json";
		
		try {
            URL url = new URL(urlString);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            
            String line;
            while ((line = reader.readLine()) != null) {
            	result.append(line);
            }
            reader.close();
        } catch (MalformedURLException e) {
        	System.err.println("URL is wrong: " + urlString);
        	e.printStackTrace();
        } catch (IOException e) {
			System.err.println("Couldn't update song catalog: ");
			e.printStackTrace();
        } 

       	return result;
	}

	private List<Song> parseResponse(StringBuilder response, String genre) throws JSONException {
		List<Song> result = new LinkedList<Song>();

		JSONObject json = new JSONObject(response.toString());
		JSONArray entries = json.getJSONObject("feed").getJSONArray("entry");
		for (int i=0;i<entries.length(); i++) {
			JSONObject track = entries.getJSONObject(i);
			
			String title = track.getJSONObject("title").getString("label");
			String link = track.getJSONArray("link").getJSONObject(1).getJSONObject("attributes").getString("href");
			Date dateAdded = Calendar.getInstance().getTime();
			//TODO: Add more entries - like price maybe...?
			
			Song song = new Song(title, link, genre, dateAdded);
			result.add(song);
		}
		return result;
	}
	
	private void saveSongCatalog(List<Song> songs) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistentAll(songs);
		} finally {
			pm.close();
		}
	}

	private MusicCatalogManager(){}
}
