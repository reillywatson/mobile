package com.vasken.comics.Downloaders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import android.util.Log;

import com.vasken.comics.Comic;
import com.vasken.util.WebRequester;

public abstract class Downloader implements WebRequester.RequestCallback {
	private WebRequester requester = new WebRequester();	
	protected Comic comic;
	protected String url;
	
	protected abstract Pattern getComicPattern();
	protected abstract Pattern getNextComicPattern();
	protected abstract Pattern getPrevComicPattern();
	protected Pattern getTitlePattern() { return null; }
	protected Pattern getAltTextPattern() { return null; }
	
	protected String getBaseComicURL() { return ""; }
	protected String getBasePrevNextURL() { return ""; }
	
	protected int headerGarbageEstimate() { return 5000; }
	
	protected boolean parseTitle(StringBuilder partialResponse) {
		Pattern p = getTitlePattern();
		if (p == null)
			return true;
		Matcher m = p.matcher(partialResponse);
		if (m.find()) {
			comic.title = m.group(1);
			Log.d("TITLE", comic.title);
			return true;
		}
		return false;
	}
	
	protected boolean parseComic(StringBuilder partialResponse) {
		Matcher m = getComicPattern().matcher(partialResponse);
		if (m.find()) {
			comic.image = getBaseComicURL() + m.group(1);
			Log.d("IMAGE", comic.image);
			return true;
		}
		return false;
	}
	
	protected boolean parseAltText(StringBuilder partialResponse) {
		Pattern p = getAltTextPattern();
		if (p == null)
			return true;
		Matcher m = p.matcher(partialResponse);
		if (m.find()) {
			comic.altText = m.group(1);
			Log.d("ALT TEXT", comic.altText);
			return true;
		}
		return false;
	}
	
	protected boolean parsePrevLink(StringBuilder partialResponse) {
		Matcher m = getPrevComicPattern().matcher(partialResponse);
		if (m.find()) {
			comic.prevUrl = getBasePrevNextURL() + m.group(1);			
			Log.d("PREV", comic.prevUrl);
			return true;
		}
		return false;
	}
	
	protected boolean parseNextLink(StringBuilder partialResponse) {
		Matcher m = getNextComicPattern().matcher(partialResponse);
		if (m.find()) {
			comic.nextUrl = getBasePrevNextURL() + m.group(1);
			Log.d("NEXT", comic.nextUrl);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean handlePartialResponse(StringBuilder responseSoFar, boolean isFinal) {
		if (isFinal || responseSoFar.length() > headerGarbageEstimate()) {
			Log.d(this.getClass().getName(),"PARSING...");
			comic = newComic();
			boolean success = parseComic(responseSoFar);
			success = success & (parsePrevLink(responseSoFar) | parseNextLink(responseSoFar));
			success &= parseTitle(responseSoFar);
			success &= parseAltText(responseSoFar);
			if (success) {
				Log.d(this.getClass().getName(), "WE HAVE A WINNER!");
			}
			return success;
		}
		return false;
	}
	
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
