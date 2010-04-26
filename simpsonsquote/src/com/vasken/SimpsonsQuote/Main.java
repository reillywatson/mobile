package com.vasken.SimpsonsQuote;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;

public class Main extends Activity {
	QuoteStore quotestore;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        try {
			quotestore = new QuoteStore(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		loadNewQuote();
		((WebView)findViewById(R.id.quote)).setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					loadNewQuote();
				}
				return true;
			}});
    }
    
    void loadNewQuote() {
    	WebView quoteview = (WebView)findViewById(R.id.quote);
		SimpsonsQuote quote = quotestore.randomQuoteThatIsntTerrible();
    	quoteview.loadData(quote.quote, "text/html", "utf-8");
    }
}