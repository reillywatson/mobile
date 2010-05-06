package com.vasken.comics.Downloaders;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.vasken.util.WebRequester;

public class GoComicsDownloader extends Downloader {
	
	public GoComicsDownloader(String url) {
		super(url);
	}
	
	// Sample HTML fragment:
	//<img alt="?fh=10b07e5a5bf87622b7e6ccc8ea3fe9b3" src="http://imgsrv.gocomics.com/dim/?fh=10b07e5a5bf87622b7e6ccc8ea3fe9b3" width="100%" />
	//<div><span class="authorText"><strong>Bill Amend</strong>April 25, 2010</span>
	//<span class="archiveText"><a href="/foxtrot/2010/04/18/"><< Previous</a><a href="/foxtrot/2010/05/02/">Next >></a></span></div>

	
	private Pattern prevComic = Pattern.compile("<span class=\"archiveText\">.*?<a href=\"(.*?)\"><< Previous</a>", Pattern.DOTALL);
	private Pattern nextComic = Pattern.compile("<span class=\"archiveText\">.*?<a href=\"(.*?)\">Next >></a>", Pattern.DOTALL);
	private Pattern imgData = Pattern.compile( "http://imgsrv.gocomics.com/dim/\\?fh=(.*?)\"", Pattern.DOTALL);
	private Pattern date = Pattern.compile("<span class=\"authorText\">.*?</strong>(.*?)</span>", Pattern.DOTALL);
	@Override
	public boolean handlePartialResponse(StringBuilder responseSoFar) {
		Log.d(this.getClass().getName(),"PARSING...");
	//	Log.d("hey", responseSoFar.toString());
		if (responseSoFar.length() > 0) {
			Matcher m = imgData.matcher(responseSoFar);
			if (m.find()) {
				Log.d("HEY", "WE HAVE A WINNER!");
				comic = newComic();
				try {
					comic.image = WebRequester.bitmapFromUrl("http://imgsrv.gocomics.com/dim?fh=" + m.group(1));
					m = nextComic.matcher(responseSoFar);
					if (m.find()) {
						String next = m.group(1);
						String[] parts = next.split("\"");
						if (parts.length > 0) {
							next = parts[parts.length - 1];
							comic.nextUrl = "http://www.gocomics.com" + next;
						}
						Log.d("NEXT URL", comic.nextUrl);
					}
					m = prevComic.matcher(responseSoFar);
					if (m.find()) {
						comic.prevUrl = "http://www.gocomics.com" + m.group(1);
						Log.d("PREV URL", comic.prevUrl);
					}
					m = date.matcher(responseSoFar);
					if (m.find()) {
						try {
							comic.pubDate = DateFormat.getDateInstance(DateFormat.MEDIUM).parse(m.group(1));
							Log.d("DATE", DateFormat.getInstance().format(comic.pubDate));
						} catch (ParseException e) {
							Log.d(getClass().getName(), "FAILED TO PARSE DATE: " + m.group(1));
							// TODO Auto-generated catch block
							e.printStackTrace();
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
