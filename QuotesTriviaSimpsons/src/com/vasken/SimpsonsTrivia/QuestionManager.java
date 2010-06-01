package com.vasken.SimpsonsTrivia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.util.Log;

public class QuestionManager {
	public static final String TRIVIA = "TRIVIA";
	public static final String EPISODE = "EPISODE";
	public static final String SPEAKER = "SPEAKER";

	private static final int SPEAKER_WEIGHT = 3;
	private static final int TRIVIA_WEIGHT = 20;
	private static final int EPISODE_WEIGHT = 5;
	
	private List<QuestionStore> questionStores = new ArrayList<QuestionStore>();
	private List<QuestionStore> disabledStores = new ArrayList<QuestionStore>();
	private HashMap<String, Integer> weights = new HashMap<String, Integer>();
	private static Random rand = new Random();
	
	private void addStore(QuestionStore store) {
		if (store.isAvailable())
			questionStores.add(store);
		else
			disabledStores.add(store);
	}
	
	public QuestionManager(Context context) throws IOException {
		addStore(new TriviaStore(context, R.raw.simpsons_trivia));
		addStore(new EpisodeStore(context, R.raw.the_simpsons));
		addStore(new SpeakerStore(context, R.raw.the_simpsons));
		weights.put(TRIVIA, TRIVIA_WEIGHT);
		weights.put(EPISODE, EPISODE_WEIGHT);
		weights.put(SPEAKER, SPEAKER_WEIGHT);
	}

	public void disableQuestionType(String type) {
		for (int i = 0; i < questionStores.size(); i++) {
			QuestionStore store = questionStores.get(i);
			if (store.storeName().equals(type)) {
				questionStores.remove(i);
				disabledStores.add(store);
				break;
			}
		}
	}

	public boolean enableQuestionType(String type) {
		for (int i = 0; i < disabledStores.size(); i++) {
			QuestionStore store = disabledStores.get(i);
			if (store.storeName().equals(type) && store.isAvailable()) {
				disabledStores.remove(i);
				questionStores.add(store);
				return true;
			}
		}
		return false;
	}
	
	public List<String> enabledTypes() {
		List<String> types = new ArrayList<String>();
		for (QuestionStore store : questionStores) {
			types.add(store.storeName());
		}
		return types;
	}
	
	public Question getQuestion() {
		List<Integer> probs = new ArrayList<Integer>();
		int total = 0;
		for (int i = 0; i < questionStores.size(); i++) {
			QuestionStore store = questionStores.get(i);
			int prob = store.numQuestions() * weights.get(store.storeName());
			probs.add(new Integer(total + prob));
			Log.d(getClass().getName(), "PROBABILITY FOR TYPE " + store.storeName() + Integer.toString(prob));
			total += prob;
		}
		int pick = rand.nextInt(total);
		for (int i = 0; i < probs.size(); i++) {
			if (pick <= probs.get(i).intValue()) {
				return questionStores.get(i).getQuestion();
			}
		}
		return null;
	}
}
