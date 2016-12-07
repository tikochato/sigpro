<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style type="text/css">

.myGrid {
	width: 100%;
	height: 600px;
}
</style>

	<div ng-controller="cooperanteController as cooperantec" class="maincontainer all_page" id="title">
		<h3>Cooperantes</h3><br/>
		<div class="row" align="center" ng-hide="cooperantec.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-primary" ng-click="cooperantec.nuevo()">Nuevo</label>
			        <label class="btn btn-primary" ng-click="cooperantec.editar()">Editar</label>
			        <label class="btn btn-primary" ng-click="cooperantec.borrar()">Borrar</label>
    			</div>
    		</div>
    		<div class="col-sm-12" align="center">
				<div id="maingrid" ui-grid="cooperantec.gridOptions" ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination>
					<div class="grid_loading" ng-hide="!cooperantec.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
			</div>
		</div>
		<div class="row" ng-show="cooperantec.mostraringreso">
			<h4 ng-hide="!cooperantec.esnuevo">Nuevo cooperante</h4>
			<h4 ng-hide="cooperantec.esnuevo">Edición de cooperante</h4>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-success" ng-click="cooperantec.guardar()">Guardar</label>
			        <label class="btn btn-danger" ng-click="cooperantec.cancelar()">Cancelar</label>
    			</div>
    		</div>
			<div class="col-sm-12">
				<form>
						<div class="form-group">
							<label for="id">ID</label>
    						<label class="form-control" id="id">{{ cooperantec.cooperante.id }}</label>
						</div>
						<div class="form-group">
							<label for="codigo">* Código</label>
    						<input type="text" class="form-control" id="codigo" placeholder="Código" ng-model="cooperantec.cooperante.codigo">
						</div>
						<div class="form-group">
							<label for="nombre">* Nombre</label>
    						<input type="text" class="form-control" id="nombre" placeholder="Nombre" ng-model="cooperantec.cooperante.nombre">
						</div>
						<div class="form-group">
							<label for="descripcion">Descripción</label>
    						<input type="text" class="form-control" id="descripcion" placeholder="Descripción" ng-model="cooperantec.cooperante.descripcion">
						</div>
						<div class="form-group">
							<label for="usuarioCreo">Usuario que creo</label>
    						<label class="form-control" id="usuarioCreo">{{ cooperantec.cooperante.usuarioCreo }}</label>
						</div>
						<div class="form-group">
							<label for="fechaCreacion">Fecha de creación</label>
    						<label class="form-control" id="fechaCreacion">{{ cooperantec.cooperante.fechaCreacion }}</label>
						</div>
						<div class="form-group">
							<label for="usuarioActualizo">Usuario que actualizo</label>
    						<label class="form-control" id="usuarioCreo">{{ cooperantec.cooperante.usuarioActualizo }}</label>
						</div>
						<div class="form-group">
							<label for="fechaActualizacion">Fecha de actualizacion</label>
    						<label class="form-control" id="usuarioCreo">{{ cooperantec.cooperante.fechaActualizacion }}</label>
						</div>
				</form>
			</div>
			<div align="center">Los campos marcados con * son obligatorios</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-success" ng-click="cooperantec.guardar()">Guardar</label>
			        <label class="btn btn-danger" ng-click="cooperantec.cancelar()">Cancelar</label>
    			</div>
    		</div>
		</div>

	</div>
