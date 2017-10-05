<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="dao.ProyectoDAO" %>
<html>
<head>
</head>
<body>
	<% ProyectoDAO.calcularTreepath(1);
	ProyectoDAO.calcularTreepath(2);
	ProyectoDAO.calcularTreepath(3);
	ProyectoDAO.calcularTreepath(4);
	ProyectoDAO.calcularTreepath(5);
	ProyectoDAO.calcularTreepath(6);%>	
</body>
</html>
