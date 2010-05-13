package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

public class GirlsWithSlingshotsDownloader extends Downloader {
	private static Pattern imgData = Pattern.compile("div id=gwsblog.*?<img src=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("<a href=\"([^>]*?)\" [^>]*?><img name=\"img2\"", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("<a href=\"([^>]*?)\" [^>]*?><img name=\"img4\"", Pattern.DOTALL);
	
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
	protected String getBaseComicURL() {
		return "http://www.gwscomic.com/";
	}
	
	@Override
	protected String getBasePrevNextURL() {
		return "http://www.gwscomic.com/";
	}
}
