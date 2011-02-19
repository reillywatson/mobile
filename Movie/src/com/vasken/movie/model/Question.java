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
	
	public Question() {	}

	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	public CharSequence getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setImage(Bitmap img) {
		this.type = ImageType;
		image = img;
	}

	public Bitmap getImage() {
		return image;
	}

	public String getRightAnswer() {
		return rightAnswer;
	}

	public void setRightAnswer(String name) {
		this.rightAnswer = name;
	}

	public void setWrongAnswers(List<String> wrongAnswers) {
		this.wrongAnswers = wrongAnswers;
	}

	public List<String> getPossibleAnswers(int size) {
		if ( size > (wrongAnswers.size()+1) ) {
			Log.w(this.getClass().toString(), "BUG! There's not that many unique questions available");
			return new ArrayList<String>();
		}
		
		Collections.shuffle(wrongAnswers);
		ArrayList<String> result = new ArrayList<String>(wrongAnswers.subList(0, size-1));
		result.add(0, rightAnswer);
		Collections.shuffle(result);
		
		return result;
	}

	@Override
	public String toString() {
		return "Question [type=" + type + ", text=" + text + ", image=" + image
				+ ", wrongAnswers=" + wrongAnswers + ", rightAnswer="
				+ rightAnswer + "]";
	}
}