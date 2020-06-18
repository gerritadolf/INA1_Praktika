package de.ina.p3;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelperBase {
	public HttpServletRequest request;
	public HttpServletResponse response;
	public HelperBase(HttpServletRequest req, HttpServletResponse res) {
		request = req;
		response = res;
	}
	
	public void doGet() {
		if(request.getSession().getAttribute("helper") == null) {
			request.getSession().setAttribute("helper", this);
		}
	}
}
