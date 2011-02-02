package com.vasken.helper.release;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class App {
	private static final String SRC_FOLDER_NAME = "src";
	private static final String RES_FOLDER_NAME = "res";
	private static final String LAYOUT_FOLDER_NAME = "layout";
	private static final String DRAWABLE_FOLDER_NAME = "drawable";
	private static final String DRAWABLE_MDPI_FOLDER_NAME = "drawable-mdpi";
	private static final String RAW_FOLDER_NAME = "raw";
	private static final String VALUES_FOLDER_NAME = "values";
	private static final String STRINGS_FILE_NAME = "strings.xml";
	private static final String PROJECT_FILE_NAME = ".project";
	private final String MANIFEST_FILE_NAME = "AndroidManifest.xml";
	
	private String srcFolderName;
	private String resFolderName;
	private List<File> srcFiles;
	private List<File> layoutFiles;
	private File stringsXml;
	private File manifestFile;
	private File eclipseProjectFile;
	
	public App(String src) {
		File dir = new File(src);
		if (!dir.isDirectory()) {
			throw new RuntimeException(src + " is not an actual folder");
		}
		
		// Grab Java files
		srcFolderName = src + File.separatorChar + SRC_FOLDER_NAME;
		File srcRoot = new File(srcFolderName);
		srcFiles = new ArrayList<File>();
		populateFiles(srcRoot, "([\\w]*[_]*)+.java", srcFiles);
		
		// Grab layout files
		resFolderName = src + File.separatorChar + RES_FOLDER_NAME;
		layoutFiles = new ArrayList<File>();
		populateFiles(new File(resFolderName + File.separatorChar + LAYOUT_FOLDER_NAME), "([\\w]*[_]*)+.xml", layoutFiles);
		
		// Grab strings.xml files
		stringsXml = new File(resFolderName + File.separatorChar + VALUES_FOLDER_NAME  + File.separatorChar + STRINGS_FILE_NAME);

		// Grab manifest xml file
		manifestFile = new File(src + File.separatorChar + MANIFEST_FILE_NAME);
		
		// Set the projectFile
		eclipseProjectFile = new File(src + File.separatorChar + PROJECT_FILE_NAME);
	}

	public void addDrawableFiles(String drawableFiles) {
		if (new File(resFolderName + File.separatorChar + DRAWABLE_FOLDER_NAME).exists()) {
			addFiles(drawableFiles, resFolderName + File.separatorChar + DRAWABLE_FOLDER_NAME);
		} else {
			addFiles(drawableFiles, resFolderName + File.separatorChar + DRAWABLE_MDPI_FOLDER_NAME);
		}
	}

	public void addRawFiles(String rawFiles) {
		addFiles(rawFiles, resFolderName + File.separatorChar + RAW_FOLDER_NAME);
	}

	public void setAppName(String appName) {
		replaceStringInFile(stringsXml, 
				"<string name=\"app_name\">(.*?)</string>", 
				"<string name=\"app_name\">"+appName+"</string>");
	}

	public void setAdmobPublisherId(String pubId) {
		replaceStringInFile(manifestFile, 
				"<meta-data android:value=\"(.*?)\" android:name=\"ADMOB_PUBLISHER_ID\"", 
				"<meta-data android:value=\""+pubId+"\" android:name=\"ADMOB_PUBLISHER_ID\"");
	}

	public void setPkgName(String oldPackage, String newPackage) {
		// Replace it in the manifest file
		replaceStringInFile(manifestFile, oldPackage, newPackage);
		
		// Replace it inside the xml layouts
		for (File file : layoutFiles) {
			replaceStringInFile(file, oldPackage, newPackage);
		}
		
		// Replace it inside the actual java code
		for (File file : srcFiles) {
			replaceStringInFile(file, oldPackage, newPackage);
			
			String oldPath = oldPackage.replace('.', '\\');
			String newPath = newPackage.replace('.', '\\');
			
			File destination = new File(file.getAbsolutePath().replace(oldPath, newPath));
			destination.getParentFile().mkdirs();
			file.renameTo(destination);
			file.getParentFile().deleteOnExit();
		}
		
		// Move the Java classes
		for (File file : layoutFiles) {			
			replaceStringInFile(file, oldPackage, newPackage);
		}
		
	}

	public void setVersionName(String versionName) {
		replaceStringInFile(manifestFile, 
				"android:versionName=\"(.*?)\"",
				"android:versionName=\""+versionName+"\"");
	}

	public void setVersionNumber(String versionNumber) {
		replaceStringInFile(manifestFile, 
				"android:versionCode=\"(.*?)\"",
				"android:versionCode=\""+versionNumber+"\"");
	}

	public void fixEclipseProject() {
		if (eclipseProjectFile.exists()) {
			String newName = eclipseProjectFile.getParentFile().getName();
			replaceStringInFile(eclipseProjectFile, 
					"<projectDescription>[\\s]*<name>(.*?)</name>[\\s]*<comment>", 
					"<projectDescription>\n\t<name>"+newName+"</name>\n\t<comment>");
		}
	}

	public void setStrings(String key, String value) {
		replaceStringInFile(stringsXml, 
				"<string name=\""+key+"\">(.*?)</string>", 
				"<string name=\""+key+"\">"+value+"</string>");
	}

	/****************************************************************/
	/************************ HELPERS *******************************/
	/****************************************************************/
	private void populateFiles(File srcRoot, final String regex, List<File> result) {		
		FilenameFilter fileNameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				boolean isAcceptable = (new File(dir + File.separator + name).isDirectory() && !name.startsWith(".")) || name.matches(regex);
				return isAcceptable;
			}
		};
		
		File[] files = srcRoot.listFiles(fileNameFilter);
		for (File file : files) {
			if (file.isDirectory()) {
				populateFiles(file, regex, result);
			}else {
				result.add(file);
			}
		}
	}
	
	private boolean copy(File src, File dst) {
		boolean worked = true;
		try {
			InputStream in = new FileInputStream(src);
		    OutputStream out = new FileOutputStream(dst);

		    // Transfer bytes from in to out
		    byte[] buf = new byte[1024];
		    int len;
		    while ((len = in.read(buf)) > 0) {
		        out.write(buf, 0, len);
		    }
		    in.close();
		    out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			worked = false;
		}
		
		return worked;
	}

	private void replaceStringInFile(File theFile, String srcRegex, String desired) {
		try {
			StringBuilder contents = new StringBuilder();
			
			BufferedReader reader = new BufferedReader(new FileReader(theFile));
			String line = reader.readLine();
			while(line != null) {
				contents.append(line + System.getProperty("line.separator"));
				line = reader.readLine();
			}
			reader.close();
			
			String contentsString;
			contentsString = contents.toString().replaceAll(srcRegex, desired);
			
			FileWriter writer = new FileWriter(theFile);
			writer.append(contentsString);
			writer.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addFiles(String files, String basePath) {
		String[] fileNames = files.split(",");
		for(String fileName : fileNames) {
			File drawable = new File(fileName.trim());
			if (drawable.exists()) {				
				boolean worked = copy(drawable, new File(basePath + File.separatorChar + drawable.getName()));
				if (!worked) {
					System.err.println("File " + drawable.getAbsolutePath() + " couldn't be copied");
				}
			}else {
				System.err.println("File " + drawable.getAbsolutePath() + " doesn't exist");
			}
		}
	}
}
