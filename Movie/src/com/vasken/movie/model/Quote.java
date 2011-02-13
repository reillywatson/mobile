package com.vasken.movie.model;

public class Quote {
	private String film;
	private String text;
	
	public Quote(String film, String text) {
		this.film = film;
		this.text = text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	public String getText() {
		return text;
	}
	public void setFilm(String film) {
		this.film = film;
	}
	public String getFilm() {
		return film;
	}

}
