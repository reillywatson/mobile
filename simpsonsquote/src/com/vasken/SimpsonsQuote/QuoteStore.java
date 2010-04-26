package com.vasken.SimpsonsQuote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.util.Log;

public class QuoteStore {
	private List<SimpsonsQuote> quotes = new ArrayList<SimpsonsQuote>();
	private Random rand = new Random();
	
	public QuoteStore(Context context) throws IOException {
		InputStream in = context.getResources().openRawResource(R.raw.quotes);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String season = reader.readLine();
		while (season != null) {
			String epTitle = reader.readLine();
			String quote = reader.readLine();
			if (season != null && epTitle != null && quote != null) {
				quotes.add(new SimpsonsQuote(season, epTitle, quote));
			}
			season = reader.readLine();
		}
		Log.d(getClass().getName(), "Found " + Integer.toString(quotes.size()) + " quotes");
	}
	
	public SimpsonsQuote randomQuote() {
		return quotes.get(rand.nextInt(quotes.size()));
	}
}
