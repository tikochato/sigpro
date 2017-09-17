<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<style>
	pre {
    
	    margin: 0 0 0px;
	    font-size: medium;
	    line-height: normal;
	    color: #333;
	    word-break: none;
	    word-wrap: break-word;
	    background-color: transparent;
	    border: none;
	    border-radius: none;
	    font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
	    padding: 0px;
	    line-height: normal;
     
	}
	
	
	
	

	</style>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="agendaController as agendac" class="maincontainer all_page" id="title">
	    
  	    <shiro:lacksPermission name="30010">
			<p ng-init="riesgoc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
			<div class="panel-heading"><h3>Agenda</h3></div>
		</div>
		
		
		<div class="row">
			<div class="col-sm-6 " >
				<select  class="inputText" ng-model="agendac.proyectoId"
					ng-options="a.text for a in agendac.prestamos"
					ng-change="agendac.cargarAgenda()"></select>
			</div>
		</div>
		
		
		
		<div class="row" align="center" >
			
			<br>
			<div class="col-sm-12 " ng-hide="!agendac.mostrarTabla">
			<div class="operation_buttons" align="right">
					<div class="btn-group">		
						<label class="btn btn-default" ng-click="agendac.exportarExcel()" uib-tooltip="Exportar" >
						<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
					</div>
			</div>
			</div>
			<div class="col-sm-12 " ng-hide="!agendac.mostrarTabla" >
				<table st-table="agendac.agenda" st-safe-src="agendac.lista" class="table table-condensed table-hover">
					<thead>
						<tr>
							<th class="label-form">EDT</th>
							<th class="label-form"></th>
							<th class="label-form">Actividad</th>
							<th class="label-form">Fecha Inicio</th>
							<th class="label-form">Fecha Fin</th>
							<th class="label-form">Estado</th>
						</tr>
					</thead>
					<tbody>
					<tr ng-repeat="row in agendac.agenda">
						
						<td>{{row.edt}}</td>
						<td><div class="{{ agendac.claseIcon(row.objetoTipo)}}"></div></td>
						<td>
							<pre>{{row.nombre}}</pre>
						</td>
						<td>{{row.fechaInicio}}</td>
						<td>{{row.fechaFin}}</td>
						<td>{{row.estado}}</td>
					</tr>
					</tbody>
				</table>
				<div class="grid_loading" ng-hide="!agendac.mostrarcargando">
				  				<div class="msg">
				      				<span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  				<br /><br />
						  				<b>Cargando, por favor espere...</b>
					  				</span>
								</div>
							</div>
				
				<br/>
				
				
			</div>
		  
	</div>
</div>
	