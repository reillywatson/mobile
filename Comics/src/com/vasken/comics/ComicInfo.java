package com.vasken.comics;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.vasken.comics.Downloaders.Downloader;

public class ComicInfo implements Comparable<ComicInfo> {
	public String name;
	public String startUrl;
	Callable<Downloader> downloaderConstructor;
	
	public Pattern titlePattern;
	public Pattern altTextPattern;
	public Pattern comicPattern;
	public Pattern prevComicPattern;
	public Pattern nextComicPattern;
	public Pattern randomComicPattern;
	public String randomLink;
	public String basePrevNextURL = "";
	public String baseComicURL = "";
	public boolean requiresReferrer;
	
	void populateFromJSON(JSONObject comicJSON) {
		try {
			if (comicJSON.has("Name"))
				name = comicJSON.getString("Name");
			if (comicJSON.has("StartURL"))
				startUrl = comicJSON.getString("StartURL");
			if (comicJSON.has("ImagePattern"))
				comicPattern = Pattern.compile(comicJSON.getString("ImagePattern"), Pattern.DOTALL);
			if (comicJSON.has("PrevComicPattern"))
				prevComicPattern = Pattern.compile(comicJSON.getString("PrevComicPattern"), Pattern.DOTALL);
			if (comicJSON.has("NextComicPattern"))
				nextComicPattern = Pattern.compile(comicJSON.getString("NextComicPattern"), Pattern.DOTALL);
			if (comicJSON.has("AltTextPattern"))
				altTextPattern = Pattern.compile(comicJSON.getString("AltTextPattern"), Pattern.DOTALL);
			if (comicJSON.has("TitlePattern"))
				titlePattern = Pattern.compile(comicJSON.getString("TitlePattern"), Pattern.DOTALL);
			if (comicJSON.has("RandomLink"))
				randomLink = comicJSON.getString("RandomLink");
			if (comicJSON.has("RandomComicPattern"))
				randomComicPattern = Pattern.compile(comicJSON.getString("RandomComicPattern"), Pattern.DOTALL);
			if (comicJSON.has("BasePrevNextURL"))
				basePrevNextURL = comicJSON.getString("BasePrevNextURL");
			if (comicJSON.has("BaseComicURL"))
				baseComicURL = comicJSON.getString("BaseComicURL");
			if (comicJSON.has("RequiresReferrer"))
				requiresReferrer = true;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	void parseJSON(Context context, int jsonResID) {
		try {
	        JSONObject comicJSON = null; //((ComicsApplication)context.getApplicationContext()).getJSONCache().get(new Integer(jsonResID));
	        if (comicJSON == null) {
				BufferedReader in = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(jsonResID)), 1024);
				StringBuilder sb = new StringBuilder();
				char[] buffer = new char[1024];
				int bytesRead = in.read(buffer, 0, buffer.length);
		        int totalBytesRead = 0;
		        while (bytesRead>=0) {
		        	sb.append(buffer, 0, bytesRead);
		        	totalBytesRead += bytesRead;
		        	bytesRead = in.read(buffer, 0, buffer.length);
		        }
				comicJSON = new JSONObject(sb.toString());
				populateFromJSON(comicJSON);
				//((ComicsApplication)context.getApplicationContext()).getJSONCache().put(new Integer(jsonResID), comicJSON);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ComicInfo(JSONObject comicJSON) {
		populateFromJSON(comicJSON);
	}
	
	public ComicInfo (Context context, int jsonResID) {
		parseJSON(context, jsonResID);
		if (this.name == null) {
			Log.e("----------------------------------", String.valueOf(jsonResID));
		}
	}
	
	public ComicInfo(Context context, int resID, String name, String startUrl) {
		parseJSON(context, resID);
		this.name = name;
		this.startUrl = startUrl;
	}
	
	public ComicInfo(String title, String url, Callable<Downloader> constructor) {
		this.name = title;
		startUrl = url;
		this.downloaderConstructor = constructor;
	}
	
	@Override
	public int compareTo(ComicInfo another) {
		return name.replaceFirst("The ", "").compareTo(another.name.replaceFirst("The ", ""));
	}
	
	@Override
	public String toString() {
		return name;
	}
}
