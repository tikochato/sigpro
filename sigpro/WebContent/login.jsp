<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app="loginController">
<!-- <html ng-app="sigpro">  -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/app/shared/includes.jsp"%>
<link rel="stylesheet" type="text/css" href="/assets/css/loginform.css" />
<script src="/app/components/login/login.controller.js"></script>
<title>Sigpro</title>
</head>
<body ng-controller="loginController as login">
<div class="container container-fluid">
		<div class="row-fluid">
		<form class="login-form">
			<div class="panel panel-default">
			  <div class="panel-heading">Sigpro</div>
			  <div class="panel-body">
			    <label for="user" class="sr-only">Usuario</label>
		        <input ng-model="login.username" ng-change="showerror=false" type="text" 
		        	id="inputUsername" class="form-control" placeholder="Usuario" required autofocus>
		        <label for="pass" class="sr-only">Contraseña</label>
		        <input ng-model="login.password" ng-change="showerror=false" type="password" 
		        	id="inputPassword" class="form-control" placeholder="Contraseña" required>
		        <br />
		        <div class="alert alert-danger text-center" role="alert" ng-show="showerror">Usario y/o Contraseña incorrectos</div>
		        <button class="btn btn-lg btn-primary btn-block" ng-click="login.login()">Ingresar</button>
		      </div>
			</div>
		</form>
	    </div>
</div> <!-- /container -->
</body>
</html>