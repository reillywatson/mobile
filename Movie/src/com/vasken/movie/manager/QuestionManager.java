package com.vasken.movie.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.vasken.movie.R;
import com.vasken.movie.model.Actor;
import com.vasken.movie.model.Movie;
import com.vasken.movie.model.Question;

public class QuestionManager {
	private static QuestionManager INSTANCE;
	
	private static final int MIN_YEAR = 1935;
	private static final int MAX_YEAR = 2009;

	public static QuestionManager sharedInstance() {
		if (INSTANCE == null) { 
			INSTANCE = new QuestionManager();
		}
		return INSTANCE;
	}
	
	public Question getNextQuestion(Context context, DatabaseManager dbManager, int questionType) {
		Question result = null;
		
		int randomYear = getRandomYear();
		
		dbManager.openDataBase();
		if (Question.ACTOR == questionType) {
			List<Actor> actors = dbManager.getBestActorEntries(randomYear);
			result = makeActorQuestion(context, actors, questionType);
		} else if (Question.ACTRESS == questionType) {
			List<Actor> actresses = dbManager.getBestActressEntries(randomYear);
			result = makeActorQuestion(context, actresses, questionType);
		} else if (Question.MOVIE == questionType) {
			List<Movie> movies = dbManager.getBestPictureEntries(randomYear);
			result = makeMovieQuestion(context, movies, questionType);
		}
		dbManager.close();
		
		Log.d(getClass().toString(), result.toString());
		return result;
	}

	private int getRandomYear() {
		int year = (int)Math.floor(((Math.random()* (MAX_YEAR-MIN_YEAR)) + MIN_YEAR));
		return year;
	}

	private Question makeActorQuestion(Context context, List<Actor> actors, int category) {
		Question result = null;
		
		// Find winner and assign loser answers
		Actor winner = null;
		List<String> wrongAnswers = new ArrayList<String>();
		for (int i=0; i<actors.size(); i++) {
			Actor actor = actors.get(i);
			if (actor.isWinner()) {
				winner = actor;
			} else {
				wrongAnswers.add(actor.getName());
			}
		}
		
		// Make Question content (either text or image)
		if (Math.random() < 0.3) {
			int questionType = Question.ImageType;
			Bitmap image = getImage(winner.getName());
			result = new Question(questionType, image, wrongAnswers, winner.getName());
		} else {
			int questionType = Question.TextType;			
			String text = "";
			if (category == Question.ACTOR) {				
				text = context.getString(R.string.actor_template, winner.getAward(), winner.getYear(), winner.getRole(), winner.getFilm());
			} else {
				text = context.getString(R.string.actress_template, winner.getAward(), winner.getYear(), winner.getRole(), winner.getFilm());
			}
			
			result = new Question(questionType, text, wrongAnswers, winner.getName());
		}
		
		return result;
	}

	private Bitmap getImage(String actorName) {
		ImageManager imgManager = new ImageManager();
		Bitmap result = imgManager.getImage(actorName); 
		return result;
	}

	private Question makeMovieQuestion(Context context, List<Movie> movies, int category) {
		Question result = null;
		
		// Find winner and assign loser answers
		Movie winner = null;
		List<String> wrongAnswers = new ArrayList<String>();
		for (int i=0; i<movies.size(); i++) {
			Movie movie = movies.get(i);
			if (movie.isWinner()) {
				winner = movie;
			} else {
				wrongAnswers.add(movie.getName());
			}
		}
		
		// Make Question content (either text or image)
		String text = context.getString(R.string.movie_template, winner.getYear());
		result = new Question(Question.TextType, text, wrongAnswers, winner.getName());
		
		return result;
	}

}
