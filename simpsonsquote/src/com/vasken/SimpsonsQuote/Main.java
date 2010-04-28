package com.vasken.SimpsonsQuote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class Main extends Activity {
	QuoteStore quotestore;
	int questionNumber;
	Button opt1;
	Button opt2;
	Button opt3;
	String currentEpisode;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        opt1 = (Button)findViewById(R.id.opt1);
        opt2 = (Button)findViewById(R.id.opt2);
        opt3 = (Button)findViewById(R.id.opt3);
        try {
			quotestore = new QuoteStore(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		questionNumber = 0;
		loadNewQuote();
		opt1.setOnClickListener(buttonClicked);
		opt2.setOnClickListener(buttonClicked);
		opt3.setOnClickListener(buttonClicked);
    }
    
    OnClickListener buttonClicked = new OnClickListener() {
		public void onClick(View v) {
			Button b = (Button)v;
			String status;
			if (b.getText().equals(currentEpisode)) {
				status = "Correct, you are clearly amazing!";
			}
			else {
				status = "Wrong, dummy!  That was from " + currentEpisode;
			}
			Log.d("WHERE'S MY TOAST!", status);
			Toast.makeText(Main.this, status, Toast.LENGTH_SHORT).show();
			loadNewQuote();
		}};
    
    // Fischer-Yeates shuffle
    static Random rng = new Random();
    <T extends Object>void shuffle(List<T> list) {
        // n is the number of items remaining to be shuffled.
        for (int n = list.size(); n > 1; n--) {
            // Pick a random element to swap with the nth element.
            int k = rng.nextInt(n);  // 0 <= k <= n-1 (0-based array)
            // Swap array elements.
            T tmp = list.get(k);
            list.set(k, list.get(n - 1));
            list.set(n - 1, tmp);
        }
    }
    
    <T extends Object>void populateListWithUniqueElements(List<T> list, int desiredSize, Callable<T> generator) {
    	try {
	    	while (list.size() < desiredSize) {	
	    		T newObj = generator.call();
	    		if (!list.contains(newObj)) {
	    			list.add(newObj);
	    		}
	    	}
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    void loadNewQuote() {
    	questionNumber++;
    	if (questionNumber > quotestore.numSeasons())
    		questionNumber = 1;
    	WebView quoteview = (WebView)findViewById(R.id.quote);
    	SimpsonsQuote quote = quotestore.quoteBySeason(questionNumber);
		List<String> episodes = new ArrayList<String>();
		currentEpisode = quote.episode;
		episodes.add(quote.episode);
		populateListWithUniqueElements(episodes, 3, new Callable<String>() { public String call() throws Exception {
				return quotestore.randomEpisode();
			}});
		
		shuffle(episodes);
    	quoteview.loadData(quote.quote, "text/html", "utf-8");
    	opt1.setText(episodes.get(0));
    	opt2.setText(episodes.get(1));
    	opt3.setText(episodes.get(2));
    }
}