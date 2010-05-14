package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

import com.vasken.comics.Comic;

public class QuestionableContentDownloader extends Downloader {
	private static Pattern imgData = Pattern.compile("<img id=\"strip\" src=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("<a href=\"([^>]*?)\">Previous", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("<a href=\"([^>]*?)\">Next", Pattern.DOTALL);
	
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
	protected String getBasePrevNextURL() {
		return "http://www.questionablecontent.net/";
	}
	
	@Override
	protected String getBaseComicURL() {
		if (url.equals(defaultUrl)) {
			return "";
		}
		return "http://www.questionablecontent.net";
	}
	
	@Override
	protected Comic newComic() {
		Comic c = super.newComic();
		c.randomUrl = "http://www.ohnorobot.com/random.pl?comic=32";
		return c;
	}
}
