package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

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
	private Pattern altText = Pattern.compile("<img.*? title=\"(.*?)\".*?/>", Pattern.DOTALL);
	private Pattern permalink = Pattern.compile("Permanent link to this comic: (.*?)</h3>", Pattern.DOTALL);
	@Override
	protected Pattern getComicPattern() {
		return imgData;
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
	protected Pattern getAltTextPattern() {
		return altText;
	}
	
	@Override
	protected Pattern getPermalinkPattern() {
		return permalink;
	}
	
	@Override
	protected String getBasePrevNextURL() {
		return "http://xkcd.com";
	}
}
