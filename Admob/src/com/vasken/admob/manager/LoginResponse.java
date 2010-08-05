package com.vasken.admob.manager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginResponse extends Response{
	public static final String TOKEN = "token";
	
	private String token;
	
	@Override
	public boolean handlePartialResponse(StringBuilder sb, boolean isDone) {
		super.handlePartialResponse(sb, isDone);
		
		JSONObject data = getTheData();
		try {
			token = data.getString(TOKEN);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	public String getToken() {
		return token;
	}
}
