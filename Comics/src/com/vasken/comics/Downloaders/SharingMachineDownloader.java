package com.vasken.comics.Downloaders;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.vasken.util.WebRequester;
import com.vasken.util.YearMonthDay;

public class SharingMachineDownloader extends Downloader {
	
	private String rootDomain;
	
	public SharingMachineDownloader(String rootDomain) {
		this.rootDomain = rootDomain;
	}
	/*
	<div class="headertext">
	Today on Toothpaste For Dinner: 			
		<a href="http://www.toothpastefordinner.com/050610/american-flavored-soup.gif">american flavored soup</b></a>		
		</div>*/
	
	private Pattern imgData = Pattern.compile( "<div class=\"headertext\">.*?<a href=\"(.*?)\">(.*?)<", Pattern.DOTALL);
	
	private String getComicUrl(YearMonthDay date) {
		return String.format("%s/index.php?date=%02d%02d%02d", rootDomain, date.getMonth(), date.getDay(), date.getYear());
	}
	
	@Override
	public boolean handlePartialResponse(StringBuilder responseSoFar) {
		Log.d(this.getClass().getName(),"PARSING...");
		if (responseSoFar.length() > 0) {
			Matcher m = imgData.matcher(responseSoFar);
			if (m.find()) {
				Log.d("HEY", "WE HAVE A WINNER!");
				comic = newComic();
				try {
					Log.d("GETTING IMAGE FROM URL", m.group(1));
					comic.image = WebRequester.bitmapFromUrl(m.group(1));
					comic.title = m.group(2);
					String[] urlParts = m.group(1).split("/");
					String dateStr = urlParts[urlParts.length - 2];
					YearMonthDay date = new YearMonthDay(Integer.parseInt(dateStr.substring(4, 6)), Integer.parseInt(dateStr.substring(0, 2)), Integer.parseInt(dateStr.substring(2, 4)));
					comic.nextUrl = getComicUrl(date.addDays(1));
					comic.prevUrl = getComicUrl(date.subtractDays(1));
					Log.d("NEXT", comic.nextUrl);
					Log.d("PREV", comic.prevUrl);
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
