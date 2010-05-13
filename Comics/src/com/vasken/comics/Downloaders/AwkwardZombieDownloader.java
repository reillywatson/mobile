package com.vasken.comics.Downloaders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AwkwardZombieDownloader extends Downloader {
	/*<p><font size="3" color="#000000" face="verdana">
	<p><center>Masturcation (Part1)</center>


	<p><img src="http://i49.photobucket.com/albums/f278/katietiedrich/comic139.png">
	<center>
	<p><a href="http://www.awkwardzombie.com/comic1-092006.php">
	<img border="0" src="http://www.awkwardzombie.com/comnav1_1.gif"></a>
	<a href="http://www.awkwardzombie.com/comic1-011810.php">
	<img border="0" src="http://www.awkwardzombie.com/comnav1_2.gif"></a>
	<a href="http://www.awkwardzombie.com/archive1.php">
	<img border="0" src="http://www.awkwardzombie.com/comnav1_3.gif"></a>
	<a href="http://www.awkwardzombie.com/comic1-020110.php">
	<img border="0" src="http://www.awkwardzombie.com/comnav1_4.gif"></a>
	<a href="http://www.awkwardzombie.com/comic1.php">
	<img border="0" src="http://www.awkwardzombie.com/comnav1_5.gif"></a>
	</center>*/
	
	private Pattern comicPattern = Pattern.compile( "<font size=\"3\".*?<center>(.*?)</center>.*?<img src=\"(.*?)\"", Pattern.DOTALL);
	private Pattern prevComic = Pattern.compile("<a href=\"([^>]*?)\">\\s*<img border=\"0\" src=\"http://www.awkwardzombie.com/comnav1_2.gif\"", Pattern.DOTALL);
	private Pattern nextComic = Pattern.compile("<a href=\"([^>]*?)\">\\s*<img border=\"0\" src=\"http://www.awkwardzombie.com/comnav1_4.gif\"", Pattern.DOTALL);
	
	protected Pattern getComicPattern() {
		return comicPattern;
	}

	protected Pattern getNextComicPattern() {
		return nextComic;
	}
	protected Pattern getPrevComicPattern() {
		return prevComic;
	}
	
	@Override
	protected boolean parseComic(StringBuilder partialResponse) {
		Matcher m = comicPattern.matcher(partialResponse);
		if (m.find()) {
			comic.image = m.group(2);
			comic.title = m.group(1);
			return true;
		}
		return false;
	}	
}
