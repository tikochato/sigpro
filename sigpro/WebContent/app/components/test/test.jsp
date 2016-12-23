<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="testController as testc" class="maincontainer all_page" id="title">
		<h3>Test</h3><br/>
		<div class="btn-group">
			<label class="btn btn-success" ng-click="testc.irAMeta(1,1)">Metas de Proyecto 1</label>
		</div>
	</div>
