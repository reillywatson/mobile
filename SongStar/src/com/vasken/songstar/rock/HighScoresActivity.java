package com.vasken.songstar.rock;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.vasken.songstar.rock.R;
import com.vasken.util.WebRequester;
import com.vasken.util.WebRequester.RequestCallback;

public class HighScoresActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.highscores);
		
		final Context theContext = this;
		
		HttpGet get = new HttpGet("http://vaskenmusic.appspot.com/vaskenmusicserver?genre="+getString(R.string.genre));
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(theContext, R.string.error_no_internet, Toast.LENGTH_LONG).show();					
				}
			});
		}
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
