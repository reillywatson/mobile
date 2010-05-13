package com.vasken.comics.Downloaders;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.vasken.util.WebRequester;

public class ShermansLagoonDownloader extends Downloader {
	/*
	  </SELECT>
      <INPUT TYPE="SUBMIT" NAME="Submit" VALUE="View"><BR>
      <FONT SIZE="1"><A HREF="/cgi-bin/sviewer.pl?selectdate=5/6/10">Next Cartoon</A>
      <A HREF="/cgi-bin/sviewer.pl?selectdate=5/4/10">Previous Cartoon</A></FONT>
      </FORM>
</TD></TR>
</TABLE>

<P>
<P ALIGN=LEFT>
<IMG SRC="dailies/SL100505.gif">*/
	
	private Pattern imgData = Pattern.compile( "</FORM>.*?<IMG SRC=\"(.*?)\"", Pattern.DOTALL);
	private Pattern prevComic = Pattern.compile("Next Cartoon.*?<A HREF=\"(.*?)\"", Pattern.DOTALL);
	private Pattern nextComic = Pattern.compile("</SELECT>.*?<A HREF=\"(.*?)\"", Pattern.DOTALL);
	private Pattern date = Pattern.compile("<TITLE>Sherman's Lagoon Comic Strip -- (.*?)</TITLE>", Pattern.DOTALL);
	
	@Override
	public boolean handlePartialResponse(StringBuilder responseSoFar) {
		Log.d(this.getClass().getName(),"PARSING...");
		if (responseSoFar.length() > 0) {
			Matcher m = imgData.matcher(responseSoFar);
			if (m.find()) {
				Log.d("HEY", "WE HAVE A WINNER!");
				comic = newComic();
				comic.image = "http://www.slagoon.com/" + m.group(1);
				m = nextComic.matcher(responseSoFar);
				if (m.find()) {
					comic.nextUrl = "http://www.slagoon.com" + m.group(1);
					Log.d("NEXT URL", comic.nextUrl);
				}
				m = prevComic.matcher(responseSoFar);
				if (m.find()) {
					comic.prevUrl = "http://www.slagoon.com" + m.group(1);
					Log.d("PREV URL", comic.prevUrl);
				}
				m = date.matcher(responseSoFar);
				if (m.find()) {
					comic.title = m.group(1);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	protected Pattern getComicPattern() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Pattern getNextComicPattern() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Pattern getPrevComicPattern() {
		// TODO Auto-generated method stub
		return null;
	}
}
