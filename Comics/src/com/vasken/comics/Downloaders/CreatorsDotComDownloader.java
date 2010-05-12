package com.vasken.comics.Downloaders;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.vasken.util.WebRequester;

public class CreatorsDotComDownloader extends Downloader {
	/*
<div class="relative">
<a href="/comics/archie/61452.html" class="time_l two" style="width:48px;"><img src="/img_comics/arrow_l.gif" alt="">prev</a>
<a href="/comics/archie/61454.html" class="time_r two" style="width:48px;">next<img src="/img_comics/arrow_r.gif" alt=""></a>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td class="black1" colspan="2"><h1><span class="time">11 May</span><img src="/img_comics/time_line.gif" alt="" class="middle">CLASSICS COMICS<img src="/img_comics/arrow.gif" alt="" class="middle"><span class="title">Archie by Fernando Ruiz and Craig Boldman</span></h1></td>
</tr>
<tr>
<td class="nav"><a href="/">Home</a> > <a href="/comics.html">Comics</a> > <strong>Archie</strong></td>
<td width="50%"><img src="/img_comics/0.gif" alt=""></td>
</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="mb10">
<tr>
<td width="50%"><img src="/img_comics/0.gif" alt=""></td>
<td>
<table border="0" cellspacing="0" cellpadding="0" width="700">
<tr>
<td align="center">
<img src="/comics/34/61453_thumb.gif" alt="" ></td>
</tr>
<tr>
<td height="8"><img src="/img_comics/0.gif" alt=""></td>
</tr>
</table>*/
	
	
	private Pattern date = Pattern.compile("<span class=\"time\">(.*?)</span>", Pattern.DOTALL);
	private Pattern imgData = Pattern.compile( "img src=\"/comics/(.*?)\"", Pattern.DOTALL);
	private Pattern prevComic = Pattern.compile("<div class=\"relative\">.*?<a href=\"(.*?)\"", Pattern.DOTALL);
	private Pattern nextComic = Pattern.compile("arrow_l.gif.*?<a href=\"(.*?)\"", Pattern.DOTALL);
	
	@Override
	public boolean handlePartialResponse(StringBuilder responseSoFar) {
		Log.d(this.getClass().getName(),"PARSING...");
		if (responseSoFar.length() > 0) {
			Matcher m = imgData.matcher(responseSoFar);
			Matcher prevComicMatcher = prevComic.matcher(responseSoFar);
			Matcher nextComicMatcher = nextComic.matcher(responseSoFar);
			boolean hasNext = nextComicMatcher.find();
			boolean hasPrev = prevComicMatcher.find();
			if (m.find() && (hasNext || hasPrev)) {
				Log.d("HEY", "WE HAVE A WINNER!");
				comic = newComic();
				try {
					comic.image = WebRequester.bitmapFromUrl("http://www.creators.com/comics/" + m.group(1));
					m = date.matcher(responseSoFar);
					if (m.matches()) {
						comic.title = m.group(1);
						Log.d("TITLE", comic.title);
					}
					
					if (hasNext) {
						String next = nextComicMatcher.group(1);
						if (!next.equals("/")) {
							comic.nextUrl = "http://www.creators.com" + nextComicMatcher.group(1);
							Log.d("NEXT URL", comic.nextUrl);
						}
					}
					
					if (hasPrev) {
						// we rely on arrow_l.gif existing to get our "next" link, but if it's the first comic, arrow_.gif isn't there,
						// and there isn't a previous link in that case
						if (!hasNext && !responseSoFar.toString().contains("arrow_l.gif")) {
							comic.nextUrl = "http://www.creators.com" + prevComicMatcher.group(1);
							Log.d("NEXT URL", comic.nextUrl);
						}
						else {
							comic.prevUrl = "http://www.creators.com" + prevComicMatcher.group(1);
							Log.d("PREV URL", comic.prevUrl);
						}
					}
				} catch (IOException e) {
					Log.d(this.getClass().getName(), "Retrieving image for comic failed!");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}
		}
		return false;
	}
}
