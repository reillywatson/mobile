package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

public class CreatorsDotComDownloader extends Downloader {
	/*
<div class="relative">
<a href="/comics/archie/61452.html" class="time_l two" style="width:48px;"><img src="/img_comics/arrow_l.gif" alt="">prev</a>
<a href="/comics/archie/61454.html" class="time_r two" style="width:48px;">next<img src="/img_comics/arrow_r.gif" alt=""></a>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td class="black1" colspan="2"><h1><span class="time">11 May</span><img src="/img_comics/time_line.gif" alt="" class="middle">CLASSICS COMICS<img src="/img_comics/arrow.gif" alt="" class="middle"><span class="title">Archie by Fernando Ruiz and Craig Boldman</span></h1></td>
</tr>
<tr>
<td class="nav"><a href="/">Home</a> > <a href="/comics.html">Comics</a> > <strong>Archie</strong></td>
<td width="50%"><img src="/img_comics/0.gif" alt=""></td>
</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="mb10">
<tr>
<td width="50%"><img src="/img_comics/0.gif" alt=""></td>
<td>
<table border="0" cellspacing="0" cellpadding="0" width="700">
<tr>
<td align="center">
<img src="/comics/34/61453_thumb.gif" alt="" ></td>
</tr>
<tr>
<td height="8"><img src="/img_comics/0.gif" alt=""></td>
</tr>
</table>*/
	
	
	private Pattern date = Pattern.compile("<span class=\"time\">(.*?)</span>", Pattern.DOTALL);
	private Pattern imgData = Pattern.compile( "img src=\"/comics/(.*?)\"", Pattern.DOTALL);
	private Pattern prevComic = Pattern.compile("<a href=\"([^>]*?)\" class=\"time_l two\"", Pattern.DOTALL);
	private Pattern nextComic = Pattern.compile("<a href=\"([^>]*?)\" class=\"time_r two\"", Pattern.DOTALL);
	
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
		return date;
	}
	
	@Override
	protected String getBaseComicURL() {
		return "http://www.creators.com/comics/";
	}
	
	@Override
	protected String getBasePrevNextURL() {
		return "http://www.creators.com";
	}
}
