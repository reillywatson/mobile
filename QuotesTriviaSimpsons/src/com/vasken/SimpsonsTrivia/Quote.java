package com.vasken.SimpsonsTrivia;

public class Quote {
	public String season;
	public String episode;
	public String quote;
	// optional -- for quotes with only one speaker
	public String speaker;
	
	public Quote(String season, String episode, String quote, String speaker) {
		this.season = season;
		this.episode = episode;
		this.quote = quote;
		this.speaker = speaker;
	}
}
