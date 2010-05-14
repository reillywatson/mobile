package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

//<a href='http://www.girlgeniusonline.com/comic.php?date=20100512'><img border=0 alt='The Previous Comic'
public class GirlGeniusDownloader extends Downloader {
	private static Pattern imgData = Pattern.compile("www.girlgeniusonline.com/ggmain/strips/(.*?)'", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("<a href='([^>]*?)'><img border=0 alt='The Previous Comic'", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("<a href='([^>]*?)'><img border=0 alt='The Next Comic'", Pattern.DOTALL);
	
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
	protected String getBaseComicURL() {
		return "http://www.girlgeniusonline.com/ggmain/strips/";
	}
}
