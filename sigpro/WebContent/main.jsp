<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" type="image/x-icon" href="/favicon.ico">
<%@ include file="/app/shared/includes.jsp"%>

<script src="/app/components/main/main.controller.jsp"></script>
<title></title>
</head>
<body ng-app="sipro" ng-controller="MainController as mainController">
<%@ include file="/app/components/menu/menu.jsp" %>
	<div id="mainview" class="all_page">
		<div ng-view class="all_page"></div>
    </div>
    <div class="div_alertas">
		<flash-message name="alertas">
		</flash-message>
	</div>
	<div class="footer"><div style="text-align: center; width: 100%;" class="label-form">- MINFIN 2017 -</div></div>
</body>
</html>

