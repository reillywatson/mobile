package com.vasken.comics.Downloaders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class XKCDDownloader extends Downloader {
	
	// Fragments:
	// <title>xkcd: Cemetery</title>
	//<h3>Image URL (for hotlinking/embedding): http://imgs.xkcd.com/comics/cemetery.png</h3>
	//<div class="menuCont">
	// <ul>
	//  <li><a href="/1/">|&lt;</a></li>
	//  <li><a href="/732/" accesskey="p">&lt; Prev</a></li>
	//  <li><a href="http://dynamic.xkcd.com/random/comic/" id="rnd_btn_t">Random</a></li>
	//  <li><a href="/734/" accesskey="n">Next &gt;</a></li>
	//  <li><a href="/">&gt;|</a></li>
	// </ul>
	//</div>
	private Pattern imgData = Pattern.compile( "<h3>Image URL \\(for hotlinking/embedding\\): (.*?)</h3>", Pattern.DOTALL);
	private Pattern title = Pattern.compile("<title>xkcd: (.*?)</title>", Pattern.DOTALL);
	private Pattern prevComic = Pattern.compile("<div class=\"menuCont\">.*?\\|&lt;.*?<a href=\"(.*?)\" accesskey=\"p\">", Pattern.DOTALL);
	private Pattern nextComic = Pattern.compile("<div class=\"menuCont\">.*?Random.*?<a href=\"(.*?)\" accesskey=\"n\">", Pattern.DOTALL);
	private Pattern altText = Pattern.compile("<img.*? title=\"(.*?)\".*?/>");
	
	@Override
	public boolean handlePartialResponse(StringBuilder responseSoFar) {
		Log.d(this.getClass().getName(),"PARSING...");
		if (responseSoFar.length() > 0) {
			Matcher m = imgData.matcher(responseSoFar);
			if (m.find()) {
				Log.d("HEY", "WE HAVE A WINNER!");
				comic = newComic();
				comic.image = m.group(1);
				m = nextComic.matcher(responseSoFar);
				if (m.find()) {
					String next = m.group(1);
					if (!next.equals("#")) {
						comic.nextUrl = "http://xkcd.com" + next;
						Log.d("NEXT URL", comic.nextUrl);
					}
				}
				m = prevComic.matcher(responseSoFar);
				if (m.find()) {
					String prev = m.group(1);
					if (!prev.equals("#")) {
						comic.prevUrl = "http://xkcd.com" + prev;
						Log.d("PREV URL", comic.prevUrl);
					}
				}
				m = title.matcher(responseSoFar);
				if (m.find()) {
					comic.title = m.group(1);
				}
				m = altText.matcher(responseSoFar);
				if (m.find()) {
					comic.altText = m.group(1);
					Log.d("ALT TEXT", comic.altText);
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
