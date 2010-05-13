package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

public class AmazingSuperPowersDownloader extends Downloader {
	private static Pattern imgData = Pattern.compile("img src=\"http://www.amazingsuperpowers.com/comics/(.*?)\"", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("<div class=\"nav-previous\"><a href=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("<div class=\"nav-next\"><a href=\"(.*?)\"", Pattern.DOTALL);
	// oddly enough, these aren't backwards
	private static Pattern altText = Pattern.compile("img src=\"http://www.amazingsuperpowers.com/comics/.*?title=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern title = Pattern.compile("img src=\"http://www.amazingsuperpowers.com/comics/.*?alt=\"(.*?)\"", Pattern.DOTALL);
	
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
		return title;
	}
	
	@Override
	protected Pattern getAltTextPattern() {
		return altText;
	}
	
	@Override
	protected String getBaseComicURL() {
		return "http://www.amazingsuperpowers.com/comics/";
	}
}
