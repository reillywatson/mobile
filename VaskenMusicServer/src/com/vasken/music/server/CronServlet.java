package com.vasken.music.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vasken.music.server.manager.HighScoreManager;
	
@SuppressWarnings("serial")
public class CronServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		HighScoreManager theManager = new HighScoreManager();
		long entriesRemoved = theManager.removeOldHighscores();
		
		System.out.println("Cron Job has been executed and removed " + entriesRemoved + " entries");

		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		writer.println("Cron Job has been executed and removed " + entriesRemoved + " entries");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		doGet(req, resp);
	}
}
