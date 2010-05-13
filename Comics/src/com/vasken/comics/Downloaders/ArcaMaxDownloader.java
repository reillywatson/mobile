package com.vasken.comics.Downloaders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArcaMaxDownloader extends Downloader {
	/*
  <div class="toon" >
  
  <h1 style="margin:10px 0px 10px; font-size:22px" title="today's Zits comic"><img src="/images/pub/amuse/comics/zits_t.jpg" style="position:relative; float:left; margin-right:10px" width="50" height="50" alt="Daily Zits Comic" />Zits</h1>
  <p class="m0">
   <small>By Jerry Scott and Jim Borgman</small><br /><br />
   <a href="http://www.arcamax.com/newspics/11/1193/119306.gif" target="_blank"><img src="http://www.arcamax.com/newspics/11/1193/119306.gif" border="0px" alt="Zits Cartoon for 05/05/2010"></a>
   <br />Copyright 2010 ZITS Partnership.  Distributed by King Features Syndicate. 
	<p style="font-size:11px">
	  <a href="http://www.dailyink.com" target="_blank"><img src="http://www.arcamax.com/images/pub/amuse/di.gif" style="position:relative; top:-2px; float:left;margin-right:5px" border="0px" alt="" /></a><a href="http://www.dailyink.com" style="color:#000" target="_blank">Click here</a> for your favorite comics, puzzles and editorial cartoons from DailyINK. <br /><br />
	</p>
 
  </p>	 
 </div>
	 <a class="next" href="/zits/">Today's</a>
	 <a class="next" href="/zits/s-728948-949011">Previous</a>
	 <a class="next" href="/zits/s-729654-265545">Next</a>
	 
	 */
	
	private static Pattern imgData = Pattern.compile( "div class=\"toon\".*?<a href=\"(.*?)\" .*? alt=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern prevComic = Pattern.compile("Today's</.*?<a class=\"next\" href=\"(.*?)\"", Pattern.DOTALL);
	private static Pattern nextComic = Pattern.compile("Previous</.*?<a class=\"next\" href=\"(.*?)\"", Pattern.DOTALL);

	
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
	protected String getBaseComicURL() {
		return "http://www.arcamax.com";
	}
	
	@Override
	protected String getBasePrevNextURL() {
		return "http://www.arcamax.com";
	}
}
