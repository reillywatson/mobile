package com.vasken.music.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vasken.music.server.manager.MusicCatalogManager;
	
@SuppressWarnings("serial")
public class CatalogUpdaterServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		MusicCatalogManager theManager = MusicCatalogManager.sharedInstace();
		
		StringBuffer result = theManager.updateCatalog();

		System.out.println( result );
		
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		writer.println(result);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		doGet(req, resp);
	}
}
