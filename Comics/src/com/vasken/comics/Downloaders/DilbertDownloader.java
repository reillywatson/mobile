package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

public class DilbertDownloader extends Downloader {
	
	/*
<div class="STR_Calendar">
<a href="/2010-05-04/" class="STR_Prev PNG_Fix"><span>Previous</span></a>

<div class="STR_DateStrip">May 5, 2010</div>

<a href="/2010-05-06/" class="STR_Next PNG_Fix"><span>Next</span></a>
<div class="STR_Image">
<a href="/strips/comic/2010-05-05/"><img src="/dyn/str_strip/000000000/00000000/0000000/000000/80000/9000/000/89035/89035.strip.gif" border="0" />
*/
	private Pattern imgData = Pattern.compile( "<div class=\"STR_Image\">.*?<img src=\"(.*?)\"", Pattern.DOTALL);
	private Pattern prevComic = Pattern.compile("<div class=\"STR_Calendar\">.*?<a href=\"(.*?)\"", Pattern.DOTALL);
	private Pattern nextComic = Pattern.compile("<div class=\"STR_DateStrip\">.*?<a href=\"(.*?)\" class=\"STR_Next", Pattern.DOTALL);
	private Pattern date = Pattern.compile("<div class=\"STR_DateStrip\">(.*?)</div>", Pattern.DOTALL);
	

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
		return date;
	}
	
	@Override
	protected String getBaseComicURL() {
		return "http://www.dilbert.com";
	}
	
	@Override
	protected String getBasePrevNextURL() {
		return "http://www.dilbert.com";
	}
}
