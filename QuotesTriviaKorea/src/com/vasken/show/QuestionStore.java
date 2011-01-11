package com.vasken.show;

public interface QuestionStore {
	String storeName();
	boolean isAvailable();
	int numQuestions();
	Question getQuestion();
}
