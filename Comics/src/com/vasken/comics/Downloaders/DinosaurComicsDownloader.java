package com.vasken.comics.Downloaders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class DinosaurComicsDownloader extends Downloader {
	
	// Fragments (Ryan North hates line breaks!):
	//<span class="rss-content"><img src="http://www.qwantz.com/comics/comic2-1729.png" class="comic" title="BUT SINCE YOU'RE HERE LET US ASK TWO QUESTIONS: DO YOU THINK NON-REFUSAL IS A PROPER SUBCLASS OF ACCEPTANCE, AND HOW COME YOU SAY &quot;FRIG&quot; SO MUCH"></span>
	//<div id="comic"><div id="previous"><!-- <span class="rss-content"><tr><td width=100 align="left" valign="top"></span> --><span class="rss-content"><a href="http://www.qwantz.com/index.php?comic=1704">&larr; previous</a></span><!-- <span class="rss-content"></td></span> --></div><div id="date"><!-- <span class="rss-content"><td width=* align="center" valign="top"></span> --><span class="rss-content">May 4th, 2010</span><!-- <span class="rss-content"></td></span> --> - <a href="#" onClick="document.getElementById('sharebox').style.display='block';return false;">share this comic with your friends!</a></div><div id="next"><!-- <span class="rss-content"><td width=100 align="right" valign="top"></span> --><span class="rss-content"><a href="http://www.qwantz.com/index.php?comic=1706">

	private Pattern comicData = Pattern.compile("<div id=\"comic\">.*?<img src=\"(.*?)\" class=\"comic\" title=\"(.*?)\"", Pattern.DOTALL);
	private Pattern title = Pattern.compile("<title>Dinosaur Comics - (.*?) - awesome fun times!</title>", Pattern.DOTALL);
	private Pattern prevComic = Pattern.compile("<div id=\"previous\">.*?<a href=\"(.*?)\">", Pattern.DOTALL);
	private Pattern nextComic = Pattern.compile("<div id=\"next\">.*?<a href=\"(.*?)\">", Pattern.DOTALL);
	
	@Override
	public boolean handlePartialResponse(StringBuilder responseSoFar) {
		Log.d(this.getClass().getName(),"PARSING...");
		if (responseSoFar.length() > 8000) {
			Matcher m = comicData.matcher(responseSoFar);
			if (m.find()) {
				Log.d("HEY", "WE HAVE A WINNER!");
				comic = newComic();
				comic.image =m.group(1);
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
