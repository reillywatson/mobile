package com.vasken.hitit;

import android.graphics.Bitmap;

public class HotItem {
	private Bitmap image;
	private String rateID;
	
	public HotItem(Bitmap img, String rate) {
		image = img;
		rateID = rate;
	}
	
	public Bitmap getImage() {
		return image;
	}
	
	public String getRateId() {
		return rateID;
	}
}
