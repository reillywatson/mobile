package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

import android.util.Log;

public class ComicsDotComDownloader extends Downloader {
	/*<div class="STR_Container FirstStrip" rel="{StripID:317327, ComicID:69, Type:'Comic', DateStrip:'2010-05-03', 
	   URL_Comic: 'peanuts', Link_Previous: '/peanuts/2010-05-02/', Link_Next: '/peanuts/2010-05-04/'}">
	<div class="STR_Comic">
				<a href="/zoom/317327/" target="_blank" class="STR_Zoom" title="Click to View Full Size"><span>zoom</span></a>
				<a href="/peanuts/2010-05-03/" class="STR_StripImage" title="Peanuts - May 3, 2010">
				<img src="http://c0389161.cdn.cloudfiles.rackspacecloud.com/dyn/str_strip/317327.full.gif" border="0" onload="STR.AttachZoomHover($(this));" alt="Peanuts - May 3, 2010" width="640" /></a>
	 */
	
	private static Pattern imgData = Pattern.compile( "<div class=\"STR_Comic\">.*?<img src=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("<div class=\"STR_Container FirstStrip\" .*? Link_Previous: '(.*?)'", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("<div class=\"STR_Container FirstStrip\" .*? Link_Next: '(.*?)'", Pattern.DOTALL);
	private static Pattern title = Pattern.compile("<div class=\"STR_Container FirstStrip\" .*? DateStrip:'(.*?)'", Pattern.DOTALL);
	
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
		return "http://www.comics.com";
	}
	
	@Override
	protected boolean parsePermalink(StringBuilder partialResponse) {
		if (comic.title != null) {
			comic.permalink = url + "/" + comic.title + "/";
			Log.d("PERMALINK", comic.permalink);
			return true;
		}
		return true;
	}
}
