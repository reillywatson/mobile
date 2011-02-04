package com.vasken.helper.release;


public class Songster {

	public static void main(String[] args) {
		String folder = args[0];
		String oldPkgName = args[1];
		String appName = args[2];
		String drawableFiles = args[3];
		String newPkgName = args[4];
		String pubId = args[5];
		String versionName = args[6];
		String versionNumber = args[7];
		String genre = args[8];
		
		App app = new App(folder);
		app.setAppName(appName);
		app.addDrawableFiles(drawableFiles);
		app.setAdmobPublisherId(pubId);
		app.setPkgName(oldPkgName, newPkgName);
		app.setVersionName(versionName);
		app.setVersionNumber(versionNumber);
		app.setStrings("genre", genre);
		
		// OPTIONAL
		app.fixEclipseProject();		
	}
	
	
}
