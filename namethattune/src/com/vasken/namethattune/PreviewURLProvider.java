package com.vasken.namethattune;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import android.util.Log;

import com.vasken.util.Base64;
import com.vasken.util.WebRequester;
import com.vasken.util.WebRequester.RequestCallback;

public class PreviewURLProvider {
	
	private WebRequester requester = new WebRequester();

	private static String computeValidator( String strURL, String strUserAgent )
	{
		try {
			String strRandom = String.format( "%08X", new Random().nextInt() );
			byte[] abRandom = strRandom.getBytes("ASCII");
			
			byte [] abStatic = Base64.decode("ROkjAaKid4EUF5kGtTNn3Q==");
			
			int p = 0;
			for( int i = 0; i < 3 && p != -1; i++ )
				p = strURL.indexOf("/", p + 1);
				
			byte [] abURL = strURL.substring(p).getBytes("ASCII");
			byte [] abUA = strUserAgent.getBytes("ASCII");
			
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(abURL);
			md5.update(abUA);
			md5.update(abStatic);
			md5.update(abRandom);
			String md5String = new String(md5.digest(), "ASCII").replace("-", "");
			return strRandom + "-" + md5String;
		} catch(UnsupportedEncodingException e) {
			Log.d("computeValidator", "what the fuck, no ascii?!");
		}
		catch(NoSuchAlgorithmException e) {
			Log.d("computeValidator", "what the fuck, no MD5?!");
		}
		return null;
	}
	
	private static HttpUriRequest createHttpRequest(String searchTerms) {
		String url = "http://ax.search.itunes.apple.com/WebObjects/MZSearch.woa/wa/search?submit=edit&restrict=true&media=music&term=" + URLEncoder.encode(searchTerms);
		String userAgent = "iTunes/9.1.1 (Macintosh; Intel Mac OS X 10.6.3) AppleWebKit/531.22.7";
		HttpGet get = new HttpGet(url);
		get.addHeader("X-Apple-Validation", computeValidator(url, userAgent));
		get.addHeader("User-Agent", userAgent);
		return get;
	}
	
	private static Pattern previewURLPattern = Pattern.compile("<key>preview-url.*?<string>(.*?)</string>", Pattern.DOTALL);
	
	public interface PreviewURLListener {
		void urlReady(String url);
		void somethingWentWrong();
	}
	
	public void getPreviewURL(String searchTerms, final PreviewURLListener listener) {
		requester.makeRequest(createHttpRequest(searchTerms), new RequestCallback() {
			@Override
			public boolean handlePartialResponse(StringBuilder responseSoFar, boolean isFinal) {
				if (isFinal) {
					Matcher m = previewURLPattern.matcher(responseSoFar);
					if (m.find()) {
						listener.urlReady(m.group(1));
					}
					else {
						listener.somethingWentWrong();
					}
					return true;
				}
				return false;
			}
			
		});
	}
}
