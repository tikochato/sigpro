<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/app/shared/includes_public.jsp"%>

<script src="/app/components/public/public.controller.js"></script>
<title>MINFIN - Sistema de Seguimiento de Proyectos</title>
</head>
<body ng-app="sigpro" ng-controller="publicController as publicController">
	<div id="mainview">
		<div ng-view></div>
    </div>
    <div class="footer">- Minfin 2016 -</div>
</body>
</html>