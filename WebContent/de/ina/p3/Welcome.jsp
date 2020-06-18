<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Willkommen <%
	String name = request.getParameter("name");
	String website1 = request.getParameter("website1");
	String website2 = request.getParameter("website2");
	if (name == "Keine Angabe")
		name = "Unbekannter";
	out.println(name);
%>
</title>
</head>
<body>
<h1>Willkommen, <%
	out.println(name);
%></h1>
<p><%
	out.println(website1);
%></p>
<p><%
	out.println(website2);
%></p>
</body>
</html>