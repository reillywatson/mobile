package com.vasken.namethattune;

import com.vasken.namethattune.PreviewURLFactory.PreviewURLListener;

import android.app.Activity;
import android.media.AsyncPlayer;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class Main extends Activity implements PreviewURLListener {
    /** Called when the activity is first created. */
	
	private boolean isDownloading = false;
	private AsyncPlayer player = new AsyncPlayer("previewer");
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        EditText edit = (EditText)findViewById(R.id.EditText01);
        edit.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (isDownloading)
					return false;
				if (event.getAction() == KeyEvent.ACTION_UP) {
					Log.d("download started", v.getText().toString());
					isDownloading = true;
					new PreviewURLFactory().getPreviewURL(v.getText().toString(), Main.this);
				}
				return true;
			}
        	
        });
    }

	@Override
	public void urlReady(String url) {
		player.stop();
		player.play(this, Uri.parse(url), false, AudioManager.STREAM_MUSIC);
		isDownloading = false;
	}
	
	@Override
	public void somethingWentWrong() {
		Toast.makeText(this, "error getting song sample!", Toast.LENGTH_SHORT).show();
		isDownloading = false;
	}
}