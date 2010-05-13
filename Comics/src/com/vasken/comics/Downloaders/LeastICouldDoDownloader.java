package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

public class LeastICouldDoDownloader extends Downloader {

	/* <td rowspan="3" id="comic">
	<img src="http://cdn.leasticoulddo.com/comics/20100512.gif" />
	<div id="nav-previous"><a href="/comic/20100511">Previous</a></div>
	<div id="nav-next"><a href="/">Next</a></div>*/
	
	private static Pattern imgData = Pattern.compile("id=\"comic\">.*?<img src=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("<div id=\"nav-previous\"><a href=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("<div id=\"nav-next\"><a href=\"(.*?)\"", Pattern.DOTALL);
	
	
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
		return "http://leasticoulddo.com";
	}
}
