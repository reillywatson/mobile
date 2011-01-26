package com.vasken.music.server.model;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Song {
	@PrimaryKey
    @Persistent
	private String title;

    @Persistent
	private String link;

    @Persistent
	private String genre;
    
    @Persistent
    private Date dateAdded;
	
    public Song(String title, String link, String genre, Date dateAdded) {
		this.title = title;
		this.link = link;
		this.genre = genre;
		this.setDateAdded(dateAdded);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Date getDateAdded() {
		return dateAdded;
	}
	
	public String toJSONString() {
		return "{ title: \""+this.title+"\", link: \""+ this.link +"\" }";
	}
}
