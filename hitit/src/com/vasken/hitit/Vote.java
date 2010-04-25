package com.vasken.hitit;

import java.util.LinkedList;
import java.util.Queue;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.vasken.hitit.UserTask.Status;

public class Vote extends Activity {
	 
	private int NOT = 1;	// Maybe this should be 3, to be nice
	private int MEH = 5;
	private int HOT = 10;	// Maybe this should be 8, to be realistic 
	
	static final int NUM_WORKERS = 2;
    static final int DESIRED_QUEUE_LENGTH = 5;
	
	private boolean waitingForImage;
	private DownloaderTask downloadTask;
	private ProgressBar progressBar;
	
	// The worker pool should only be referenced in queueNextItem,
	// because it's not thread-safe
	private Queue<Worker> workerPool = new LinkedList<Worker>();
	private Queue<HotItem> itemQueue = new LinkedList<HotItem>();
	private Queue<RatingInfo> ratingsQueue = new LinkedList<RatingInfo>();
	private HotItem currentItem;
	
    int pendingQueue = 0;
    
    public OnClickListener defaultClickListener(final int rating) {
    	return new OnClickListener() {
	    	public void onClick(View v) {
	    		if (!waitingForImage)
	    			ratingsQueue.add(new RatingInfo(currentItem.getRateId(), rating));
	    			refresh(rating);
	    	}
    	};
    }
    
	/** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        downloadTask = null;
        
        for (int i = 0; i < NUM_WORKERS; i++) {
        	workerPool.add(new Worker(this));
        }
		
		((ImageButton)findViewById(R.id.hot)).setOnClickListener(defaultClickListener(HOT));		
		((ImageButton)findViewById(R.id.meh)).setOnClickListener(defaultClickListener(MEH));
		((ImageButton)findViewById(R.id.not)).setOnClickListener(defaultClickListener(NOT));
        
		refresh(0);
    }

	private void refresh(final int rating) {
	   	waitingForImage = true;

	   	progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		progressBar.setVisibility(View.VISIBLE);
		
    	HotItem item = itemQueue.poll();
    	if (item != null) {
    		pendingQueue--;
    		showItem(item);
    	}
    	
    	
    	if (downloadTask == null || downloadTask.getStatus() == Status.FINISHED || downloadTask.isCancelled()) {
    		downloadTask = null;
    		downloadTask = new DownloaderTask();
        	downloadTask.execute();
    	}else if (downloadTask.getStatus() == Status.RUNNING){
    		Log.d( getClass().getName(), "Don't do anything. We've already got a running task");
    	}else{
    		Log.d( getClass().getName(), "WTF. How did I get here?");
    	}
	}
    
    public void showItem(final HotItem item) {    	
    	if (item == null || item.getImage() == null || item.getImage().getIntrinsicHeight() < 10) {
    		Log.w(getClass().getName(), "Couldn't find enough data for this page. THIS SHOULDN'T HAPPEN! ");
    		return;
    	}
    	runOnUiThread(new Runnable() { public void run() { 
    		currentItem = item;
			((ImageView)findViewById(R.id.photo)).setImageDrawable(item.getImage());
			progressBar = (ProgressBar) findViewById(R.id.progress_bar);
			progressBar.setVisibility(View.GONE);
			waitingForImage = false;
		}});
    }
    
    void itemReady(final HotItem item) {
    	if (item != null) {
	    	if (waitingForImage) {
	    		pendingQueue--;
				showItem(item);
	    	}
	    	else {
	    		itemQueue.add(item);
	    	}
    	}
    }
    
    private class DownloaderTask extends UserTask<Void, HotItem, HotItem> {

		@Override
		public HotItem doInBackground(Void... params) {
			HotItem nextItem = null;
			
			while(itemQueue.size() < DESIRED_QUEUE_LENGTH) {
				RatingInfo rating = ratingsQueue.poll();
				if (rating == null)
					rating = new RatingInfo(null, 0);
				
				Worker worker = workerPool.poll();
				if (worker != null) {
					nextItem = worker.getPageData(rating.id,rating.rating);
					workerPool.add(worker);
					
					if (nextItem == null) {
						// We're most likely not connected to the internet.
						// Either way, let's not crash. Maybe we should show a toast or something....
						return null;						
					}
					
					if (isUsableImage(nextItem.getImage())) {
						itemReady(nextItem);
					}
				}
	
				Log.d("QUEUE LENGTH", Integer.toString(itemQueue.size()));
			}
			
			return nextItem;
		}
		
		private boolean isUsableImage(Drawable d) {
			return d != null && d.getIntrinsicHeight() > 10 && d.getIntrinsicWidth() > 10;
		}
    	
    }
	
	class RatingInfo {
		public String id;
		public int rating;
		public RatingInfo(String id, int rating) {
			this.id = id;
			this.rating = rating;
		}
	}
}