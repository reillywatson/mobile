package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

public class UserFriendlyDownloader extends Downloader {

	private static Pattern imgData = Pattern.compile("<img border=\"0\" src=\"(.*?)\" [^>]*? ALT=\"Strip for", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("<area shape=\"rect\" href=\"([^>]*?)\" [^>]*? alt=\"Previous Cartoon\">", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("<area shape=\"rect\" href=\"([^>]*?)\" [^>]*? alt=\"\">", Pattern.DOTALL);
	
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
		return "http://ars.userfriendly.org";
	}
}
