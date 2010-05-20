package com.vasken.hitit;

import android.graphics.Bitmap;

public class HotItem {
	private Bitmap image;
	private String imageURL;
	private String rateID;
	private String resultTotals;
	private double resultAverage;
	private Bitmap resultImage;	
	
	public HotItem(Bitmap img, String rate) {
		image = img;
		rateID = rate;
	}
	
	public HotItem() {
	}
	
	public HotItem(String imageURL, String rateID, String resultTotals, double resultAverage) {
		this.imageURL = imageURL;
		this.rateID = rateID;
		this.resultTotals = resultTotals;
		this.resultAverage = resultAverage;
	}

	public Bitmap getImage() {
		return image;
	}
	
	public void setImage(Bitmap i) {
		image = i;
	}
	
	public String getImageURL() {
		return imageURL;
	}
	
	public void setImageURL(String url) {
		imageURL = url;
	}
	
	public String getRateId() {
		return rateID;
	}
	
	public void setRateId(String s) {
		rateID = s;
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

	public void setResultImage(Bitmap img) {
		resultImage = img;
	}
	
	public Bitmap getResultImage() {
		return resultImage;
	}
}
