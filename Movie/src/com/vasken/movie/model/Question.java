package com.vasken.movie.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.Bitmap;
import android.util.Log;

public class Question {
	public static final int ImageType = 0;
	public static final int TextType = 1;

	private int type;
	private String text;
	private Bitmap image;
	private List<String> wrongAnswers;
	private String rightAnswer;
	
	private Question(int type, List<String> wrongAnswers, String rightAnswer) {
		this.type = type;
		this.wrongAnswers = wrongAnswers;
		this.rightAnswer = rightAnswer;
	}
	
	public Question(int type, String text, List<String> wrongAnswers, String rightAnswer) {
		this(type, wrongAnswers, rightAnswer);
		this.text = text;
	}
	
	public Question(int type, Bitmap image, List<String> wrongAnswers, String rightAnswer) {
		this(type, wrongAnswers, rightAnswer);
		this.image = image;
	}
	
	public int getType() {
		return type;
	}

	public CharSequence getText() {
		return text;
	}

	public Bitmap getImage() {
		return image;
	}

	public Object getRightAnswer() {
		return rightAnswer;
	}

	public List<String> getPossibleAnswers(int size) {
		if ( size > (wrongAnswers.size()+1) ) {
			Log.w(this.getClass().toString(), "BUG! There's not that many unique questions available");
		}
		
		Collections.shuffle(wrongAnswers);
		ArrayList<String> result = new ArrayList<String>(wrongAnswers.subList(0, size-1));
		result.add(0, rightAnswer);
		Collections.shuffle(result);
		
		return result;
	}

}
