package com.vasken.hitit;

import java.util.LinkedList;
import java.util.Queue;

import com.vasken.hitit.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Vote extends Activity {
	
	class RatingInfo {
		public String id;
		public int rating;
		public RatingInfo(String id, int rating) {
			this.id = id;
			this.rating = rating;
		}
	}
	
	private HotItem currentItem;
	
	boolean waitingForImage;

	// The worker pool should only be referenced in queueNextItem,
	// because it's not thread-safe
	private Queue<Worker> workerPool = new LinkedList<Worker>();
	private Queue<HotItem> itemQueue = new LinkedList<HotItem>();
	private Queue<RatingInfo> ratingsQueue = new LinkedList<RatingInfo>();
	 
	private int NOT = 1;
	private int MEH = 5;
	private int HOT = 10;
	
	static final int NUM_WORKERS = 2;
	// This queue is small because we keep running out of memory, and it's not that clear why.
	// I think our HotItems aren't being GCed, or we occasionally get back a really big image (1/2 meg)
    static final int DESIRED_QUEUE_LENGTH = 5;
    
    int pendingQueue = 0;
    
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
        
        for (int i = 0; i < NUM_WORKERS; i++) {
        	workerPool.add(new Worker(this));
        }
		
		((ImageButton)findViewById(R.id.hot)).setOnClickListener(defaultClickListener(HOT));		
		((ImageButton)findViewById(R.id.meh)).setOnClickListener(defaultClickListener(MEH));
		((ImageButton)findViewById(R.id.not)).setOnClickListener(defaultClickListener(NOT));
        
		refresh(0);
		
		// You can use this to test ADMOB - Logcat should print out whatever your device ID is
		// com.admob.android.ads.AdManager.setTestDevices( new String[] { "A7078C908F25F51F01EAD8C45B005A63" } );
    }

	private void refresh(final int rating) {
		getNext();
		new Thread() {
		    public void run() {
		    	String id = currentItem == null ? null : currentItem.getRateId();
		    	ratingsQueue.add(new RatingInfo(id, rating));
		    	queueNextItem();
		    }
		  }.start();
	}
    
    public void showItem(final HotItem item) {
    	if (item == null || item.getImage() == null || item.getImage().getHeight() < 10) {
    		Log.d(getClass().getName(), "Couldn't find enough data for this page.");
    		return;
    	}
    	runOnUiThread(new Runnable() { public void run() { 
			((ImageView)findViewById(R.id.photo)).setImageBitmap(item.getImage());
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
    	if (pendingQueue < DESIRED_QUEUE_LENGTH || itemQueue.size() < NUM_WORKERS) {
    		new Thread() { public void run() {
    			queueNextItem();
    		}}.run();
    	}
    }
    
    void queueNextItem() {
    	Worker worker = workerPool.poll();
		if (worker != null) {
			pendingQueue++;
			RatingInfo rating = ratingsQueue.poll();
			if (rating == null)
				rating = new RatingInfo(null, 0);
			Log.d("QUEUE LENGTH", Integer.toString(itemQueue.size()));
    		HotItem nextItem = worker.getPageData(rating.id,rating.rating);
    		workerPool.add(worker);
    		itemReady(nextItem);
		}
    }
    
    public void getNext() {
    	waitingForImage = true;
    	HotItem item = itemQueue.poll();
    	if (item != null) {
    		pendingQueue--;
    		showItem(item);
    	}
    }
}