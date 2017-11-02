<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="recursotipoController as recursotipoc"
	class="maincontainer all_page" id="title">

	<script type="text/ng-template" id="buscarcomponentepropiedad.jsp">
    	<%@ include file="/app/components/recursotipo/buscarrecursopropiedad.jsp"%>
  	</script>
  	<shiro:lacksPermission name="28010">
		<p ng-init="recursotipoc.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
	<div class="panel panel-default">
	  <div class="panel-heading"><h3>Tipos de Recurso</h3></div>
	</div>
	<br />


	<div class="row" align="center" ng-if="!recursotipoc.mostraringreso">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="28040">
					<label class="btn btn-primary" ng-click="recursotipoc.nuevo()" uib-tooltip="Nuevo tipo recurso">
					<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="28010">
					<label class="btn btn-primary" ng-click="recursotipoc.editar()" uib-tooltip="Editar tipo recurso">
					<span class="glyphicon glyphicon-pencil"></span> Editar</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="28030">
					<label class="btn btn-danger" ng-click="recursotipoc.borrar()" uib-tooltip="Borrar tipo recurso">
					<span class="glyphicon glyphicon-trash"></span> Borrar</label>
				</shiro:hasPermission>				
			</div>
		</div>
		<shiro:hasPermission name="28010">
				<div class="col-sm-12" align="center">
			<div style="height: 35px;">
				<div style="text-align: right;">
					<div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href
							ng-click="recursotipoc.reiniciarVista()" role="button"
							uib-tooltip="Reiniciar la vista de la tabla"><span
							class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
				</div>
			</div>
			<br />
			<div id="maingrid" ui-grid="recursotipoc.gridOptions"
				ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns
				ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
				<div class="grid_loading" ng-hide="!recursotipoc.mostrarcargando">
					<div class="msg">
						<span><i class="fa fa-spinner fa-spin fa-4x"></i> <br />
						<br /> <b>Cargando, por favor espere...</b> </span>
					</div>
				</div>
			</div>
			<br/>
			<div class="total-rows">Total de {{  recursotipoc.totalRecursotipos + (recursotipoc.totalRecursotipos == 1 ? " Tipo de Recurso" : " Tipos de Recurso" ) }}</div>
			<ul uib-pagination total-items="recursotipoc.totalRecursotipos"
				ng-model="recursotipoc.paginaActual"
				max-size="recursotipoc.numeroMaximoPaginas"
				items-per-page="recursotipoc.elementosPorPagina"
				first-text="Primero" last-text="Último" next-text="Siguiente"
				previous-text="Anterior" class="pagination-sm" boundary-links="true"
				force-ellipses="true" ng-change="recursotipoc.cambioPagina()"></ul>
		</div>
		</shiro:hasPermission>

	</div>

	<div class="row second-main-form" ng-if="recursotipoc.mostraringreso">
	<div class="page-header">
		<h2 ng-hide="!recursotipoc.esnuevo"><small>Nuevo Tipo de Recurso</small></h2>
		<h2 ng-hide="recursotipoc.esnuevo"><small>Edición de Tipo de Recurso</small></h2>
	</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="28020">
					<label class="btn btn-success" ng-click="form.$valid ? recursotipoc.guardar() : '' " ng-disabled="form.$invalid" uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="recursotipoc.irATabla()" uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
		<div class="col-sm-12">
			<form name ="form">
				<div class="form-group" ng-show="!recursotipoc.esnuevo">
					<label for="id" class="floating-label id_class">ID {{recursotipoc.recursotipo.id }}</label>
					<br/><br/> 
				</div>

				<div class="form-group">
					<input type="text" id="nombre" class="inputText" ng-value="recursotipoc.recursotipo.nombre" ng-model="recursotipoc.recursotipo.nombre" onblur="this.setAttribute('value', this.value);" ng-required="true">
					<label class="floating-label">* Nombre</label> 
				</div>
				<div class="form-group">
				   <textarea class="inputText" rows="4"
				   ng-model="recursotipoc.recursotipo.descripcion" ng-value="recursotipoc.recursotipo.descripcion"   
				   onblur="this.setAttribute('value', this.value);" ng-required="false" ></textarea>
				   <label class="floating-label">Descripción</label>
				</div>
				<br/>
				
				<div align="center">
				<h5 class="label-form">Propiedades</h5>
					<div style="height: 35px; width: 75%">
						<div style="text-align: right;">
							<div class="btn-group" role="group" aria-label="">
								<a class="btn btn-default" href
									ng-click="recursotipoc.buscarPropiedad()" role="button"
									uib-tooltip="Asignar nueva propiedad" tooltip-placement="left">
									<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
								</a>
							</div>
						</div>
					</div>
					<br/>
					<table style="width: 75%;"
					st-table="recursotipoc.recursopropiedades"
					class="table table-striped  table-bordered">
					<thead >
						<tr>
							<th class="label-form">ID</th>
							<th class="label-form">Nombre</th>
							<th class="label-form">Descripicon</th>
							<th class="label-form">Tipo Dato</th>
							<th style="width: 30px;" class="label-form">Quitar</th>

						</tr>
					</thead>
					<tbody>
						<tr st-select-row="row"
							ng-repeat="row in recursotipoc.recursopropiedades">
							<td>{{row.id}}</td>
							<td>{{row.nombre}}</td>
							<td>{{row.descripcion}}</td>
							<td>{{row.datotiponombre}}</td>
							<td>
								<button type="button"
									ng-click="recursotipoc.eliminarPropiedad2(row)"
									class="btn btn-sm btn-danger">
									<i class="glyphicon glyphicon-minus-sign"> </i>
								</button>
							</td>
						</tr>
					</tbody>
				</table>
				</div>
				<br/>
				
				<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo" class="label-form">Usuario que creo</label> 
									<p> {{ recursotipoc.recursotipo.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion" class="label-form">Fecha de creación</label>
									<p id="fechaCreacion"> {{ recursotipoc.recursotipo.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo" class="label-form">Usuario que actualizo</label> 
									<p id="usuarioCreo">{{ recursotipoc.recursotipo.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion" class="label-form">Fecha de actualizacion</label> 
									<p id="usuarioCreo">{{ recursotipoc.recursotipo.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>

			</form>
		</div>
		<br />
		<div align="center" class="label-form">Los campos marcados con * son obligatorios</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="28020">
					<label class="btn btn-success" ng-click="form.$valid ? recursotipoc.guardar() : '' " ng-disabled="form.$invalid" uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="recursotipoc.irATabla()" uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
	</div>
</div>
