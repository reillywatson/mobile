package com.vasken.SimpsonsQuote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
			quotestore = new QuoteStore(this, R.raw.simpsons);
		} catch (IOException e) {
			Log.e(getClass().getName(), Log.getStackTraceString(e));
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
			Toast.makeText(Main.this, status, Toast.LENGTH_SHORT).show();
			loadNewQuote();
		}};

	// Man this is suuuuper generic, maybe such a function already exists?
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
    	final WebView quoteview = (WebView)findViewById(R.id.quote);
    	SimpsonsQuote quote = quotestore.randomQuote();
		List<String> episodes = new ArrayList<String>();
		currentEpisode = quote.episode;
		episodes.add(quote.episode);
		populateListWithUniqueElements(episodes, 3, new Callable<String>() { public String call() throws Exception {
				return quotestore.randomEpisode();
			}});
		
		Collections.shuffle(episodes);
    	quoteview.loadData(quote.quote, "text/html", "utf-8");
    	quoteview.setBackgroundColor(0);
    	quoteview.getSettings().setJavaScriptEnabled(true);  
    	  
    	/* WebViewClient must be set BEFORE calling loadUrl! */  
    	quoteview.setWebViewClient(new WebViewClient() {  
    	    @Override  
    	    public void onPageFinished(WebView view, String url)  
    	    {  
    	    	quoteview.loadUrl("javascript:(function() { " +  
    	                "document.getElementsByTagName('body')[0].style.color = 'white'; " +  
    	                "})()");  
    	    }  
    	});  
    	// otherwise if we go from something that fits on one page to something that doesn't, the
    	// scroll indicator doesn't show up
    	quoteview.setVerticalScrollbarOverlay(true);
    	opt1.setText(episodes.get(0));
    	opt2.setText(episodes.get(1));
    	opt3.setText(episodes.get(2));
    }
}