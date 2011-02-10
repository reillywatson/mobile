package com.vasken.movie.manager;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.vasken.movie.model.Actor;
import com.vasken.movie.model.Movie;
import com.vasken.movie.model.Question;

public class QuestionManager {
	private static QuestionManager INSTANCE;
	
	public Question getNextQuestion(DatabaseManager dbManager) {
		dbManager.openDataBase();
		List<Actor> actors = dbManager.getBestActorEntries(2008);
		for (int i=0; i<actors.size(); i++) {
			Log.d("----------", actors.get(i).toString());
		}
		List<Actor> actresses = dbManager.getBestActressEntries(2008);
		for (int i=0; i<actresses.size(); i++) {
			Log.d("----------", actresses.get(i).toString());
		}
		List<Movie> movies = dbManager.getBestPictureEntries(2008);
		for (int i=0; i<movies.size(); i++) {
			Log.d("----------", movies.get(i).toString());
		}
		dbManager.close();
		
		return new Question(1, "", new ArrayList<String>(), "");
	}

	public static QuestionManager sharedInstance() {
		if (INSTANCE == null) { 
			INSTANCE = new QuestionManager();
		}
		return INSTANCE;
	}

}
