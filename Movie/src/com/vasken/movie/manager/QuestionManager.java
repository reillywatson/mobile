package com.vasken.movie.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    
    private Map<QuestionType, Integer[]> yearRanges;

	public static QuestionManager sharedInstance(DatabaseManager dbManager) {
		if (INSTANCE == null) { 
			INSTANCE = new QuestionManager();
			// This could be done somewhere else and stored forever
			// Except it seems to be fairly quick (under 2sec on my phone)
			// So I'm not too worried about it
			INSTANCE.populateRanges(dbManager);
		}
		return INSTANCE;
	}

	private void populateRanges(DatabaseManager dbManager) {
		yearRanges = new HashMap<QuestionType, Integer[]>();
		
		dbManager.openDataBase();
		try {
			yearRanges.put(QuestionType.ACTOR, dbManager.getYearRanges(QuestionType.ACTOR));
			yearRanges.put(QuestionType.ACTRESS, dbManager.getYearRanges(QuestionType.ACTRESS));
			yearRanges.put(QuestionType.DIRECTOR, dbManager.getYearRanges(QuestionType.DIRECTOR));
			yearRanges.put(QuestionType.MOVIE, dbManager.getYearRanges(QuestionType.MOVIE));
			yearRanges.put(QuestionType.SUPPORTING_ACTOR, dbManager.getYearRanges(QuestionType.SUPPORTING_ACTOR));
			yearRanges.put(QuestionType.SUPPORTING_ACTRESS, dbManager.getYearRanges(QuestionType.SUPPORTING_ACTRESS));
		} finally {
			dbManager.close();
		}
	}

	public Question getNextQuestion(Context context, DatabaseManager dbManager, QuestionType questionType) {
		Question result = null;
		
		dbManager.openDataBase();
		try {
			if (QuestionType.QUOTE == questionType) {
				Quote quote = dbManager.getRandomQuote();
				List<Movie> movies = dbManager.getMoviesFromSameYear(quote.getFilm());
				result = makeQuoteQuestion(context, quote, movies);
			} else {
				int randomYear = getRandomYear(questionType);
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
				}
			}
		} catch(Exception e) {
			Log.e(this.getClass().toString(), " Failed to get make a question. Retrying... " + questionType);
			result = getNextQuestion(context, dbManager, questionType);
		} finally {
			dbManager.close();
		}
		
		return result;
	}

	private Question makeQuoteQuestion(Context context, Quote quote, List<Movie> movies) {
		List<String> wrongAnswers = new ArrayList<String>();
		
		int year = 1983;
		for(Movie movie : movies) {
			if (!movie.getName().equals(quote.getFilm())) {
				wrongAnswers.add(movie.getName());
				year = movie.getYear();
			}
		}
		
		StringBuilder quoteText = new StringBuilder("<br/><br/>").append(quote.getText());
		String questionText = context.getString(R.string.quote_template, year, quoteText);

		return new Question(Question.TextType, questionText, wrongAnswers, quote.getFilm());
	}

	private Question makeDirectorQuestion(Context context, List<NominatedPerson> directors, QuestionType questionType) {
		Question result = new Question();
		
		// Find winner and assign loser answers
		NominatedPerson winner = (NominatedPerson)setWinnerAndLosers(directors, result);
		
		// Make Question content (either text or image)
		String text = context.getString(R.string.director_template, winner.getYear(), winner.getFilm());
		result.setText(text);
		result.setType(Question.TextType);
		
		return result;
	}

	private Question makeMovieQuestion(Context context, List<Movie> movies, QuestionType questionType) {
		Question result = new Question();
		
		// Find winner and assign loser answers
		NominatedItem winner = setWinnerAndLosers(movies, result);
		
		// Make Question content (either text or image)
		String text = context.getString(R.string.movie_template, winner.getYear());
		result.setType(Question.TextType);
		result.setText(text);
		
		return result;
	}

	private Question makeActorQuestion(Context context, List<Actor> actors, QuestionType category) {
		Question result = new Question();
		
		// Find winner and assign loser answers
		NominatedItem winner = setWinnerAndLosers(actors, result);
		Actor actor = (Actor)winner;
		
		// Make Question content (either text or image)
		if (Math.random() < 0.3) {
			Bitmap image = getImage(winner.getName());
			
			int template = 0;
			if (category == QuestionType.ACTOR || category == QuestionType.SUPPORTING_ACTOR) {				
				template = R.string.actor_template_img;
			} else if (category == QuestionType.ACTRESS || category == QuestionType.SUPPORTING_ACTRESS) {
				template = R.string.actress_template_img;
			}
			String text = context.getString(template, actor.getYear(), actor.getRole(), actor.getFilm());

			result.setType(Question.ImageType);
			result.setImage(image);
			result.setText(text);
		} else {
			int template = 0;
			if (category == QuestionType.ACTOR || category == QuestionType.SUPPORTING_ACTOR) {				
				template = R.string.actor_template;
			} else if (category == QuestionType.ACTRESS || category == QuestionType.SUPPORTING_ACTRESS) {
				template = R.string.actress_template;
			}
			String text = context.getString(template, actor.getAward(), actor.getYear(), actor.getRole(), actor.getFilm());

			result.setType(Question.TextType);
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

	private int getRandomYear(QuestionType questionType) {
		Integer[] range = yearRanges.get(questionType);
		int minYear = range[0];
		int maxYear = range[1];
		
		Random random = new Random();
		int year = random.nextInt(maxYear - minYear + 1) + minYear;
		
		return year;
	}

}
