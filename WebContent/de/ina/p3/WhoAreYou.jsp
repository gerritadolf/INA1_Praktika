<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Seite eingeben</title>
</head>
<body>
<h1>Seite eingeben</h1>
<%
	// Parameter holen
	String website1 = request.getParameter("website1");
	String website2 = request.getParameter("website2");
	// Wenn der Parameter nicht null ist, wird die Fehlermeldung angezeigt
	if(website1 != null)
		out.println("<p>\""+website1+"\" ist nicht zulässig.</p>");
	else
		// Setze den Namen auf einen leeren String, damit im Inputfeld der Placeholder gezeigt wird
		website1 = "";
	
	if(website2 != null)
		out.println("<p>\""+website2+"\" ist nicht zulässig.</p>");
	else
		website2 = "";
%>
<form action="${pageContext.request.contextPath}/p3/Check">
<input type="text" name="name" placeholder="Dein Name">
<input type="text" name="website1" placeholder="Website 1">
<input type="text" name="website2" placeholder="Website 2">
<input type="submit" name="buttom" value="Okay">
</form>
</body>
</html>