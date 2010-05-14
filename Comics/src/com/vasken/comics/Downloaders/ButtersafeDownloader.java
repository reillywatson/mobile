package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

import com.vasken.comics.Comic;

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
	
	@Override
	protected Comic newComic() {
		Comic c = super.newComic();
		c.randomUrl = "http://www.ohnorobot.com/random.pl?comic=1307";
		return c;
	}
}
