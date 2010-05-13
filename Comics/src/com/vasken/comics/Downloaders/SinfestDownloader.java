package com.vasken.comics.Downloaders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class SinfestDownloader extends Downloader {
	/*<table border="5" cellpadding="0" cellspacing="0" style="border: 5px solid black; border-collapse: collapse" bordercolor="#111111" width="800" id="AutoNumber1" height="234" bgcolor="#FFFFFF">
	  <tr>
	    <td width="50%" height="145"><p align="center"><b><font fact="Fixedsys">
	    <br>2010-05-10: Space Lair</font><font face="Photoshop Small"> <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="800" id="AutoNumber2" bgcolor="#FFFFFF" height="83">
	      <tr>
	        <td width="50%" height="1">
	            
	        </td>
	      </tr>
	      <tr>
	        <td width="50%" height="1">
	        <p align="center">&nbsp;</td>
	      </tr>
	      <tr>
	        <td width="50%" height="107"><p align="center"><img src="http://sinfest.net/comikaze/comics/2010-05-10.gif" alt="Space Lair" border="0" />        
	        &nbsp;</td>
	      </tr>
	      <tr>
	        <td width="50%" height="52"><p align="center"><font color="#000000"><a href="http://sinfest.net/archive_page.php?comicID=1"><img src="images/first_a.gif" border="0" alt="First" /></a>&nbsp;|&nbsp;
		<a href="http://sinfest.net/archive_page.php?comicID=3533"><img src="images/prev_a.gif" border="0" alt="Previous" /></a>&nbsp;|&nbsp;
		<a href="http://sinfest.net/archive_page.php?comicID=3535"><img src="images/next_a.gif" border="0" alt="Next" /></a>	</font><br>
	        </td>
	      </tr>
	    
	    </td>
	  </tr>
	  </table>*/
	
	private Pattern comicData = Pattern.compile("<img src=\"http://sinfest.net/comikaze/(.*?)\" alt=\"(.*?)\"", Pattern.DOTALL);
	private Pattern prevComic = Pattern.compile("<a href=\"([^>]*?)\"><img src=\"images/prev_a.gif\"", Pattern.DOTALL);
	private Pattern nextComic = Pattern.compile("<a href=\"([^>]*?)\"><img src=\"images/next_a.gif\"", Pattern.DOTALL);
	
	protected Pattern getComicPattern() {
		return comicData;
	}
	protected Pattern getNextComicPattern() {
		return nextComic;
	}
	protected Pattern getPrevComicPattern() {
		return prevComic;
	}
	
	@Override
	protected boolean parseComic(StringBuilder partialResponse) {
		Matcher m = comicData.matcher(partialResponse);
		if (m.find()) {
			comic.image = "http://sinfest.net/comikaze/" + m.group(1);
			comic.title = m.group(2);
			Log.d("IMAGE", comic.image);
			Log.d("TITLE", comic.title);
			return true;
		}
		return false;
	}
}
