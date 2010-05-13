package com.vasken.comics.Downloaders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AchewoodDownloader extends Downloader {
	/*<h2 id="comic_navigation">
	<span class="left"><a href="index.php?date=04222010" class="dateNav" title="Previous comic">&laquo;</a></span>
	<span class="date">April 26, 2010</span>
	<span class="right"><a href="index.php" class="dateNav" title="Next comic">&raquo;</a></span>
	</h2>

	<p id="comic_body">
	<a href="http://m.assetbar.com/achewood/one_strip?b=M%5ea11f09b8576e606bcb5038dfdb92fb821&amp;u=http%3A%2F%2Fachewood.com%2Fcomic.php%3Fdate%3D04262010"><img src="http://m.assetbar.com/achewood/autaux?b=M%5ea11f09b8576e606bcb5038dfdb92fb821&amp;u=http%3A%2F%2Fachewood.com%2Fcomic.php%3Fdate%3D04262010" title="Home is where the nosebag is. "
	 class="comic" alt="Comic for April 26, 2010" /></a><br/>
	</p>*/
	
	private static Pattern imgData = Pattern.compile( "<p id=\"comic_body\">.*?<img src=\"(.*?)\" title=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("id=\"comic_navigation\">.*?<span class=\"left\"><a href=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("id=\"comic_navigation\">.*?<span class=\"right\"><a href=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern date = Pattern.compile("<span class=\"date\">(.*?)</span>", Pattern.DOTALL);

	
	protected Pattern getComicPattern() {
		return imgData;
	}

	protected Pattern getNextComicPattern() {
		return nextComic;
	}
	protected Pattern getPrevComicPattern() {
		return prevComic;
	}
	protected Pattern getTitlePattern() {
		return date;
	}
	
	@Override
	protected boolean parseComic(StringBuilder partialResponse) {
		Matcher m = imgData.matcher(partialResponse);
		if (m.find()) {
			comic.image = m.group(1).replaceAll("&amp;", "&");
			comic.altText = m.group(2);
			return true;
		}
		return false;
	}
	
	@Override
	protected String getBasePrevNextURL() {
		return "http://www.achewood.com/";
	}
	
}
