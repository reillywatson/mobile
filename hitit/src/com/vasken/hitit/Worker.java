package com.vasken.hitit;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.graphics.Bitmap;
import android.util.Log;

import com.vasken.util.WebRequester;

public class Worker implements WebRequester.RequestCallback {	
	private HttpPost httpPost;
    private Pattern p =  Pattern.compile("<img id='mainPic'.*?src='(.*?)'.*?>.*?<input type=\"hidden\" name=\"ratee\" value=\"(.*?)\".*?>", Pattern.DOTALL);
    private Pattern prevRatingRegex = Pattern.compile("<div class=\"score\"[^>]*?>(.*?)</div>.*?Based on ([^<]*?) votes", Pattern.DOTALL);
    private ExecutorService threadPool = Executors.newFixedThreadPool(1);
    String id;
    int rating;
    String imageURL;
    HotItem item;
    String gender;
    
	public Worker(String postUrl, String gender) {
		this.gender = gender;
		httpPost = new HttpPost(postUrl);		
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		httpPost.addHeader("Accept-Encoding", "gzip");
	}
	
	// @return null when there was a problem loading the page
	public HotItem getPageData(String id, int rating, String imageURL) {
		return requestDataFromServer(id, rating, imageURL);
	}
	
	private HotItem requestDataFromServer(String id, int rating, String imageURL) {
		Log.d(getClass().getName(), "<<<<<<<<<< Start Page Loading" + rating +"    " + id);
		this.id = id;
		this.rating = rating;
		this.imageURL = imageURL;
		item = null;
		if (rating > 0 && id != null) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("rate", "1"));
	        params.add(new BasicNameValuePair("state", "rate"));
	        params.add(new BasicNameValuePair("minR", "9"));
	        params.add(new BasicNameValuePair("rateAction", "vote"));
	        params.add(new BasicNameValuePair("ratee", id));
	        params.add(new BasicNameValuePair("r"+id, ""+rating));
	        try {
				httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			} catch (UnsupportedEncodingException e) {
				return null;
			}
			
			Log.d(getClass().getName(), "Rating Set...  " + id + ": " + rating);
		}
		new WebRequester().makeRequest(httpPost, this, WebRequester.CompressionMethod.GZipCompression);
		return item;
	}
	
	void addRatingResults(String id, StringBuilder sb, HotItem item) {
		Matcher m = prevRatingRegex.matcher(sb);
		if (m.find()) {
			String avgRating = m.group(1);
			String numRatings = m.group(2);
			
			item.setResultAverage(Double.parseDouble(avgRating));
			item.setResultTotals(numRatings);
			
			Log.d(getClass().getName(), "Rating ID: " + id);
			Log.d(getClass().getName(), "Average Rating: " + avgRating);
			Log.d(getClass().getName(), "Num ratings: " + numRatings);
			sendDataToServer(id, avgRating, numRatings, imageURL);
		}
	}
	
	void sendDataToServer(final String ratingId, final String avgRating, final String numRatings, final String imageURL) {
		if (imageURL != null) {
			threadPool.submit(new Runnable() { public void run() {
				try {
					Log.d("SENDING RATING INFO", "URL: " + imageURL + "avg: " + avgRating + " num: " + numRatings + " id: " + ratingId);
					HttpPost post = new HttpPost("http://vaskendroid.appspot.com/submit");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
			        params.add(new BasicNameValuePair("RatingID", ratingId.trim()));
			        params.add(new BasicNameValuePair("AverageRating", avgRating.trim()));
			        params.add(new BasicNameValuePair("NumRatings", numRatings.trim()));
			        params.add(new BasicNameValuePair("ImageURL", imageURL.trim()));
			        params.add(new BasicNameValuePair("Gender", gender));
					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					new WebRequester().makeRequest(post, new WebRequester.RequestCallback() {
						@Override
						public boolean handlePartialResponse(StringBuilder response, boolean isFinal) {
							return true;
						}
					});
				} catch (Exception e) { e.printStackTrace(); }
			}});
		}
	}

	@Override
	public boolean handlePartialResponse(StringBuilder sb, boolean isFinal) {
		// This (26000 bytes) seems like the sort of thing that could no longer be true in the future.
    	// I suppose worst case, the app would just run a bit slower.
    	if (sb.length() > 26000) {
            Matcher m = p.matcher(sb);
            
            boolean matches = m.find();
            
            if(matches) {
            	try {
            		HotItem theHotItem = new HotItem();
            		if (id != null && rating > 0) {
            			addRatingResults(id, sb, theHotItem);
            		}
            		theHotItem.setImageURL(m.group(1));
            		Bitmap image = WebRequester.bitmapFromUrl(m.group(1));
        			
        			theHotItem.setImage(image);
        			theHotItem.setRateId( m.group(2));
        	        Log.d(getClass().getName(), ">>>>>>>>>>> Done Page Loading");
        	        item = theHotItem;
                	return true;
				} catch (Exception e) { Log.d(this.getClass().toString(), "Not a valid imageUrl: " + m.group(1));	}
            }
    	}
    	return false;
	}
}