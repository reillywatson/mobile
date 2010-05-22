package com.vasken.helper.release;

public class AppMaker {

	public static App create(String folder) {
		App theApp = new App(folder);
		
		return theApp;
	}

}
