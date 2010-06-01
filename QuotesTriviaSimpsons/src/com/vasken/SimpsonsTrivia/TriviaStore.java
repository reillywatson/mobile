package com.vasken.SimpsonsTrivia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.util.Log;

public class TriviaStore implements QuestionStore {
	protected List<Question> questions = new ArrayList<Question>();
	protected static Random rand = new Random();
	private static String TRIVIA_TEMPLATE = "<span style='color: white'><b>%1$s</b></span>";
			
	public boolean isAvailable() {
		return !questions.isEmpty();
	}

	public int numQuestions() {
		return questions.size();
	}
	
	public Question getQuestion() {
		return questions.get(rand.nextInt(questions.size()));
	}
	
	public String storeName() {
		return QuestionManager.TRIVIA;
	}
	
	public TriviaStore(Context context, int triviaId) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(triviaId)));
		String line = reader.readLine();
		while (line != null) {
			Question theQuestion = new Question();
			theQuestion.question = String.format(TRIVIA_TEMPLATE, line);
			for (int i=0; i<3; i++){
				String answer = reader.readLine();
				if (answer.startsWith("* ")) {
					answer = answer.replace("* ", "");
					theQuestion.correctAnswer = answer;
				}
				theQuestion.answers.add(answer);
			}
			questions.add(theQuestion);
			line = reader.readLine();
		}
		Log.d(getClass().getName(), "Found " + Integer.toString(questions.size()) + " quotes");
	}
}
