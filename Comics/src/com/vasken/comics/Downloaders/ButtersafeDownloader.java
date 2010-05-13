package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

public class ButtersafeDownloader extends Downloader {

	private static Pattern imgData = Pattern.compile("<div id=\"comic\">.*?<img src=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("<a href=\"([^>]*?)\" rel=\"prev\"", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("<a href=\"([^>]*?)\" rel=\"next\"", Pattern.DOTALL);
	private static Pattern title = Pattern.compile("<div id=\"comic\">.*?<img .*? title=\"(.*?)\"", Pattern.DOTALL);
	
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

}
