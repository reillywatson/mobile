package com.vasken.SimpsonsTrivia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.util.Log;

public class TriviaStore {
	private List<Question> questions = new ArrayList<Question>();
	private Random rand = new Random();
	
	public TriviaStore(Context context, int triviaId) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(triviaId)));
		String line = reader.readLine();
		while (line != null) {
			Question theQuestion = new Question();
			theQuestion.question = line;
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

	public boolean isAvailable() {
		return questions.size() > 0;
	}

	public Question getQuestion() {
		return questions.get(rand.nextInt(questions.size()));
	}

}
