<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="ganttController as ganttc" class="maincontainer all_page" id="title">
	    <h3>Gantt</h3><br/>
		<ds:gantt-chart id="ganttChartView" items="ganttc.items" settings="ganttc.settings" auto-refresh="{{ true }}">
    	</ds:gantt-chart>
	</div>
