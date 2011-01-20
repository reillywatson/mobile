package com.vasken.music.server;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.com.google.common.base.Pair;
import com.vasken.music.server.manager.HighScoreManager;
import com.vasken.music.server.model.HighScoreEntry;
	
@SuppressWarnings("serial")
public class VaskenMusicServerServlet extends HttpServlet {

	private static final String NAME = "name";
	private static final String SCORE = "score";
	private static final String GENRE = "genre";
	private static final String VERSION = "version";
	private static final String HASH = "id";
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		String genre = req.getParameter(GENRE);

		HighScoreManager theManager = HighScoreManager.sharedInstace();
		StringBuilder response = getJSONResponse(theManager, genre);
		
		resp.setContentType("application/json");
		resp.getWriter().print(response);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		StringBuilder response;
		try {
			HighScoreManager theManager = HighScoreManager.sharedInstace();
			
			String name = req.getParameter(NAME);
			String score = req.getParameter(SCORE);
			String genre = req.getParameter(GENRE);
			String version = req.getParameter(VERSION);
			String hash = req.getParameter(HASH);
			HighScoreEntry entry = theManager.generateHighScoreEntry(name, score, genre, version, hash);

			theManager.checkIfHighScore(entry);
			
			if (entry.isHighScoreToday() || entry.isHighScoreEver()) {
				theManager.save(entry);
			}
			
			response = getJSONResponse(theManager, entry.getGenre());
			
		} catch (Exception e){
			response = new StringBuilder();
			if (e.getMessage() != null && e.getMessage().equals(HighScoreManager.DECRYPTION_FAILED_EXCEPTION)) {
				response.append("Error { message: "+HighScoreManager.DECRYPTION_FAILED_EXCEPTION+" }"); 
			} else if (e.getMessage() != null && e.getMessage().equals(HighScoreManager.INVALID_DATA_EXCEPTION)) {
				response.append("Error { message: "+HighScoreManager.INVALID_DATA_EXCEPTION+" }"); 
			} else if (e.getMessage() != null && e.getMessage().equals(HighScoreManager.MISSING_DATA_EXCEPTION)) {
				response.append("Error { message: "+HighScoreManager.MISSING_DATA_EXCEPTION+" }"); 
			} else {
				response.append("Error { message: "+e.getMessage()+" }"); 
				e.printStackTrace();
			}
		}
		
		resp.setContentType("application/json");
		resp.getWriter().println(response);
		
	}
	
	private StringBuilder getJSONResponse(HighScoreManager theManager, String genre) {
		Pair<List<HighScoreEntry>, List<HighScoreEntry>> highScores = theManager.getHighScores(genre);
		
		StringBuilder sb = new StringBuilder();
		sb.append("{ \"HighScores\" : { \n");
		sb.append("\t\"Today\" : [ \n");
		for (HighScoreEntry anEntry : highScores.first) {
			sb.append("\t\t{ \n");
			sb.append("\t\t\t\"name\": \""+anEntry.getName()+"\",\n");
			sb.append("\t\t\t\"score\": \""+anEntry.getScore()+"\"\n");
			sb.append("\t\t},\n");
		}
		sb.deleteCharAt(sb.length()-2);
		sb.append("\t], \n");
		
		sb.append("\t\"Ever\" : [\n");
		for (HighScoreEntry anEntry : highScores.second) {
			sb.append("\t\t{ \n");
			sb.append("\t\t\t\"name\": \""+anEntry.getName()+"\",\n");
			sb.append("\t\t\t\"score\": \""+anEntry.getScore()+"\"\n");
			sb.append("\t\t},\n");
		}
		sb.deleteCharAt(sb.length()-2);
		sb.append("\t]\n");
		sb.append("\t}\n");
		sb.append("}\n");
		
		return sb;
	}
}
