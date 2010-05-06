package com.vasken.comics;

import com.vasken.comics.Downloaders.Downloader;
import com.vasken.comics.Downloaders.GoComicsDownloader;
import com.vasken.util.UserTask;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class Main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        new DownloadTask().execute(new GoComicsDownloader("http://www.gocomics.com/foxtrot/2010/04/25/"));
    }

    class DownloadTask extends UserTask<Downloader, Void, Comic> {
    	@Override
    	public Comic doInBackground(Downloader... params) {
    		return params[0].getComic();
    	}
    	
    	@Override
    	public void onPostExecute(Comic comic) {
    		if (comic != null) {
    			if (comic.image != null) {
	    		
		    		ImageView imgView = (ImageView)Main.this.findViewById(R.id.ImageView01);
		    		imgView.setImageBitmap(comic.image);
	    		}
    		}
    	}

    }
}
