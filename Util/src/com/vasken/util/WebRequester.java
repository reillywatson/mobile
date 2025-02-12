package com.vasken.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class WebRequester {
	
	public enum CompressionMethod {
		NoCompression,
		GZipCompression
	}
	
	public static Bitmap bitmapFromUrl(String urlString) throws IOException {
		URL bitmapUrl = new URL(urlString);
        HttpGet httpRequest = null;

        try {
                httpRequest = new HttpGet(bitmapUrl.toURI());
        } catch (URISyntaxException e) {
                e.printStackTrace();
        }

        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);

        HttpEntity entity = response.getEntity();
        BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity); 
        InputStream instream = bufHttpEntity.getContent();
        return BitmapFactory.decodeStream(instream);
	}
	
	public interface RequestCallback {
		// return true when you've gotten back enough of the response
		boolean handlePartialResponse(StringBuilder responseSoFar, boolean isFinal);
	}
	
	private DefaultHttpClient httpclient = new DefaultHttpClient();
	private final char[] buffer = new char[0x10000];
	private final StringBuilder sb = new StringBuilder(64*1024);
	
	public void makeRequest(HttpUriRequest request, RequestCallback callback) throws Exception {
		makeRequest(request, callback, CompressionMethod.NoCompression);
	}
	
	public void makeRequest(HttpUriRequest request, RequestCallback callback, CompressionMethod method) throws Exception {
		Reader in = null;
		try {
			sb.delete(0, sb.length());
			HttpResponse response = httpclient.execute(request);
			
			if (method == CompressionMethod.GZipCompression) {
				in = new InputStreamReader(new GZIPInputStream(response.getEntity().getContent()));
			}
			else {
				in = new InputStreamReader(response.getEntity().getContent());
			}
			
	        int bytesRead = in.read(buffer, 0, buffer.length);
	        int totalBytesRead = 0;
	        while (bytesRead>=0) {
	        	sb.append(buffer, 0, bytesRead);
	        	totalBytesRead += bytesRead;
	        	bytesRead = in.read(buffer, 0, buffer.length);
	        	
	        	if (callback.handlePartialResponse(sb, (bytesRead < 0)))
	        		break;
	        }
		} catch (Exception e) {
			Log.d(this.getClass().getName(), Log.getStackTraceString(e));

			// It's generally a bad idea for a library to hide failures
			throw e;
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
