package com.vasken.SimpsonsTrivia;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class QuestionTypeManager {
	public static final String TRIVIA = "TRIVIA";
	public static final String EPISODE = "EPISODE";
	public static final String SPEAKER = "SPEAKER";

	private static final int SPEAKER_PERCENT = 25;
	private static final int TRIVIA_PERCENT = 30;
	private static final int EPISODE_PERCENT = 45;
	private static final int DEFAULT = -1;
	
	static QuestionType speakerQuestion;
	static QuestionType episodeQuestion;
	static QuestionType triviaQuestion;
	static List<QuestionType> questionTypes;
	static List<String> typeNames;
	static int numActiveQuestionTypes = 3;
	
	public QuestionTypeManager() {
		speakerQuestion = new QuestionType(SPEAKER, SPEAKER_PERCENT);
		episodeQuestion = new QuestionType(EPISODE, EPISODE_PERCENT);
		triviaQuestion = new QuestionType(TRIVIA, TRIVIA_PERCENT);
		
		questionTypes = new ArrayList<QuestionType>();
		questionTypes.add(speakerQuestion);
		questionTypes.add(episodeQuestion);
		questionTypes.add(triviaQuestion);
		
		typeNames = new ArrayList<String>();
		typeNames.add(SPEAKER);
		typeNames.add(TRIVIA);
		typeNames.add(EPISODE);
	}

	public static void disableQuestion(String type) {
		// Disable these types of Questions
		numActiveQuestionTypes -= 1;
		QuestionType questionToDisable = getQuestionType(type);

		int numTimesToDrain = numActiveQuestionTypes;
		for (int i=0; i<questionTypes.size(); i++) {
			QuestionType question = questionTypes.get(i);
			
			// Increase percent of active question types
			if (question.isEnabled() && !question.type.equals(type)){
				question.drainLikelyhood(questionToDisable, numTimesToDrain);
				numTimesToDrain --;
			}
		}
	}

	public static void enableQuestion(String type) {
		// Enable these types of Questions
		QuestionType theQuestion = getQuestionType(type);
		numActiveQuestionTypes += 1;
		theQuestion.setTypeLikelyhood(type, DEFAULT );
		
		for (QuestionType aQuestion : questionTypes) {
			if (aQuestion.isEnabled() && !aQuestion.type.equals(type)){
				aQuestion.setTypeLikelyhood(type, 0);
				
				List<String> questionTypesToSplit = getOtherQuestionTypes(aQuestion.type, theQuestion.type);
				for (String questionType : questionTypesToSplit) {
					theQuestion.drainTypeLikelyhood(aQuestion, questionType, numActiveQuestionTypes);
				}
			}
		}
	}
	
	private static List<String> getOtherQuestionTypes(String type, String type2) {
		List<String> result = new ArrayList<String>();
		for (String questionType: typeNames) {
			if (!questionType.equals(type) && !questionType.equals(type2)) {
				result.add(questionType);
			}
		}
		return result;
	}

	private static QuestionType getQuestionType(String type) {
		for (QuestionType question : questionTypes ) {
			if (question.type.equals(type))
				return question;
		}
		
		Log.d("QuestionType", "Couldn't find any questions for " + type);
		return null;
	}
	
	public class QuestionType {
		int speakerLikelyhood;
		int triviaLikelyhood;
		int episodeLikelyhood;
		String type;

		public QuestionType(String t, int likelyhood) {
			type = t;
			setTypeLikelyhood(t, likelyhood);
		}

		public void setTypeLikelyhood(String type, int percent) {
			if (type.equals(EPISODE)) {
				episodeLikelyhood = percent == DEFAULT ? EPISODE_PERCENT : percent;
			} else if (type.equals(TRIVIA)) {
				triviaLikelyhood = percent == DEFAULT ? TRIVIA_PERCENT : percent;
			} else if (type.equals(SPEAKER)) {
				speakerLikelyhood = percent == DEFAULT ? SPEAKER_PERCENT : percent;
			}
		}

		public int getTypeLikelyhood(String type) {
			if (type.equals(EPISODE)) {
				return episodeLikelyhood;
			} else if (type.equals(TRIVIA)) {
				return triviaLikelyhood;
			} else if (type.equals(SPEAKER)) {
				return speakerLikelyhood;
			}
			return 0;
		}

		public void drainTypeLikelyhood(QuestionType from, String qType, int numActive) {
			int amountToDrain = from.getTypeLikelyhood(qType)/numActive;
			
			setTypeLikelyhood(qType, this.getTypeLikelyhood(qType) + amountToDrain);
			from.setTypeLikelyhood(qType, from.getTypeLikelyhood(qType) - amountToDrain);
		}

		public void drainLikelyhood(QuestionType from, int numActive) {
			drainTypeLikelyhood(from, SPEAKER, numActive);
			drainTypeLikelyhood(from, TRIVIA, numActive);
			drainTypeLikelyhood(from, EPISODE, numActive);
		}

		public int likelyhood() {
			return speakerLikelyhood + triviaLikelyhood + episodeLikelyhood;
		}
		
		public boolean isEnabled() {
			return likelyhood() > 0;
		}

	}
}
