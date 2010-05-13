package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

public class CyanideAndHappinessDownloader extends Downloader {

	/* I can't believe that < Previous works...
	<img alt="Cyanide and Happiness, a daily webcomic" src="http://www.explosm.net/db/files/Comics/Kris/cat.png">
	<a href="/comics/2042/">< Previous</a> | <a href="/comics/2044/">Next ></a>*/
	
	private static Pattern title = Pattern.compile("<title>(.*?) - Explosm.net</title>", Pattern.DOTALL);
	private static Pattern imgData = Pattern.compile("<img alt=\"Cyanide and Happiness, a daily webcomic\" src=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("<a href=\"([^>]*?)\">< Previous", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("<a href=\"([^>]*?)\">Next >", Pattern.DOTALL);
	
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
		return "http://www.explosm.net";
	}

}
