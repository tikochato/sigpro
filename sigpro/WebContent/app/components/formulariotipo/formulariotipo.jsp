<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div ng-controller="formulariotipoController as formulariotipoc"
	class="maincontainer all_page" id="title">
	<h3>Tipo de Formulario</h3>
	<br />
	<div class="row" align="center" ng-if="!formulariotipoc.mostraringreso">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-primary" ng-click="formulariotipoc.nuevo()">Nuevo</label>
				<label class="btn btn-primary" ng-click="formulariotipoc.editar()">Editar</label>
				<label class="btn btn-primary" ng-click="formulariotipoc.borrar()">Borrar</label>
			</div>
		</div>
		<div class="col-sm-12" align="center">
			<div style="height: 35px;">
				<div style="text-align: right;">
					<div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href
							ng-click="formulariotipoc.reiniciarVista()" role="button"
							uib-tooltip="Reiniciar la vista de la tabla"
							tooltip-placement="left"><span
							class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
				</div>
			</div>
			<br />
			<div id="maingrid" ui-grid="formulariotipoc.gridOptions"
				ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns
				ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
				<div class="grid_loading" ng-hide="!formulariotipoc.mostrarcargando">
					<div class="msg">
						<span><i class="fa fa-spinner fa-spin fa-4x"></i> <br />
						<br /> <b>Cargando, por favor espere...</b> </span>
					</div>
				</div>
			</div>
			<ul uib-pagination total-items="formulariotipoc.totalFormularios"
				ng-model="formulariotipoc.paginaActual"
				max-size="formulariotipoc.numeroMaximoPaginas"
				items-per-page="formulariotipoc.elementosPorPagina"
				first-text="Primero" last-text="Último" next-text="Siguiente"
				previous-text="Anterior" class="pagination-sm" boundary-links="true"
				force-ellipses="true" ng-change="formulariotipoc.cambioPagina()"></ul>
		</div>
	</div>

	<div class="row" ng-if="formulariotipoc.mostraringreso">
		<h4 ng-hide="!formulariotipoc.esnuevo">Nuevo Tipo Riesgo</h4>
		<h4 ng-hide="formulariotipoc.esnuevo">Edición de Tipo Riesgo</h4>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-success" ng-click="formulariotipoc.guardar()">Guardar</label>
				<label class="btn btn-primary" ng-click="formulariotipoc.irATabla()">Ir a Tabla</label>
			</div>
		</div>
		<div class="col-sm-12">
			<form>
				<div class="form-group">
					<label for="id">ID</label> 
					<label class="form-control" id="id">{{formulariotipoc.formulariotipo.id }}</label>
				</div>

				<div class="form-group">
					<label for="nombre">* Nombre</label> 
					<input type="text" class="form-control" id="nombre" placeholder="Nombre"ng-model="formulariotipoc.formulariotipo.nombre">
				</div>
				<div class="form-group">
					<label for="descripcion">Descripción</label> 
					<input type="text"class="form-control" id="descripcion" placeholder="Descripción"ng-model="formulariotipoc.formulariotipo.descripcion">
				</div>
				<div class="form-group">
					<label for="usuarioCreo">Usuario que creo</label> 
					<label class="form-control" id="usuarioCreo">{{ formulariotipoc.formulariotipo.usuarioCreo }}</label>
				</div>
				<div class="form-group">
					<label for="fechaCreacion">Fecha de creación</label> 
					<label class="form-control" id="fechaCreacion">{{ formulariotipoc.formulariotipo.fechaCreacion }}</label>
				</div>
				<div class="form-group">
					<label for="usuarioActualizo">Usuario que actualizo</label> 
					<label class="form-control" id="usuarioCreo">{{ formulariotipoc.formulariotipo.usuarioActualizo }}</label>
				</div>
				<div class="form-group">
					<label for="fechaActualizacion">Fecha de actualizacion</label> 
					<label class="form-control" id="usuarioCreo">{{ formulariotipoc.formulariotipo.fechaActualizacion }}</label>
				</div>
				<br />
			</form>
		</div>
		<br />
		<div align="center">Los campos marcados con * son obligatorios</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-success" ng-click="formulariotipoc.guardar()">Guardar</label>
				<label class="btn btn-primary" ng-click="formulariotipoc.irATabla()">Ir a Tabla</label>
			</div>
		</div>
	</div>
</div>
