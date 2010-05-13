package com.vasken.comics.Downloaders;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.vasken.util.WebRequester;

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
	
	private String getComicUrl(Calendar calendar) {
		return String.format("%s/index.php?date=%02d%02d%02d", rootDomain, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE), calendar.get(Calendar.YEAR));
	}
	
	@Override
	public boolean handlePartialResponse(StringBuilder responseSoFar) {
		Log.d(this.getClass().getName(),"PARSING...");
		if (responseSoFar.length() > 0) {
			Matcher m = imgData.matcher(responseSoFar);
			if (m.find()) {
				Log.d("HEY", "WE HAVE A WINNER!");
				comic = newComic();
				Log.d("GETTING IMAGE FROM URL", m.group(1));
				comic.image = m.group(1);
				comic.title = m.group(2);
				String[] urlParts = m.group(1).split("/");
				String dateStr = urlParts[urlParts.length - 2];
				Calendar calendar = new GregorianCalendar();
				calendar.set(Calendar.MONTH, Integer.parseInt(dateStr.substring(0, 2)) - 1);
				calendar.set(Calendar.DATE, Integer.parseInt(dateStr.substring(2, 4)));
				calendar.set(Calendar.YEAR, Integer.parseInt(dateStr.substring(4, 6)));
				calendar.add(Calendar.DATE, 1);
				comic.nextUrl = getComicUrl(calendar);
				calendar.add(Calendar.DATE, -2);
				comic.prevUrl = getComicUrl(calendar);
				Log.d("NEXT", comic.nextUrl);
				Log.d("PREV", comic.prevUrl);
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
