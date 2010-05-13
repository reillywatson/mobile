package com.vasken.comics.Downloaders;

import java.util.regex.Pattern;

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
		return "http://www.slagoon.com/";
	}
	
	@Override
	protected String getBasePrevNextURL() {
		return "http://www.slagoon.com";
	}
}
