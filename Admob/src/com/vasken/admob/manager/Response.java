package com.vasken.admob.manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.vasken.util.WebRequester.RequestCallback;

public class Response implements RequestCallback {

	private static final String ERRORS = "errors";
	private static final String WARNINGS = "warnings";
	private static final String DATA = "data";
	private static final String PAGE = "page";
	private static final String CURRENT = "current";
	private static final String TOTAL = "total";
	private static final String TOKEN = "token";

	private String token;
	
	@Override
	public boolean handlePartialResponse(StringBuilder sb, boolean isDone) {
		if (isDone) {
			try {
				JSONObject jsonResponse = new JSONObject(sb.toString());
				JSONArray errors = jsonResponse.getJSONArray(ERRORS);
				JSONArray warnings = jsonResponse.getJSONArray(WARNINGS);
				JSONObject theData = jsonResponse.getJSONObject(DATA);
				JSONObject page = jsonResponse.getJSONObject(PAGE);
				
				if (errors.length() > 0) {
					Log.e(getClass().getName(), errors.toString());
					return false;
				}
				
				if (warnings.length() > 0) {
					Log.w(getClass().getName(), errors.toString());
				}
				
				token = theData.getString(TOKEN);
				int currentPage = page.getInt(CURRENT);
				int totalPages = page.getInt(TOTAL);
			} catch (JSONException e) {
				Log.d(getClass().getName(), Log.getStackTraceString(e));
				return false;
			}
			return true;
		}
		return false;
	}

	
}
