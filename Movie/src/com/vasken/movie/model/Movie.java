package com.vasken.movie.model;

public class Movie extends NominatedItem {
	private String actors;

	public Movie(String award, int yr, String name, boolean isWinner, String actors) {
		super(award, yr, name, isWinner);
		this.actors = actors;
	}
	
	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	@Override
	public String toString() {
		return "Movie [actors=" + actors + ", getAward()=" + getAward()
				+ ", getYear()=" + getYear() + ", getName()=" + getName()
				+ ", isWinner()=" + isWinner() + "]";
	}
}
