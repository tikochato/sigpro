<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style type="text/css">
.myGrid {
	width: 100%;
	height: 600px;
}
</style>

<div ng-controller="proyectoController as controller"
	class="maincontainer all_page" id="title">
	<h3>Proyectos</h3>
	<br />
	<div class="row" align="center" ng-hide="controller.esColapsado">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-primary" ng-click="controller.nuevo()">Nuevo</label>
				<label class="btn btn-primary" ng-click="controller.editar()">Editar</label> <label
					class="btn btn-primary" ng-click="controller.borrar()">Borrar</label>
			</div>
		</div>
		<div class="col-sm-12" align="center">
			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="controller.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
			<br/>
		
			<div id="grid1" ui-grid="controller.gridOpciones" ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination
				ui-grid-pagination>
				<div class="grid_loading" ng-hide="!controller.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<ul uib-pagination total-items="cooperantec.totalCooperantes" 
						ng-model="cooperantec.paginaActual" 
						max-size="cooperante.numeroMaximoPaginas" 
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="cooperantec.cambioPagina()"
				></ul>
		</div>
	</div>
	<div class="row" ng-show="controller.esColapsado">
		<h4 ng-hide="!controller.esNuevo">Nuevo Proyecto</h4>
			<h4 ng-hide="controller.esNuevo">Edición de proyecto</h4>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-success" ng-click="controller.guardar()">Guardar</label> 
				<label class="btn btn-primary" ng-click="controller.irATabla()">Ir a Tabla</label>
			</div>
		</div>
		<div class="col-sm-12">
			
			<form>
				<div class="form-group">
					<label for="campo1">* Nombre</label> 
					<input type="text" ng-model="controller.entidadseleccionada.nombre"
						class="form-control" id="t_nombre" placeholder="Nombre">
				</div>

				<div class="form-group">
					<label for="campo1">* SNIP</label> 
					<input type="number" ng-model="controller.entidadseleccionada.snip"
						class="form-control" id="n_snip" placeholder="Nombre">
				</div>

				<div class="form-group">
				<label for="campo1">* Tipo Proyecto</label>
					<select  id="s_tipo_proyecto"
						class="form-control" ng-model="controller.entidadseleccionada.proyectotipoid">
						<option value="">Seleccione una opción</option>
						<option ng-repeat="option in controller.proyectotipos"
							ng-selected="option.selected = controller.entidadseleccionada.proyectotipoid"
							value="{{option.id}}">{{option.nombre}}
						</option>
						
					</select>
				</div>
				
				<div class="form-group">
					<label for="campo1">* Unidad Ejecutra</label>
					<select  id="s_unidad_ejecutora"
						class="form-control" ng-model="controller.entidadseleccionada.unidadejecutoraid">
						<option value="">Seleccione una opción</option>
						<option ng-repeat="option_ue in controller.unidadesejecutoras"
							ng-selected="option_ue.selected = controller.entidadseleccionada.unidadejecutoraid"
							value="{{option_ue.id}}">{{option_ue.nombre}}
						</option>
						
					</select>
				</div>
				
				<div class="form-group">
					<label for="campo1">* Cooperante</label>
					<select  id="s_cooperante"  
						class="form-control" ng-model="controller.entidadseleccionada.cooperanteid">
						<option value="">Seleccione una opción</option>
						<option ng-repeat="option_c in controller.cooperantes" 
						ng-selected="option_c.id = controller.entidadseleccionada.unidadejecutoraid"
						value="{{option_c.id}}">{{option_c.nombre}}
						</option>
						
						
						
					</select>
				</div>
				

				<div class="form-group">
					<label for="campo2">Descripción</label> 
					<input type="text" ng-model="controller.entidadseleccionada.descripcion"
						class="form-control" id="campo2" placeholder="Descripción">
				</div>
				
				<div class="form-group">
							<label for="usuarioCreo">Usuario que creo</label>
    						<label class="form-control" id="usuarioCreo">{{ controller.entidadseleccionada.usuariocrea }}</label>
						</div>
						<div class="form-group">
							<label for="fechaCreacion">Fecha de creación</label>
    						<label class="form-control" id="fechaCreacion">{{ controller.entidadseleccionada.fechacrea }}</label>
						</div>
						<div class="form-group">
							<label for="usuarioActualizo">Usuario que actualizo</label>
    						<label class="form-control" id="usuarioCreo">{{ controller.entidadseleccionada.usuarioActualizo }}</label>
						</div>
						<div class="form-group">
							<label for="fechaActualizacion">Fecha de actualizacion</label>
    						<label class="form-control" id="usuarioCreo">{{ controller.entidadseleccionada.fechaActualizacion }}</label>
						</div>

			</form>
		</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-success" ng-click="controller.guardar()">Guardar</label> 
				<label class="btn btn-primary" ng-click="controller.irATabla()">Ir a Tabla</label>
			</div>
		</div>
	</div>

</div>
