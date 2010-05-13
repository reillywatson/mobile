package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

public class CtrlAltDelDownloader extends Downloader {

	/*
	<img src="/comics/cad/20100512.jpg" alt="Stacked deck" />
	
	<div class="navigation">
		<a href="/cad/20100510" class="nav-back">Back</a>
		<a href="/cad/20021023" class="nav-first">First</a>
		<a href="http://www.cad-forums.com/showthread.php?t=107927" class="nav-comment">Comment</a>
		<a href="http://www.addthis.com/bookmark.php" onclick="addthis_url = location.href; addthis_title = document.title; return addthis_click(this);" target="_blank" class="nav-share">Share</a>
		<a class="nav-bookmark-none" title="Bookmark this comic to save your place.">Bookmark</a>
		<a href="/cad/archive" class="nav-archives">Archives</a>
		<a href="/cad/random" class="nav-random">Random</a>
		<a href="/cad/" class="nav-last">Last</a>
		<a href="/" class="nav-next">Next</a>
	</div>*/
	
	private static Pattern title = Pattern.compile("<img [^>]*? alt=\"([^>]*?)\" />[^<]*?<div class=\"navigation\"");
	private static Pattern imgData = Pattern.compile("<img src=\"([^>]*?)\"[^<]*?<div class=\"navigation\"", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("<a href=\"([^>]*?)\" class=\"nav-back\"", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("<a href=\"([^>]*?)\" class=\"nav-next\"", Pattern.DOTALL);
	
	
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
		return "http://www.cad-comic.com";
	}
	
	@Override
	protected String getBaseComicURL() {
		return "http://www.cad-comic.com";
	}
}
