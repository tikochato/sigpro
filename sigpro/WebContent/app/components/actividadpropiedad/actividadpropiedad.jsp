<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="actividadpropiedadController as actividadpropiedadc" class="maincontainer all_page" id="title">
		<h3>Propiedades de Actividad</h3><br/>
		<div class="row" align="center" ng-hide="actividadpropiedadc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="crearCooperante">
			       		<label class="btn btn-primary" ng-click="actividadpropiedadc.nuevo()">Nueva</label>
			       </shiro:hasPermission>
			       <shiro:hasPermission name="editarCooperante"><label class="btn btn-primary" ng-click="actividadpropiedadc.editar()">Editar</label></shiro:hasPermission>
			       <shiro:hasPermission name="eliminarCooperante">
			       		<label class="btn btn-primary" ng-click="actividadpropiedadc.borrar()">Borrar</label>
			       </shiro:hasPermission>


    			</div>
    		</div>
    		<shiro:hasPermission name="verCooperante">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="actividadpropiedadc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="actividadpropiedadc.gridOptions" ui-grid-save-state
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!actividadpropiedadc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<ul uib-pagination total-items="actividadpropiedadc.totalCooperantes"
						ng-model="actividadpropiedadc.paginaActual"
						max-size="actividadpropiedadc.numeroMaximoPaginas"
						items-per-page="actividadpropiedadc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="actividadpropiedadc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>

		</div>
		<div class="row main-form" ng-show="actividadpropiedadc.mostraringreso">
			<h4 ng-hide="!actividadpropiedadc.esnuevo">Nueva Propiedad</h4>
			<h4 ng-hide="actividadpropiedadc.esnuevo">Edición de Propiedad</h4>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-success" ng-click="form.$valid ? actividadpropiedadc.guardar() : ''" ng-disabled="!form.$valid">Guardar</label>
			        <label class="btn btn-primary" ng-click="actividadpropiedadc.irATabla()">Ir a Tabla</label>
    			</div>
    		</div>

			<div class="col-sm-12">
				<form name="form">
						<div class="form-group">
							<label for="id">ID</label>
    						<p class="form-control-static">{{ actividadpropiedadc.actividadpropiedad.id }}</p>
						</div>
						<div class="form-group">
							<label for="nombre">* Nombre</label>
    						<input type="text" class="form-control" id="nombre" placeholder="Nombre" ng-model="actividadpropiedadc.actividadpropiedad.nombre" ng-required="true">
						</div>
						<div class="form-group">
							<label for="nombre">* Tipo dato</label>
							<select class="form-control" ng-model="actividadpropiedadc.actividadpropiedad.datotipoid"
								ng-options="tipo as tipo.nombre for tipo in actividadpropiedadc.tipodatos track by tipo.id"
								ng-readonly="true"
								ng-disabled="!actividadpropiedadc.esnuevo" ng-required="true">
								<option value="">Seleccione una opción</option>
							</select>
						</div>
						<div class="form-group">
							<label for="descripcion">Descripción</label>
    						<input type="text" class="form-control" id="descripcion" placeholder="Descripción" ng-model="actividadpropiedadc.actividadpropiedad.descripcion">
						</div>
						<div class="panel panel-default">
							<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
							<div class="panel-body">
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group" style="text-align: right">
											<label for="usuarioCreo">Usuario que creo</label>
				    						<p class="form-control-static">{{ actividadpropiedadc.actividadpropiedad.usuarioCreo }}</p>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<label for="fechaCreacion">Fecha de creación</label>
				    						<p class="form-control-static">{{ actividadpropiedadc.actividadpropiedad.fechaCreacion }}</p>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group" style="text-align: right">
											<label for="usuarioActualizo">Usuario que actualizo</label>
				    						<p class="form-control-static">{{ actividadpropiedadc.actividadpropiedad.usuarioActualizo }}</p>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<label for="fechaActualizacion">Fecha de actualizacion</label>
				    						<p class="form-control-static">{{ actividadpropiedadc.actividadpropiedad.fechaActualizacion }}</p>
										</div>
									</div>
								</div>
							</div>
						</div>
				</form>
			</div>
			<div align="center">Los campos marcados con * son obligatorios</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="col-sm-12 operation_buttons" align="right">
					<div class="btn-group">
				        <label class="btn btn-success" ng-click="form.$valid ? actividadpropiedadc.guardar() : ''" ng-disabled="!form.$valid">Guardar</label>
				        <label class="btn btn-primary" ng-click="actividadpropiedadc.irATabla()">Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
