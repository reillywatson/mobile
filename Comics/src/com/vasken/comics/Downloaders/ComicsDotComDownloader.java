package com.vasken.comics.Downloaders;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.vasken.util.WebRequester;

public class ComicsDotComDownloader extends Downloader {
	/*<div class="STR_Container FirstStrip" rel="{StripID:317327, ComicID:69, Type:'Comic', DateStrip:'2010-05-03', 
	   URL_Comic: 'peanuts', Link_Previous: '/peanuts/2010-05-02/', Link_Next: '/peanuts/2010-05-04/'}">
	<div class="STR_Comic">
				<a href="/zoom/317327/" target="_blank" class="STR_Zoom" title="Click to View Full Size"><span>zoom</span></a>
				<a href="/peanuts/2010-05-03/" class="STR_StripImage" title="Peanuts - May 3, 2010">
				<img src="http://c0389161.cdn.cloudfiles.rackspacecloud.com/dyn/str_strip/317327.full.gif" border="0" onload="STR.AttachZoomHover($(this));" alt="Peanuts - May 3, 2010" width="640" /></a>
	 */
	
	private Pattern imgData = Pattern.compile( "<div class=\"STR_Comic\">.*?<img src=\"(.*?)\"", Pattern.DOTALL);
	private Pattern stripInfo = Pattern.compile("<div class=\"STR_Container FirstStrip\".*?DateStrip:'(.*?)'.*?Link_Previous: '(.*?)', Link_Next: '(.*?)'", Pattern.DOTALL);
	
	@Override
	public boolean handlePartialResponse(StringBuilder responseSoFar) {
		Log.d(this.getClass().getName(),"PARSING...");
		if (responseSoFar.length() > 0) {
			Matcher m = imgData.matcher(responseSoFar);
			if (m.find()) {
				Log.d("HEY", "WE HAVE A WINNER!");
				comic = newComic();
				comic.image = m.group(1);
				m = stripInfo.matcher(responseSoFar);
				if (m.find()) {
					comic.title = m.group(1);
					if (m.group(2).length() > 0) {
						comic.prevUrl = "http://www.comics.com" + m.group(2);
					}
					if (m.group(3).length() > 0) {
						comic.nextUrl = "http://www.comics.com" + m.group(3);
					}
				}
				return true;
			}
		}
		return false;
	}

	
	@Override
	protected Pattern getComicPattern() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Pattern getNextComicPattern() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Pattern getPrevComicPattern() {
		// TODO Auto-generated method stub
		return null;
	}
}
