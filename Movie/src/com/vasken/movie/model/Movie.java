package com.vasken.movie.model;

public class Movie {

	private String award;
	private int year;
	private String name;
	private boolean isWinner;
	private String actors;

	public Movie(String award, int yr, String name, boolean isWinner, String actors) {
		this.award = award;
		this.year = yr;
		this.name = name;
		this.isWinner = isWinner;
		this.actors = actors;
	}

	public String getAward() {
		return award;
	}

	public void setAward(String award) {
		this.award = award;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isWinner() {
		return isWinner;
	}

	public void setWinner(boolean isWinner) {
		this.isWinner = isWinner;
	}

	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	@Override
	public String toString() {
		return "Movie [award=" + award + ", year=" + year + ", name=" + name
				+ ", isWinner=" + isWinner + ", actors=" + actors + "]";
	}
}
