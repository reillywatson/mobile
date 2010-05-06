package com.vasken.comics.Downloaders;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.vasken.util.WebRequester;

public class ASofterWorldDownloader extends Downloader {
	/*
	<span class="rss-content">
	<font size=+0>
				<img src="http://www.asofterworld.com/clean/redrighthand.jpg" title="Marvel:SVU" BORDER=0> 
	<br> 
				<table width=700><tr><td align="left">
	<img src="http://www.asofterworld.com/dopeoplereallystillusespacers.jpg">
				<a STYLE="text-decoration: none" href="http://www.asofterworld.com/index.php?id=554">back</a>
	</td><td>
			<center> 

	<a href="http://www.topatoco.com/merchant.mvc?Screen=PROD&Store_Code=TO&Product_Code=ASW-PRINTS&Category_Code=ASW&Product_Attributes[1]:value=555" style="text-decoration:none" target="_blank"><font color="green">buy this print  </a></font>
	<font size=-3><img src="http://www.asofterworld.com/dopeoplereallystillusespacers.jpg">
	<a STYLE="text-decoration: none" href="http://digg.com/submit?http://www.asofterworld.com/index.php?id=555" target="_new"><img src="http://www.asofterworld.com/digg.gif" border=0> digg </a>  <a STYLE="text-decoration: none" href="http://www.facebook.com/sharer.php?u=http://www.asofterworld.com/index.php?id=555" target="_new"><img src="http://www.asofterworld.com/facebook.gif" border=0> facebook </a>  <a STYLE="text-decoration: none" href="http://reddit.com/submit?url=http://www.asofterworld.com/index.php?id=555" target="_new"><img src="http://www.asofterworld.com/reddit.gif" border=0> reddit </a>  <a STYLE="text-decoration: none" href="http://www.stumbleupon.com/submit?url=http://www.asofterworld.com/index.php?id=555" target="_new"><img src="http://www.asofterworld.com/stumbleupon.gif" border=0> stumbleupon </a>  </font> </center>

	</td><td align="right">

				<a STYLE="text-decoration: none" href="http://www.asofterworld.com/index.php?id=555">next</a>*/
	
	// Ooh, the Softer World HTML is brutal, these regexes seem pretty brittle but they're the best I can muster under the circumstances
	private Pattern comicData = Pattern.compile("<span class=\"rss-content\">.*?<img src=\"(.*?)\" title=\"(.*?)\"", Pattern.DOTALL);
	private Pattern title = Pattern.compile("<TITLE>A Softer World: (.*?)</TITLE>", Pattern.DOTALL);
	private Pattern prevComic = Pattern.compile("<span class=\"rss-content\">.*?dopeoplereallystillusespacers.*?href=\"(.*?)\"", Pattern.DOTALL);
	private Pattern nextComic = Pattern.compile("http://www.asofterworld.com/stumbleupon.gif.*?href=\"(.*?)\"", Pattern.DOTALL);
	
	@Override
	public boolean handlePartialResponse(StringBuilder responseSoFar) {
		Log.d(this.getClass().getName(),"PARSING...");
		if (responseSoFar.length() > 8000) {
			Matcher m = comicData.matcher(responseSoFar);
			if (m.find()) {
				Log.d("HEY", "WE HAVE A WINNER!");
				comic = newComic();
				try {
					comic.image = WebRequester.bitmapFromUrl(m.group(1));
					comic.altText = m.group(2);
					Log.d("ALT TEXT", comic.altText);
					m = nextComic.matcher(responseSoFar);
					if (m.find()) {
						comic.nextUrl = m.group(1);
						Log.d("NEXT URL", comic.nextUrl);
					}
					m = prevComic.matcher(responseSoFar);
					if (m.find() && !url.endsWith("=1")) {
						comic.prevUrl = m.group(1);
						Log.d("PREV URL", comic.prevUrl);
					}
					m = title.matcher(responseSoFar);
					if (m.find()) {
						comic.title = m.group(1);
						Log.d("TITLE", comic.title);
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
