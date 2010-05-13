package com.vasken.comics;

import java.util.concurrent.Callable;

import com.vasken.comics.Downloaders.Downloader;

public class ComicInfo implements Comparable<ComicInfo> {
	String name;
	String startUrl;
	Callable<Downloader> downloaderConstructor;
	public ComicInfo(String name, String startUrl, Callable<Downloader> constructor) {
		this.name = name; this.startUrl = startUrl; this.downloaderConstructor = constructor;
	}
	@Override
	public int compareTo(ComicInfo another) {
		return name.compareTo(another.name);
	}
	@Override
	public String toString() {
		return name;
	}
}
