package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

public class SharkAttackDownloader extends Downloader {
	private static Pattern title = Pattern.compile("<title>Shark attack! Webcomic - (.*?)</title>", Pattern.DOTALL);
	private static Pattern imgData = Pattern.compile("<img src=\"/images/strips/(.*?)\"", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("<a href=\"([^>]*?)\">[^<]*?<img src=\"/images/SAC_Previous_002.png\"", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("<a href=\"([^>]*?)\">[^<]*?<img src=\"/images/SAC_Next_002.png\"", Pattern.DOTALL);
	
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
		return "http://www.sharkattackcomics.com";
	}
	
	@Override
	protected String getBaseComicURL() {
		return "http://www.sharkattackcomics.com/images/strips/";
	}
}
