package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

public class LookingForGroupDownloader extends Downloader {

	/*
	<td><a href="/page/1"><img alt="First" title="First" src="http://images.lfgcomic.com/comic-nav-top_0_0.jpg" class="hover" style="width: 104px;  height: 40px;" /></a></td>
	<td><a href="/page/355"><img alt="Previous" title="Previous" src="http://images.lfgcomic.com/comic-nav-top_0_1.jpg" class="hover" style="width: 127px; height: 40px;" /></a></td>

	<td><a href="/issues.php"><img alt="Archives" title="Archives" src="http://images.lfgcomic.com/comic-nav-top_0_2.jpg" class="hover" style="width: 120px; height: 40px;" /></a></td>
	<td><a href="#"><img alt="Next" title="Next" src="http://images.lfgcomic.com/comic-nav-top_0_3.jpg" class="hover" style="width: 95px; height: 40px;" /></a></td>
	<td><a href="/page/356"><img alt="Last" title="Last" src="http://images.lfgcomic.com/comic-nav-top_0_4.jpg" class="hover" style="width: 102px;  height: 40px;" /></a></td>
	*/
	private static Pattern title = Pattern.compile("<title>Looking For Group &raquo; (.*?)</title>");
	private static Pattern imgData = Pattern.compile("id=\"comic\">.*?<img src=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("<a href=\"([^>]*?)\"><img alt=\"Previous\"", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("<a href=\"([^>]*?)\"><img alt=\"Next\"", Pattern.DOTALL);
	
	
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
	protected String getBasePrevNextURL() {
		return "http://www.lfgcomic.com";
	}

}
