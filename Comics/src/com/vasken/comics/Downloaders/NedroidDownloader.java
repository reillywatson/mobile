package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

import com.vasken.comics.Comic;

public class NedroidDownloader extends Downloader {
	private static Pattern altText = Pattern.compile("<div id=\"comic\">[^<]*?<img .*? title=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern title = Pattern.compile("<div id=\"comic\">[^<]*?<img .*? alt=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern imgData = Pattern.compile("<div id=\"comic\">[^<]*?<img src=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("<a href=\"([^>]*?)\" rel=\"prev\"", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("a href=\"([^>]*?)\" rel=\"next\"", Pattern.DOTALL);
	
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
	protected Comic newComic() {
		Comic c = super.newComic();
		c.randomUrl = "http://nedroid.com/?randomcomic=1";
		return c;
	}
}
