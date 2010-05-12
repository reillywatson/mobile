package com.vasken.comics.Downloaders;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.vasken.util.WebRequester;

public class AwkwardZombieDownloader extends Downloader {
	/*<p><font size="3" color="#000000" face="verdana">
	<p><center>Masturcation (Part1)</center>


	<p><img src="http://i49.photobucket.com/albums/f278/katietiedrich/comic139.png">
	<center>
	<p><a href="http://www.awkwardzombie.com/comic1-092006.php">
	<img border="0" src="http://www.awkwardzombie.com/comnav1_1.gif"></a>
	<a href="http://www.awkwardzombie.com/comic1-011810.php">
	<img border="0" src="http://www.awkwardzombie.com/comnav1_2.gif"></a>
	<a href="http://www.awkwardzombie.com/archive1.php">
	<img border="0" src="http://www.awkwardzombie.com/comnav1_3.gif"></a>
	<a href="http://www.awkwardzombie.com/comic1-020110.php">
	<img border="0" src="http://www.awkwardzombie.com/comnav1_4.gif"></a>
	<a href="http://www.awkwardzombie.com/comic1.php">
	<img border="0" src="http://www.awkwardzombie.com/comnav1_5.gif"></a>
	</center>*/
	
	private Pattern comicPattern = Pattern.compile( "<font size=\"3\".*?<center>(.*?)</center>.*?<img src=\"(.*?)\"", Pattern.DOTALL);
	private Pattern prevComic = Pattern.compile("<a href=\"([^>]*?)\">\\s*<img border=\"0\" src=\"http://www.awkwardzombie.com/comnav1_2.gif\"", Pattern.DOTALL);
	private Pattern nextComic = Pattern.compile("<a href=\"([^>]*?)\">\\s*<img border=\"0\" src=\"http://www.awkwardzombie.com/comnav1_4.gif\"", Pattern.DOTALL);
	
	@Override
	public boolean handlePartialResponse(StringBuilder responseSoFar) {
		Log.d(this.getClass().getName(),"PARSING...");
		if (responseSoFar.length() > 0) {
			// We have to match on all 3 of these, because this is one of the odd comics where the navigation is on the bottom
			Matcher m = comicPattern.matcher(responseSoFar);
			Matcher prev = prevComic.matcher(responseSoFar);
			Matcher next = nextComic.matcher(responseSoFar);
			boolean hasPrev = prev.find();
			boolean hasNext = next.find();
			if (m.find() && (hasPrev || hasNext)) {
				Log.d("HEY", "WE HAVE A WINNER!");
				comic = newComic();
				try {
					comic.image = WebRequester.bitmapFromUrl(m.group(2));
					comic.title = m.group(1);
					if (hasPrev) {
						String prevUrl = prev.group(1);
						//prevUrl = prevUrl.substring(prevUrl.lastIndexOf("http://"));
						comic.prevUrl = prevUrl;
						Log.d("PREV URL", comic.prevUrl);
					}
					if (hasNext) {
						String nextUrl = next.group(1);
						//nextUrl = nextUrl.substring(nextUrl.lastIndexOf("http://"));
						comic.nextUrl = nextUrl;
						Log.d("NEXT URL", comic.nextUrl);
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
