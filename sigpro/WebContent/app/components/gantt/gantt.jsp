<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="ganttController as ganttc" class="maincontainer all_page" id="title">
	    <h3>Gantt</h3>
	    <div class="operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-primary" ng-click="ganttc.cargar()"><span class="glyphicon glyphicon glyphicon-import" aria-hidden="true"></span></label>
				<label class="btn btn-primary" ng-click="ganttc.exportar()"><span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
				<label class="btn btn-primary" ng-click="ganttc.zoomAcercar()"><span class="glyphicon glyphicon-zoom-in" aria-hidden="true"></span></label>
				<label class="btn btn-primary" ng-click="ganttc.zoomAlejar()"><span class="glyphicon glyphicon-zoom-out" aria-hidden="true"></span></label>
			</div>
		</div>
	    <div class="gantt-chart">
			<div ds:gantt-chart id="ganttChartView" items="items" settings="settings" auto-refresh="{{ true }}" style="min-height: 240px"></div>
    	</div>
	</div>
