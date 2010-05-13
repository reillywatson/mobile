package com.vasken.comics.Downloaders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

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
	private Pattern nextComic = Pattern.compile("<div class=\"STR_DateStrip\">.*?<a href=\"(.*?)\"", Pattern.DOTALL);
	private Pattern date = Pattern.compile("<div class=\"STR_DateStrip\">(.*?)</div>", Pattern.DOTALL);
	
	@Override
	public boolean handlePartialResponse(StringBuilder responseSoFar) {
		Log.d(this.getClass().getName(),"PARSING...");
		if (responseSoFar.length() > 0) {
			Matcher m = imgData.matcher(responseSoFar);
			if (m.find()) {
				Log.d("HEY", "WE HAVE A WINNER!");
				comic = newComic();
					comic.image = "http://www.dilbert.com" + m.group(1);
					m = nextComic.matcher(responseSoFar);
					if (m.find()) {
						comic.nextUrl = "http://www.dilbert.com" + m.group(1);
						Log.d("NEXT URL", comic.nextUrl);
					}
					m = prevComic.matcher(responseSoFar);
					if (m.find()) {
						comic.prevUrl = "http://www.dilbert.com" + m.group(1);
						Log.d("PREV URL", comic.prevUrl);
					}
					m = date.matcher(responseSoFar);
					if (m.find()) {
						comic.title = m.group(1);
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
