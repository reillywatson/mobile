package com.vasken.music.server.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class HighScoreEntry {

	public static List<Integer> Genres() {
		List<Integer> genres = new ArrayList<Integer>();
		genres.add(new Integer(1));
		genres.add(new Integer(2));
		genres.add(new Integer(3));
		genres.add(new Integer(4));
		genres.add(new Integer(5));
		genres.add(new Integer(6));
		genres.add(new Integer(7));
		genres.add(new Integer(8));
		genres.add(new Integer(9));
		genres.add(new Integer(10));
		genres.add(new Integer(11));
		genres.add(new Integer(12));
		genres.add(new Integer(13));
		genres.add(new Integer(14));
		genres.add(new Integer(15));
		genres.add(new Integer(16));
		genres.add(new Integer(17));
		genres.add(new Integer(18));
		genres.add(new Integer(19));
		genres.add(new Integer(20));
		genres.add(new Integer(21));
		
		return genres;
	}
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
	private String name;

    @Persistent
	private int score;

    @Persistent
    private String genre;

    @Persistent
    private String appVersion;
    
    @Persistent
    private Date date;
    
    @NotPersistent 
    private boolean isHighScoreToday;

    @NotPersistent 
    private boolean isHighScoreEver;
	
	public HighScoreEntry(String name, int score, String genre, String version, Date date) {
		this.name = name;
		this.score = score;
		this.genre = genre;
		this.appVersion = version;
		this.setDate(date);
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public boolean isHighScoreToday() {
		return isHighScoreToday;
	}

	public boolean isHighScoreEver() {
		return isHighScoreEver;
	}
    
    public void setHighScoreToday(boolean isHighScoreToday) {
		this.isHighScoreToday = isHighScoreToday;
	}

	public void setHighScoreEver(boolean isHighScoreEver) {
		this.isHighScoreEver = isHighScoreEver;
	}
}
