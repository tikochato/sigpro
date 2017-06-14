<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="ganttController as ganttc" class="maincontainer all_page" id="title">
		<script type="text/ng-template" id="editarPrestamo.jsp">
    		<%@ include file="/app/components/gantt/editarPrestamo.jsp"%>
  		</script>
  		<script type="text/ng-template" id="editarComponente.jsp">
    		<%@ include file="/app/components/gantt/editarComponente.jsp"%>
  		</script>
  		<script type="text/ng-template" id="editarProducto.jsp">
    		<%@ include file="/app/components/gantt/editarProducto.jsp"%>
  		</script>
  		<script type="text/ng-template" id="editarSubproducto.jsp">
    		<%@ include file="/app/components/gantt/editarSubproducto.jsp"%>
  		</script>
		<script type="text/ng-template" id="editarActividad.jsp">
    		<%@ include file="/app/components/gantt/editarActividad.jsp"%>
			
  		</script>
  		
  		<script type="text/ng-template" id="buscarPorProyecto.jsp">
    		<%@ include file="/app/components/prestamo/buscarPorProyecto.jsp"%>
  	 	</script>
  	 	
  	 	<script type="text/ng-template" id="pesoProducto.jsp">
    		<%@ include file="/app/components/gantt/pesoProducto.jsp"%>
  	 	</script>
  		
	    <div class="panel panel-default">
	    	<div class="panel-heading"><h3>Gantt</h3></div>
	    </div>
	    <div class="grid_loading" ng-hide="!ganttc.mostrarcargando">
		  	<div class="msg">
		      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
				  <br /><br />
				  <b>Cargando, por favor espere...</b>
			  </span>
			</div>
		</div>
		
		<div class="subtitulo">
			{{ ganttc.objetoTipoNombre }} {{ ganttc.proyectoNombre }}
		</div>
	    <div class="row" align="center" >
		    <div class="operation_buttons" align="right">
		    <form>
		    	
				<div class="btn-group">
					<label class="btn btn-default" ng-click="ganttc.exportar()"><span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"
						uib-tooltip="Exportar"></span> Exportar</label>
					<label class="btn btn-default" ng-click="ganttc.zoomAcercar()"><span class="glyphicon glyphicon-zoom-in" aria-hidden="true"
						uib-tooltip="Acercar vista"></span></label>
					<label class="btn btn-default" ng-click="ganttc.zoomAlejar()"><span class="glyphicon glyphicon-zoom-out" aria-hidden="true"
						uib-tooltip="Alejar vista"></span></label>
				</div>
				
			</form>
			</div>
			
			<div class="operation_buttons" align="left">
			<div class="btn-group">
				<label class="btn btn-default" ng-click="ganttc.pesoProducto(ganttc.proyectoid)"><span class="glyphicon glyphicon-sort-by-attributes" aria-hidden="true"
					uib-tooltip="Peso de productos"></span></label>
			</div>
			</div>
		</div>
		 <div class="row" align="center" >
		 
		    <div class="gantt-chart">
				<div ds:gantt-chart id="ganttChartView" items="items" 
				settings="settings" auto-refresh="{{ true }}" style="min-height: 400px"></div>
	    	</div>
			<br/>
		</div>
	</div>
