package com.vasken.songstar.rock.model;

import com.vasken.songstar.rock.R;

import android.util.Log;

public class Achievement {

	// Pair is a API 5 construct. I'm still using API 4
	public static String ACH1_KEY= "ACH1";
	public static String ACH1_VALUE= "Pair";
	public  static String ACH2_KEY= "ACH2";
	public static String ACH2_VALUE= "quint";
	public  static String ACH3_KEY= "ACH3";
	public static String ACH3_VALUE= "Deca";
	public  static String ACH4_KEY= "ACH4";
	public static String ACH4_VALUE= "fifteen";
	public  static String ACH5_KEY= "ACH5";
	public static String ACH5_VALUE= "XX";
	public  static String ACH6_KEY= "ACH6";
	public static String ACH6_VALUE= "treizeci";
	public  static String ACH7_KEY= "ACH7";
	public static String ACH7_VALUE= "cinquante";
	public  static String ACH8_KEY= "ACH8";
	public static String ACH8_VALUE= "mybest";
	public  static String ACH9_KEY= "ACH9";
	public static String ACH9_VALUE= "centinaio";
	public  static String ACH10_KEY= "ACH10";
	public static String ACH10_VALUE= "crazy";
	public  static String ACH11_KEY= "ACH11";
	public static String ACH11_VALUE= "cheater";
	public  static String ACH12_KEY= "ACH12";
	public static String ACH12_VALUE= "curious";
	public  static String ACH13_KEY= "ACH13";
	public static String ACH13_VALUE= "interested";
	public  static String ACH14_KEY= "ACH14";
	public static String ACH14_VALUE= "fan";
	public  static String ACH15_KEY= "ACH15";
	public static String ACH15_VALUE= "dedicated";

	private boolean isEmpty;
	private String key;
	
	public Achievement(String key) {
		this.isEmpty = false;
		this.key = key;
	}

	public Achievement() {
		this.isEmpty = true;
	}

	public boolean isEmpty() {
		return this.isEmpty;
	}

	public int getIndex() {
		int index = -1;
		
		if (this.key.equals(ACH1_KEY)) {
			index = 0;
		}else if (this.key.equals(ACH2_KEY)) {
			index = 1;
		}else if (this.key.equals(ACH12_KEY)) {
			index = 2;
		}else if (this.key.equals(ACH3_KEY)) {
			index = 3;
		}else if (this.key.equals(ACH4_KEY)) {
			index = 4;
		}else if (this.key.equals(ACH5_KEY)) {
			index = 5;
		}else if (this.key.equals(ACH13_KEY)) {
			index = 6;
		}else if (this.key.equals(ACH6_KEY)) {
			index = 7;
		}else if (this.key.equals(ACH7_KEY)) {
			index = 8;
		}else if (this.key.equals(ACH8_KEY)) {
			index = 9;
		}else if (this.key.equals(ACH9_KEY)) {
			index = 10;
		}else if (this.key.equals(ACH14_KEY)) {
			index = 11;
		}else if (this.key.equals(ACH10_KEY)) {
			index = 12;
		}else if (this.key.equals(ACH15_KEY)) {
			index = 13;
		}else if (this.key.equals(ACH11_KEY)) {
			index = 14;
		}else {
			Log.w(Achievement.class.toString(), "No index for achievement" + this.key);	
		}
		
		return index;
	}
	
	public int getIcon() {
		int icon = R.drawable.ach_locked;
		
		if (this.key.equals(ACH1_KEY)) {
			icon = R.drawable.ach1;
		}else if (this.key.equals(ACH2_KEY)) {
			icon = R.drawable.ach2;
		}else if (this.key.equals(ACH3_KEY)) {
			icon = R.drawable.ach3;
		}else if (this.key.equals(ACH4_KEY)) {
			icon = R.drawable.ach4;
		}else if (this.key.equals(ACH5_KEY)) {
			icon = R.drawable.ach5;
		}else if (this.key.equals(ACH6_KEY)) {
			icon = R.drawable.ach6;
		}else if (this.key.equals(ACH7_KEY)) {
			icon = R.drawable.ach7;
		}else if (this.key.equals(ACH8_KEY)) {
			icon = R.drawable.ach8;
		}else if (this.key.equals(ACH9_KEY)) {
			icon = R.drawable.ach9;
		}else if (this.key.equals(ACH10_KEY)) {
			icon = R.drawable.ach10;
		}else if (this.key.equals(ACH11_KEY)) {
			icon = R.drawable.ach11;
		}else if (this.key.equals(ACH12_KEY)) {
			icon = R.drawable.ach12;
		}else if (this.key.equals(ACH13_KEY)) {
			icon = R.drawable.ach13;
		}else if (this.key.equals(ACH14_KEY)) {
			icon = R.drawable.ach14;
		}else if (this.key.equals(ACH15_KEY)) {
			icon = R.drawable.ach15;
		} else {
			Log.w(Achievement.class.toString(), "No icon for achievement" + this.key);
		}
		
		return icon;
	}

	public int getSmallIcon() {
		int icon = R.drawable.ach_locked;
		
		if (this.key.equals(ACH1_KEY)) {
			icon = R.drawable.ach1_sm;
		}else if (this.key.equals(ACH2_KEY)) {
			icon = R.drawable.ach2_sm;
		}else if (this.key.equals(ACH3_KEY)) {
			icon = R.drawable.ach3_sm;
		}else if (this.key.equals(ACH4_KEY)) {
			icon = R.drawable.ach4_sm;
		}else if (this.key.equals(ACH5_KEY)) {
			icon = R.drawable.ach5_sm;
		}else if (this.key.equals(ACH6_KEY)) {
			icon = R.drawable.ach6_sm;
		}else if (this.key.equals(ACH7_KEY)) {
			icon = R.drawable.ach7_sm;
		}else if (this.key.equals(ACH8_KEY)) {
			icon = R.drawable.ach8_sm;
		}else if (this.key.equals(ACH9_KEY)) {
			icon = R.drawable.ach9_sm;
		}else if (this.key.equals(ACH10_KEY)) {
			icon = R.drawable.ach10_sm;
		}else if (this.key.equals(ACH11_KEY)) {
			icon = R.drawable.ach11_sm;
		}else if (this.key.equals(ACH12_KEY)) {
			icon = R.drawable.ach12_sm;
		}else if (this.key.equals(ACH13_KEY)) {
			icon = R.drawable.ach13_sm;
		}else if (this.key.equals(ACH14_KEY)) {
			icon = R.drawable.ach14_sm;
		}else if (this.key.equals(ACH15_KEY)) {
			icon = R.drawable.ach15_sm;
		} else {
			Log.w(Achievement.class.toString(), "No icon for achievement" + this.key);
		}
		
		return icon;
	}

}
