package com.vasken.show;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

	private static final int SECONDS_TO_WAIT_ON_SPLASH_SCREEN = 3 * 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash);
		
		final Context thisContext = this;
		
		Runnable startAppTaskRunnable = new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(thisContext, Main.class);
				thisContext.startActivity(intent);
				finish();
			}
		};
		
		Handler mHandler = new Handler();
		mHandler.postDelayed(startAppTaskRunnable, SECONDS_TO_WAIT_ON_SPLASH_SCREEN);
	}
}
