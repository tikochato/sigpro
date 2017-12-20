<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="ganttController as ganttc" class="maincontainer all_page" id="title">
		
  		<script type="text/ng-template" id="buscarPorProyecto.jsp">
    		<%@ include file="/app/components/pep/buscarPorProyecto.jsp"%>
  	 	</script>
  	 	  		
	    <div class="panel panel-default">
	    	<div class="panel-heading"><h3>Gantt</h3></div>
	    </div>
	    
		
		<div class="subtitulo">
			{{ ganttc.objetoTipoNombre }} {{ ganttc.proyectoNombre }}
		</div>
		<div class="operation_buttons" align="right">
						<div class="btn-group">
							<label class="btn btn-default" ng-click="ganttc.pesoProducto(ganttc.proyectoid)" ng-hide="true"><span class="glyphicon glyphicon-sort-by-attributes" aria-hidden="true"
								uib-tooltip="Peso de productos"></span></label>
							<label class="btn btn-default" ng-click="ganttc.exportar()"><span class="glyphicon glyphicon-paste" aria-hidden="true"
								uib-tooltip="Exportar Gantt a Project"></span></label>
							<label class="btn btn-default" ng-click="ganttc.zoomAcercar()"><span class="glyphicon glyphicon-zoom-in" aria-hidden="true"
								uib-tooltip="Acercar vista"></span></label>
							<label class="btn btn-default" ng-click="ganttc.zoomAlejar()"><span class="glyphicon glyphicon-zoom-out" aria-hidden="true"
								uib-tooltip="Alejar vista"></span></label>
						</div>
					</div>
		<div class="row" align="center" style="height: 90%" id="reporte">	
				 <div align="center" id="gantt">
				    <div class="gantt-chart">
						<div ds:gantt-chart id="ganttChartView" items="items"  settings="settings" auto-refresh="{{ true }}" style="height: 400px;">
						</div>
						<div class="grid_loading" ng-hide="!ganttc.mostrarcargando" style="z-index: 100">
						  	<div class="msg">
						      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
								  <br /><br />
								  <b>Cargando, por favor espere...</b>
							  </span>
							</div>
						  </div>
			    	</div>
					<br/>
				</div>
		</div>
	</div>
