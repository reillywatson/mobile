package com.vasken.songstar.rock;

import java.util.Collection;
import java.util.Iterator;

import net.roarsoftware.lastfm.Geo;
import net.roarsoftware.lastfm.Tag;
import net.roarsoftware.lastfm.Track;

public class LastFMDataProvider {

	static final String API_KEY = "cfde9719a23088047f6125b5a68f04dd";
	// I don't know what this is for, I don't think we need it but it's here just in case
	// static final String API_SECRET = "839e547d2bce26f197d976a608d9b4cb";
	
	Collection<Track> getTopTracks() {
		return getTopTracksForTag("");
	}
	
	Collection<Track> getTopTracksForTag(String tag) {
		return Tag.getTopTracks(tag, API_KEY);
	}
	
	Collection<Track> getSimilarTracks(Track track) {
		if (track == null)
			return null;
		return Track.getSimilar(track.getArtist(), track.getName(), track.getMbid(), API_KEY);
	}
	
	Track getRandomTopTrack(int cutoff) {
		return selectRandom(Geo.getTopTracks("United States", API_KEY), cutoff);
	}
	
	Track selectRandom(Collection<Track> trackList, int cutoff) {
		if (trackList == null)
			return null;
		int trackNo = (int)(Math.random() * Math.min(cutoff, trackList.size()));
		Track track = null;
		Iterator<Track> itr = trackList.iterator();
		for (int i = 0; i < trackNo; i++)
			track = itr.next();
		return track;
		
	}
	
	Track getRandomTrackForTag(String tag, int cutoff) {
		return selectRandom(getTopTracksForTag(tag), cutoff);
	}
}
