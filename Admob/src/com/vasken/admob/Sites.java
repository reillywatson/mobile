package com.vasken.admob;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.vasken.admob.manager.StatsResponse;
import com.vasken.admob.manager.Worker;

public class Sites extends Activity {
	private String token;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        showTodaysRevenue();
    }

	private void showTodaysRevenue() {
		String startDate = "2010-04-22";
		String endDate = "2010-06-03";
		StatsResponse response = Worker.stats(token, "a14bd273e643f6a", startDate, endDate);
		
		Log.d("------", response.getTheData().toString());
		
	}
    
}