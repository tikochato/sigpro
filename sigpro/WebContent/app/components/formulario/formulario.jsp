<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div ng-controller="formularioController as formularioc"
	class="maincontainer all_page" id="title">
	<script type="text/ng-template" id="buscarPorFormulario.jsp">
    		<%@ include file="/app/components/formulario/buscarPorFormulario.jsp"%>
  	</script>
	<h3>Formularios</h3>
	<br />
	<div class="row" align="center" ng-if="!formularioc.mostraringreso">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-primary" ng-click="formularioc.nuevo()">Nuevo</label>
				<label class="btn btn-primary" ng-click="formularioc.editar()">Editar</label>
				<label class="btn btn-primary" ng-click="formularioc.borrar()">Borrar</label>
			</div>
		</div>
		<div class="col-sm-12" align="center">
			<div style="height: 35px;">
				<div style="text-align: right;">
					<div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href
							ng-click="formularioc.reiniciarVista()" role="button"
							uib-tooltip="Reiniciar la vista de la tabla"
							tooltip-placement="left"><span
							class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
				</div>
			</div>
			<br />
			<div id="maingrid" ui-grid="formularioc.gridOptions"
				ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns
				ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
				<div class="grid_loading" ng-hide="!formularioc.mostrarcargando">
					<div class="msg">
						<span><i class="fa fa-spinner fa-spin fa-4x"></i> <br />
						<br /> <b>Cargando, por favor espere...</b> </span>
					</div>
				</div>
			</div>
			<ul uib-pagination total-items="formularioc.totalFormularios"
				ng-model="formularioc.paginaActual"
				max-size="formularioc.numeroMaximoPaginas"
				items-per-page="formularioc.elementosPorPagina"
				first-text="Primero" last-text="Último" next-text="Siguiente"
				previous-text="Anterior" class="pagination-sm" boundary-links="true"
				force-ellipses="true" ng-change="formularioc.cambioPagina()"></ul>
		</div>
	</div>

	<div class="row" ng-if="formularioc.mostraringreso">
		<h4 ng-hide="!formularioc.esnuevo">Nuevo Formulario</h4>
		<h4 ng-hide="formularioc.esnuevo">Edición de Formulario</h4>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-success" ng-click="formularioc.guardar()">Guardar</label>
				<label class="btn btn-primary" ng-click="formularioc.irATabla()">Ir a Tabla</label>
			</div>
		</div>
		<div class="col-sm-12">
			<form>
				<div class="form-group">
					<label for="id">ID</label> <label class="form-control" id="id">{{ formularioc.formulario.id }}</label>
				</div>
				<div class="form-group">
					<label for="nombre">* Código</label> <input type="text"
						class="form-control" id="nombre" placeholder="Código"
						ng-model="formularioc.formulario.codigo">
				</div>
				
				<div class="form-group">
					<label for="campo3">* Tipo Formulario</label>
		          	<div class="input-group">
		            	<input type="hidden" class="form-control" ng-model="formularioc.formulariotipoid" /> 
		            	<input type="text" class="form-control" id="iproyt" name="iproyt" placeholder="Nombre Tipo Formulario" ng-model="formularioc.formulariotiponombre" ng-readonly="true" required/>
		            	<span class="input-group-addon" ng-click="formularioc.buscarFormularioTipo()"><i class="glyphicon glyphicon-search"></i></span>
		          	</div>
				</div>
				
				<div class="form-group">
					<label for="descripcion">Descripción</label> <input type="text"
						class="form-control" id="descripcion" placeholder="Descripción"
						ng-model="formularioc.formulario.descripcion">
				</div>
				<div class="form-group">
					<label for="usuarioCreo">Usuario que creo</label> <label
						class="form-control" id="usuarioCreo">{{
						formularioc.formulario.usuarioCreo }}</label>
				</div>
				<div class="form-group">
					<label for="fechaCreacion">Fecha de creación</label> <label
						class="form-control" id="fechaCreacion">{{
						formularioc.formulario.fechaCreacion }}</label>
				</div>
				<div class="form-group">
					<label for="usuarioActualizo">Usuario que actualizo</label> <label
						class="form-control" id="usuarioCreo">{{
						formularioc.formulario.usuarioActualizo }}</label>
				</div>
				<div class="form-group">
					<label for="fechaActualizacion">Fecha de actualizacion</label> <label
						class="form-control" id="usuarioCreo">{{
						formularioc.formulario.fechaActualizacion }}</label>
				</div>

				<br />
				<div align="center">
					<div style="height: 35px; width: 75%">
						<div style="text-align: right;">
							<div class="btn-group" role="group" aria-label="">
								<a class="btn btn-default" href
									ng-click="formularioc.buscarFormularioItem()" role="button"
									uib-tooltip="Asignar nuevo item" tooltip-placement="left">
									<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
								</a>
							</div>
						</div>
					</div>
					
					<table style="width: 75%;"
					st-table="formularioc.formularioitemtipos"
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
							ng-repeat="row in formularioc.formularioitemtipos">
							<td>{{row.id}}</td>
							<td>{{row.nombre}}</td>
							<td>{{row.descripcion}}</td>
							<td>{{row.datotiponombre}}</td>
							<td>
								<button type="button"
									ng-click="formularioc.eliminarPropiedad2(row)"
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
				<label class="btn btn-success" ng-click="formularioc.guardar()">Guardar</label>
				<label class="btn btn-primary" ng-click="formularioc.irATabla()">Ir a Tabla</label>
			</div>
		</div>
	</div>
</div>