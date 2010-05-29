package com.vasken.SimpsonsTrivia;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;
import android.util.Log;

import com.vasken.SimpsonsTrivia.QuoteStore.SimpsonsQuote;
import com.vasken.util.StringUtils;

public class Worker {
	private QuoteStore quotestore;
	private TriviaStore triviastore;
	
	private Callable<String>episodeNameGenerator = new Callable<String>() {
		public String call() throws Exception {
			return StringUtils.unescapeHtml(quotestore.randomEpisode());
		}
	};
	private Callable<String>speakerGenerator = new Callable<String>() {
		public String call() throws Exception {
			return StringUtils.unescapeHtml(quotestore.randomSpeaker());
		}
	};
	
	public Worker(Context theContext) {
		try {
			quotestore = new QuoteStore(theContext, R.raw.the_simpsons);
			triviastore = new TriviaStore(theContext, R.raw.simpsons_trivia);
		} catch (IOException e) {
			Log.e(getClass().getName(), Log.getStackTraceString(e));
		}
	}
	
	public Question getTriviaQuestion() {
		return triviastore.getQuestion();
	}

	public Question getEpisodeQuestion() {
		Question result = new Question();
		SimpsonsQuote quote = quotestore.randomQuote();

		result.question = quote.quote;
		result.correctAnswer = StringUtils.unescapeHtml(quote.episode);
		result.answers.add(result.correctAnswer);
		
		populateListWithUniqueElements(result.answers, 3, episodeNameGenerator);
		
		return result;
	}
	
	public Question getSpeakerQuestion() {
		if (quotestore.canDoSpeakerQuestions()) {
			Question result = new Question();
			SimpsonsQuote quote = quotestore.randomQuote();
			while (quote.speaker == null) {
				quote = quotestore.randomQuote();
			}
			
			result.question = removeSpeaker(quote.quote);
			result.correctAnswer = StringUtils.unescapeHtml(quote.speaker);
			result.answers.add(StringUtils.unescapeHtml(result.correctAnswer));
			
			populateListWithUniqueElements(result.answers, 3, speakerGenerator);
			Collections.shuffle(result.answers);
			
			return result;
		}
		
		Log.e(getClass().getName(), "ERROR: You shouldn't be asking for speaker questions.");
		return null;
	}

	public boolean hasTrivia() {
		return triviastore.isAvailable();
	}

	public boolean hasSpeakerQuestions() {
		return quotestore.canDoSpeakerQuestions();
	}

	/*************************** HELPERS *****************************/
	/*****************************************************************/
	// Man this is suuuuper generic, maybe such a function already exists?
	<T extends Object> void populateListWithUniqueElements(List<T> list,
			int desiredSize, Callable<T> generator) {
		try {
			while (list.size() < desiredSize) {
				T newObj = generator.call();
				if (!list.contains(newObj)) {
					list.add(newObj);
				}
			}
		} catch (Exception e) {
			Log.d(getClass().getName(), Log.getStackTraceString(e));
		}
	}

	private String removeSpeaker(String quote) {
		int slashB = quote.indexOf("</b>");
		String noSpeaker = quote.substring(slashB + 5, quote.length()).trim();
		if (noSpeaker.startsWith(":"))
			noSpeaker = noSpeaker.replaceFirst(":", "").trim();
		Log.d("BEFORE", quote);
		return "<dl><dd>" + noSpeaker;
	}
}
