package com.vasken.SimpsonsTrivia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import com.vasken.util.StringUtils;

import android.content.Context;
import android.util.Log;

public class EpisodeStore implements QuestionStore {
	protected List<Quote> quotes = new ArrayList<Quote>();
	protected static Random rand = new Random();
	private static String EPISODE_TEMPLATE = "<span style='color: white'><span><b>Name the episode:</b></span><p><div style='margin-left: -40px'>%1$s</span></div></span>";
	
	public EpisodeStore(Context context, int storeid) throws IOException {
		InputStream in = context.getResources().openRawResource(storeid);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String season = reader.readLine();
		while (season != null) {
			String epTitle = reader.readLine();
			String quote = reader.readLine();
			Quote sq = createQuote(season, epTitle, quote);
			if (sq != null)
				quotes.add(sq);
			season = reader.readLine();
		}
		Log.d(getClass().getName(), "Found " + Integer.toString(quotes.size()) + " quotes");
	}
	
	protected Quote createQuote(String season, String epTitle, String quote) {
		return new Quote(season, epTitle, quote, null);
	}
	
	public int numQuestions() {
		return quotes.size();
	}
	
	public boolean isAvailable() {
		return !quotes.isEmpty();
	}
	
	public Question getQuestion() {
		Question result = null;
		Quote quote = randomQuote();
		if (quote != null) {
			result = new Question();
			result.question = questionForQuote(quote);
			result.correctAnswer = StringUtils.unescapeHtml(answerForQuote(quote));
			result.answers.add(result.correctAnswer);
			
			populateListWithUniqueElements(result.answers, 3, answerGenerator);
		}
		return result;
	}
	
	private Quote randomQuoteFromList(List<Quote> list) {
		return list.get(rand.nextInt(list.size()));
	}
	
	public Quote randomQuote() {
		return randomQuoteFromList(quotes);
	}
	
	protected String questionForQuote(Quote quote) {
		return String.format(EPISODE_TEMPLATE, quote.quote);
	}
	
	protected String answerForQuote(Quote quote) {
		return quote.episode;
	}


	@Override
	public String storeName() {
		return QuestionManager.EPISODE;
	}
	
	private Callable<String>answerGenerator = new Callable<String>() {
		public String call() throws Exception {
			return StringUtils.unescapeHtml(answerForQuote(randomQuote()));
		}
	};
	
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
}
