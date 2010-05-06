package com.vasken.hitit;

import java.util.LinkedList;
import java.util.Queue;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vasken.util.UserTask;
import com.vasken.util.UserTask.Status;

public class Vote extends Activity {
	 
	private int NOT = 1;	// Maybe this should be 3, to be nice
	private int MEH = 5;
	private int HOT = 10;	// Maybe this should be 8, to be realistic 
	
	static final int NUM_WORKERS = 2;
    static final int DESIRED_QUEUE_LENGTH = 20;
	
	private boolean waitingForImage;
	private DownloaderTask downloadTask;
	private ProgressBar progressBar;
	private String currentId = null;
	
	// The worker pool should only be referenced in queueNextItem,
	// because it's not thread-safe
	private Queue<Worker> workerPool = new LinkedList<Worker>();
	private Queue<HotItem> itemQueue = new LinkedList<HotItem>();
	private Queue<RatingInfo> ratingsQueue = new LinkedList<RatingInfo>();
    //private Set<String> seenItems = new HashSet<String>();
	
    public OnClickListener defaultClickListener(final int rating) {
    	return new OnClickListener() {
	    	public void onClick(View v) {
	    		if (!waitingForImage)
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
        	workerPool.add(new Worker(getString(R.string.rate_url_female)));
        }
		
		((ImageButton)findViewById(R.id.hot)).setOnClickListener(defaultClickListener(HOT));		
		((ImageButton)findViewById(R.id.meh)).setOnClickListener(defaultClickListener(MEH));
		((ImageButton)findViewById(R.id.not)).setOnClickListener(defaultClickListener(NOT));

	   	progressBar = (ProgressBar) findViewById(R.id.progress_bar);
	   	
		refresh(0);
    }

	private void refresh(final int rating) {
	   	waitingForImage = true;
	   	
	   	ratingsQueue.add(new RatingInfo(currentId, rating));
		progressBar.setVisibility(View.VISIBLE);
		
    	HotItem item = itemQueue.poll();
    	if (item != null) {
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
    
    public void setTotals(Bitmap image, String totals, double results) {
    	if (image == null ) {
    		findViewById(R.id.rate_result).setVisibility(View.INVISIBLE);
    	}else {
    		findViewById(R.id.rate_result).setVisibility(View.VISIBLE);
    	}

    	((ImageView)findViewById(R.id.rate_result_thumbnail)).setImageBitmap(image);
    	if ( totals == null ) {
    		((ImageView)findViewById(R.id.rate_result_image)).setImageBitmap(null);
    		((TextView)findViewById(R.id.rate_result_totals)).setText("Not enough votes");
    	}else{
	    	String totalsString = Integer.valueOf(totals) > 5000 ? "Over 5000" : totals; 
	    	((TextView)findViewById(R.id.rate_result_totals)).setText(totalsString + " votes");
	    	
	    	if (results < 7) {
	    		((ImageView)findViewById(R.id.rate_result_image)).setImageResource(R.drawable.black_thumbs_down);
	    	}else if(results < 8.8) {
	    		((ImageView)findViewById(R.id.rate_result_image)).setImageResource(R.drawable.black_bottle);
	    	}else {
	    		((ImageView)findViewById(R.id.rate_result_image)).setImageResource(R.drawable.black_thumbs_up);
	    	}
    	}
    }
	
	public void showItem(final HotItem item) {
    	runOnUiThread(new Runnable() { public void run() { 
    		currentId = item.getRateId();
			((ImageView)findViewById(R.id.photo)).setImageBitmap(item.getImage());
			progressBar = (ProgressBar) findViewById(R.id.progress_bar);
			progressBar.setVisibility(View.GONE);
			waitingForImage = false;
			
			setTotals(item.getResultImage(), item.getResultTotals(), item.getResultAverage());
		}});
    }
    
    void itemReady(final HotItem item) {
    	if (item != null ){//&& !seenItems.contains(item.getRateId())) {
    		//seenItems.add(item.getRateId());
    		if (waitingForImage) {
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
			
			HotItem lastItem = new HotItem(null, "5");
			while(itemQueue.size() < DESIRED_QUEUE_LENGTH) {
				RatingInfo rating = ratingsQueue.poll();
				if (rating == null)
					rating = new RatingInfo(null, 0);
				
				Worker worker = workerPool.poll();
				if (worker != null) {
					nextItem = worker.getPageData(lastItem.getRateId(), 5);
					workerPool.add(worker);
					
					if (nextItem == null) {
						// We're most likely not connected to the internet.
						// Either way, let's not crash. Maybe we should show a toast or something....
						return null;						
					}
					
					if (isUsableImage(nextItem.getImage())) {
						if (lastItem.getImage() != null) {
							Log.d("---------------------", "Setting the result image" + nextItem.getImage().getHeight() + " " + nextItem.getResultTotals() + " " + nextItem.getResultAverage());
							nextItem.setResultImage(lastItem.getImage());
						}
						itemReady(nextItem);
						
						lastItem = nextItem;
					}
				}
				Log.d("-------------", "Queue Length: " + itemQueue.size());
			}
			Log.d("-------------", "The worker's done");
			
			return nextItem;
		}
		
		private boolean isUsableImage(Bitmap d) {
			return d != null && d.getHeight() > 10 && d.getWidth() > 10;
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