package com.vasken.movie.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.vasken.movie.R;
import com.vasken.movie.model.Actor;
import com.vasken.movie.model.Movie;
import com.vasken.movie.model.NominatedItem;
import com.vasken.movie.model.NominatedPerson;
import com.vasken.movie.model.Question;
import com.vasken.movie.model.QuestionType;
import com.vasken.movie.model.Quote;

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
	
	public Question getNextQuestion(Context context, DatabaseManager dbManager, QuestionType questionType) {
		Question result = null;
		
		int randomYear = getRandomYear();
		
		dbManager.openDataBase();
		if (QuestionType.ACTOR == questionType) {
			List<Actor> actors = dbManager.getBestActorEntries(randomYear);
			result = makeActorQuestion(context, actors, questionType);
		} else if (QuestionType.ACTRESS == questionType) {
			List<Actor> actresses = dbManager.getBestActressEntries(randomYear);
			result = makeActorQuestion(context, actresses, questionType);
		} else if (QuestionType.MOVIE == questionType) {
			List<Movie> movies = dbManager.getBestPictureEntries(randomYear);
			result = makeMovieQuestion(context, movies, questionType);
		} else if (QuestionType.DIRECTOR == questionType) {
			List<NominatedPerson> directors =  dbManager.getBestDirectorEntries(randomYear);
			result = makeDirectorQuestion(context, directors, questionType);
		} else if (QuestionType.SUPPORTING_ACTOR == questionType) {
			List<Actor> supportingActors =  dbManager.getBestSupportingActorEntries(randomYear);
			result = makeActorQuestion(context, supportingActors, questionType);
		} else if (QuestionType.SUPPORTING_ACTRESS == questionType) {
			List<Actor> supportingActresses =  dbManager.getBestSupportingActressesEntries(randomYear);
			result = makeActorQuestion(context, supportingActresses, questionType);
		} else if (QuestionType.QUOTE == questionType) {
			Quote quote = dbManager.getRandomQuote();
			if (quote == null) {
				// Do Error handling
			}
			List<Movie> movies = dbManager.getMoviesFromSameYear(quote.getFilm());
			result = makeQuoteQuestion(context, quote, movies);
		}
		dbManager.close();
		
		Log.d(getClass().toString(), result.toString());
		return result;
	}

	private Question makeQuoteQuestion(Context context, Quote quote, List<Movie> movies) {
		List<String> wrongAnswers = new ArrayList<String>();
		
		int year = 1983;
		for(Movie movie : movies) {
			wrongAnswers.add(movie.getName());
			year = movie.getYear();
		}
		String questionText = context.getString(R.string.quote_template, year, quote.getText());

		return new Question(Question.TextType, questionText, wrongAnswers, quote.getFilm());
	}

	private Question makeDirectorQuestion(Context context, List<NominatedPerson> directors, QuestionType questionType) {
		Question result = new Question();
		
		// Find winner and assign loser answers
		NominatedPerson winner = (NominatedPerson)setWinnerAndLosers(directors, result);
		
		// Make Question content (either text or image)
		String text = context.getString(R.string.director_template, winner.getYear(), winner.getFilm());
		result.setText(text);
		
		return result;
	}

	private Question makeMovieQuestion(Context context, List<Movie> movies, QuestionType questionType) {
		Question result = new Question();
		
		// Find winner and assign loser answers
		NominatedItem winner = setWinnerAndLosers(movies, result);
		
		// Make Question content (either text or image)
		String text = context.getString(R.string.movie_template, winner.getYear());
		result.setText(text);
		
		return result;
	}

	private Question makeActorQuestion(Context context, List<Actor> actors, QuestionType category) {
		Question result = new Question();
		
		// Find winner and assign loser answers
		NominatedItem winner = setWinnerAndLosers(actors, result);
		
		// Make Question content (either text or image)
		if (Math.random() < 0.3) {
			Bitmap image = getImage(winner.getName());
			result.setImage(image);
		} else {
			String text = "";
			Actor actor = (Actor)winner;
			int template = 0;
			if (category == QuestionType.ACTOR || category == QuestionType.SUPPORTING_ACTOR) {				
				template = R.string.actor_template;
			} else if (category == QuestionType.ACTRESS || category == QuestionType.SUPPORTING_ACTRESS) {
				template = R.string.actress_template;
			}
			text = context.getString(template, actor.getAward(), actor.getYear(), actor.getRole(), actor.getFilm());
			
			result.setText(text);
		}
		
		return result;
	}

	private NominatedItem setWinnerAndLosers(List<?> items, Question result) {
		NominatedItem winner = null;
		List<String> wrongAnswers = new ArrayList<String>();
		for (Object item : items) {
			NominatedItem nominatedItem = (NominatedItem) item; 
			if (nominatedItem.isWinner()) {
				winner = nominatedItem;
			} else {
				wrongAnswers.add(nominatedItem.getName());
			}
		}
		result.setRightAnswer(winner.getName());
		result.setWrongAnswers(wrongAnswers);
		return winner;
		
	}

	private Bitmap getImage(String actorName) {
		ImageManager imgManager = new ImageManager();
		Bitmap result = imgManager.getImage(actorName); 
		return result;
	}

	private int getRandomYear() {
		int year = (int)Math.floor(((Math.random()* (MAX_YEAR-MIN_YEAR)) + MIN_YEAR));
		return year;
	}

}
