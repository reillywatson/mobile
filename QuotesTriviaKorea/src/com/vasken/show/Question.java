package com.vasken.show;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable{

	/**
	 * I've never actually gotten around to figuring out why I would need this...
	 */
	private static final long serialVersionUID = -834114564356957602L;
	
	String question;
	String correctAnswer;
	List<String> answers;
	
	public Question() {
		answers = new ArrayList<String>();
	}
}
