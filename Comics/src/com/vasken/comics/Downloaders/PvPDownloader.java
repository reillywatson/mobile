package com.vasken.comics.Downloaders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private Pattern prevComic = Pattern.compile("<div id=\"navbar-previous\">[^<]*?<a href=\"(.*?)\" rel=\"prev\"", Pattern.DOTALL);
	private Pattern nextComic = Pattern.compile("<div id=\"navbar-next\">.*?<a href=\"(.*?)\" rel=\"next\"", Pattern.DOTALL);

	@Override
	protected boolean parseComic(StringBuilder partialResponse) {
		Matcher m = comicPattern.matcher(partialResponse);
		if (m.find()) {
			comic.image = m.group(1);
			comic.title = m.group(2);
			return true;
		}
		return false;
	}
	
	@Override
	protected Pattern getComicPattern() {
		return comicPattern;
	}

	@Override
	protected Pattern getNextComicPattern() {
		return nextComic;
	}

	@Override
	protected Pattern getPrevComicPattern() {
		return prevComic;
	}
}
