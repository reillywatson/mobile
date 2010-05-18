package com.vasken.comics.Downloaders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import android.util.Log;

import com.vasken.comics.Comic;
import com.vasken.comics.ComicInfo;
import com.vasken.util.WebRequester;

public class Downloader implements WebRequester.RequestCallback {
	private WebRequester requester = new WebRequester();	
	protected Comic comic;
	protected String url;
	protected String defaultUrl;
	protected Pattern titlePattern;
	protected Pattern altTextPattern;
	protected Pattern comicPattern;
	protected Pattern prevComicPattern;
	protected Pattern nextComicPattern;
	protected Pattern randomComicPattern;
	protected String baseComicURL = "";
	protected String basePrevNextURL = "";
	protected String randomComicURL;
	
	protected Pattern getComicPattern() { return comicPattern; }
	protected Pattern getNextComicPattern() { return nextComicPattern; }
	protected Pattern getPrevComicPattern() { return prevComicPattern; }
	protected Pattern getTitlePattern() { return titlePattern; }
	protected Pattern getAltTextPattern() { return altTextPattern; }
	protected Pattern getRandomComicPattern() { return randomComicPattern; }
	
	protected String getBaseComicURL() { return baseComicURL; }
	protected String getBasePrevNextURL() { return basePrevNextURL; }
	protected String getBaseRandomURL() { return getBasePrevNextURL(); }
	
	public Downloader(ComicInfo info) {
		comicPattern = info.comicPattern;
		titlePattern = info.titlePattern;
		altTextPattern = info.altTextPattern;
		prevComicPattern = info.prevComicPattern;
		nextComicPattern = info.nextComicPattern;
		basePrevNextURL = info.basePrevNextURL;
		baseComicURL = info.baseComicURL;
		randomComicPattern = info.randomComicPattern;
		randomComicURL = info.randomLink;
		Log.d("COMIC PATTERN", comicPattern.toString());
		if (titlePattern != null)
			Log.d("TITLE PATTERN", titlePattern.toString());
		
	}
	
	public Downloader() {
	}
		
	// We're assuming all comics use permalinks right now,
	// but that we don't want to remember where you left off
	// if you're on the latest comic.
	protected boolean parsePermalink(StringBuilder partialResponse) {
		if (!url.equals(defaultUrl)) {
			comic.permalink = url;
			Log.d("PERMALINK", comic.permalink);
		}
		return true;
	}
	
	protected boolean parseRandomURL(StringBuilder partialResponse) {
		if (randomComicURL != null) {
			comic.randomUrl = randomComicURL;
			return true;
		}
		Pattern p = getRandomComicPattern();
		if (p == null)
			return true;
		Matcher m = p.matcher(partialResponse);
		if (m.find()) {
			comic.randomUrl = getBaseRandomURL() + m.group(1);
			Log.d("RANDOM", comic.randomUrl);
			return true;
		}
		return false;
	}
	
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
			comic.image = getBaseComicURL() + m.group(1).replaceAll("&amp;", "&");
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
		Pattern p = getPrevComicPattern();
		if (p == null)
			return true;
		Matcher m = p.matcher(partialResponse);
		if (m.find() && !url.endsWith("=1")) {
			String prev = m.group(1);
			if (!prev.equals("#") && !prev.equals("/") && prev.length() > 0) {
				comic.prevUrl = getBasePrevNextURL() + prev;			
				Log.d("PREV", comic.prevUrl);
			}
			return true;
		}
		return false;
	}
	
	protected boolean parseNextLink(StringBuilder partialResponse) {
		Pattern p = getNextComicPattern();
		if (p == null)
			return true;
		Matcher m = p.matcher(partialResponse);
		if (m.find()) {
			String next = m.group(1);
			if (!next.equals("#") && !next.equals("/") && !next.equals(url) && next.length() > 0) {
				comic.nextUrl = getBasePrevNextURL() + m.group(1);
				Log.d("NEXT", comic.nextUrl);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean handlePartialResponse(StringBuilder responseSoFar, boolean isFinal) {
		// This sometimes breaks next links (when we get a partial response that contains the prev link but not the next link),
		// but it's hard to tell if we're on the newest strip (ie no next link), so maybe we'll only handle full responses for now...
		if (isFinal) {
			Log.d(this.getClass().getName(),"PARSING...");
			//Log.d("RESPONSE", responseSoFar.toString());
			comic = newComic();
			boolean success = parseComic(responseSoFar);
			success = success & (parsePrevLink(responseSoFar) | parseNextLink(responseSoFar));
			success &= parseTitle(responseSoFar);
			success &= parseAltText(responseSoFar);
			success &= parsePermalink(responseSoFar);
			success &= parseRandomURL(responseSoFar);
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
	
	public String getUrl() {
		return url;
	}
	
	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
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
