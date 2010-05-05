package com.vasken.comics.Downloaders;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.vasken.comics.Comic;
import com.vasken.util.WebRequester;

public abstract class Downloader implements WebRequester.RequestCallback {
	WebRequester requester = new WebRequester();	
	Comic comic;

	public Comic getComic(String url) {
		comic = null;
		HttpUriRequest request = createHttpRequest(url);
		requester.makeRequest(request, this);
		return comic;
	}
	
	public HttpUriRequest createHttpRequest(String url) {
		return new HttpGet(url);
	}
}
