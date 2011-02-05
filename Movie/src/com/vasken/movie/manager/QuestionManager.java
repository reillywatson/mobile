package com.vasken.movie.manager;

import java.util.ArrayList;

import com.vasken.movie.model.Question;

public class QuestionManager {
	private static QuestionManager INSTANCE;
	
	public Question getNextQuestion() {
		Question stub = new Question(0, "", new ArrayList<String>(), "");
		
		return stub;
	}

	public static QuestionManager sharedInstance() {
		if (INSTANCE == null) { 
			INSTANCE = new QuestionManager();
		}
		return INSTANCE;
	}

}
