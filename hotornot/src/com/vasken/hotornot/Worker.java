package com.vasken.hotornot;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class Worker {
	
	private DefaultHttpClient httpclient;
	
	private HttpPost httpPost;
    
	private HttpResponse response;
	
	private final char[] buffer = new char[0x10000];
	private final StringBuilder sb = new StringBuilder(64*1024);

    private Pattern p =  Pattern.compile("<img id='mainPic'.*?src='(.*?)'.*?>.*?<input type=\"hidden\" name=\"ratee\" value=\"(.*?)\".*?>", Pattern.DOTALL); //Pattern.compile("<img.*? src=\"(.*?)\".*?id=\"mainPic\">");
    
	public Worker(Context context) {
		httpPost = new HttpPost(context.getString(R.string.rate_url_female));
        httpclient = new DefaultHttpClient();
		
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		
	}
	
	// @return null when there was a problem loading the page
	public HotItem getPageData(String id, int rating) {
		return requestDataFromServer(id, rating);
	}
	
	private HotItem requestDataFromServer(String id, int rating) {
		sb.delete(0, sb.length());
		try {
			Log.d(getClass().getName(), "<<<<<<<<<< Start Page Loading");

			if (rating > 0 && id != null) {
//				rate=1&state=rate&ratee=9313113&r9313113=8&minR=9&rateAction=vote
				HttpParams params = httpPost.getParams();
				params.setIntParameter("rate", 1);
				params.setParameter("state", "rate");
				params.setIntParameter("minR", 9 );
				params.setParameter("rateAction", "vote" );
				params.setParameter("ratee", id);
				params.setParameter("r"+id, rating );
				
				Log.d(getClass().getName(), "Rating Set...  " + id + ": " + rating);
			}
			
			response = httpclient.execute(httpPost);			

	    	Reader in = new InputStreamReader(response.getEntity().getContent());

	        int bytesRead = in.read(buffer, 0, buffer.length);
	        int totalBytesRead = 0;
	        while (bytesRead>=0) {
	        	sb.append(buffer, 0, bytesRead);
	        	totalBytesRead += bytesRead;
	        	bytesRead = in.read(buffer, 0, buffer.length);
	        	// This seems like the sort of thing that could no longer be true in the future.
	        	// I suppose worst case, the app would just run a bit slower.
	        	if (totalBytesRead > 26000) {
		            Matcher m = p.matcher(sb);
		            
		            boolean matches = m.find();
		            
		            if(matches) {
		            	try {
		                	URL url = new URL(m.group(1));
		        	    	InputStream is = (InputStream)url.getContent();
		        			Drawable d = Drawable.createFromStream(is, "src");
		                	HotItem theHotItem = new HotItem(d, m.group(2));
		                	in.close();
		        	        Log.d(getClass().getName(), ">>>>>>>>>>> Done Page Loading");
		                	return theHotItem;
						} catch (Exception e) { Log.d(this.getClass().toString(), "Not a valid imageUrl: " + m.group(1));	}
		            }
	        	}
	        }
		} catch (Exception e) {
			Log.d(this.getClass().getName(), Log.getStackTraceString(e));
		}

		Log.d(getClass().getName(), ">>>>>>>>>>> Done Page Loading");
		return null;
	}
}
