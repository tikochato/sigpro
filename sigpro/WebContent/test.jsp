<%@page import="utilities.Utils, org.joda.time.DateTime"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dao.UsuarioDAO" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	
	Integer days = Utils.getWorkingDays(new DateTime(2016,5,2,0,0,0,0), new DateTime(2017,10,16,0,0,0,0));
	out.print(days);
%>
</body>
</html>