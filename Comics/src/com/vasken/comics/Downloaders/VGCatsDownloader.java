package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

public class VGCatsDownloader extends Downloader {

	private static Pattern title = Pattern.compile("<title>(.*?)</title>", Pattern.DOTALL);
	private static Pattern imgData = Pattern.compile("<img src=\"archives.gif\".*?</table>.*?<img src=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("<a href=\"([^>]*?)\"><img src=\"back.gif\"", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("<a href=\"([^>]*?)\"><img src=\"next.gif\"", Pattern.DOTALL);
	
	
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

	protected String getBaseComicURL() {
		return "http://www.vgcats.com/comics/";
	}
	
	protected String getBasePrevNextURL() {
		return "http://www.vgcats.com/comics/";
	}
}
