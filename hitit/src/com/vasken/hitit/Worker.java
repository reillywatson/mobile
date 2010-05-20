package com.vasken.hitit;

import org.apache.http.client.methods.HttpPost;

import com.vasken.util.WebRequester;

public abstract class Worker  implements WebRequester.RequestCallback{
	
	HttpPost httpPost;
	
	public Worker(String postUrl) {
		httpPost = new HttpPost(postUrl);		
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		httpPost.addHeader("Accept-Encoding", "gzip");
	}
	
	public abstract HotItem getPageData(String rateId, int i, String imageURL);

}
