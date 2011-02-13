package com.vasken.movie.model;

public class NominatedItem {

	private String award;
	private int year;
	private String name;
	private boolean isWinner;

	public NominatedItem(String award, int year, String name, boolean isWinner) {
		this.award = award;
		this.year = year;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isWinner() {
		return isWinner;
	}

	@Override
	public String toString() {
		return "NominatedItem [award=" + award + ", year=" + year + ", name=" + name + ", isWinner=" + isWinner + "]";
	}
}
