package com.vasken.SimpsonsTrivia;

public interface QuestionStore {
	String storeName();
	boolean isAvailable();
	int numQuestions();
	Question getQuestion();
}
