package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

public class MegaTokyoDownloader extends Downloader{
	private static Pattern title = Pattern.compile("<div id=\"title\">(.*?)</div>", Pattern.DOTALL);
	private static Pattern imgData = Pattern.compile("<span id=\"strip\".*? src=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("<li class=\"prev\"><a href=\"\\.(.*?)\"", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("<li class=\"next\"><a href=\"\\.(.*?)\"", Pattern.DOTALL);
	
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
	protected String getBasePrevNextURL() {
		return "http://www.megatokyo.com";
	}
	
	@Override
	protected String getBaseComicURL() {
		return "http://www.megatokyo.com/";
	}

}
