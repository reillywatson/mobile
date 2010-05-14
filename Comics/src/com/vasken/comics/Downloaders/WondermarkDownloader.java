package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

import com.vasken.comics.Comic;

public class WondermarkDownloader extends Downloader {
	private static Pattern title = Pattern.compile("a2a_config.linkname=\"(.*?)\";", Pattern.DOTALL);
	private static Pattern altText = Pattern.compile("<div id=\"comic\".*? title=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern imgData = Pattern.compile("<div id=\"comic\".*? src=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("<div class=\"comic-nav-previous\">[^<]*?<a href=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("<div class=\"comic-nav-next\">[^<]*?<a href=\"(.*?)\"", Pattern.DOTALL);
	
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
		c.randomUrl = "http://www.ohnorobot.com/random.pl?comic=148";
		return c;
	}
}
