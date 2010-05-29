package com.vasken.SimpsonsTrivia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.util.Log;

public class QuoteStore {
	private List<SimpsonsQuote> quotes = new ArrayList<SimpsonsQuote>();
	private HashMap<String, List<SimpsonsQuote>> quotesBySeason = new HashMap<String, List<SimpsonsQuote>>();
	private Random rand = new Random();
	private Pattern speakerPattern =  Pattern.compile("<b>(.*?)</b>", Pattern.DOTALL);
	private int numSpeakerQuestions = 0;
	private int numEpisodeQuestions = 0;
	private Set<String> speakers = new TreeSet<String>();
	
	public QuoteStore(Context context, int storeid) throws IOException {
		InputStream in = context.getResources().openRawResource(storeid);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String season = reader.readLine();
		while (season != null) {
			String epTitle = reader.readLine();
			String quote = reader.readLine();
			if (season != null && epTitle != null && quote != null) {
				String speaker = speakerOfQuote(quote);
				if (speaker != null) {
					numSpeakerQuestions++;
					speakers.add(speaker);
				}
				numEpisodeQuestions++;
				SimpsonsQuote sq = new SimpsonsQuote(season, epTitle, quote, speaker);
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
	
	public int getNumSpeakerQuestions() {
		return numSpeakerQuestions;
	}
	
	public int getNumEpisodeQuestons() {
		return numEpisodeQuestions;
	}
	
	public int getNumSpeakers() {
		return speakers.size();
	}
	
	public boolean canDoSpeakerQuestions() {
		 return getNumSpeakerQuestions() > 10 && getNumSpeakers() > 3;
	}
	
	private String speakerOfQuote(String quote) {
		Matcher m = speakerPattern.matcher(quote);
		if (m.find()) {
			String speaker = m.group(1).trim();
			if (!m.find()) {
				if (speaker.endsWith(":")) {
					speaker = speaker.substring(0, speaker.length() - 1).trim();
				}
				if (speaker.length() > 0)
					return speaker;
			}
		}
		return null;
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
	
	public String randomSpeaker() {
		String speaker = null;
		while (speaker == null) {
			speaker = randomQuote().speaker;
		}
		return speaker;
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
	
	public class SimpsonsQuote {
		public String season;
		public String episode;
		public String quote;
		// optional -- for quotes with only one speaker
		public String speaker;
		
		public SimpsonsQuote(String season, String episode, String quote, String speaker) {
			this.season = season;
			this.episode = episode;
			this.quote = quote;
			this.speaker = speaker;
		}
	}
}
