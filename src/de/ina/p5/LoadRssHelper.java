package de.ina.p5;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.ina.beans.Rss;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class LoadRssHelper extends HelperBase{
	protected ArrayList<Rss> rssFeedList;
	protected PersistenceUtil<Rss> pu;
	protected LoadRssHelper(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		pu = new PersistenceUtil<Rss>();
	}
	
	public void doGet() {
		super.doGet();
		
		ArrayList<Rss> rssFeedList = new ArrayList<Rss>();
		ArrayList<String> urlList = new ArrayList<String>();
		
		// Total ist standardmäßig 3
		int total = 3;
		
		// Überschreibe Total, falls vorhanden
		if(request.getParameter("total") != null) {
			total = Integer.parseInt(request.getParameter("total"));
		}
		
		// Hole jede Url über die id, die als Parameter angehangen wurde
		for(int i = 0; i< total; i++ ) {
			urlList.add(request.getParameter(Integer.toString(i)));
		}
		
		// Lade jeden RSS-Feed der URL
		for(String url: urlList) {
			rssFeedList.addAll(GetRssFeed(url));
		}
		
		
		// Gebe die Feeds aus.
		response.setContentType("text/html");
		try {
			PrintWriter out = response.getWriter();
			out.println("<html><body>");
			// No Rss feed List was valid
			if(rssFeedList.size() == 0) {
				out.println("<h1>Fehler</h1>");
				out.println("<p>Es wurde kein RSS-Feed gefunden</p>");
			} else {
				out.println("<h1>Dein Feed</h1>");
				for(Rss item: rssFeedList) {
					
					out.println("<a href=\""+item.getLink()+"\" target=\"_blank\">"+item.getTitle()+"</h1>");
					out.println("<br>");
					out.println("<br>");
				}
			}
			out.println("</body></html>");
			out.close();
			
		} catch(IOException ex) {
			
		}
		
	}
	
	private ArrayList<Rss> GetRssFeed(String name) {
		String successfulUrl = name;
        Document xml;
        ArrayList<Rss> returnList = new ArrayList<Rss>();
        
		if(name == null || name == "") {
			return returnList;
		}
		 // Check for Http in URL
        if(!name.contains("http")) {
            name = "https://" + name;
        }

        // First try: Use base Url
        xml = GetXmlText(name);
        int tries = 0, abort = 0;

        // If XML is null, the base url does not work
        while(xml == null && abort == 0) {
            tries++;
            switch (tries) {
                // Check for the path url/feed
                case 1: {
                    successfulUrl = name + "/feed";
                    xml = GetXmlText(successfulUrl);
                    break;
                }
                // Check for the path url/rss
                case 2: {
                    successfulUrl = name + "/rss";
                    xml = GetXmlText(successfulUrl);
                    break;
                }
                // Check for the path url/feeds/posts/default
                case 3: {
                    successfulUrl = name + "/feeds/posts/default";
                    xml = GetXmlText(successfulUrl);
                    break;
                }
                // check for trivial /index.rss
                case 4: {
                    successfulUrl = name + "/index.rss";
                    xml = GetXmlText(successfulUrl);
                    break;
                }
                // Analyze Website Content of Landing Page
                case 5: {
                    successfulUrl = GetRssFeedOfWebsite(name);
                    if(successfulUrl == null) break;
                    xml= GetXmlText(successfulUrl);
                    break;
                }
                // Default case: No RSS Feed found.
                default:{
                    tries = 0;
                    abort = 1;
                    return returnList;
                }
            }
        }

        // when xml is still null, no suitable rss feed url was found
        if(xml == null) {
            return returnList;
        }

        // Put all items in NodeList
        xml.getDocumentElement().normalize();
        NodeList nodeList = xml.getElementsByTagName("item");

        // Iterate through items
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node item = nodeList.item(i);
            // Create new Rss Bean
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) item;
                // Fill Rss ResultList
                Rss rss = new Rss(eElement.getElementsByTagName("title").item(0).getTextContent(),
                		eElement.getElementsByTagName("description").item(0).getTextContent(), 
                		eElement.getElementsByTagName("link").item(0).getTextContent());
                returnList.add(rss);
                pu.saveOrUpdate(rss);
            }
        }
        
        return returnList;
	}
	
    private static Document GetXmlText(String link) {
    	try {
	        // Form url
	        URL url = new URL(link);
	
	        // Set up HTTP Context
	        HttpURLConnection connection =
	                (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");
	        connection.setRequestProperty("Accept", "application/xml");

        	// If errors occure, no suitible xml content found.
        
            InputStream xml = connection.getInputStream();
            // If response is not Ok, return null
            if(connection.getResponseCode() != 200) return null;

            // Build up DocumentBuilderFactory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
            	DocumentBuilder db = dbf.newDocumentBuilder();
            	try{
                    // Try parse document in XML. When an exception is thrown, the XML body is invalid
                    Document doc = db.parse(xml);
                    return doc;
                } catch (SAXException ex) {
                    return null;
                }
            } catch(ParserConfigurationException ex) {
            	return null;
            }
            
        } catch (IOException ex) {
            return null;
        }
    }

    private static String GetRssFeedOfWebsite(String link) {
        // Load and analyze HTML Content
        String body = null;
        try {
        	URL url = new URL(link);

            // Set up HTTP Context
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            InputStream input = connection.getInputStream();
            
            body = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))
            .lines()
            .collect(Collectors.joining("\n"));
       

            // Has Body an "application/rss+xml" link-tag
            if(body.contains("type=\"application/rss+xml\"")) {
                // Crop body until the url of rss feed is shown
                body = body.substring(body.indexOf("type=\"application/rss+xml\""));
                // Add 6 chars to crop the href=" part
                body = body.substring(body.indexOf("href=\"")+6);
                // Take the part to the next ". The result is the rss url
                body = body.substring(0,body.indexOf("\""));

                return body;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
