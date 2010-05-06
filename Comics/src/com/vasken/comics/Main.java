package com.vasken.comics;

import com.vasken.comics.Downloaders.DinosaurComicsDownloader;
import com.vasken.comics.Downloaders.Downloader;
import com.vasken.comics.Downloaders.XKCDDownloader;
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
        downloadComic("http://www.qwantz.com/index.php?comic=1705");
    }
    
    public void downloadComic(String url) {
    	  new DownloadTask().execute(new DinosaurComicsDownloader(url));
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
		    		imgView.setLongClickable(comic.altText != null);
		    		TextView alt = (TextView)Main.this.findViewById(R.id.alt_text);
		    		alt.setVisibility((comic.altText != null) ? View.VISIBLE : View.GONE);
		    		if (comic.altText != null) {
		    			alt.setText(comic.altText);
		    		}
		    		Button prev = (Button)Main.this.findViewById(R.id.prev_comic);
		    		Button next = (Button)Main.this.findViewById(R.id.next_comic);
		    		boolean enablePrev = comic.prevUrl != null && !comic.prevUrl.equals(comic.url);
		    		boolean enableNext = comic.nextUrl != null && !comic.nextUrl.equals(comic.url);
		    		prev.setEnabled(enablePrev);
		    		next.setEnabled(enableNext);
		    		if (enablePrev) {
		    			prev.setOnClickListener(new OnClickListener() {
		    				public void onClick(View arg0) {
								Main.this.downloadComic(comic.prevUrl);
							}});
		    		}
		    		if (enableNext) {
		    			next.setOnClickListener(new OnClickListener() {
		    				public void onClick(View arg0) {
								Main.this.downloadComic(comic.nextUrl);
							}});
		    		}
		    		if (comic.title != null) {
		    			TextView tv = (TextView)Main.this.findViewById(R.id.title);
		    			tv.setText(comic.title);
		    		}
	    		}
    		}
    	}

    }
}
