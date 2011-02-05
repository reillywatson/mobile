package com.vasken.movie;

import com.vasken.movie.manager.QuestionManager;
import com.vasken.movie.model.Question;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Trivia extends Activity {
	private static QuestionManager theManager;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        theManager = QuestionManager.sharedInstance();
        Question question = theManager.getNextQuestion();
        prepareQuestion(question);
    }

	private void prepareQuestion(Question question) {
		ImageView image = (ImageView)findViewById(R.id.image);
		TextView text = (TextView)findViewById(R.id.text);
		
		if (question.getType() == Question.ImageType) {
			image.setImageBitmap(question.getImage());
		} else if ( question.getType() == Question.TextType) {
			text.setText(question.getText());
		}
	}
    
    
}