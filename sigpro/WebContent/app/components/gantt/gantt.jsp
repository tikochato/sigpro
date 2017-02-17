<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="ganttController as ganttc" class="maincontainer all_page" id="title">
	    <h3>Gantt</h3>
	    <br/>
	    <div class="gantt-chart">
			<div ds:gantt-chart id="ganttChartView" items="items" settings="settings" auto-refresh="{{ true }}"></div>
    	</div>
	</div>
