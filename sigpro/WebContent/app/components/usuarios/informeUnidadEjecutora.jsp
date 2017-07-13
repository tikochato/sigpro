<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<div ng-controller="informeUnidadController as controller" class="maincontainer all_page" id="title">
	<shiro:authenticated>
		<div class="panel panel-default">
			<div class="panel-heading"><h3>Informe Unidad Ejecutora</h3></div>
		</div>
			
		<div class="row second-main-form">
			<br>
				
	    </div>
	</shiro:authenticated>
	
</div>