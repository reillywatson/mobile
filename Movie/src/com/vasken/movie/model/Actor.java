package com.vasken.movie.model;

public class Actor extends NominatedPerson {
	private String role;

	public Actor(String award, int year, String film, String name, String role, boolean isWinner) {
		super(award, year, film, name, isWinner);
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
