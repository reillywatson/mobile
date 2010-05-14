package com.vasken.comics.Downloaders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapesNBabesDownloader extends Downloader {
    private static Pattern title = Pattern.compile("<title>(.*?)</title>", Pattern.DOTALL);
	private static Pattern imgData = Pattern.compile("<div id=[\"']comic[\"']>[^<]*?<img src='(.*?)'", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("<a href=\"([^>]*?)\">&laquo; Previous", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("<a href=\"([^>]*?)\">Next &raquo;", Pattern.DOTALL);
	private static Pattern randomComic = Pattern.compile("Random Comic.*?<a href=\"(.*?)\"", Pattern.DOTALL);
	
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
	protected Pattern getRandomComicPattern() {
		return randomComic;
	}

	@Override
	protected boolean parseTitle(StringBuilder partialResponse) {
		Matcher m = title.matcher(partialResponse);
		if (m.find()) {
			String[] groups = m.group(1).split("&raquo;");
			if (groups.length > 0) {
				comic.title = groups[groups.length - 1];
			}
			return true;
		}
		return false;
	}
}
