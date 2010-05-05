package com.vasken.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class WebRequester {
	
	public static Bitmap bitmapFromUrl(String urlString) throws IOException {
		URL url = new URL(urlString);
    	InputStream is = (InputStream)url.getContent();
    	Bitmap image = BitmapFactory.decodeStream(is, null, null);
    	return image;
	}
	
	public interface RequestCallback {
		// return true when you've gotten back enough of the response
		boolean handlePartialResponse(StringBuilder responseSoFar);
	}
	
	private DefaultHttpClient httpclient = new DefaultHttpClient();
	private final char[] buffer = new char[0x10000];
	private final StringBuilder sb = new StringBuilder(64*1024);

	public void makeRequest(HttpUriRequest request, RequestCallback callback) {
		Reader in = null;
		try {
			sb.delete(0, sb.length());
			HttpResponse response = httpclient.execute(request);
	        in = new InputStreamReader(response.getEntity().getContent());
	
	        int bytesRead = in.read(buffer, 0, buffer.length);
	        int totalBytesRead = 0;
	        while (bytesRead>=0) {
	        	sb.append(buffer, 0, bytesRead);
	        	totalBytesRead += bytesRead;
	        	bytesRead = in.read(buffer, 0, buffer.length);
	        	
	        	if (callback.handlePartialResponse(sb))
	        		break;
	        }
		} catch (Exception e) {
			Log.d(this.getClass().getName(), Log.getStackTraceString(e));
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// We're hosed, just leave the damn thing open.
					e.printStackTrace();
				}
			}
		}
	}
}
