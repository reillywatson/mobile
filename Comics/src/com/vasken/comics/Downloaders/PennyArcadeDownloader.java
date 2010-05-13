package com.vasken.comics.Downloaders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class PennyArcadeDownloader extends Downloader {
	
	/*<li class="float_left first"><a href="/comic/1998/11/18/">First</a></li>
	<li class="float_left back"><a href="/comic/2010/4/28/">Back</a></li>
	<li class="float_left news"><a href="/2010/4/30/">News</a></li>
	<li class="float_left next"><a href="/comic/2010/5/3/">Next</a></li>
	<li class="float_left new"><a href="/comic/">New</a></li>
	<div class="body">
	<img src="http://art.penny-arcade.com/photos/849892997_oKHRu-O.png" alt="A Boy Must Learn, Part Three"   />
	</div>*/
	private Pattern imgData = Pattern.compile( "<div class=\"body\">.*?<img src=\"(.*?)\" alt=\"(.*?)\"", Pattern.DOTALL);
	private Pattern prevComic = Pattern.compile("<li class=\"float_left back\"><a href=\"(.*?)\">", Pattern.DOTALL);
	private Pattern nextComic = Pattern.compile("<li class=\"float_left next\"><a href=\"(.*?)\">", Pattern.DOTALL);
	
	@Override
	public boolean handlePartialResponse(StringBuilder responseSoFar) {
		Log.d(this.getClass().getName(),"PARSING...");
		if (responseSoFar.length() > 0) {
			Matcher m = imgData.matcher(responseSoFar);
			if (m.find()) {
				Log.d("HEY", "WE HAVE A WINNER!");
				comic = newComic();
				comic.image = m.group(1);
				comic.title = m.group(2);
				m = nextComic.matcher(responseSoFar);
				if (m.find()) {
					comic.nextUrl = "http://www.penny-arcade.com" + m.group(1);
					Log.d("NEXT URL", comic.nextUrl);
				}
				m = prevComic.matcher(responseSoFar);
				if (m.find()) {
					comic.prevUrl = "http://www.penny-arcade.com" + m.group(1);
					Log.d("PREV URL", comic.prevUrl);
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
