package com.vasken.comics.Downloaders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class PvPDownloader extends Downloader {

	/*<div id="comic">
	<img src="http://www.pvponline.com/comics/pvp20100505.png" alt="Guest Strip by Danielle Corsetto" title="Guest Strip by Danielle Corsetto" />
	</div>
	<div id="navbar">
		<div id="navbar-previous">
			<a href="http://www.pvponline.com/2010/05/04/guest-strip-by-stephen-mccranie/" rel="prev">&lsaquo; Previous</a>		</div>
		<div id="navbar-date">
			2010 | May 5		</div>
		<div id="navbar-title-wrap">
			<a href="http://www.pvponline.com/2010/05/05/guest-strip-by-danielle-corsetto-2/" rel="bookmark" title="Permanent Link to Guest Strip by Danielle Corsetto" id="navbar-title">Guest Strip by Danielle Corsetto</a>
		</div>
		<div id="navbar-next">
			<a href="http://www.pvponline.com/2010/05/06/guest-strip-by-karl-kerschl-2/" rel="next">&rsaquo; Next</a>		</div>
	</div>
	*/
	private Pattern comicPattern = Pattern.compile( "<div id=\"comic\">.*?<img src=\"(.*?)\".*?title=\"(.*?)\"", Pattern.DOTALL);
	private Pattern prevComic = Pattern.compile("<div id=\"navbar-previous\">.*?<a href=\"(.*?)\"", Pattern.DOTALL);
	private Pattern nextComic = Pattern.compile("<div id=\"navbar-next\">.*?<a href=\"(.*?)\"", Pattern.DOTALL);
	
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
				comic.image = m.group(1);
				comic.title = m.group(2);
				if (hasPrev) {
					comic.prevUrl = prev.group(1);
					Log.d("PREV URL", comic.prevUrl);
				}
				if (hasNext) {
					String nextUrl = next.group(1);
					if (nextUrl.contains("pvponline.com")) {
						comic.nextUrl = next.group(1);
						Log.d("NEXT URL", comic.nextUrl);
					}
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
