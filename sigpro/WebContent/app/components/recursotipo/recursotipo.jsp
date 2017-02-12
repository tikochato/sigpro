<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div ng-controller="recursotipoController as recursotipoc"
	class="maincontainer all_page" id="title">

	<script type="text/ng-template" id="buscarcomponentepropiedad.jsp">
    	<%@ include file="/app/components/recursotipo/buscarrecursopropiedad.jsp"%>
  	</script>
	<h3>Tipo de Recurso</h3>
	<br />


	<div class="row" align="center" ng-if="!recursotipoc.mostraringreso">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-primary" ng-click="recursotipoc.nuevo()">Nuevo</label>
				<label class="btn btn-primary" ng-click="recursotipoc.editar()">Editar</label>
				<label class="btn btn-primary" ng-click="recursotipoc.borrar()">Borrar</label>
			</div>
		</div>
		<div class="col-sm-12" align="center">
			<div style="height: 35px;">
				<div style="text-align: right;">
					<div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href
							ng-click="recursotipoc.reiniciarVista()" role="button"
							uib-tooltip="Reiniciar la vista de la tabla"
							tooltip-placement="left"><span
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
			<ul uib-pagination total-items="recursotipoc.totalRecursos"
				ng-model="recursotipoc.paginaActual"
				max-size="recursotipoc.numeroMaximoPaginas"
				items-per-page="recursotipoc.elementosPorPagina"
				first-text="Primero" last-text="Último" next-text="Siguiente"
				previous-text="Anterior" class="pagination-sm" boundary-links="true"
				force-ellipses="true" ng-change="recursotipoc.cambioPagina()"></ul>
		</div>
	</div>

	<div class="row main-form" ng-if="recursotipoc.mostraringreso">
		<h4 ng-hide="!recursotipoc.esnuevo">Nuevo Tipo de Recurso</h4>
		<h4 ng-hide="recursotipoc.esnuevo">Edición de Tipo de Recurso</h4>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-success" ng-click="form.$valid ? recursotipoc.guardar() : '' " ng-disabled="form.$invalid">Guardar</label>
				<label class="btn btn-primary" ng-click="recursotipoc.irATabla()">Ir a Tabla</label>
			</div>
		</div>
		<div class="col-sm-12">
			<form name ="form">
				<div class="form-group" ng-show="!recursotipoc.esnuevo">
					<label for="id">ID</label> 
					<p class="form-control-static" id="id"> {{recursotipoc.recursotipo.id }}</p>
				</div>

				<div class="form-group">
					<label for="nombre">* Nombre</label> 
					<input type="text" class="form-control" id="nombre" placeholder="Nombre"ng-model="recursotipoc.recursotipo.nombre" ng-required="true">
				</div>
				<div class="form-group">
					<label for="descripcion">Descripción</label> 
					<input type="text"class="form-control" id="descripcion" placeholder="Descripción"ng-model="recursotipoc.recursotipo.descripcion" ng-required="true">
				</div>
				<div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo">Usuario que creo</label> 
									<p class="form-control-static"> {{ recursotipoc.recursotipo.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion">Fecha de creación</label>
									<p class="form-control-static" id="fechaCreacion"> {{ recursotipoc.recursotipo.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo">Usuario que actualizo</label> 
									<p class="form-control-static" id="usuarioCreo">{{ recursotipoc.recursotipo.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion">Fecha de actualizacion</label> 
									<p class="form-control-static" id="usuarioCreo">{{ recursotipoc.recursotipo.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>

				<br />
				<div align="center">
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
							<th>ID</th>
							<th>Nombre</th>
							<th>Descripicon</th>
							<th>Tipo Dato</th>
							<th style="width: 30px;">Quitar</th>

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
			</form>
		</div>
		<br />
		<div align="center">Los campos marcados con * son obligatorios</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-success" ng-click="form.$valid ? recursotipoc.guardar() : '' " ng-disabled="form.$invalid">Guardar</label>
				<label class="btn btn-primary" ng-click="recursotipoc.irATabla()">Ir a Tabla</label>
			</div>
		</div>
	</div>
</div>
