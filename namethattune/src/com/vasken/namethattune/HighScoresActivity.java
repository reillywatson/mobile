package com.vasken.namethattune;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.vasken.util.WebRequester;
import com.vasken.util.WebRequester.RequestCallback;

public class HighScoresActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.highscores);
		
		HttpGet get = new HttpGet("http://vaskenmusic.appspot.com/vaskenmusicserver?genre="+getString(R.string.genre));
		
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
						Log.d(HighScoresActivity.class.toString(), responseSoFar.toString());
						
						highScores = new JSONObject(responseSoFar.toString()).getJSONObject("HighScores");
						
						JSONArray scoresToday = highScores.getJSONArray("Today");
						TableLayout tableLayoutToday = (TableLayout)findViewById(R.id.highScoresToday);
						fillLayoutWithData(scoresToday, tableLayoutToday);
						
						JSONArray scoresEver = highScores.getJSONArray("Ever");
						TableLayout tableLayoutEver = (TableLayout)findViewById(R.id.highScoresEver);
						fillLayoutWithData(scoresEver, tableLayoutEver);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					return true;
				}
				return false;
			}
		});
	}
	
	private void fillLayoutWithData(JSONArray scores, TableLayout tableLayout) {
		for (int i=0; i<5; i++){
			TableRow row = (TableRow)tableLayout.getChildAt(i+2);
			TextView name = (TextView)row.getChildAt(0);
			TextView score = (TextView)row.getChildAt(1);

			String nameText = "";
			String scoreText = "";
			if (scores.length() > i) {
				JSONObject highScore;
				try {
					highScore = scores.getJSONObject(i);
					nameText = highScore.getString("name");
					scoreText = highScore.getString("score");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			name.setText(nameText);
			score.setText(scoreText);
		}
	}
	
}
