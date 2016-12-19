<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<div ng-controller="proyectotipoController as proyectotipoc" class="maincontainer all_page" id="title">
	<script type="text/ng-template" id="buscarpropiedad.jsp">
    	<%@ include file="/app/components/proyecto/buscarpropiedad.jsp"%>
  	</script>
		<h3>Tipo de Proyecto</h3><br/>
		<div class="row" align="center" ng-hide="proyectotipoc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-primary" ng-click="proyectotipoc.nuevo()">Nuevo</label>
			        <label class="btn btn-primary" ng-click="proyectotipoc.editar()">Editar</label>
			        <label class="btn btn-primary" ng-click="proyectotipoc.borrar()">Borrar</label>
    			</div>
    		</div>
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="proyectotipoc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="proyectotipoc.gridOptions" ui-grid-save-state 
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!proyectotipoc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<ul uib-pagination total-items="proyectotipoc.totalCooperantes" 
						ng-model="proyectotipoc.paginaActual" 
						max-size="proyectotipoc.numeroMaximoPaginas" 
						items-per-page="proyectotipoc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="proyectotipoc.cambioPagina()"
				></ul>
			</div>
		</div>
		<div class="row" ng-show="proyectotipoc.mostraringreso">
			<h4 ng-hide="!proyectotipoc.esnuevo">Nuevo cooperante</h4>
			<h4 ng-hide="proyectotipoc.esnuevo">Edición de cooperante</h4>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-success" ng-click="proyectotipoc.guardar()">Guardar</label>
			        <label class="btn btn-primary" ng-click="proyectotipoc.irATabla()">Ir a Tabla</label>
    			</div>
    		</div>
			<div class="col-sm-12">
				<form>
					<uib-tabset active="active">
					<br/>
						<uib-tab index="0" heading="Tipo Proyecti">
							<div class="form-group">
								<label for="id">ID</label>
	    						<label class="form-control" id="id">{{ proyectotipoc.proyectotipo.id }}</label>
							</div>
							
							<div class="form-group">
								<label for="nombre">* Nombre</label>
	    						<input type="text" class="form-control" id="nombre" placeholder="Nombre" ng-model="proyectotipoc.proyectotipo.nombre">
							</div>
							<div class="form-group">
								<label for="descripcion">Descripción</label>
	    						<input type="text" class="form-control" id="descripcion" placeholder="Descripción" ng-model="proyectotipoc.proyectotipo.descripcion">
							</div>
							<div class="form-group">
								<label for="usuarioCreo">Usuario que creo</label>
	    						<label class="form-control" id="usuarioCreo">{{ proyectotipoc.proyectotipo.usuarioCreo }}</label>
							</div>
							<div class="form-group">
								<label for="fechaCreacion">Fecha de creación</label>
	    						<label class="form-control" id="fechaCreacion">{{ proyectotipoc.proyectotipo.fechaCreacion }}</label>
							</div>
							<div class="form-group">
								<label for="usuarioActualizo">Usuario que actualizo</label>
	    						<label class="form-control" id="usuarioCreo">{{ proyectotipoc.proyectotipo.usuarioActualizo }}</label>
							</div>
							<div class="form-group">
								<label for="fechaActualizacion">Fecha de actualizacion</label>
	    						<label class="form-control" id="usuarioCreo">{{ proyectotipoc.proyectotipo.fechaActualizacion }}</label>
							</div>
						</uib-tab>
						<uib-tab index="2" heading="Propiedades">
							<br/>
							<div style="height: 35px;">
								<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
									<a class="btn btn-default" href ng-click="proyectotipoc.buscarPropiedad()" role="button" 
										uib-tooltip="Asignar nueva propiedad" tooltip-placement="left">
										<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
									</a>
									<a class="btn btn-default" href ng-click="proyectotipoc.eliminarPropiedad()" role="button" 
										uib-tooltip="Eliminar propiedad" tooltip-placement="left">
										<span class="glyphicon glyphicon-minus" aria-hidden="true"></span>
									</a>
								</div>
								</div>
							</div>
							
							<div id="proyectoPropiedadgrid" ui-grid="proyectotipoc.gridOptionsProyectoPropiedad" ui-grid-save-state  
									ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid"
									ng-show="proyectotipoc.mostrarPropiedad"
									>
								<div class="grid_loading" ng-hide="!proyectotipoc.mostrarcargandoProyProp">
							  	<div class="msg">
							      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
									  <br /><br />
									  <b>Cargando, por favor espere...</b>
								  </span>
								</div>
							  </div>
							</div> 
						
						</uib-tab>					
					</uib-tabset>
				</form>
			</div>
			<div align="center">Los campos marcados con * son obligatorios</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-success" ng-click="proyectotipoc.guardar()">Guardar</label>
			        <label class="btn btn-primary" ng-click="proyectotipoc.irATabla()">Ir a Tabla</label>
    			</div>
    		</div>
		</div>
	</div>
