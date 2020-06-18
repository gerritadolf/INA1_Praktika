<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>RSS-Feed</title>
</head>
<body>
<h1>Feed-Reader</h1>
<%
	// Anzahl der Input-Felder
	Integer total = 0;

	// Wenn die Seite zum ersten Mal geladen wurde, ist der parameter null, wnen nicht, überschreibe total
	if(request.getParameter("total") != null) {
		total = Integer.parseInt(request.getParameter("total"));
	}
	// Setze total standardmäßig auf 3
	if(total == 0) {
		total = 3;
		request.setAttribute("total", total);
	}
	
	// Generiere Inputfelder
	// Der Name des Inputs ist die Id von 0 bis total-1. Diese dient zur späteren identifierung im Servlet
	out.println("<form action=\""+ request.getContextPath() + "/p4/LoadRss\">");
	for (int i = 0; i < total; i++) {
		
		String webContent = "";
		if(request.getParameter(Integer.toString(i)) != null) webContent = request.getParameter(Integer.toString(i));
		System.out.println(i +" " + webContent);
		out.println("<input name=\""+i+"\" value=\""+webContent+"\" placeholder=\"Website "+ i +"\"/>");
	}
	out.println("<input type=\"submit\" name=\"button\" value=\"Okay\">");
	out.println("</form>");
	
	// Setze die nächstgrößere Anzahl fest
	int addTotal = ++total;
%>
<br>
<br>
	<form action="">
	<input type="submit" name="buttonadd" value="Feld hinzufügen">
	<input type="hidden" name="total" value='<%=addTotal %>'>
	</form>
	
	<form action="">
	<input type="submit" name="buttonremove" value="Feld löschen">
	<input type="hidden" name="total" value='<%=--total %>'>
	</form>
</body>
</html>