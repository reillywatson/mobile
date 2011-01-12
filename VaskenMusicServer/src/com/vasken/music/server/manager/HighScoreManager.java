package com.vasken.music.server.manager;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import sun.nio.cs.HistoricallyNamedCharset;

import com.google.appengine.repackaged.com.google.common.base.Pair;
import com.vasken.music.server.model.HighScoreEntry;

public class HighScoreManager {

	public static final String INVALID_DATA_EXCEPTION = "INVALID_DATA_EXCEPTION";
	public static final String DECRYPTION_FAILED_EXCEPTION = "DECRYPTION_FAILED_EXCEPTION";
	public static final String MISSING_DATA_EXCEPTION = "MISSING_DATA_EXCEPTION";
	
	private static final String NAME = "name=";
	private static final String SCORE = "score=";
	private static final String GENRE = "genre=";
	private static final String VERSION = "version=";
	private static final String ITEM_DIVIDER = "-=-=-=-=-=";

	private static final String secretKey = "T4iSisV4sk3ns4w3s0m3S3cr3Tk39";
	
	private static HighScoreManager INSTANCE = null;

	public static HighScoreManager sharedInstace() {
		if (INSTANCE == null) {
			INSTANCE = new HighScoreManager();
		}
		return INSTANCE;
	}
	
	public HighScoreEntry generateHighScoreEntry(String encryptedData) throws Exception {
		if (encryptedData == null || encryptedData.isEmpty()) {
			throw new Exception(MISSING_DATA_EXCEPTION);
		}
		
		String unencryptedData = decrypt(encryptedData);
		
		if (!unencryptedData.contains(NAME) 
			|| !unencryptedData.contains(SCORE) 
			|| !unencryptedData.contains(GENRE) 
			|| !unencryptedData.contains(VERSION)) {
			throw new Exception(INVALID_DATA_EXCEPTION);
		}
		
		String[] dataArray = unencryptedData.split(ITEM_DIVIDER);
		String name = dataArray[0].trim().replace(NAME, "");
		int score = Integer.parseInt(dataArray[1].trim().replace(SCORE, ""));
		String genre = dataArray[2].trim().replace(GENRE, "");
		String version = dataArray[3].trim().replace(VERSION, "");
		Date today = new Date();

		return new HighScoreEntry(name, score, genre, version, today);
	}
	
	public void checkIfHighScore(HighScoreEntry entry) {
		Pair<List<HighScoreEntry>, List<HighScoreEntry>> highScores = getHighScores(entry.getGenre());
		
		List<HighScoreEntry> today = highScores.first;
	    if (today.size() < 5 || Integer.valueOf(today.get(today.size()-1).getScore()) < Integer.valueOf(entry.getScore())) {
	    	entry.setHighScoreEver(true);
	    }

		List<HighScoreEntry> allTime = highScores.second;
	    if (allTime.size() < 5 || Integer.valueOf(allTime.get(allTime.size()-1).getScore()) < Integer.valueOf(entry.getScore())) {
	    	entry.setHighScoreToday(true);
	    }
	}
	
	@SuppressWarnings("unchecked")
	public Pair<List<HighScoreEntry>, List<HighScoreEntry>> getHighScores(String genre) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
	    
		Pair<List<HighScoreEntry>, List<HighScoreEntry>> result;
		
		Query allTimeQuery = pm.newQuery(HighScoreEntry.class);
		allTimeQuery.setOrdering("score desc");
		allTimeQuery.setRange(0, 5);
		List<HighScoreEntry> allTimeHighScores;
		if (genre != null) {
			allTimeQuery.setFilter("genre == genreParam");
			allTimeQuery.declareParameters("String genreParam");
			allTimeHighScores = (List<HighScoreEntry>) allTimeQuery.execute(genre);
		} else {
			allTimeHighScores = (List<HighScoreEntry>) allTimeQuery.execute();;
		}
	    allTimeQuery.closeAll();

	    List<HighScoreEntry> todayHighScores;
		Query todayQuery = pm.newQuery(HighScoreEntry.class);
		todayQuery.setOrdering("score desc");
		todayQuery.setFilter("date == dateParam");
		todayQuery.declareImports("import java.util.Date");
		todayQuery.setRange(0, 5);
		if (genre != null) {
			todayQuery.setFilter("genre == genreParam");
			todayQuery.declareParameters("String genreParam, Date dateParam");
		    todayHighScores = (List<HighScoreEntry>) todayQuery.execute(genre, new Date());
		} else {
			todayQuery.declareParameters("Date dateParam");
			todayHighScores = (List<HighScoreEntry>) todayQuery.execute(new Date());
		}
	    todayQuery.closeAll();
	    
	    result = new Pair<List<HighScoreEntry>, List<HighScoreEntry>>(todayHighScores, allTimeHighScores);
	    return result;
	}

	public void save(HighScoreEntry entry) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.makePersistent(entry);
        } finally {
            pm.close();
        }
	}

	private String decrypt(String encrypted) throws Exception{
		if (encrypted.isEmpty()) {
			throw new Exception(DECRYPTION_FAILED_EXCEPTION);
		}
		return encrypted;
	}

	@SuppressWarnings("unchecked")
	public long removeOldHighscores() {
		long result = 0;
		PersistenceManager pm = PMF.get().getPersistenceManager();

		// This can probably be optimised. But I don't really know how these queries work
		for (int genre : HighScoreEntry.Genres()) {
			Query allTimeQuery = pm.newQuery(HighScoreEntry.class);
			allTimeQuery.setOrdering("score desc");
			allTimeQuery.setFilter("genre == genreParam");
			allTimeQuery.declareParameters("String genreParam");
			allTimeQuery.setRange(0, 5);
			
			List<HighScoreEntry> allTimeHighScores = (List<HighScoreEntry>) allTimeQuery.execute(String.valueOf(genre));
			allTimeQuery.closeAll();
		    
		    if (allTimeHighScores.size() == 5) {
		    	HighScoreEntry lowest = allTimeHighScores.get(allTimeHighScores.size()-1);
		    	int goodScore = lowest.getScore();
		    	Date initialDate = lowest.getDate();
		    	
		    	System.out.println("public long removeOldHighscores() " + allTimeHighScores.size() 
		    			+ " genre " + genre 
		    			+ " lower then " + goodScore
		    			+ " newer then " + initialDate);
		    	
			    Query lowerScoresQuery = pm.newQuery(HighScoreEntry.class);
			    lowerScoresQuery.setFilter("score < goodScore");
			    lowerScoresQuery.declareParameters("int goodScore");
			    result += lowerScoresQuery.deletePersistentAll(goodScore);

			    Query newerThenQuery = pm.newQuery(HighScoreEntry.class);
			    newerThenQuery.setFilter("score == goodScore && date > initialDate");
			    newerThenQuery.declareImports("import java.util.Date");
			    newerThenQuery.declareParameters("int goodScore, Date initialDate");
			    result += newerThenQuery.deletePersistentAll(goodScore, initialDate);
		    }
		}
		return result;
	}
}
