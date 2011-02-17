package com.vasken.movie;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vasken.movie.manager.DatabaseManager;
import com.vasken.movie.manager.QuestionManager;
import com.vasken.movie.model.Question;
import com.vasken.movie.model.QuestionType;

public class Trivia extends Activity {
	private static final double CHANCE_TO_PICK_SUPPORTING_ACTOR = 0.3;
	
	private static QuestionManager theManager;
	private String category;
	
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
        
		ImageView image = (ImageView)findViewById(R.id.image);
		TextView text = (TextView)findViewById(R.id.text);
		TextView answer1 = (TextView)findViewById(R.id.answer1);
		TextView answer2 = (TextView)findViewById(R.id.answer2);
		TextView answer3 = (TextView)findViewById(R.id.answer3);
		
		
		if (question.getType() == Question.ImageType) {
			image.setImageBitmap(question.getImage());
		} else if ( question.getType() == Question.TextType) {
			text.setText(question.getText());
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
			questionType = QuestionType.MOVIE;
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
				loadNextQuestion();
			} else {
				handleWrongAnswer();
				loadNextQuestion();
			}
 		}

	}

	protected void handleCorrectAnswer() {
		Toast.makeText(this, "RIGHT", Toast.LENGTH_LONG);
	}

	protected void handleWrongAnswer() {
		Toast.makeText(this, "WRONG", Toast.LENGTH_LONG);
	}
}