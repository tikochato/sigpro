<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="ganttController as ganttc" class="maincontainer all_page" id="title">
	    <div class="panel panel-default">
	    	<div class="panel-heading"><h3>Gantt</h3></div>
	    </div>
		<div class="subtitulo">
			{{ ganttc.objetoTipoNombre }} {{ ganttc.proyectoNombre }}
		</div>
	    <div class="row" align="center" >
		    <div class="operation_buttons" align="right">
		    <form>
		    	
				<div class="btn-group">
					<label class="btn btn-primary" ng-click="ganttc.exportar()"><span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"
						uib-tooltip="Exportar"> Exportar</span></label>
					<label class="btn btn-default" ng-click="ganttc.zoomAcercar()"><span class="glyphicon glyphicon-zoom-in" aria-hidden="true"
						uib-tooltip="Acercar vista"></span></label>
					<label class="btn btn-default" ng-click="ganttc.zoomAlejar()"><span class="glyphicon glyphicon-zoom-out" aria-hidden="true"
						uib-tooltip="Alejar vista"></span></label>
				</div>
				
			</form>
			</div>
		</div>
		 <div class="row" align="center" >
		 
		    <div class="gantt-chart">
				<div ds:gantt-chart id="ganttChartView" items="items" settings="settings" auto-refresh="{{ true }}" style="min-height: 400px"></div>
	    	</div>
			<br/>
		</div>
	</div>
