package com.vasken.SimpsonsTrivia;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity {
	private static final String QUESTION = "QUESTION";

	private static final int NUM_QUESTION = 10;
	private static final int PROGRESS_BAR_STEP = 10;
	private static final int PROGRESS_BAR_SECONDARY_STEP = 5;
	private static final int SECONDS_TO_WAIT_BEFORE_RESULT_UPDATES = 2 * 1000;

	private String SPEAKER_TEMPLATE = "<span style='color: white'><span><b>Who said it?</b></span><p><div style='margin-left: -40px'>%1$s</div></span>";
	private String EPISODE_TEMPLATE = "<span style='color: white'><span><b>Name the episode:</b></span><p><div style='margin-left: -40px'>%1$s</span></div></span>";
	private String TRIVIA_TEMPLATE = "<span style='color: white'><b>%1$s</b></span>";
	
	private Question currentQuestion;
	static int answersStreak = 0;

	private Button opt1;
	private Button opt2;
	private Button opt3;

	private Random rand = new Random();	
	private Handler mHandler = new Handler();
	private Worker theWorker;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		TextView result = ((TextView) findViewById(R.id.result));
		result.setBackgroundResource(R.drawable.neutral);
		result.setText(Main.this.getString(R.string.start_text, NUM_QUESTION));

		theWorker = new Worker(this);
		new QuestionTypeManager();
		
		opt1 = (Button) findViewById(R.id.opt1);
		opt1.setOnClickListener(buttonClicked);
		opt2 = (Button) findViewById(R.id.opt2);
		opt2.setOnClickListener(buttonClicked);
		opt3 = (Button) findViewById(R.id.opt3);
		opt3.setOnClickListener(buttonClicked);

		final WebView quoteview = (WebView) findViewById(R.id.quote);
		quoteview.setBackgroundColor(0);

		if (savedInstanceState == null) {
			loadNewQuote();
		} else {
			reloadQuote(savedInstanceState);
		}
		
		initializeScore();
	}

	private void loadNewQuote() {
		int nextRandInt = rand.nextInt(100);
		if (theWorker.hasSpeakerQuestions() && nextRandInt <= QuestionTypeManager.speakerQuestion.likelyhood()) {
			currentQuestion = theWorker.getSpeakerQuestion();
			currentQuestion.question  = String.format(SPEAKER_TEMPLATE, currentQuestion.question);
		} else if (theWorker.hasTrivia() && nextRandInt <= QuestionTypeManager.speakerQuestion.likelyhood() + QuestionTypeManager.triviaQuestion.likelyhood()) {
			currentQuestion = theWorker.getTriviaQuestion();
			currentQuestion.question  = String.format(TRIVIA_TEMPLATE, currentQuestion.question);
		} else {
			currentQuestion = theWorker.getEpisodeQuestion();
			currentQuestion.question  = String.format(EPISODE_TEMPLATE, currentQuestion.question);
		}
		
		final WebView quoteview = (WebView) findViewById(R.id.quote);
		quoteview.loadData(currentQuestion.question, "text/html", "utf-8");
		// otherwise if we go from something that fits on one page to something
		// that doesn't, the scroll indicator doesn't show up
		quoteview.setVerticalScrollbarOverlay(true);

		opt1.setText(currentQuestion.answers.get(0));
		opt2.setText(currentQuestion.answers.get(1));
		opt3.setText(currentQuestion.answers.get(2));
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();

		if (QuestionTypeManager.episodeQuestion.isEnabled()) {
			menu.add(R.string.menu_hide_episode_questions)
				.setIcon(R.drawable.menu_episode_hide)
				.setEnabled(QuestionTypeManager.speakerQuestion.isEnabled() || QuestionTypeManager.triviaQuestion.isEnabled());
		}else {
			menu.add(R.string.menu_show_episode_questions)
				.setIcon(R.drawable.menu_episode);
		}
		
		if (QuestionTypeManager.speakerQuestion.isEnabled()) {
			menu.add(R.string.menu_hide_quote_questions)
				.setIcon(R.drawable.menu_quote_hide)
				.setEnabled(theWorker.hasSpeakerQuestions() && (QuestionTypeManager.episodeQuestion.isEnabled() || QuestionTypeManager.triviaQuestion.isEnabled()));
		}else {
			menu.add(R.string.menu_show_quote_questions)
			.setIcon(R.drawable.menu_quote);
		}
		
		if (QuestionTypeManager.triviaQuestion.isEnabled()) {
			menu.add(R.string.menu_hide_trivia_questions)
				.setIcon(R.drawable.menu_trivia_hide)
				.setEnabled(theWorker.hasTrivia() && (QuestionTypeManager.episodeQuestion.isEnabled() || QuestionTypeManager.speakerQuestion.isEnabled() ));
		}else {
			menu.add(R.string.menu_show_trivia_questions)
				.setIcon(R.drawable.menu_trivia);
		}
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (getString(R.string.menu_hide_quote_questions).equals(item.getTitle())) {
			QuestionTypeManager.disableQuestion(QuestionTypeManager.SPEAKER);
		} else if (getString(R.string.menu_show_quote_questions).equals(item.getTitle())) {
			QuestionTypeManager.enableQuestion(QuestionTypeManager.SPEAKER);
		} else if (getString(R.string.menu_hide_trivia_questions).equals(item.getTitle())) {
			QuestionTypeManager.disableQuestion(QuestionTypeManager.TRIVIA);
		} else if (getString(R.string.menu_show_trivia_questions).equals(item.getTitle())) {
			QuestionTypeManager.enableQuestion(QuestionTypeManager.TRIVIA);
		} else if (getString(R.string.menu_hide_episode_questions).equals(item.getTitle())) {
			QuestionTypeManager.disableQuestion(QuestionTypeManager.EPISODE);
		} else if (getString(R.string.menu_show_episode_questions).equals(item.getTitle())) {
			QuestionTypeManager.enableQuestion(QuestionTypeManager.EPISODE);	
		}
		
		Log.d("SpeakerQuestions", ""+QuestionTypeManager.speakerQuestion.likelyhood());
		Log.d("TriviaQuestions", ""+QuestionTypeManager.triviaQuestion.likelyhood());
		Log.d("EpisodeQuestions", ""+QuestionTypeManager.episodeQuestion.likelyhood());
		return true;
	}

	// I could just make everything static, but I almost forgot how to use this.
	// So I wanted a reminder in code.
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putSerializable(QUESTION, currentQuestion);

		super.onSaveInstanceState(savedInstanceState);
	}
	
	/************************* HELPERS *************************/
	private void initializeScore() {
		Progress progress = (Progress) findViewById(R.id.score);
		progress.setMax(NUM_QUESTION * PROGRESS_BAR_STEP);
		progress.setSecondaryProgress(PROGRESS_BAR_SECONDARY_STEP);
	}
	
	private OnClickListener buttonClicked = new OnClickListener() {
		public void onClick(View v) {
			Button b = (Button) v;
			if (b.getText().equals(currentQuestion.correctAnswer)) {
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
				result.setText(Main.this.getString(R.string.wrong, currentQuestion.correctAnswer));
			}

			mHandler.removeCallbacks(mUpdateTimeTask);
			mHandler.postDelayed(mUpdateTimeTask, SECONDS_TO_WAIT_BEFORE_RESULT_UPDATES);

			loadNewQuote();

			Progress progress = (Progress) findViewById(R.id.score);
			progress.setProgress(answersStreak * PROGRESS_BAR_STEP);
			progress.setSecondaryProgress(progress.getProgress() + PROGRESS_BAR_SECONDARY_STEP);
		}
	};
	
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			TextView result = ((TextView) findViewById(R.id.result));
			result.setBackgroundResource(R.drawable.neutral);
			result.setText("Question " + (answersStreak + 1) + " of " + NUM_QUESTION);

			mHandler.postAtTime(this, System.currentTimeMillis() + SECONDS_TO_WAIT_BEFORE_RESULT_UPDATES);
		}
	};
		
	private void reloadQuote(Bundle savedInstanceState) {
		Question question = (Question) savedInstanceState.getSerializable(QUESTION);
		currentQuestion = question;
		
		WebView quoteview = (WebView) findViewById(R.id.quote);
		quoteview.loadData(question.question, "text/html", "utf-8");

		opt1.setText(currentQuestion.answers.get(0));
		opt2.setText(currentQuestion.answers.get(1));
		opt3.setText(currentQuestion.answers.get(2));
	}
}