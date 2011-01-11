package com.vasken.namethattune;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vasken.util.WebRequester;
import com.vasken.util.WebRequester.RequestCallback;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.Toast;

public class HighScoresActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.highscores);
		
		HttpGet get = new HttpGet("http://1.latest.vaskenmusic.appspot.com/vaskenmusicserver?genre="+getString(R.string.genre));
		
		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean hasMobileConnection = conMan.getNetworkInfo(0).isConnectedOrConnecting();
		boolean hasWifiConnection = conMan.getNetworkInfo(1).isConnectedOrConnecting();
		
		if (!hasMobileConnection && !hasWifiConnection) {
			Toast.makeText(this, "You need an internet connection, to submit your score.", Toast.LENGTH_LONG).show();
			finish();
		}

		new WebRequester().makeRequest(get, new RequestCallback() {
			@Override
			public boolean handlePartialResponse(StringBuilder responseSoFar, boolean isFinal) {
				if (isFinal) {
					Log.d(Main.class.toString(), responseSoFar.toString());
					
					JSONObject highScores;
					try {
						highScores = new JSONObject(responseSoFar.toString());
						
						JSONArray scoresToday = highScores.getJSONArray("Today");
						for (int i=0; i<scoresToday.length(); i++){
							JSONObject highScore = scoresToday.getJSONObject(i);
							Log.d(Main.class.toString(), highScore.getString("name"));
							Log.d(Main.class.toString(), highScore.getString("score"));
						}
						JSONArray scoresEver = highScores.getJSONArray("Ever");
						for (int i=0; i<scoresEver.length(); i++){
							JSONObject highScore = scoresEver.getJSONObject(i);
							Log.d(Main.class.toString(), highScore.getString("name"));
							Log.d(Main.class.toString(), highScore.getString("score"));
						}
						TableLayout tableLayout = (TableLayout)findViewById(R.id.highScores);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return true;
				}
				return false;
			}
		});
	}
	
}
