package com.vasken.songster.rock.manager;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.vasken.songster.rock.Main;
import com.vasken.songster.rock.model.Achievement;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class State {
	private static final String PREFERENCES_NAME = "PREFERENCES_NAME";
	
	private static final String PREF_DATE= "DATE_LAST_RUN";
	private static final String PREF_CONSECUTIVE_DAYS = "CONSECUTIVE_DAYS";
	
	private int streak;
	private int lastStreak;
	private Map<String, Boolean> achievements;
	private Calendar lastRun;
	private int consecutiveDaysRun;
	
	public static State loadSavedInstance(Activity context) {
		State result = new State();
		
		SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		
		Map<String, Boolean> achievements = new HashMap<String, Boolean>();
		setAchievement(achievements, preferences, Achievement.ACH1_KEY, Achievement.ACH1_VALUE);
		setAchievement(achievements, preferences, Achievement.ACH2_KEY, Achievement.ACH2_VALUE);
		setAchievement(achievements, preferences, Achievement.ACH3_KEY, Achievement.ACH3_VALUE);
		setAchievement(achievements, preferences, Achievement.ACH4_KEY, Achievement.ACH4_VALUE);
		setAchievement(achievements, preferences, Achievement.ACH5_KEY, Achievement.ACH5_VALUE);
		setAchievement(achievements, preferences, Achievement.ACH6_KEY, Achievement.ACH6_VALUE);
		setAchievement(achievements, preferences, Achievement.ACH7_KEY, Achievement.ACH7_VALUE);
		setAchievement(achievements, preferences, Achievement.ACH8_KEY, Achievement.ACH8_VALUE);
		setAchievement(achievements, preferences, Achievement.ACH9_KEY, Achievement.ACH9_VALUE);
		setAchievement(achievements, preferences, Achievement.ACH10_KEY, Achievement.ACH10_VALUE);
		setAchievement(achievements, preferences, Achievement.ACH11_KEY, Achievement.ACH11_VALUE);
		setAchievement(achievements, preferences, Achievement.ACH12_KEY, Achievement.ACH12_VALUE);
		setAchievement(achievements, preferences, Achievement.ACH13_KEY, Achievement.ACH13_VALUE);
		setAchievement(achievements, preferences, Achievement.ACH14_KEY, Achievement.ACH14_VALUE);
		setAchievement(achievements, preferences, Achievement.ACH15_KEY, Achievement.ACH15_VALUE);
		result.setAchievements(achievements);
		
		long lastRunDateInMillis = preferences.getLong(PREF_DATE, 0);
		Calendar lastRun = Calendar.getInstance();
		lastRun.setTimeInMillis(lastRunDateInMillis);
		result.setLastRun(lastRun);
		
		int consecutiveDaysRunning = preferences.getInt(PREF_CONSECUTIVE_DAYS, 0);
		result.setConsecutiveDaysRunning(consecutiveDaysRunning);
		
		return result;
	}

	public int getLastStreak() {
		return lastStreak;
	}

	public void setLastStreak(int streak) {
		this.lastStreak = streak;
	}

	public int getStreak() {
		return streak;
	}

	public void setStreak(int streak) {
		this.streak = streak;
	}

	private static void setAchievement(Map<String, Boolean> ach, SharedPreferences prefs, String key, String value) {
		String ach1Value = prefs.getString(key, "");
		if (ach1Value.equals(value)) {
			ach.put(key, Boolean.TRUE);
		}
	}

	private void setAchievements(Map<String, Boolean> achievements) {
		this.achievements = achievements;
	}
	
	public Map<String, Boolean> getAchievements() {
		return achievements;
	}
	
	public void addAchievement(String key) {
		achievements.put(key, Boolean.TRUE);
	}
	
	public void save(Main context) {
		if (achievements == null) {
			// Paranoid guard
			Log.w(this.getClass().toString() + "save()", "this.achievements is null");
			return;
		}
		
		SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		
		saveAchievement(editor, Achievement.ACH1_KEY, Achievement.ACH1_VALUE);
		saveAchievement(editor, Achievement.ACH2_KEY, Achievement.ACH2_VALUE);
		saveAchievement(editor, Achievement.ACH3_KEY, Achievement.ACH3_VALUE);
		saveAchievement(editor, Achievement.ACH4_KEY, Achievement.ACH4_VALUE);
		saveAchievement(editor, Achievement.ACH5_KEY, Achievement.ACH5_VALUE);
		saveAchievement(editor, Achievement.ACH6_KEY, Achievement.ACH6_VALUE);
		saveAchievement(editor, Achievement.ACH7_KEY, Achievement.ACH7_VALUE);
		saveAchievement(editor, Achievement.ACH8_KEY, Achievement.ACH8_VALUE);
		saveAchievement(editor, Achievement.ACH9_KEY, Achievement.ACH9_VALUE);
		saveAchievement(editor, Achievement.ACH10_KEY, Achievement.ACH10_VALUE);
		saveAchievement(editor, Achievement.ACH11_KEY, Achievement.ACH11_VALUE);
		saveAchievement(editor, Achievement.ACH12_KEY, Achievement.ACH12_VALUE);
		saveAchievement(editor, Achievement.ACH13_KEY, Achievement.ACH13_VALUE);
		saveAchievement(editor, Achievement.ACH14_KEY, Achievement.ACH14_VALUE);
		saveAchievement(editor, Achievement.ACH15_KEY, Achievement.ACH15_VALUE);
		
		editor.putInt(PREF_CONSECUTIVE_DAYS, getConsecutiveDaysRunning());
		editor.putLong(PREF_DATE, getLastRun().getTimeInMillis());
		
		editor.commit();
	}

	public void setLastRun(Calendar date) {
		lastRun = date;
	}

	public Calendar getLastRun() {
		return lastRun;
	}

	public int getConsecutiveDaysRunning() {
		return consecutiveDaysRun;
	}

	public void setConsecutiveDaysRunning(int days) {
		consecutiveDaysRun = days;
	}

	private void saveAchievement(Editor editor, String key, String value) {
		if (achievements.containsKey(key) && achievements.get(key)) {
			editor.putString(key, value);
		}
	}
}
