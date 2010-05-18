package com.vasken.seinfeld;

import java.util.ArrayList;
import java.util.List;

public class Question {

	String question;
	String correctAnswer;
	List<String> answers;
	
	public Question() {
		answers = new ArrayList<String>();
	}
}
