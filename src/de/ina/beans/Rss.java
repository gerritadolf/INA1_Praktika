package de.ina.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
public class Rss {
	private int Id;
	private String Title;
	private String Description;
	private String Link;
	
	public Rss(String title, String description, String link) {
		Link = link;
		Title = title;
		Description = description;
	}
	
	@javax.persistence.Id
	@GeneratedValue
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public void setLink(String link) {
		Link = link;
	}
	
	public String getTitle() {
		return Title;
	}
	public String getDescription() {
		return Description;
	}
	public String getLink() {
		return Link;
	}
}
