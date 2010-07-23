package com.vasken.namethattune;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.vasken.namethattune.PreviewURLProvider.PreviewURLListener;
import com.vasken.util.UserTask;
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
        
        new BackgroundWorker().execute("rock");
    }
    
    private android.widget.Button.OnClickListener buttonClicked = new android.widget.Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Button b = (Button)v;
			if (b.getText().equals(correct)) {
				Toast.makeText(Main.this, "CORRECT!", Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(Main.this, "WRONGO!", Toast.LENGTH_SHORT).show();
			}
			new BackgroundWorker().execute("rock");
		}};
    
	public void sampleRetrievalError() {
		runOnUiThread(new Runnable() { public void run() {Toast.makeText(Main.this, "error getting song sample!  Retrying.", Toast.LENGTH_SHORT).show();}});
		new BackgroundWorker().execute("rock");
	}
	
	String trackToString(final Track track) {
		return track.getArtist() + " - " + track.getName();
	}
	
	public void displayOptions(final List<Track> tracks) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (tracks.size() < 4) {
					Log.d("displayOptions", "DID NOT GET ENOUGH TRACKS");
					sampleRetrievalError();
					return;
				}
				Track rightTrack = tracks.remove(0);
				correct = trackToString(rightTrack);
				Collections.shuffle(tracks);
				tracks.add((int)(Math.random() * 4), rightTrack);

				opt1.setText(trackToString(tracks.get(0)));
				opt2.setText(trackToString(tracks.get(1)));
				opt3.setText(trackToString(tracks.get(2)));
				opt4.setText(trackToString(tracks.get(3)));
			}});
	}
	
	class BackgroundWorker extends UserTask<String, List<Track>, List<Track>> {
		
		private LastFMDataProvider lastfm = new LastFMDataProvider();
		private PreviewURLProvider itunes = new PreviewURLProvider();
		
		@Override
		public List<Track> doInBackground(String... tags) {
			Track correct = lastfm.getRandomTopTrack(1000);
		//	Track correct = lastfm.getRandomTrackForTag(tags[0], 1000);
			Collection<Track> alternatives = lastfm.getSimilarTracks(correct);
			if (alternatives == null)
				sampleRetrievalError();
			final List<Track> tracks = new LinkedList<Track>(alternatives);
			itunes.getPreviewURL(trackToString(correct), new PreviewURLListener() {
				@Override
				public void somethingWentWrong() {
					sampleRetrievalError();
				}

				@Override
				public void urlReady(String url) {
					
					player.stop();
					// I should be able to reuse the same MediaPlayer, but I keep
					// getting errors that I don't yet understand about invalid states
					player.release();
					player = new MediaPlayer();
					try {
						player.setDataSource(url);
						player.prepare();
						player.start();
						if (tracks.size() < 4)
							sampleRetrievalError();
						else
							displayOptions(tracks);
					} catch (Exception e) {
						sampleRetrievalError();
						e.printStackTrace();
						return;
					}
				}});
			tracks.add(0, correct);
			return tracks;
		}
	}
}