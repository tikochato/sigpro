<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="ganttController as ganttc" class="maincontainer all_page" id="title">
	    <h3>Gantt</h3>
	    <br/>
	    <div class="operation_buttons" align="left" >
			<div class="btn-group">
				<label class="btn btn-default" ng-click="ganttc.cargar()">Cargar archivo</label>
			</div>
		</div>
	    <div class="gantt-chart">
			<div ds:gantt-chart id="ganttChartView" items="items" settings="settings" auto-refresh="{{ true }}"></div>
    	</div>
	</div>
