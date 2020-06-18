package de.ina.p3;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.ina.beans.Website;

public class CheckHelper extends HelperBase{
	protected Website inputData;
	protected CheckHelper(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
	}
	
	public void doGet() {
		// Führe die Funktion der HelperBase aus
		super.doGet();
		
		// Schaue, ob inputData gesetzt ist und setze es wenn nicht
		if(request.getSession().getAttribute("inputData") == null) {
			request.getSession().setAttribute("inputData", inputData);
		}
		
		// Validierungsvariablen für Website 1 und 2
		Boolean isWebsite1Valid = false;
		Boolean isWebsite2Valid = false;
		
		// Bean mit Daten füllen
		inputData = new Website(request.getParameter("name"), request.getParameter("website1"), request.getParameter("website2"));
		
		// Website 1 validieren
		try{
			new URL(inputData.getWebsite1()).toURI();
			isWebsite1Valid = true;
		} catch (URISyntaxException ex) {
			isWebsite1Valid = false;
		} catch (MalformedURLException ex) {
			isWebsite1Valid = false;
		}
		
		// Website 2 validieren
		try{
			new URL(inputData.getWebsite2()).toURI();
			isWebsite2Valid = true;
		} catch (URISyntaxException ex) {
			isWebsite2Valid = false;
		} catch (MalformedURLException ex) {
			isWebsite2Valid = false;
		}
		
		// Wenn Name nicht gesetzt, "Keine Angabe" setzen
		if(inputData.getName() == "") {
			inputData.setName("Keine Angabe");
		}
		
		// Mache einen Redirect, abhängig von der Validierung
		if(!isWebsite1Valid || !isWebsite2Valid) {
			String websitePath = "/firstTomcatApplication/p3/?";
			if(!isWebsite1Valid) websitePath += "website1=" + inputData.getWebsite1();
			if(!isWebsite2Valid) websitePath += "&website2=" + inputData.getWebsite2();
			
			response.setStatus(301);
			response.setHeader("Location", websitePath);
			response.setHeader("Connection", "close");
		} else {
			response.setStatus(301);
			response.setHeader("Location", "Welcome?name=" + inputData.getName() + "&website1=" + inputData.getWebsite1() + "&website2=" + inputData.getWebsite2());
			response.setHeader("Connection", "close");
		}
	}
	
}
