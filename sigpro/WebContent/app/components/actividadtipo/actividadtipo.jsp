<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div ng-controller="actividadtipoController as actividadtipoc"
	class="maincontainer all_page" id="title">

	<script type="text/ng-template" id="buscaractividadpropiedad.jsp">
    	<%@ include file="/app/components/actividadtipo/buscaractividadpropiedad.jsp"%>
  	</script>
	<h3>Tipo de Actividad</h3>
	<br />


	<div class="row" align="center" ng-if="!actividadtipoc.mostraringreso">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-primary" ng-click="actividadtipoc.nuevo()">Nuevo</label>
				<label class="btn btn-primary" ng-click="actividadtipoc.editar()">Editar</label>
				<label class="btn btn-primary" ng-click="actividadtipoc.borrar()">Borrar</label>
			</div>
		</div>
		<div class="col-sm-12" align="center">
			<div style="height: 35px;">
				<div style="text-align: right;">
					<div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href
							ng-click="actividadtipoc.reiniciarVista()" role="button"
							uib-tooltip="Reiniciar la vista de la tabla"
							tooltip-placement="left"><span
							class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
				</div>
			</div>
			<br />
			<div id="maingrid" ui-grid="actividadtipoc.gridOptions"
				ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns
				ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
				<div class="grid_loading" ng-hide="!actividadtipoc.mostrarcargando">
					<div class="msg">
						<span><i class="fa fa-spinner fa-spin fa-4x"></i> <br />
						<br /> <b>Cargando, por favor espere...</b> </span>
					</div>
				</div>
			</div>
			<ul uib-pagination total-items="actividadtipoc.totalCooperantes"
				ng-model="actividadtipoc.paginaActual"
				max-size="actividadtipoc.numeroMaximoPaginas"
				items-per-page="actividadtipoc.elementosPorPagina"
				first-text="Primero" last-text="Último" next-text="Siguiente"
				previous-text="Anterior" class="pagination-sm" boundary-links="true"
				force-ellipses="true" ng-change="actividadtipoc.cambioPagina()"></ul>
		</div>
	</div>

	<div class="row" ng-if="actividadtipoc.mostraringreso">
		<h4 ng-hide="!actividadtipoc.esnuevo">Nuevo Tipo Componente</h4>
		<h4 ng-hide="actividadtipoc.esnuevo">Edición de Tipo Componente</h4>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-success" ng-click="actividadtipoc.guardar()">Guardar</label>
				<label class="btn btn-primary" ng-click="actividadtipoc.irATabla()">Ir a Tabla</label>
			</div>
		</div>
		<div class="col-sm-12">
			<form>
				<div class="form-group">
					<label for="id">ID</label> 
					<label class="form-control" id="id">{{actividadtipoc.actividadtipo.id }}</label>
				</div>

				<div class="form-group">
					<label for="nombre">* Nombre</label> 
					<input type="text" class="form-control" id="nombre" placeholder="Nombre"ng-model="actividadtipoc.actividadtipo.nombre">
				</div>
				<div class="form-group">
					<label for="descripcion">Descripción</label> 
					<input type="text"class="form-control" id="descripcion" placeholder="Descripción"ng-model="actividadtipoc.actividadtipo.descripcion">
				</div>
				<div class="form-group">
					<label for="usuarioCreo">Usuario que creo</label> 
					<label class="form-control" id="usuarioCreo">{{ actividadtipoc.actividadtipo.usuarioCreo }}</label>
				</div>
				<div class="form-group">
					<label for="fechaCreacion">Fecha de creación</label> 
					<label class="form-control" id="fechaCreacion">{{ actividadtipoc.actividadtipo.fechaCreacion }}</label>
				</div>
				<div class="form-group">
					<label for="usuarioActualizo">Usuario que actualizo</label> 
					<label class="form-control" id="usuarioCreo">{{ actividadtipoc.actividadtipo.usuarioActualizo }}</label>
				</div>
				<div class="form-group">
					<label for="fechaActualizacion">Fecha de actualizacion</label> 
					<label class="form-control" id="usuarioCreo">{{ actividadtipoc.actividadtipo.fechaActualizacion }}</label>
				</div>

				<br />
				<div align="center">
					<div style="height: 35px; width: 75%">
						<div style="text-align: right;">
							<div class="btn-group" role="group" aria-label="">
								<a class="btn btn-default" href
									ng-click="actividadtipoc.buscarPropiedad()" role="button"
									uib-tooltip="Asignar nueva propiedad" tooltip-placement="left">
									<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
								</a>
							</div>
						</div>
					</div>
					
					<table style="width: 75%;"
					st-table="actividadtipoc.actividadpropiedades"
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
							ng-repeat="row in actividadtipoc.actividadpropiedades">
							<td>{{row.id}}</td>
							<td>{{row.nombre}}</td>
							<td>{{row.descripcion}}</td>
							<td>{{row.datotiponombre}}</td>
							<td>
								<button type="button"
									ng-click="actividadtipoc.eliminarPropiedad2(row)"
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
				<label class="btn btn-success" ng-click="actividadtipoc.guardar()">Guardar</label>
				<label class="btn btn-primary" ng-click="actividadtipoc.irATabla()">Ir a Tabla</label>
			</div>
		</div>
	</div>
</div>
