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
	private Pattern randomComic = Pattern.compile("trex-omg.gif\".*?<a href=\"(.*?)\"", Pattern.DOTALL);
	
	@Override
	protected boolean parseComic(StringBuilder partialResponse) {
		Matcher m = comicData.matcher(partialResponse);
		if (m.find()) {
			comic.image = m.group(1);
			comic.altText = m.group(2);
			Log.d("IMAGE", comic.image);
			Log.d("ALT TEXT", comic.altText);
			return true;
		}
		return false;
	}
	
	@Override
	protected Pattern getComicPattern() {
		return comicData;
	}

	@Override
	protected Pattern getNextComicPattern() {
		return nextComic;
	}

	@Override
	protected Pattern getPrevComicPattern() {
		return prevComic;
	}

	@Override
	protected Pattern getTitlePattern() {
		return title;
	}
	
	@Override
	protected Pattern getRandomComicPattern() {
		return randomComic;
	}

}
