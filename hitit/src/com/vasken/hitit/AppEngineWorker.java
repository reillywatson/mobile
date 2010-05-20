package com.vasken.hitit;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.HttpGet;

import android.graphics.Bitmap;
import android.util.Log;

import com.vasken.util.WebRequester;

public class AppEngineWorker extends Worker {
	private HttpGet httpGet;
	
	private Queue<HotItem> items = new LinkedList<HotItem>();
	
	private Pattern hotItemPattern =  Pattern.compile(
		"<HotItem>" +
			"<RatingID>(.*?)</RatingID>" +
			"<ImageURL>(.*?)</ImageURL>" +
			"<AverageRating>(.*?)</AverageRating>" +
			"<NumRatings>(.*?)</NumRatings>" +
		"</HotItem>"
		, Pattern.DOTALL);
			
	public AppEngineWorker(String url) {
		httpGet = new HttpGet(url);
    	httpGet.addHeader("NumResults", "10");
	}

	@Override
	public boolean handlePartialResponse(StringBuilder dataSoFar, boolean isFinal) {
		if (isFinal) {
			Matcher m = hotItemPattern.matcher(dataSoFar);
			while (m.find()) {
				try {
					HotItem theHotItem = new HotItem();

	    			theHotItem.setRateId(m.group(1));
					theHotItem.setImageURL(m.group(2));
					theHotItem.setResultAverage(Double.parseDouble(m.group(3)));
					theHotItem.setResultTotals(m.group(4));
	        		
					Bitmap image = WebRequester.bitmapFromUrl(theHotItem.getImageURL());
	    			theHotItem.setImage(image);
	            	
	    			items.add(theHotItem);
				} catch (IOException e) {
					Log.d(getClass().getName(), Log.getStackTraceString(e));
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public HotItem getPageData(String rateId, int rating, String imageURL) {
		HotItem theCurrentItem = items.poll();
		if (theCurrentItem == null) {
			// Magically populate 'items'
			new WebRequester().makeRequest(httpGet, this);
			
			// Grab the first item
			theCurrentItem = items.poll();
		}
		
		if (theCurrentItem == null) {
			Log.d(getClass().getName(), "OH GOD! We're in BIG trouble - theCurrentItem is null.");
		}
		
		return theCurrentItem;
	}

}
