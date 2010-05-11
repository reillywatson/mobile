package com.vasken.comics.Downloaders;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.vasken.util.WebRequester;

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
	private Pattern prevComic = Pattern.compile("<a href=\"http://sinfest.net/archive_page.php?comicID=(\\d*?)\">\\w*?<img src=\"images/prev_a.gif\"", Pattern.DOTALL);
	private Pattern nextComic = Pattern.compile("<a href=\"http://sinfest.net/archive_page.php?comicID=(\\d*?)\">\\w*?<img src=\"images/next_a.gif\"", Pattern.DOTALL);
	
	@Override
	public boolean handlePartialResponse(StringBuilder responseSoFar) {
		Log.d(this.getClass().getName(),"PARSING...");
		if (responseSoFar.length() > 12000) {
			Matcher m = comicData.matcher(responseSoFar);
			if (m.find()) {
				Log.d("HEY", "WE HAVE A WINNER!");
				comic = newComic();
				try {
					comic.image = WebRequester.bitmapFromUrl("http://sinfest.net/comikaze/" + m.group(1));
					comic.title = m.group(2);
					Log.d("TITLE", comic.title);
					m = nextComic.matcher(responseSoFar);
					if (m.find()) {
						comic.nextUrl = "http://sinfest.net/archive_page.php?comicID=" + m.group(1);
						Log.d("NEXT URL", comic.nextUrl);
					}
					m = prevComic.matcher(responseSoFar);
					if (m.find() && !url.endsWith("=1")) {
						comic.prevUrl = "http://sinfest.net/archive_page.php?comicID=" + m.group(1);
						Log.d("PREV URL", comic.prevUrl);
					}
				} catch (IOException e) {
					Log.d(this.getClass().getName(), "Retrieving image for comic failed!");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}
		}
		return false;
	}
}
