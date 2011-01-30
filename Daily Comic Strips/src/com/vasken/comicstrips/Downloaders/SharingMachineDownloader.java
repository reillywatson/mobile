package com.vasken.comicstrips.Downloaders;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vasken.comicstrips.Comic;

import android.util.Log;

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
		return String.format("%s/index.php?date=%02d%02d%02d", rootDomain, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE), calendar.get(Calendar.YEAR) - 2000);
	}
	
	@Override
	protected boolean parseComic(StringBuilder partialResponse) {
		Matcher m = imgData.matcher(partialResponse);
		if (m.find()) {
			comic.image = m.group(1);
			comic.title = m.group(2);
			String[] urlParts = m.group(1).split("/");
			String dateStr = urlParts[urlParts.length - 2];
			Calendar calendar = new GregorianCalendar();
			calendar.set(Calendar.MONTH, Integer.parseInt(dateStr.substring(0, 2)) - 1);
			calendar.set(Calendar.DATE, Integer.parseInt(dateStr.substring(2, 4)));
			calendar.set(Calendar.YEAR, Integer.parseInt(dateStr.substring(4, 6)) + 2000);
			calendar.add(Calendar.DATE, 1);
			Calendar now = new GregorianCalendar();
			Date nowDate = new Date();
			now.set(nowDate.getYear() + 1900, nowDate.getMonth(), nowDate.getDate());
			if (calendar.before(now)) {
				comic.nextUrl = getComicUrl(calendar);
				Log.d("NEXT", comic.nextUrl);
			}
			calendar.add(Calendar.DATE, -2);
			comic.prevUrl = getComicUrl(calendar);
			Log.d("PREV", comic.prevUrl);
			return true;
		}
		return false;
	}

	@Override
	protected Pattern getComicPattern() {
		return imgData;
	}

	@Override
	protected Pattern getNextComicPattern() {
		return null;
	}

	@Override
	protected Pattern getPrevComicPattern() {
		return null;
	}
	
	@Override
	protected Comic newComic() {
		Comic c = super.newComic();
		c.randomUrl = rootDomain + "/randomComicViewer.php";
		return c;
	}
}
