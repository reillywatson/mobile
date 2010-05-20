package com.vasken.hitit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.graphics.Bitmap;
import android.util.Log;

import com.vasken.util.WebRequester;

public class AppEngineWorker extends Worker {
	private Queue<HotItem> items = new LinkedList<HotItem>();
	
	private Pattern hotItemPattern =  Pattern.compile(
		"<HotItem>" +
//			"<RatingID>(.*?)</RatingID>" +
			"<ImageURL>(.*?)</ImageURL>" +
			"<AverageRating>(.*?)</AverageRating>" +
			"<NumRatings>(.*?)</NumRatings>" +
		"</HotItem>"
		, Pattern.DOTALL);
			
	public AppEngineWorker(String url) {
		super(url);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("NumResults", "10"));
        try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
        	Log.d(getClass().getName(), Log.getStackTraceString(e));
		}
	}

	@Override
	public boolean handlePartialResponse(StringBuilder dataSoFar, boolean isFinal) {
		if (dataSoFar.toString().endsWith("</HotItems>")) {
			Matcher m = hotItemPattern.matcher(dataSoFar);
			while (m.find()) {
				try {
					HotItem theHotItem = new HotItem();

	    			theHotItem.setRateId("m.group(1)");
					theHotItem.setImageURL(m.group(1));
					theHotItem.setResultAverage(Double.parseDouble(m.group(2)));
					theHotItem.setResultTotals(m.group(3));
	        		
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
			new WebRequester().makeRequest(httpPost, this, WebRequester.CompressionMethod.GZipCompression);
			
			// Grab the first item
			theCurrentItem = items.poll();
		}
		
		if (theCurrentItem == null) {
			Log.d(getClass().getName(), "OH GOD! We're in BIG trouble - theCurrentItem is null.");
		}
		
		return theCurrentItem;
	}

}
