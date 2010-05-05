package com.vasken.comics.Downloaders;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.vasken.comics.Comic;
import com.vasken.util.WebRequester;

public class GoComicsDownloader extends Downloader {
	
	  //<li><a href="/foxtrot/2010/04/25/" class="prev">Previous feature</a></li>        <li><a href="#" class="cal" onclick="toggleCal(); return false">Show Calendar</a><div id="calDiv"></div></li>
      //       </ul>

   // <p class="feature_item"><a href = 'http://imgsrv.gocomics.com/dim/?fh=0a7c63547d207b37b752fe41ecc9364f&w=900.0'><img alt="FoxTrot" height="422" src="http://imgsrv.gocomics.com/dim/?fh=0a7c63547d207b37b752fe41ecc9364f" width="600" /></a> </p>

	private Pattern prevComic = Pattern.compile("<a href=\"(.*?)\" class=\"prev\">", Pattern.DOTALL);
	private Pattern nextComic = Pattern.compile("<a href=\"(.*?)\" class=\"next\">", Pattern.DOTALL);
	private Pattern imgData = Pattern.compile( "<p class=\"feature_item\">.*?<img .*? src=\"(.*?)\"", Pattern.DOTALL);

	
	@Override
	public boolean handlePartialResponse(StringBuilder responseSoFar) {
		if (responseSoFar.length() > 9000) {
			Matcher m = imgData.matcher(responseSoFar);
			if (m.find()) {
				comic = new Comic();
				try {
					comic.image = WebRequester.bitmapFromUrl(m.group(1));
					m = nextComic.matcher(responseSoFar);
					if (m.find()) {
						comic.nextUrl = m.group(1);
					}
					m = prevComic.matcher(responseSoFar);
					if (m.find()) {
						comic.prevUrl = m.group(1);
					}
				} catch (IOException e) {
					Log.d(this.getClass().getName(), "Retrieving image for comic failed!");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}
		}
		// TODO Auto-generated method stub
		return false;
	}

}
