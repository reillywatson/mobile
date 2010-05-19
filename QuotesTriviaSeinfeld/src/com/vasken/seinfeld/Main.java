package com.vasken.seinfeld;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.Callable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.vasken.util.StringUtils;

public class Main extends Activity {
	private static final String QUESTION = "QUESTION";
	private static final String CHOICE1 = "CHOICE1";
	private static final String CHOICE2 = "CHOICE2";
	private static final String CHOICE3 = "CHOICE3";
	private static final String CURRENT_ANSWER = "CURRENT_ANSWER";

	private String currentQuestion;
	private String currentAnswer;
	private String choice1;
	private String choice2;
	private String choice3;

	int desiredPercentOfSpeakerQuestions = 25;
	int desiredPercentOfTriviaQuestions = 20;

	static int answersStreak = 0;

	private Handler mHandler = new Handler();

	private QuoteStore quotestore;
	private TriviaStore triviastore;
	Button opt1;
	Button opt2;
	Button opt3;
	Random rand = new Random();

	Timer theTimer;

	final int SECONDS_TO_WAIT = 2 * 1000;
	final int STEP = 10;
	final int SECONDARY_STEP = 5;
	final int NUM_QUESTION = 10;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		opt1 = (Button) findViewById(R.id.opt1);
		opt2 = (Button) findViewById(R.id.opt2);
		opt3 = (Button) findViewById(R.id.opt3);
		try {
			quotestore = new QuoteStore(this, R.raw.seinfeld);
			triviastore = null;
		} catch (IOException e) {
			Log.e(getClass().getName(), Log.getStackTraceString(e));
		}

		theTimer = new Timer();

		TextView result = ((TextView) findViewById(R.id.result));
		result.setBackgroundResource(R.drawable.neutral);
		result.setText(Main.this.getString(R.string.start_text, NUM_QUESTION));

		if (savedInstanceState == null) {
			loadNewQuote();
		} else {
			reloadQuote(savedInstanceState);
		}

		opt1.setOnClickListener(buttonClicked);
		opt2.setOnClickListener(buttonClicked);
		opt3.setOnClickListener(buttonClicked);

		final WebView quoteview = (WebView) findViewById(R.id.quote);
		quoteview.setBackgroundColor(0);

		initializeScore();
	}

	private void initializeScore() {
		Progress progress = (Progress) findViewById(R.id.score);
		progress.setMax(NUM_QUESTION * STEP);
		progress.setSecondaryProgress(SECONDARY_STEP);
	}

	OnClickListener buttonClicked = new OnClickListener() {
		public void onClick(View v) {
			Button b = (Button) v;
			if (b.getText().equals(currentAnswer)) {
				answersStreak += 1;

				TextView result = ((TextView) findViewById(R.id.result));
				result.setBackgroundResource(R.drawable.correct);
				result.setText(R.string.correct);

				if (answersStreak == NUM_QUESTION) {
					answersStreak = 0;

					AlertDialog.Builder builder;
					AlertDialog alertDialog;

					builder = new AlertDialog.Builder(Main.this);
					builder.setTitle(R.string.win_title)
							.setIcon(R.drawable.win).setMessage(
									R.string.win_message).setCancelable(true)
							.setPositiveButton(R.string.win_button,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					alertDialog = builder.create();
					alertDialog.show();
				}
			} else {
				answersStreak = 0;

				TextView result = ((TextView) findViewById(R.id.result));
				result.setBackgroundResource(R.drawable.wrong);
				result.setText(Main.this.getString(R.string.wrong,
						currentAnswer));
			}

			mHandler.removeCallbacks(mUpdateTimeTask);
			mHandler.postDelayed(mUpdateTimeTask, SECONDS_TO_WAIT);

			loadNewQuote();

			Progress progress = (Progress) findViewById(R.id.score);
			progress.setProgress(answersStreak * STEP);
			progress.setSecondaryProgress(progress.getProgress()
					+ SECONDARY_STEP);
		}
	};

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			TextView result = ((TextView) findViewById(R.id.result));
			result.setBackgroundResource(R.drawable.neutral);
			result.setText("Question " + (answersStreak + 1) + " of "
					+ NUM_QUESTION);

			mHandler.postAtTime(this, System.currentTimeMillis()
					+ SECONDS_TO_WAIT);
		}
	};

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	String removeSpeaker(String quote) {
		int slashB = quote.indexOf("</b>");
		String noSpeaker = quote.substring(slashB + 5, quote.length()).trim();
		if (noSpeaker.startsWith(":"))
			noSpeaker = noSpeaker.replaceFirst(":", "").trim();
		Log.d("BEFORE", quote);
		return "<dl><dd>" + noSpeaker;
	}

	private String nameSpeakerPrefix = "<span><b>Who said it?</b></span><p>";
	private String episodePrefix = "<span><b>Name the episode:</b></span><p>";

	void loadNewQuote() {
		final WebView quoteview = (WebView) findViewById(R.id.quote);

		SimpsonsQuote quote = quotestore.randomQuote();
		List<String> answers = new ArrayList<String>();

		boolean isSpeakerQuestion = false;
		boolean isTriviaQuestion = false;
		int nextRandInt = rand.nextInt(100);
		if (quotestore.canDoSpeakerQuestions()
				&& nextRandInt <= desiredPercentOfSpeakerQuestions) {
			isSpeakerQuestion = true;
			while (quote.speaker == null) {
				quote = quotestore.randomQuote();
			}
		} else if (triviastore != null && triviastore.isAvailable()
				&& nextRandInt <= desiredPercentOfSpeakerQuestions
						+ desiredPercentOfTriviaQuestions) {
			isTriviaQuestion = true;
		}

		String question = quote.quote;

		if (isTriviaQuestion) {
			Question triviaQuestion = triviastore.getQuestion();
			question = "<b>" + triviaQuestion.question + "</b>";
			currentAnswer = triviaQuestion.correctAnswer;
			answers = triviaQuestion.answers;
		} else {
			Callable<String> generator;
			if (isSpeakerQuestion) {
				currentAnswer = quote.speaker;
				generator = new Callable<String>() {
					public String call() throws Exception {
						return quotestore.randomSpeaker();
					}
				};
				question = nameSpeakerPrefix
						+ "<div style='margin-left: -40px'>"
						+ removeSpeaker(question) + "</div>";

			} else {
				currentAnswer = quote.episode;
				generator = new Callable<String>() {
					public String call() throws Exception {
						return quotestore.randomEpisode();
					}
				};
				question = episodePrefix + "<div style='margin-left: -40px'>"
						+ question + "</div>";

			}

			answers.add(currentAnswer);
			populateListWithUniqueElements(answers, 3, generator);
			
			currentAnswer = StringUtils.unescapeHtml(currentAnswer);
		}
		Collections.shuffle(answers);

		question = "<span style='color: white'> " + question + " </span>";
		currentQuestion = question;

		quoteview.loadData(question, "text/html", "utf-8");
		// otherwise if we go from something that fits on one page to something
		// that doesn't, the
		// scroll indicator doesn't show up
		quoteview.setVerticalScrollbarOverlay(true);

		choice1 = StringUtils.unescapeHtml(answers.get(0));
		choice2 = StringUtils.unescapeHtml(answers.get(1));
		choice3 = StringUtils.unescapeHtml(answers.get(2));

		opt1.setText(choice1);
		opt2.setText(choice2);
		opt3.setText(choice3);
	}

	// I could just make everything static, but I almost forgot how to use this.
	// So I wanted a reminder in code.
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putString(QUESTION, currentQuestion);
		savedInstanceState.putString(CHOICE1, choice1);
		savedInstanceState.putString(CHOICE2, choice2);
		savedInstanceState.putString(CHOICE3, choice3);
		savedInstanceState.putString(CURRENT_ANSWER, currentAnswer);

		super.onSaveInstanceState(savedInstanceState);
	}

	private void reloadQuote(Bundle savedInstanceState) {
		String question = savedInstanceState.getString(QUESTION);
		String answer1 = savedInstanceState.getString(CHOICE1);
		String answer2 = savedInstanceState.getString(CHOICE2);
		String answer3 = savedInstanceState.getString(CHOICE3);
		String current = savedInstanceState.getString(CURRENT_ANSWER);

		choice1 = answer1;
		choice2 = answer2;
		choice3 = answer3;
		currentQuestion = question;
		currentAnswer = current;

		WebView quoteview = (WebView) findViewById(R.id.quote);
		quoteview.loadData(question, "text/html", "utf-8");

		opt1.setText(answer1);
		opt2.setText(answer2);
		opt3.setText(answer3);
	}
}