package com.vasken.comics.Downloaders;

import java.io.IOException;
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

	
	private Pattern prevComic = Pattern.compile("<a href=\"(.*?)\"><< Previous</a>", Pattern.DOTALL);
	private Pattern nextComic = Pattern.compile("<a href=\"(.*?)\">Next >></a>", Pattern.DOTALL);
	private Pattern imgData = Pattern.compile( "http://imgsrv.gocomics.com/dim/\\?fh=(.*?)\"", Pattern.DOTALL);

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
						comic.nextUrl = "http://www.gocomics.com" + m.group(1);
					}
					m = prevComic.matcher(responseSoFar);
					if (m.find()) {
						comic.prevUrl = "http://www.gocomics.com" + m.group(1);
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
