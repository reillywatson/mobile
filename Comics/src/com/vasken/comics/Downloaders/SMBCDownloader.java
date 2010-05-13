package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

public class SMBCDownloader extends Downloader {
	private static Pattern imgData = Pattern.compile("usemap=\"#buttons\".*?<img src='(.*?)'", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("<map name=\"buttons\">[^<]*?<area[^>]*?>[^<]*?<area [^>]*? href=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("<map name=\"buttons\">[^<]*?<area[^>]*?>[^<]*?<area[^>]*?>[^<]*?<area[^>]*?>[^<]*?<area [^>]*? href=\"(.*?)\"", Pattern.DOTALL);
	
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
	protected String getBasePrevNextURL() {
		return "http://www.smbc-comics.com";
	}
}
