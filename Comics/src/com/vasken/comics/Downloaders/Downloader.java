package com.vasken.comics.Downloaders;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import android.util.Log;

import com.vasken.comics.Comic;
import com.vasken.util.WebRequester;

public abstract class Downloader implements WebRequester.RequestCallback {
	private WebRequester requester = new WebRequester();	
	protected Comic comic;
	protected String url;
	
	public void setUrl(String url) {
		this.url = url;
	}

	protected Comic newComic() {
		Comic c = new Comic();
		c.url = url;
		return c;
	}
	
	public Comic getComic() {
		Log.d("GETTING COMIC", url);
		comic = null;
		HttpUriRequest request = createHttpRequest(url);
		requester.makeRequest(request, this);
		return comic;
	}
	
	public HttpUriRequest createHttpRequest(String url) {
		return new HttpGet(url);
	}
}
