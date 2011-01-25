package com.vasken.music.server.manager;

import java.util.ArrayList;
import java.util.List;

import com.vasken.music.server.model.Song;

public class MusicCatalogManager {
	private static MusicCatalogManager INSTANCE = null;

	public static MusicCatalogManager sharedInstace() {
		if (INSTANCE == null) {
			INSTANCE = new MusicCatalogManager();
		}
		return INSTANCE;
	}
	
	public void updateCatalog() {
		// TODO
	}
	
	public List<Song> getSongCatalog() {
		List<Song> result = new ArrayList<Song>();
		// TODO
		
		return result;
	}

	private MusicCatalogManager(){}
}
