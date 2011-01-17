package com.vasken.namethattune;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.roarsoftware.lastfm.Track;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vasken.util.UserTask;
import com.vasken.util.WebRequester;
import com.vasken.util.WebRequester.RequestCallback;

public class Main extends Activity {
	private static int NUM_ANSWERS = 3;
	private static final int SECONDS_TO_WAIT_BEFORE_RESULT_UPDATES = 2 * 1000;
	private static final int SECONDS_TO_WAIT_BEFORE_TIMER_UPDATES = 1 * 1000;
	
	private Handler mHandler = new Handler();
	private MediaPlayer player = new MediaPlayer();
	private Button opt1, opt2, opt3;
	private String correct;
	private static int secondsLeftInTrack;
	private boolean questionWasAnswered = false;
	private boolean downloadInProgress = false;
	private Context theContext;
	
	private State theState;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);
        setVolumeControlStream(AudioManager.STREAM_MUSIC); 

//		FOR DEBUG PURPOSES
//      SharedPreferences preferences = this.getSharedPreferences("PREFERENCES_NAME", Context.MODE_PRIVATE);
//		Editor editor = preferences.edit();
//		editor.clear();
//		editor.commit();
		
        theContext = this;
        theState = State.loadSavedInstance(this);
        
        getNewTrack(); 
    }
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		menu.add(getString(R.string.high_scores)).
			setIcon(R.drawable.menu_high_scores);
		
		menu.add(getString(R.string.submit_score, theState.getLastStreak())).
			setIcon(R.drawable.submit_icon).
			setEnabled(theState.getLastStreak() != 0);
		
		menu.add(getString(R.string.achievements)).
			setIcon(R.drawable.menu_ach);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals(getString(R.string.high_scores))) {
			Intent intent = new Intent(this, HighScoresActivity.class);
			this.startActivity(intent);
		} else if (item.getTitle().equals(getString(R.string.achievements))) {
			Intent intent = new Intent(this, AchievementsActivity.class);
			this.startActivity(intent);
		} else {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);  
			alert.setTitle("High Score Entry");  
			  
			// Set an EditText view to get user input   
			final EditText input = new EditText(this);
			input.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
			input.setSingleLine(true);
			input.setHint(R.string.enter_name);
			alert.setView(input);
			  
			alert.setPositiveButton("Send",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							submitScore(input.getText().toString(), theState.getLastStreak());
						}
					});
			  
			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {

						}
					});

			alert.show();
		}
		return true;
	}
    
    private void showGameScreen() {
    	setContentView(R.layout.main);
    	
        opt1 = (Button)findViewById(R.id.Button01);
        opt2 = (Button)findViewById(R.id.Button02);
        opt3 = (Button)findViewById(R.id.Button03);
        opt1.setOnClickListener(buttonClicked);
        opt2.setOnClickListener(buttonClicked);
        opt3.setOnClickListener(buttonClicked);
        
        theState.setLastStreak(0);
        theState.setStreak(0);
		
        UserActionManager.openedApplication(theState);
        
        ((TextView)findViewById(R.id.answerStreak)).setText(getString(R.string.streak, theState.getStreak()));
    }
    
    @Override
    protected void onPause() {
    	// Save user's achievements
    	theState.save(this);
    	
    	// Do this so we don't keep playing music after the user exits
    	player.stop();
    	super.onPause();
    }
    
    private Button.OnClickListener buttonClicked = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (questionWasAnswered)
				return;
			
			Button b = (Button)v;
			if (b.getText().equals(correct)) {
				Achievement achievement = UserActionManager.correctAnswer(theState);

//				FOR DEBUG PURPOSES
//				if (achievement.isEmpty()) {
//					achievement = UserActionManager.addNextAchievement(theState);
//				}
				
				if (!achievement.isEmpty()) {
					showAchievement(achievement);
				}
				
				ImageView answer = (ImageView)Main.this.findViewById(R.id.answer);
				answer.setImageResource(R.drawable.correct);
				Log.d(this.getClass().toString(), theState.getAchievements().toString());
			}
			else {
				ImageView answer = (ImageView)Main.this.findViewById(R.id.answer);
				answer.setImageResource(R.drawable.wrong);
				UserActionManager.wrongAnswer(theState);
			}
			
			((TextView)findViewById(R.id.answerStreak)).setText(getString(R.string.streak, theState.getStreak()));
			questionWasAnswered = true;
			
			mHandler.removeCallbacks(mUpdateTimeTask);
			mHandler.postDelayed(mUpdateResult, SECONDS_TO_WAIT_BEFORE_RESULT_UPDATES);
			
			if (!downloadInProgress)
				getNewTrack();
		}};
    
	public void sampleRetrievalError() {
		runOnUiThread(new Runnable() { public void run() {Toast.makeText(Main.this, "error getting song sample!  Retrying.", Toast.LENGTH_SHORT).show();}});
		getNewTrack();
	}

	protected void showAchievement(Achievement achievement) {
		View layout = getLayoutInflater().inflate(R.layout.achivement_layout,
		                               (ViewGroup) findViewById(R.id.achievement_layout_root));

		ImageView image = (ImageView) layout.findViewById(R.id.achievement_image);
		image.setImageResource(achievement.getIcon());

		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.TOP|Gravity.LEFT, 20, 20);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}

	protected void submitScore(String name, int streak) {
		String appVersion;
    	ComponentName comp = new ComponentName(this, Main.class);
    	PackageInfo pinfo;
		try {
			pinfo = this.getPackageManager().getPackageInfo(comp.getPackageName(), 0);
			appVersion = String.valueOf(pinfo.versionCode);
		} catch (NameNotFoundException e1) {
			appVersion = "0";
		} 
    	
		HttpPost post = new HttpPost("http://vaskenmusic.appspot.com/vaskenmusicserver");
		post.setHeader("Content-type", "application/x-www-form-urlencoded");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("data", 
        		"name=" + name +"-=-=-=-=-=" +
        		"score="+ streak +"-=-=-=-=-=" +
        		"genre="+getString(R.string.genre)+"-=-=-=-=-=" +
        		"version="+appVersion));
        
        try {
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			Log.e(Main.class.toString(), "", e);
			return;
		}
		
		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean hasMobileConnection = conMan.getNetworkInfo(0).isConnectedOrConnecting();
		boolean hasWifiConnection = conMan.getNetworkInfo(1).isConnectedOrConnecting();
		
		if (!hasMobileConnection && !hasWifiConnection) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(theContext, "You need an internet connection, to submit your score.", Toast.LENGTH_LONG).show();
				}
			});
			return;
		}

		new WebRequester().makeRequest(post, new RequestCallback() {
			@Override
			public boolean handlePartialResponse(StringBuilder responseSoFar, boolean isFinal) {
				if (isFinal) {
					Log.d(Main.class.toString(), responseSoFar.toString());
					
					Intent intent = new Intent(theContext, HighScoresActivity.class);
					theContext.startActivity(intent);
					
					return true;
				}
				return false;
			}
		});
	}

	void getNewTrack() {
		new BackgroundWorker().execute();
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
				if (tracks.size() < NUM_ANSWERS) {
					Log.d("displayOptions", "DID NOT GET ENOUGH TRACKS");
					sampleRetrievalError();
					return;
				}
				
				correct = tracks.remove(0);
				questionWasAnswered = false;
				Collections.shuffle(tracks);
				tracks.add((int)(Math.random() * NUM_ANSWERS), correct);

				opt1.setText(tracks.get(0));
				opt2.setText(tracks.get(1));
				opt3.setText(tracks.get(2));
				
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

	private Runnable mSetupTimeTask = new Runnable() {
		public void run() {
			secondsLeftInTrack = Integer.parseInt(getString(R.string.seconds));
			TextView timeLeft = (TextView)findViewById(R.id.timerTimeLeft);
			timeLeft.setText("" + secondsLeftInTrack);
			
			mHandler.removeCallbacks(mUpdateTimeTask);
			mHandler.postDelayed(mUpdateTimeTask, SECONDS_TO_WAIT_BEFORE_TIMER_UPDATES);
		}
	};
	
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			secondsLeftInTrack--;
			TextView timeLeft = (TextView)findViewById(R.id.timerTimeLeft);
			timeLeft.setText("" + secondsLeftInTrack);
			
			if (secondsLeftInTrack > 0) {
				mHandler.postDelayed(this, SECONDS_TO_WAIT_BEFORE_TIMER_UPDATES);
			} else {
				secondsLeftInTrack = Integer.parseInt(getString(R.string.seconds));;
				player.stop();
			}
		}
	};
	
	private Runnable mUpdateResult = new Runnable() {
		public void run() {
			ImageView answer = (ImageView)Main.this.findViewById(R.id.answer);
			answer.setImageDrawable(null);
		}
	};
	
	class BackgroundWorker extends UserTask<String, List<String>, List<String>> {
		
		void parseJSON(JSONObject json) throws JSONException {
			List<String> tracks = new LinkedList<String>();
			JSONArray entries = json.getJSONObject("feed").getJSONArray("entry");
			while (tracks.size() < NUM_ANSWERS) {
				JSONObject randomTrack = entries.getJSONObject((int) (Math.random() * entries.length()));
				String title = randomTrack.getJSONObject("title").getString("label");
				if (!tracks.contains(title)) {
					if (tracks.size() == 0) {
						String link = randomTrack.getJSONArray("link").getJSONObject(1).getJSONObject("attributes").getString("href");
						previewUrlReady(link);
					}
					tracks.add(title);
				}
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

				if (findViewById(R.id.splashScreen) != null) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showGameScreen();
						}
					});
				}
				runOnUiThread(mSetupTimeTask);

			} catch (Exception e) {
				e.printStackTrace();
				sampleRetrievalError();
				return;
			}
		}

		@Override
		public List<String> doInBackground(String... tags) {
			final List<String> tracks = new LinkedList<String>();
			final String url = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/" 
				+ "sf=143441/" 			// Some magic shit
				+ "limit=300/" 			// Number of results 
				+ "genre=" + getString(R.string.genre) + "/"  			// Type of music
				+ "explicit=true/"  	// Naughty songs
				+ "json";
			JSONObject json = cache.cachedObject(url);
			
			if (json != null) {
				try {
					parseJSON(json);
				} catch (JSONException e) {}
			}
			else {
				ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				boolean hasMobileConnection = conMan.getNetworkInfo(0).isConnectedOrConnecting();
				boolean hasWifiConnection = conMan.getNetworkInfo(1).isConnectedOrConnecting();
				
				if (!hasMobileConnection && !hasWifiConnection) {					
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(theContext, "The application needs an internet connection, to work", Toast.LENGTH_LONG).show();
							finish();
						}
					});
					return tracks;
				}
				
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