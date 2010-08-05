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

	private JSONArray errors;
	private JSONArray warnings;
	private JSONObject theData;
	private int currentPage;
	private int totalPages;
	
	@Override
	public boolean handlePartialResponse(StringBuilder sb, boolean isDone) {
		if (isDone) {
			Log.d(getClass().getName(), sb.toString());
			try {
				JSONObject jsonResponse = new JSONObject(sb.toString());
				errors = jsonResponse.getJSONArray(ERRORS);
				warnings = jsonResponse.getJSONArray(WARNINGS);
				theData = jsonResponse.getJSONObject(DATA);
				JSONObject page = jsonResponse.getJSONObject(PAGE);
				
				if (errors.length() > 0) {
					Log.e(getClass().getName(), errors.toString());
					return false;
				}
				
				if (warnings.length() > 0) {
					Log.w(getClass().getName(), errors.toString());
				}
				
				currentPage = page.getInt(CURRENT);
				totalPages = page.getInt(TOTAL);
			} catch (JSONException e) {
				Log.d(getClass().getName(), Log.getStackTraceString(e));
				return false;
			}
			return true;
		}
		return false;
	}
	
	public JSONObject getTheData() {
		return theData;
	}

	public int getCurrentPage() {
		return currentPage;
	}
	
	public int getTotalPages() {
		return totalPages;
	}
}
