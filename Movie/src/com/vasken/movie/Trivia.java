package com.vasken.movie;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vasken.movie.manager.DatabaseManager;
import com.vasken.movie.manager.QuestionManager;
import com.vasken.movie.model.Question;
import com.vasken.movie.model.QuestionType;

public class Trivia extends Activity {
	private static final double CHANCE_TO_PICK_SUPPORTING_ACTOR = 0.3;
	private static final double CHANCE_TO_PICK_QUOTE = 0.9;
	private static final String QUESTION_TEMPLATE = "<span style='color: white'>%1$s</span>";
	
	private static QuestionManager theManager;
	private String category;
	private int numRightAnswers;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        theManager = QuestionManager.sharedInstance();
        
        category = getIntent().getExtras().getString(CategoryActivity.CATEGORY);
        
        loadNextQuestion();
    }

	private void loadNextQuestion() {
		DatabaseManager dbManager = new DatabaseManager(this);
		
		QuestionType questionType = determineQuestionType();
		
		Question question = theManager.getNextQuestion(this, dbManager, questionType);

		WebView text = (WebView)findViewById(R.id.text);
		text.setBackgroundColor(0);
		text.getSettings().setMinimumFontSize(20);
		ImageView image = (ImageView)findViewById(R.id.image);
		Button answer1 = (Button)findViewById(R.id.answer1);
		Button answer2 = (Button)findViewById(R.id.answer2);
		Button answer3 = (Button)findViewById(R.id.answer3);
		
		String formattedString = String.format(QUESTION_TEMPLATE, question.getText());
		text.loadData(formattedString, "text/html", "utf-8");
		if (question.getType() == Question.ImageType) {
			image.setVisibility(View.VISIBLE);
			image.setImageBitmap(question.getImage());
		} else if ( question.getType() == Question.TextType) {
			image.setVisibility(View.GONE);
		}
		
		List<String> possibleAnswers = question.getPossibleAnswers(3);
		if (possibleAnswers.size() > 2) {
			answer1.setText(possibleAnswers.get(0));
			answer2.setText(possibleAnswers.get(1));
			answer3.setText(possibleAnswers.get(2));
		}
		
		answer1.setOnClickListener(new AnswerClickListener(question));
		answer2.setOnClickListener(new AnswerClickListener(question));
		answer3.setOnClickListener(new AnswerClickListener(question));
	}

	private QuestionType determineQuestionType() {
		QuestionType questionType = QuestionType.ACTOR;
		
		if (category.equals(CategoryActivity.CATEGORY_ACTORS)) {
			if (Math.random() < CHANCE_TO_PICK_SUPPORTING_ACTOR) {
				questionType = QuestionType.SUPPORTING_ACTOR;
			} else {
				questionType = QuestionType.ACTOR;
			}
		} else if (category.equals(CategoryActivity.CATEGORY_ACTRESSES)) {
			if (Math.random() < CHANCE_TO_PICK_SUPPORTING_ACTOR) {
				questionType = QuestionType.SUPPORTING_ACTRESS;
			} else {
				questionType = QuestionType.ACTRESS;
			}
		} else if (category.equals(CategoryActivity.CATEGORY_DIRECTORS)) {
			questionType = QuestionType.DIRECTOR;
		} else if (category.equals(CategoryActivity.CATEGORY_MOVIES)) {
			if (Math.random() < CHANCE_TO_PICK_QUOTE) {
				questionType = QuestionType.QUOTE;
			} else {
				questionType = QuestionType.MOVIE;
			}
		}

		return questionType;
	}

	private class AnswerClickListener implements OnClickListener {
		private Question theQuestion;
		
		public AnswerClickListener(Question question) {
			theQuestion = question;
		}
		
		@Override
		public void onClick(View v) {
			TextView theView = (TextView)v;
			if (theView.getText().equals(theQuestion.getRightAnswer())) {
				handleCorrectAnswer();
			} else {
				handleWrongAnswer();
			}
			loadNextQuestion();
 		}

	}

	protected void handleCorrectAnswer() {
		View layout = getLayoutInflater().inflate(R.layout.result_layout,
                (ViewGroup) findViewById(R.id.achievement_layout_root));

		ImageView image = (ImageView) layout.findViewById(R.id.result_image);
		image.setImageResource(R.drawable.trivia_right);
		
		Toast toast = new Toast(Trivia.this);
		toast.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
		
		numRightAnswers++;
		TextView result = (TextView)Trivia.this.findViewById(R.id.bottom_banner);
		result.setText(Trivia.this.getString(R.string.trivia_streak, numRightAnswers));
	}

	protected void handleWrongAnswer() {
		View layout = getLayoutInflater().inflate(R.layout.result_layout,
                (ViewGroup) findViewById(R.id.achievement_layout_root));

		ImageView image = (ImageView) layout.findViewById(R.id.result_image);
		image.setImageResource(R.drawable.trivia_wrong);
		
		Toast toast = new Toast(Trivia.this);
		toast.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
		
		numRightAnswers = 0;
		TextView result = (TextView)Trivia.this.findViewById(R.id.bottom_banner);
		result.setText("");
	}
}