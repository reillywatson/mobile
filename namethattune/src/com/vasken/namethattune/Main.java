package com.vasken.namethattune;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vasken.namethattune.PreviewURLProvider.PreviewURLListener;
import com.vasken.util.UserTask;
import com.vasken.util.WebRequester;
import com.vasken.util.WebRequester.RequestCallback;

import net.roarsoftware.lastfm.Track;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Main extends Activity {
    /** Called when the activity is first created. */
	
	private MediaPlayer player = new MediaPlayer();
	private Button opt1, opt2, opt3, opt4;
	String correct;
	boolean downloadInProgress = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        opt1 = (Button)findViewById(R.id.Button01);
        opt2 = (Button)findViewById(R.id.Button02);
        opt3 = (Button)findViewById(R.id.Button03);
        opt4 = (Button)findViewById(R.id.Button04);
        opt1.setOnClickListener(buttonClicked);
        opt2.setOnClickListener(buttonClicked);
        opt3.setOnClickListener(buttonClicked);
        opt4.setOnClickListener(buttonClicked);
        getNewTrack();
    }
    
    private android.widget.Button.OnClickListener buttonClicked = new android.widget.Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Button b = (Button)v;
			if (b.getText().equals(correct)) {
				Toast.makeText(Main.this, "CORRECT!", Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(Main.this, "WRONGO!  It was " + correct, Toast.LENGTH_SHORT).show();
			}
			if (!downloadInProgress)
				getNewTrack();
		}};
    
	public void sampleRetrievalError() {
		runOnUiThread(new Runnable() { public void run() {Toast.makeText(Main.this, "error getting song sample!  Retrying.", Toast.LENGTH_SHORT).show();}});
		getNewTrack();
	}

	void getNewTrack() {
		new BackgroundWorker().execute("rock");
		downloadInProgress = true;
	}
	
	String trackToString(final Track track) {
		return track.getArtist() + " - " + track.getName();
	}
	
	public void displayOptions(final List<String> tracks) {
		downloadInProgress = false;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (tracks.size() < 4) {
					Log.d("displayOptions", "DID NOT GET ENOUGH TRACKS");
					sampleRetrievalError();
					return;
				}
				correct = tracks.remove(0);
				Collections.shuffle(tracks);
				tracks.add((int)(Math.random() * 4), correct);

				opt1.setText(tracks.get(0));
				opt2.setText(tracks.get(1));
				opt3.setText(tracks.get(2));
				opt4.setText(tracks.get(3));
			}});
	}
	
	// this cache is pretty dumb for now!
	final JSONCache cache = new JSONCache();
	
	class JSONCache { 
		JSONObject _obj;
		String _id;
		
		JSONObject cachedObject(String id) {
			if (_id != null && _id.equals(id))
				return _obj;
			return null;
		}
		
		void cacheObject(String id, JSONObject obj) {
			_id = id;
			_obj = obj;
		}
	}
	
	class BackgroundWorker extends UserTask<String, List<String>, List<String>> {
		
		void parseJSON(JSONObject json) throws JSONException {
			List<String> tracks = new LinkedList<String>();
			JSONArray entries = json.getJSONObject("feed").getJSONArray("entry");
			for (int i = 0; i < 4; i++) {
				JSONObject randomTrack = entries.getJSONObject((int) (Math.random() * entries.length()));
				String title = randomTrack.getJSONObject("title").getString("label");
				if (i == 0) {
					String link = randomTrack.getJSONArray("link").getJSONObject(1).getJSONObject("attributes").getString("href");
					previewUrlReady(link);
				}
				tracks.add(title);
			}
			displayOptions(tracks);
		}
		
		private void previewUrlReady(String url) {
			player.stop();
			// I should be able to reuse the same MediaPlayer, but I keep
			// getting errors that I don't yet understand about invalid states
			player.release();
			player = new MediaPlayer();
			try {
				player.setDataSource(url);
				player.prepare();
				player.start();
			} catch (Exception e) {
				sampleRetrievalError();
				e.printStackTrace();
				return;
			}
		}
		
		@Override
		public List<String> doInBackground(String... tags) {
			final List<String> tracks = new LinkedList<String>();
			final String url = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/sf=143441/limit=300/genre=18/explicit=true/json";
			JSONObject json = cache.cachedObject(url);
			
			if (json != null) {
				try {
					parseJSON(json);
				} catch (JSONException e) {}
			}
			else {
				new WebRequester().makeRequest(new HttpGet(url), new RequestCallback() {
					@Override
					public boolean handlePartialResponse(StringBuilder responseSoFar, boolean isFinal) {
						if (isFinal) {
							
							try {
							JSONObject json = new JSONObject(responseSoFar.toString());
							parseJSON(json);
							cache.cacheObject(url, json);
	
							} catch (JSONException e) {}
							return true;
						}
						return false;
					}
				});
			}
			return tracks;
		}
	}
}