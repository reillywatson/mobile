package com.vasken.movie.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Log;

import com.vasken.util.WebRequester;

public class ImageManager {
	private static final String GOOGLE_IMAGE_URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
	private static final String RESPONSE_DATA = "responseData";
	private static final String RESULTS = "results";
	private static final String UNESCAPED_URL = "unescapedUrl";
	
	public Bitmap getImage(String actorName) {
		Bitmap image = null;
		
		String googleSearchURL = GOOGLE_IMAGE_URL + URLEncoder.encode(actorName);
		URL url;
		try {
			url = new URL(googleSearchURL);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
	        StringBuilder response = new StringBuilder();
	        String line = reader.readLine();
	        while (line != null) {
	        	response.append(line);       	
	        	line = reader.readLine();
	        }
	        
	        JSONArray responseData = new JSONObject(response.toString()).getJSONObject(RESPONSE_DATA).getJSONArray(RESULTS);
	        for (int i=0; ( image == null && i < responseData.length()); i++) {
	        	JSONObject result = responseData.getJSONObject(i);
	        	String urlString = result.getString(UNESCAPED_URL);
	        	image = WebRequester.bitmapFromUrl(urlString);
	        }
		} catch (MalformedURLException e) {
			Log.e("-----", actorName, e);
			e.printStackTrace();
		} catch (JSONException e) {
			Log.e("-----", actorName, e);
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("-----", actorName, e);
			e.printStackTrace();
		}
        
        return image;
	}

}
