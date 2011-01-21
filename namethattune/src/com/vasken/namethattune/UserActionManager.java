package com.vasken.namethattune;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class UserActionManager {

	private static final String ENCRYPTION_SEED = "T4iSisV4sk3ns4w3s0m3S3cr3Tk39";
	private static final String PARAM_NAME = "name";
	private static final String PARAM_SCORE = "score";
	private static final String PARAM_GENRE = "genre";
	private static final String PARAM_VERSION = "version";
	private static final String PARAM_HASH = "id";

	public static Achievement correctAnswer(State theState) {
		theState.setStreak(theState.getStreak() + 1);
		
		Achievement result = new Achievement();
		if (theState.getStreak() == 2) {
			if (!theState.getAchievements().containsKey(Achievement.ACH1_KEY)) {
				theState.addAchievement(Achievement.ACH1_KEY);
				result = new Achievement(Achievement.ACH1_KEY);
			}
		} else if (theState.getStreak() == 5){
			if (!theState.getAchievements().containsKey(Achievement.ACH2_KEY)) {
				theState.addAchievement(Achievement.ACH2_KEY);
				result = new Achievement(Achievement.ACH2_KEY);
			}
		} else if (theState.getStreak() == 10){
			if (!theState.getAchievements().containsKey(Achievement.ACH3_KEY)) {
				theState.addAchievement(Achievement.ACH3_KEY);
				result = new Achievement(Achievement.ACH3_KEY);
			}
		} else if (theState.getStreak() == 15){
			if (!theState.getAchievements().containsKey(Achievement.ACH4_KEY)) {
				theState.addAchievement(Achievement.ACH4_KEY);
				result = new Achievement(Achievement.ACH4_KEY);
			}
		} else if (theState.getStreak() == 20){
			if (!theState.getAchievements().containsKey(Achievement.ACH5_KEY)) {
				theState.addAchievement(Achievement.ACH5_KEY);
				result = new Achievement(Achievement.ACH5_KEY);
			}
		} else if (theState.getStreak() == 30){
			if (!theState.getAchievements().containsKey(Achievement.ACH6_KEY)) {
				theState.addAchievement(Achievement.ACH6_KEY);
				result = new Achievement(Achievement.ACH6_KEY);
			}
		} else if (theState.getStreak() == 50){
			if (!theState.getAchievements().containsKey(Achievement.ACH7_KEY)) {
				theState.addAchievement(Achievement.ACH7_KEY);
				result = new Achievement(Achievement.ACH7_KEY);
			}
		} else if (theState.getStreak() == 75){
			if (!theState.getAchievements().containsKey(Achievement.ACH8_KEY)) {
				theState.addAchievement(Achievement.ACH8_KEY);
				result = new Achievement(Achievement.ACH8_KEY);
			}
		} else if (theState.getStreak() == 100){
			if (!theState.getAchievements().containsKey(Achievement.ACH9_KEY)) {
				theState.addAchievement(Achievement.ACH9_KEY);
				result = new Achievement(Achievement.ACH9_KEY);
			}
		} else if (theState.getStreak() == 200){
			if (!theState.getAchievements().containsKey(Achievement.ACH10_KEY)) {
				theState.addAchievement(Achievement.ACH10_KEY);
				result = new Achievement(Achievement.ACH10_KEY);
			}
		} else if (theState.getStreak() == 300){
			if (!theState.getAchievements().containsKey(Achievement.ACH10_KEY)) {
				theState.addAchievement(Achievement.ACH11_KEY);
				result = new Achievement(Achievement.ACH11_KEY);
			}
		}
		
		return result;
	}

	public static void wrongAnswer(State theState) {
		theState.setLastStreak(theState.getStreak());
		theState.setStreak(0);
	}

	public static Achievement openedApplication(State theState) {
		Calendar rightNow = Calendar.getInstance();
		
		Achievement result = new Achievement();
		Calendar lastRan = theState.getLastRun();
		if (theState.getConsecutiveDaysRunning() == 7) {
			// 7 Consecutive days
			if (!theState.getAchievements().containsKey(Achievement.ACH14_KEY)) {
				theState.addAchievement(Achievement.ACH14_KEY);
				result = new Achievement(Achievement.ACH14_KEY);
			}
		} else if (wasYesterday(lastRan)) {
			theState.setConsecutiveDaysRunning(theState.getConsecutiveDaysRunning() + 1);
		
			// 2 Consecutive days
			if (!theState.getAchievements().containsKey(Achievement.ACH12_KEY)) {
				theState.addAchievement(Achievement.ACH12_KEY);
				result = new Achievement(Achievement.ACH12_KEY);
			}
		} else if (wasLastWeek(lastRan)) {
			// Consecutive weeks
			if (!theState.getAchievements().containsKey(Achievement.ACH13_KEY)) {
				theState.addAchievement(Achievement.ACH13_KEY);
				result = new Achievement(Achievement.ACH13_KEY);
			}
		} else if (wasLastMonth(lastRan)) {
			// Consecutive months
			if (!theState.getAchievements().containsKey(Achievement.ACH15_KEY)) {
				theState.addAchievement(Achievement.ACH15_KEY);
				result = new Achievement(Achievement.ACH15_KEY);
			}
		}

		theState.setLastRun(rightNow);
		return result;
	}

	private static boolean wasLastMonth(Calendar lastRan) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.add(Calendar.MONTH, -1);
			
		return (lastRan.get(Calendar.YEAR) == rightNow.get(Calendar.YEAR))  
			&& (lastRan.get(Calendar.MONTH) == rightNow.get(Calendar.MONTH));
	}

	private static boolean wasLastWeek(Calendar lastRan) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.add(Calendar.WEEK_OF_YEAR, -1);
		
		return (lastRan.get(Calendar.YEAR) == rightNow.get(Calendar.YEAR))  
			&& (lastRan.get(Calendar.WEEK_OF_YEAR) == rightNow.get(Calendar.WEEK_OF_YEAR));
	}

	private static boolean wasYesterday(Calendar lastRan) {
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DAY_OF_YEAR, -1);
		
		return yesterday.get(Calendar.YEAR) == lastRan.get(Calendar.YEAR) 
			&& yesterday.get(Calendar.DAY_OF_YEAR) == lastRan.get(Calendar.DAY_OF_YEAR);
	}

	public static Achievement addNextAchievement(State theState) {
		Achievement result = new Achievement();
		
		if (!theState.getAchievements().containsKey(Achievement.ACH1_KEY)) {
			result = new Achievement(Achievement.ACH1_KEY);
			theState.addAchievement(Achievement.ACH1_KEY);
		}else if (!theState.getAchievements().containsKey(Achievement.ACH2_KEY)) {
			result = new Achievement(Achievement.ACH2_KEY);
			theState.addAchievement(Achievement.ACH2_KEY);
		}else if (!theState.getAchievements().containsKey(Achievement.ACH3_KEY)) {
			result = new Achievement(Achievement.ACH3_KEY);
			theState.addAchievement(Achievement.ACH3_KEY);
		}else if (!theState.getAchievements().containsKey(Achievement.ACH4_KEY)) {
			result = new Achievement(Achievement.ACH4_KEY);
			theState.addAchievement(Achievement.ACH4_KEY);
		}else if (!theState.getAchievements().containsKey(Achievement.ACH5_KEY)) {
			result = new Achievement(Achievement.ACH5_KEY);
			theState.addAchievement(Achievement.ACH5_KEY);
		}else if (!theState.getAchievements().containsKey(Achievement.ACH6_KEY)) {
			result = new Achievement(Achievement.ACH6_KEY);
			theState.addAchievement(Achievement.ACH6_KEY);
		}else if (!theState.getAchievements().containsKey(Achievement.ACH7_KEY)) {
			result = new Achievement(Achievement.ACH7_KEY);
			theState.addAchievement(Achievement.ACH7_KEY);
		}else if (!theState.getAchievements().containsKey(Achievement.ACH8_KEY)) {
			result = new Achievement(Achievement.ACH8_KEY);
			theState.addAchievement(Achievement.ACH8_KEY);
		}else if (!theState.getAchievements().containsKey(Achievement.ACH9_KEY)) {
			result = new Achievement(Achievement.ACH9_KEY);
			theState.addAchievement(Achievement.ACH9_KEY);
		}else if (!theState.getAchievements().containsKey(Achievement.ACH10_KEY)) {
			result = new Achievement(Achievement.ACH10_KEY);
			theState.addAchievement(Achievement.ACH10_KEY);
		}else if (!theState.getAchievements().containsKey(Achievement.ACH11_KEY)) {
			result = new Achievement(Achievement.ACH11_KEY);
			theState.addAchievement(Achievement.ACH11_KEY);
		}else if (!theState.getAchievements().containsKey(Achievement.ACH12_KEY)) {
			result = new Achievement(Achievement.ACH12_KEY);
			theState.addAchievement(Achievement.ACH12_KEY);
		}else if (!theState.getAchievements().containsKey(Achievement.ACH13_KEY)) {
			result = new Achievement(Achievement.ACH13_KEY);
			theState.addAchievement(Achievement.ACH13_KEY);
		}else if (!theState.getAchievements().containsKey(Achievement.ACH14_KEY)) {
			result = new Achievement(Achievement.ACH14_KEY);
			theState.addAchievement(Achievement.ACH14_KEY);
		}else if (!theState.getAchievements().containsKey(Achievement.ACH15_KEY)) {
			result = new Achievement(Achievement.ACH15_KEY);
			theState.addAchievement(Achievement.ACH15_KEY);
		}
		
		return result;
	}

	public static List<NameValuePair> getHashedParameters(String name, State theState, Context context) throws Exception {
		String genre = context.getString(R.string.genre);
		String score = String.valueOf(theState.getLastStreak());
		String appVersion;
    	ComponentName comp = new ComponentName(context, Main.class);
    	PackageInfo pinfo;
		try {
			pinfo = context.getPackageManager().getPackageInfo(comp.getPackageName(), 0);
			appVersion = String.valueOf(pinfo.versionCode);
		} catch (NameNotFoundException e1) {
			appVersion = "0";
		}

		String unhashedValues = name + score + genre + appVersion;
		String hash = Crypto.createHash(ENCRYPTION_SEED, unhashedValues);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PARAM_NAME, name));
        params.add(new BasicNameValuePair(PARAM_SCORE, score));
        params.add(new BasicNameValuePair(PARAM_GENRE, genre));
        params.add(new BasicNameValuePair(PARAM_VERSION, appVersion));
        params.add(new BasicNameValuePair(PARAM_HASH, hash));
        
        Log.d("-------", params.toString());
        return params;
	}
}
