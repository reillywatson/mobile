package com.vasken.admob.manager;

import android.util.Log;

import com.vasken.util.WebRequester.RequestCallback;

public class Response implements RequestCallback {

	@Override
	public boolean handlePartialResponse(StringBuilder data, boolean isDone) {
		if (isDone) {
			Log.d("--------", data.toString());
			return true;
		}
		return false;
	}

	
}
