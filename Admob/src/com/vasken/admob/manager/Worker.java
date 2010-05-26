package com.vasken.admob.manager;

import org.apache.http.client.methods.HttpPost;

import android.content.Context;
import android.util.Log;

import com.vasken.util.WebRequester;

public class Worker {

	public static Response login(Context cx, String email, String password) {
		Response admobResponse = new Response(); 

		HttpPost post = new HttpPost("https://api.admob.com/v2/auth/login?" +
				"client_key=k4f2c8dda31650901c70ae49d2021893&" +
				"email="+email+"&" +
				"password="+password);
		post.setHeader("Host", "api.admob.com");
		
		Log.d("---- REQUEST ----", post.getURI().toString());
		
		new WebRequester().makeRequest(post, admobResponse);
		

		return admobResponse;
	}
}
