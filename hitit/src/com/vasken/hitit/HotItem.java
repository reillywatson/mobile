package com.vasken.hitit;

import android.graphics.drawable.Drawable;

public class HotItem {
	private Drawable image;
	private String rateID;
	private String resultTotals;
	private double resultAverage;	
	
	public HotItem(Drawable img, String rate) {
		image = img;
		rateID = rate;
	}
	
	public Drawable getImage() {
		return image;
	}
	
	public String getRateId() {
		return rateID;
	}

	public void setResultTotals(String totals) {
		resultTotals = totals;
	}

	public String getResultTotals() {
		return resultTotals;
	}

	public double getResultAverage() {
		return resultAverage;
	}

	public void setResultAverage(double average) {
		resultAverage = average;
	}
}
