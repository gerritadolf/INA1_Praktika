package de.ina.p4;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class Check
 */
@WebServlet("/p4/LoadRss")
public class LoadRss extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadRss() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	LoadRssHelper ch;
		if(req.getSession().getAttribute("helper") == null) {
			ch = new LoadRssHelper(req, res);
		} else {
			ch = (LoadRssHelper) req.getSession().getAttribute("helper");
		}
		
		ch.doGet();
	}
}
