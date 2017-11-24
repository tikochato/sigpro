<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" type="image/x-icon" href="/favicon.ico">
<%@ include file="/app/shared/includes.jsp"%>
<link rel="stylesheet" type="text/css" href="/assets/css/loginform.css" />
<script src="/app/components/login/login.controller.js"></script>
<title></title>
</head>
<body ng-app="sipro" ng-controller="loginController as login">
<div class="container container-fluid">
		<div class="row-fluid">
		<div class="" style="margin-top: -90px;">
			<img src="/assets/img/marcas/Logo_BID.png" alt="BID" width="125" height="90">
		</div>
		<form class="login-form">
			<div class="panel panel-default">
			  <div class="panel-heading" style="background-color: #1f3b6a; color: #ffffff;">{{ login.sistema_nombre }}</div>
			  <div class="panel-body">
			    <label for="user" class="sr-only">Usuario</label>
		        <input ng-model="login.username" ng-change="showerror=false" type="text" 
		        	id="inputUsername" class="form-control" placeholder="Usuario" required autofocus>
		        <label for="pass" class="sr-only">Contraseña</label>
		        <input ng-model="login.password" ng-change="showerror=false" type="password" 
		        	id="inputPassword" class="form-control" placeholder="Contraseña" required>
		        <br />
		        <div class="alert alert-danger text-center" role="alert" ng-show="showerror">Usuario y/o Contraseña incorrectos</div>
		        <button class="btn btn-lg btn-primary btn-block" ng-click="login.login()">Ingresar</button>
		      </div>
			</div>
		</form>
	    </div>
</div>
</body>
</html>