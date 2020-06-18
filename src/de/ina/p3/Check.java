package de.ina.p3;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class Check
 */
@WebServlet("/p3/Check")
public class Check extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Check() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		CheckHelper ch;
		if(req.getSession().getAttribute("helper") == null) {
			ch = new CheckHelper(req, res);
		} else {
			ch = (CheckHelper) req.getSession().getAttribute("helper");
		}
		
		ch.doGet();
	}
}
