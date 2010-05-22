package com.vasken.helper.release;

import java.io.File;

public class Main {

	public static void main(String[] args) {
		String folder = args[0];
		String appName = args[1];
		File icon = new File(args[2]);
		String oldPkgName = args[3];
		String newPkgName = args[4];
		String pubId = args[5];
		String versionName = args[6];
		String versionNumber = args[7];
		
		App app = AppMaker.create(folder);
		app.setAppName(appName);
		if (icon.exists()) {
			app.setAppIcon(icon);
		}else {
			System.err.println("Icon " + icon + " doesn't exist");
		}
		app.setAdmobPublisherId(pubId);
		app.setVersionName(versionName);
		app.setVersionNumber(versionNumber);
		
		app.setPkgName(oldPkgName, newPkgName);
		
	}
	
	
}
