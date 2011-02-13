package com.vasken.movie.model;

public class NominatedPerson extends NominatedItem {
	private String film;

	public NominatedPerson(String award, int year, String film, String name, boolean isWinner) {
		super(award, year, name, isWinner);
		this.film = film;
	}

	public String getFilm() {
		return film;
	}

	public void setFilm(String film) {
		this.film = film;
	}

	@Override
	public String toString() {
		return "NominatedPerson [film=" + film + ", getAward()=" + getAward()
				+ ", getYear()=" + getYear() + ", getFilm()=" + getFilm()
				+ ", isWinner()=" + isWinner() + "]";
	}	
}
