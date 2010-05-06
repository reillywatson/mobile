package com.vasken.comics;

import java.text.DateFormat;

import com.vasken.comics.Downloaders.Downloader;
import com.vasken.comics.Downloaders.GoComicsDownloader;
import com.vasken.util.UserTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        downloadComic("http://www.gocomics.com/foxtrot/2005/01/01");
    }
    
    public void downloadComic(String url) {
    	  new DownloadTask().execute(new GoComicsDownloader(url));
    }

    class DownloadTask extends UserTask<Downloader, Void, Comic> {
    	@Override
    	public Comic doInBackground(Downloader... params) {
    		return params[0].getComic();
    	}
    	
    	@Override
    	public void onPostExecute(final Comic comic) {
    		if (comic != null) {
    			if (comic.image != null) {
		    		ImageView imgView = (ImageView)Main.this.findViewById(R.id.ImageView01);
		    		imgView.setImageBitmap(comic.image);
		    		Button prev = (Button)Main.this.findViewById(R.id.prev_comic);
		    		Button next = (Button)Main.this.findViewById(R.id.next_comic);
		    		prev.setEnabled(comic.prevUrl != null);
		    		next.setEnabled(comic.nextUrl != null);
		    		if (comic.prevUrl != null) {
		    			prev.setOnClickListener(new OnClickListener() {
		    				public void onClick(View arg0) {
								Main.this.downloadComic(comic.prevUrl);
							}});
		    		}
		    		if (comic.nextUrl != null) {
		    			next.setOnClickListener(new OnClickListener() {
		    				public void onClick(View arg0) {
								Main.this.downloadComic(comic.nextUrl);
							}});
		    		}
		    		if (comic.pubDate != null) {
		    			TextView tv = (TextView)Main.this.findViewById(R.id.pub_date);
		    			tv.setText(DateFormat.getDateInstance().format(comic.pubDate));
		    		}
	    		}
    		}
    	}

    }
}
