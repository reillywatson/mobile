package com.vasken.comics.Downloaders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	

	protected Pattern getComicPattern() {
		return imgData;
	}

	protected Pattern getNextComicPattern() {
		return nextComic;
	}
	protected Pattern getPrevComicPattern() {
		return prevComic;
	}
	
	@Override
	protected boolean parseComic(StringBuilder partialResponse) {
		Matcher m = imgData.matcher(partialResponse);
		if (m.find()) {
			comic.image = m.group(1);
			comic.title = m.group(2);
			return true;
		}
		return false;
	}
	
	@Override
	protected String getBasePrevNextURL() {
		return "http://www.penny-arcade.com";
	}
}
