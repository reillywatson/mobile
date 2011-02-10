package com.vasken.movie.model;

public class Actor {

	private String award;
	private int year;
	private String film;
	private String name;
	private String role;
	private boolean isWinner;

	public Actor(String award, int year, String film, String name, String role, boolean isWinner) {
		this.award = award;
		this.year = year;
		this.film = film;
		this.name = name;
		this.role = role;
		this.isWinner = isWinner;
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

	public String getFilm() {
		return film;
	}

	public void setFilm(String film) {
		this.film = film;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isWinner() {
		return isWinner;
	}

	@Override
	public String toString() {
		return "Actor [award=" + award + ", year=" + year + ", film=" + film
				+ ", name=" + name + ", role=" + role + ", isWinner="
				+ isWinner + "]";
	}
}
