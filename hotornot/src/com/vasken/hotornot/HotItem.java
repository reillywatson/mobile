package com.vasken.hotornot;

import android.graphics.drawable.Drawable;

public class HotItem {
	private Drawable image;
	private String rateID;
	
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
}
