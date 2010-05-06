package com.vasken.comics.Downloaders;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.vasken.util.WebRequester;

public class AchewoodDownloader extends Downloader {
	/*<h2 id="comic_navigation">
	<span class="left"><a href="index.php?date=04222010" class="dateNav" title="Previous comic">&laquo;</a></span>
	<span class="date">April 26, 2010</span>
	<span class="right"><a href="index.php" class="dateNav" title="Next comic">&raquo;</a></span>
	</h2>

	<p id="comic_body">
	<a href="http://m.assetbar.com/achewood/one_strip?b=M%5ea11f09b8576e606bcb5038dfdb92fb821&amp;u=http%3A%2F%2Fachewood.com%2Fcomic.php%3Fdate%3D04262010"><img src="http://m.assetbar.com/achewood/autaux?b=M%5ea11f09b8576e606bcb5038dfdb92fb821&amp;u=http%3A%2F%2Fachewood.com%2Fcomic.php%3Fdate%3D04262010" title="Home is where the nosebag is. "
	 class="comic" alt="Comic for April 26, 2010" /></a><br/>
	</p>*/
	
	private Pattern imgData = Pattern.compile( "<p id=\"comic_body\">.*?<img src=\"(.*?)\" title=\"(.*?)\"", Pattern.DOTALL);
	private Pattern prevComic = Pattern.compile("id=\"comic_navigation\">.*?<span class=\"left\">.*?<a href=\"(.*?)\"", Pattern.DOTALL);
	private Pattern nextComic = Pattern.compile("id=\"comic_navigation\">.*?<span class=\"right\">.*?<a href=\"(.*?)\"", Pattern.DOTALL);
	private Pattern date = Pattern.compile("<span class=\"date\">(.*?)</span>", Pattern.DOTALL);
	
	@Override
	public boolean handlePartialResponse(StringBuilder responseSoFar) {
		Log.d(this.getClass().getName(),"PARSING...");
		if (responseSoFar.length() > 0) {
			Matcher m = imgData.matcher(responseSoFar);
			if (m.find()) {
				Log.d("HEY", "WE HAVE A WINNER!");
				comic = newComic();
				try {
					comic.image = WebRequester.bitmapFromUrl(m.group(1).replaceAll("&amp;", "&"));
					comic.altText = m.group(2);
					m = nextComic.matcher(responseSoFar);
					if (m.find()) {
						if (m.group(1).contains("index.php")) {
							comic.nextUrl = "http://www.achewood.com/" + m.group(1);
							Log.d("NEXT URL", comic.nextUrl);
						}
					}
					m = prevComic.matcher(responseSoFar);
					if (m.find()) {
						comic.prevUrl = "http://www.achewood.com/" + m.group(1);
						Log.d("PREV URL", comic.prevUrl);
					}
					m = date.matcher(responseSoFar);
					if (m.find()) {
						comic.title = m.group(1);
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
