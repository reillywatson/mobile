package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

public class PerryBibleFellowshipDownloader extends Downloader {
	
	private static Pattern title = Pattern.compile("<center>.*?<span class=\"main\">.*?<b>([^<]*?)<", Pattern.DOTALL);
	private static Pattern imgData = Pattern.compile("<img id=\"topimg\" src=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("<A id=older href=\"([^>]*?)\">Older</A>", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("<A id=older href=\"([^>]*?)\">Newer</A>", Pattern.DOTALL);
	
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
	
	protected Pattern getTitlePattern() {
		return title;
	}
	
	@Override
	protected String getBasePrevNextURL() {
		return "http://pbfcomics.com/";
	}
	
	@Override
	protected String getBaseComicURL() {
		return "http://pbfcomics.com/";
	}
}
