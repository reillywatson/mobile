package com.vasken.comics.Downloaders;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import android.util.Log;

public class GoComicsDownloader extends Downloader {
	// Sample HTML fragment:
	//<img alt="?fh=10b07e5a5bf87622b7e6ccc8ea3fe9b3" src="http://imgsrv.gocomics.com/dim/?fh=10b07e5a5bf87622b7e6ccc8ea3fe9b3" width="100%" />
	//<div><span class="authorText"><strong>Bill Amend</strong>April 25, 2010</span>
	//<span class="archiveText"><a href="/foxtrot/2010/04/18/"><< Previous</a><a href="/foxtrot/2010/05/02/">Next >></a></span></div>
	
	private Pattern prevComic = Pattern.compile("<span class=\"archiveText\">.*?<a href=\"([^>]*?)\"><< Previous</a>", Pattern.DOTALL);
	private Pattern nextComic = Pattern.compile("<span class=\"archiveText\">.*?<a href=\"([^>]*?)\">Next >></a>", Pattern.DOTALL);
	private Pattern imgData = Pattern.compile( "http://imgsrv.gocomics.com/dim/\\?fh=(.*?)\"", Pattern.DOTALL);
	private Pattern date = Pattern.compile("<span class=\"authorText\">.*?</strong>(.*?)</span>", Pattern.DOTALL);
	
	@Override
	protected Pattern getComicPattern() {
		return imgData;
	}
	@Override
	protected Pattern getNextComicPattern() {
		return nextComic;
	}
	@Override
	protected Pattern getPrevComicPattern() {
		return prevComic;
	}
	
	@Override
	protected Pattern getTitlePattern() {
		return date;
	}

	private String getComicUrl(Date d) {
		return String.format("/%04d/%02d/%02d/", d.getYear() + 1900, d.getMonth() + 1, d.getDate());
	}
	
	@Override
	protected boolean parsePermalink(StringBuilder partialResponse) {
		if (url.split("/").length > 4) {
			comic.permalink = url;
		}
		else if (comic.title != null){
			Date d = new Date(Date.parse(comic.title));
			comic.permalink = url + getComicUrl(d);
		}
		else {
			return false;
		}
		Log.d("PERMALINK", comic.permalink);
		return true;
	}
	
	@Override
	protected String getBaseComicURL() {
		return "http://imgsrv.gocomics.com/dim?fh=";
	}
	
	@Override
	protected String getBasePrevNextURL() {
		return "http://www.gocomics.com";
	}
}
