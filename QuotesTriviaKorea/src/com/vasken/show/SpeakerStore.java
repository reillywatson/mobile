package com.vasken.show;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.util.Log;

// SpeakerStore: we sell speakers galore!
public class SpeakerStore extends EpisodeStore {
	private static String SPEAKER_TEMPLATE = "<span style='color: white'><span><b>Who said it?</b></span><p><div style='margin-left: -40px'>%1$s</div></span>";
	private static Pattern speakerPattern =  Pattern.compile("<b>(.*?)</b>", Pattern.DOTALL);
	private Set<String> speakers;
	
	public SpeakerStore(Context context, int resID) throws IOException {
		super(context, resID);
	}
	
	protected Quote createQuote(String season, String epTitle, String quote) {
		Quote sq = null;
		String speaker = speakerOfQuote(quote);
		if (speaker != null) {
			if (speakers == null)
				speakers = new TreeSet<String>();
			speakers.add(speaker);
			quote = removeSpeaker(quote);
			sq = new Quote(season, epTitle, quote, speaker);
		}
		return sq;
	}
	
	public boolean isAvailable() {
		 return super.isAvailable() && speakers.size() > 3;
	}
	
	protected String answerForQuote(Quote quote) {
		return quote.speaker;
	}
	
	protected String questionForQuote(Quote quote) {
		return String.format(SPEAKER_TEMPLATE, quote.quote);
	}
	
	private String speakerOfQuote(String quote) {
		Matcher m = null;
		m = speakerPattern.matcher(quote);
		
		if (m.find()) {
			String speaker = m.group(1).trim();
			if (!m.find()) {
				if (speaker.endsWith(":")) {
					speaker = speaker.substring(0, speaker.length() - 1).trim();
				}
				if (speaker.length() > 0) {
					if (speaker.contains("<") || speaker.contains(">"))
						Log.e(SpeakerStore.class.toString(), "DON'T SHIP IT!! The quotes file is fucked up: " + speaker);
					return speaker;
				}
					
			}
		}
		return null;
	}
	
	private String removeSpeaker(String quote) {
		int slashB = quote.indexOf("</b>");
		String noSpeaker = quote.substring(slashB + 5, quote.length()).trim();
		if (noSpeaker.startsWith(":"))
			noSpeaker = noSpeaker.replaceFirst(":", "").trim();
		return "<dl><dd>" + noSpeaker;
	}

	@Override
	public String storeName() {
		return QuestionManager.SPEAKER;
	}
}