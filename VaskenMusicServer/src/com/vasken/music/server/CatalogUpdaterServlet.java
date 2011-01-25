package com.vasken.music.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.vasken.music.server.manager.MusicCatalogManager;
	
@SuppressWarnings("serial")
public class CatalogUpdaterServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		MusicCatalogManager theManager = MusicCatalogManager.sharedInstace();
		
		try {
			StringBuffer result = theManager.updateCatalog();
			System.out.println("CatalogUpdaterServlet has been executed.\n" + result);
		}catch(JSONException e) {
			System.err.println("Failed to parse response");
			e.printStackTrace();
		}
		
//		resp.setContentType("text/plain");
//		PrintWriter writer = resp.getWriter();
//		writer.println("CatalogUpdaterServlet has been executed.");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		doGet(req, resp);
	}
}
