package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

public class BookOfBiffDownloader extends Downloader {
	private static Pattern title = Pattern.compile("<div id=\"comic\">[^<]*?<img .*? alt=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern imgData = Pattern.compile("<div id=\"comic\">[^<]*?<img src=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("<a href=\"([^>]*?)\">&#9668; Previous", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("a href=\"([^>]*?)\">Next &#9658;", Pattern.DOTALL);
	
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
