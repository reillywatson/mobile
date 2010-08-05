package com.vasken.admob.manager;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class StatsResponse extends Response{
	public static final String TOKEN = "token";

	JSONArray stats;
	
	public StatsResponse() {
		stats = new JSONArray();
		
//		impressions": 0, 
//		"clicks": 0, 
//		"ctr": 0, 
//		"cost": 0, 
//		"cpc": 0, 
//		"ecpm": 0, 
//		"date": "2009-12-10", 
//		"ad_id": "a11111111111111"
	}
	
	@Override
	public boolean handlePartialResponse(StringBuilder sb, boolean isDone) {
		super.handlePartialResponse(sb, isDone);
		
		JSONObject data = getTheData();
		
		Log.d(getClass().getName(), data.toString());
		
		return true;
	}
}
