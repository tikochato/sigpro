<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="proyectotipoController as proyectotipoc"
	class="maincontainer all_page" id="title">

	<script type="text/ng-template" id="buscarpropiedad.jsp">
    	<%@ include file="/app/components/prestamo/buscarpropiedad.jsp"%>
  	</script>
  	<shiro:lacksPermission name="36010">
		<p ng-init="proyectotipoc.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
	<h3>Tipo de Proyecto</h3>
	<br />


	<div class="row" align="center" ng-if="!proyectotipoc.mostraringreso">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="36040">
					<label class="btn btn-primary" ng-click="proyectotipoc.nuevo()">Nuevo</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="36010">
					<label class="btn btn-primary" ng-click="proyectotipoc.editar()">Editar</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="36030">
					<label class="btn btn-primary" ng-click="proyectotipoc.borrar()">Borrar</label>
				</shiro:hasPermission>
			</div>
		</div>
		<shiro:hasPermission name="36010">
		<div class="col-sm-12" align="center">
			<div style="height: 35px;">
				<div style="text-align: right;">
					<div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href
							ng-click="proyectotipoc.reiniciarVista()" role="button"
							uib-tooltip="Reiniciar la vista de la tabla"
							tooltip-placement="left"><span
							class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
				</div>
			</div>
			<br />
			<div id="maingrid" ui-grid="proyectotipoc.gridOptions"
				ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns
				ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
				<div class="grid_loading" ng-hide="!proyectotipoc.mostrarcargando">
					<div class="msg">
						<span><i class="fa fa-spinner fa-spin fa-4x"></i> <br />
						<br /> <b>Cargando, por favor espere...</b> </span>
					</div>
				</div>
			</div>
			<br/>
			<div class="total-rows">Total de {{  proyectotipoc.totalProyectotipos + (proyectotipoc.totalProyectotipos == 1 ? " Tipo de Proyecto" : " Tipos de Proyectos" ) }}</div>
			<ul uib-pagination total-items="proyectotipoc.totalProyectotipos"
				ng-model="proyectotipoc.paginaActual"
				max-size="proyectotipoc.numeroMaximoPaginas"
				items-per-page="proyectotipoc.elementosPorPagina"
				first-text="Primero" last-text="Último" next-text="Siguiente"
				previous-text="Anterior" class="pagination-sm" boundary-links="true"
				force-ellipses="true" ng-change="proyectotipoc.cambioPagina()"></ul>
		</div>
		</shiro:hasPermission>	
	</div>

	<div class="row main-form" ng-if="proyectotipoc.mostraringreso">
		<h4 ng-hide="!proyectotipoc.esnuevo">Nuevo tipo de proyecto</h4>
		<h4 ng-hide="proyectotipoc.esnuevo">Edición tipo de proyecto</h4>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="36020">
					<label class="btn btn-success" ng-click="form.$valid ? proyectotipoc.guardar() : ''" ng-disabled="!form.$valid">Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="proyectotipoc.irATabla()">Ir a Tabla</label>
			</div>
		</div>
		<div class="col-sm-12">
			<form name="form" id="form">
				<div class="form-group" ng-show="!proyectotipoc.esnuevo">
					<label for="id">ID</label>
					<p class="form-control-static">{{proyectotipoc.proyectotipo.id }}</p>
				</div>

				<div class="form-group">
					<label for="nombre">* Nombre</label> <input type="text"
						class="form-control" id="nombre" placeholder="Nombre" ng-model="proyectotipoc.proyectotipo.nombre" ng-required="true">
				</div>
				<div class="form-group">
					<label for="descripcion">Descripción</label> <input type="text"
						class="form-control" id="descripcion" placeholder="Descripción"
						ng-model="proyectotipoc.proyectotipo.descripcion">
				</div>
				<br />
				<div align="center">
					<h5>Propiedades</h5>
					<div style="height: 35px; width: 75%">
						<div style="text-align: right;">
							<div class="btn-group" role="group" aria-label="">
								<a class="btn btn-default" href
									ng-click="proyectotipoc.buscarPropiedad()" role="button"
									uib-tooltip="Asignar nueva propiedad" tooltip-placement="left">
									<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
								</a>
							</div>
						</div>
					</div>
					<br/>
					<table style="width: 75%;"
					st-table="proyectotipoc.proyectopropiedades"
					class="table table-striped  table-bordered">
					<thead >
						<tr>
							<th>ID</th>
							<th>Nombre</th>
							<th>Descripicon</th>
							<th>Tipo Dato</th>
							<th style="width: 30px;">Quitar</th>

						</tr>
					</thead>
					<tbody>
						<tr st-select-row="row"
							ng-repeat="row in proyectotipoc.proyectopropiedades">
							<td>{{row.id}}</td>
							<td>{{row.nombre}}</td>
							<td>{{row.descripcion}}</td>
							<td>{{row.datotiponombre}}</td>
							<td>
								<button type="button"
									ng-click="proyectotipoc.eliminarPropiedad2(row)"
									class="btn btn-sm btn-danger">
									<i class="glyphicon glyphicon-minus-sign"> </i>
								</button>
							</td>
						</tr>
					</tbody>
				</table>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo">Usuario que creo</label> 
									<p class="form-control-static"> {{ proyectotipoc.proyectotipo.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion">Fecha de creación</label>
									<p class="form-control-static" id="fechaCreacion"> {{ proyectotipoc.proyectotipo.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo">Usuario que actualizo</label> 
									<p class="form-control-static" id="usuarioCreo">{{ proyectotipoc.proyectotipo.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion">Fecha de actualizacion</label> 
									<p class="form-control-static" id="usuarioCreo">{{ proyectotipoc.proyectotipo.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
		<br />
		<div align="center">Los campos marcados con * son obligatorios</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="36020">
					<label class="btn btn-success" ng-click="form.$valid ? proyectotipoc.guardar() : ''" ng-disabled="!form.$valid">Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="proyectotipoc.irATabla()">Ir a Tabla</label>
			</div>
		</div>
	</div>
</div>
