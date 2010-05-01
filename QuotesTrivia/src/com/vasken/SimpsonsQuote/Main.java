package com.vasken.SimpsonsQuote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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
	String currentAnswer;
	Random rand = new Random();
	
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
			if (b.getText().equals(currentAnswer)) {
				status = "Correct, you are clearly amazing!";
			}
			else {
				status = "Wrong, dummy!  That was " + currentAnswer;
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
    
    String removeSpeaker(String quote) {
    	int slashB = quote.indexOf("</b>");
    	String noSpeaker = quote.substring(slashB + 5, quote.length() - 1).trim();
    	if (noSpeaker.startsWith(":"))
    			noSpeaker = noSpeaker.replaceFirst(":", "").trim();
    	Log.d("BEFORE", quote);
    	Log.d("AFTER", noSpeaker);
    	return noSpeaker;
    }
    
    private String nameSpeakerPrefix = "<b>Name that speaker:</b><p>";
    
    void loadNewQuote() {
    	questionNumber++;
    	final WebView quoteview = (WebView)findViewById(R.id.quote);
    	SimpsonsQuote quote = quotestore.randomQuote();
		List<String> answers = new ArrayList<String>();
		
		boolean canBeSpeakerQuestion = (quote.speaker != null && quotestore.getNumSpeakerQuestions() > 10 && quotestore.getNumSpeakers() > 3);
				
		boolean isSpeakerQuestion = canBeSpeakerQuestion && rand.nextInt(3) == 1;
		Callable<String> generator;
		
		String question = quote.quote;
		
		if (isSpeakerQuestion) {
			currentAnswer = quote.speaker;
			generator = new Callable<String>() { public String call() throws Exception {
				return quotestore.randomSpeaker();
			}};
			question = nameSpeakerPrefix + removeSpeaker(question);
		}
		else {
			currentAnswer = quote.episode;
			generator = new Callable<String>() { public String call() throws Exception {
				return quotestore.randomEpisode();
			}};
		}

		answers.add(currentAnswer);
		populateListWithUniqueElements(answers, 3, generator);
		
		
		Collections.shuffle(answers);
    	quoteview.loadData(question, "text/html", "utf-8");
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
    	opt1.setText(answers.get(0));
    	opt2.setText(answers.get(1));
    	opt3.setText(answers.get(2));
    }
}