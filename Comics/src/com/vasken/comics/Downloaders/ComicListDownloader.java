package com.vasken.comics.Downloaders;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;

import com.vasken.comics.ComicInfo;
import com.vasken.util.WebRequester;


public class ComicListDownloader implements WebRequester.RequestCallback {
	
	void downloadComicList() {
		int version = 1; // TODO take this from whatever the latest version info we have is
		try {
			new WebRequester().makeRequest(new HttpGet("http://vaskencomics.appspot.com?platform=android&version=" + String.valueOf(version)), this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	List<ComicInfo> comicList = new ArrayList<ComicInfo>();
	@Override
	public boolean handlePartialResponse(StringBuilder sb, boolean isFinal) {
		if (!isFinal)
			return false;
		String response = sb.toString();
		if (!response.startsWith("["))
			return true; // we're up to date already
        try {
			JSONArray comics = new JSONArray(response);
			for (int i = 0; i < comics.length(); i++) {
				ComicInfo comic = new ComicInfo(comics.getJSONObject(i));
				comicList.add(comic);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return true;
	}
	
}
