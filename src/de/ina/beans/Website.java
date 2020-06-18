package de.ina.beans;

public class Website {
	private String Name;
	private String Website1;
	private String Website2;
	
	public Website(String name, String website1, String website2) {
		Name = name;
		Website1 = website1;
		Website2 = website2;
	}
	public String getName() {
		return Name;
	}
	public String getWebsite1() {
		return Website1;
	}
	public String getWebsite2() {
		return Website2;
	}
	public void setName(String name) {
		Name = name;
	}
	public void setWebsite1(String website) {
		Website1 = website;
	}
	public void setWebsite2(String website) {
		Website2 = website;
	}
}
