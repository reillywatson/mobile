package com.vasken.helper.release;


public class Main {

	public static void main(String[] args) {
		String folder = args[0];
		String oldPkgName = args[1];
		String appName = args[2];
		String drawableFiles = args[3];
		String rawFiles = args[4];
		String newPkgName = args[5];
		String pubId = args[6];
		
		App app = new App(folder);
		app.setAppName(appName);
		app.addDrawableFiles(drawableFiles);
		app.addRawFiles(rawFiles);
		app.setAdmobPublisherId(pubId);
		app.setPkgName(oldPkgName, newPkgName);
		
		// OPTIONAL
		app.fixEclipseProject();
	}
	
	
}
