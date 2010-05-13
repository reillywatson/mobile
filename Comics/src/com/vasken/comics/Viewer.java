package com.vasken.comics;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.vasken.comics.Downloaders.Downloader;
import com.vasken.util.StringUtils;
import com.vasken.util.UserTask;

public class Viewer extends Activity {
	Downloader currentDownloader;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.viewer);
    	int comicNo = 0;
    	Bundle extras = getIntent().getExtras(); 
    	if(extras !=null) {
    		comicNo = extras.getInt("com.vasken.comics.comicNo");
    	}
    	selectComic(ComicList.comicList().get(comicNo));
    }
	
	public void selectComic(ComicInfo info) {
	    	try {
				currentDownloader = info.downloaderConstructor.call();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			downloadComic(info.startUrl);
	    }
	    
	    public void downloadComic(String url) {
	    	currentDownloader.setUrl(url);
	    	new DownloadTask().execute(currentDownloader);
	    }

	    class DownloadTask extends UserTask<Downloader, Void, Comic> {
	    	@Override
	    	public Comic doInBackground(Downloader... params) {
	    		return params[0].getComic();
	    	}
	    	
	    	@Override
	    	public void onPostExecute(final Comic comic) {
	    		if (comic != null) {
	    			WebView webView = (WebView)Viewer.this.findViewById(R.id.WebView);
		    		webView.setVisibility((comic.image != null) ? View.VISIBLE : View.GONE);
		    		if (comic.image != null) {
		    			webView.loadUrl(comic.image);
	    			}
		    		TextView alt = (TextView)Viewer.this.findViewById(R.id.alt_text);
		    		alt.setVisibility((comic.altText != null) ? View.VISIBLE : View.GONE);
		    		if (comic.altText != null) {
		    			alt.setText(StringUtils.unescapeHtml(comic.altText));
		    		}
		    		Button prev = (Button)Viewer.this.findViewById(R.id.prev_comic);
		    		Button next = (Button)Viewer.this.findViewById(R.id.next_comic);
		    		boolean enablePrev = comic.prevUrl != null && !comic.prevUrl.equals(comic.url);
		    		boolean enableNext = comic.nextUrl != null && !comic.nextUrl.equals(comic.url);
		    		prev.setEnabled(enablePrev);
		    		next.setEnabled(enableNext);
		    		if (enablePrev) {
		    			prev.setOnClickListener(new OnClickListener() {
		    				public void onClick(View arg0) {
		    					Viewer.this.downloadComic(comic.prevUrl);
							}});
		    		}
		    		if (enableNext) {
		    			next.setOnClickListener(new OnClickListener() {
		    				public void onClick(View arg0) {
		    					Viewer.this.downloadComic(comic.nextUrl);
							}});
		    		}
	    			TextView tv = (TextView)Viewer.this.findViewById(R.id.title);
		    		if (comic.title != null) {
		    			tv.setText(StringUtils.unescapeHtml(comic.title));
		    		} else {
		    			tv.setText("");
		    		}
		    		
	    		}
	    	}
	    }
	    
}
