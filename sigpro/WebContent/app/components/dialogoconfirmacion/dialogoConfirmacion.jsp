<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>


     <div class="row second-main-form" style="width: 75%">
        <h3>
			{{dialogoCtrl.titulo}}
		</h3>
		<br>
        <p>{{dialogoCtrl.textoCuerpo}}</p>
        <br>
        <div class="form-group" align="right">
	        <button class="btn btn-primary" type="button" ng-click="dialogoCtrl.aceptar()">{{dialogoCtrl.textoBotonOk}}</button>
	        <button class="btn btn-default" type="button" ng-click="dialogoCtrl.cancelar()">{{dialogoCtrl.textoBotonCancelar}}</button>
        </div>
    </div>
