package com.vasken.SimpsonsQuote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import android.content.Context;
import android.util.Log;

public class QuoteStore {
	private List<SimpsonsQuote> quotes = new ArrayList<SimpsonsQuote>();
	private HashMap<String, List<SimpsonsQuote>> quotesBySeason = new HashMap<String, List<SimpsonsQuote>>();
	private Random rand = new Random();
	
	public QuoteStore(Context context, int storeid) throws IOException {
		InputStream in = context.getResources().openRawResource(storeid);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String season = reader.readLine();
		while (season != null) {
			String epTitle = reader.readLine();
			String quote = reader.readLine();
			if (season != null && epTitle != null && quote != null) {
				SimpsonsQuote sq = new SimpsonsQuote(season, epTitle, quote);
				quotes.add(sq);
				List<SimpsonsQuote> seasonList = quotesBySeason.get(season);
				if (seasonList == null) {
					seasonList = new ArrayList<SimpsonsQuote>();
					quotesBySeason.put(season, seasonList);
				}
				quotesBySeason.get(season).add(sq);
			}
			season = reader.readLine();
		}
		Log.d(getClass().getName(), "Found " + Integer.toString(quotes.size()) + " quotes");
	}
	
	public int numSeasons() {
		return quotesBySeason.size();
	}
	
	public SimpsonsQuote randomQuoteFromList(List<SimpsonsQuote> list) {
		return list.get(rand.nextInt(list.size()));
	}
	
	public SimpsonsQuote randomQuote() {
		return randomQuoteFromList(quotes);
	}
	
	public SimpsonsQuote quoteBySeason(int season) {
		List<String> seasons = new ArrayList<String>(quotesBySeason.keySet());
		Collections.sort(seasons);
		return randomQuoteFromList(quotesBySeason.get(seasons.get(season - 1)));
	}
	
	public String randomEpisode() {
		return randomQuote().episode;
	}
	
	// performance on this isn't so great...
	// returns quotes from seasons 2-9, "the good Simpsons"
	public SimpsonsQuote randomQuoteThatIsntTerrible() {
		SimpsonsQuote quote = randomQuote();
		while (quote.season.startsWith("Season 1")) {
			quote = randomQuote();
		}
		return quote;
	}
}
