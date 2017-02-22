<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="programapropiedadController as programapropiedadc" class="maincontainer all_page" id="title">
		<h3>Propiedades de Program</h3><br/>
		<div class="row" align="center" ng-hide="programapropiedadc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="25040">
			       		<label class="btn btn-primary" ng-click="programapropiedadc.nuevo()">Nuevo</label>
			       </shiro:hasPermission>
			       <shiro:hasPermission name="25020"><label class="btn btn-primary" ng-click="programapropiedadc.editar()">Editar</label></shiro:hasPermission>
			       <shiro:hasPermission name="25030">
			       		<label class="btn btn-primary" ng-click="programapropiedadc.borrar()">Borrar</label>
			       </shiro:hasPermission>
    			</div>
    		</div>
    		<shiro:hasPermission name="25010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="programapropiedadc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left">
							<span class="glyphicon glyphicon-repeat" aria-hidden="true"></span>
						</a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="programapropiedadc.gridOptions" ui-grid-save-state
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!programapropiedadc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br/>
				<div class="total-rows">Total de {{  programapropiedadc.totalProgramaPropiedades + (programapropiedadc.totalProgramaPropiedades == 1 ? " Propiedad" : " Propiedades" ) }}</div>
				<ul uib-pagination total-items="programapropiedadc.totalProgramaPropiedades"
						ng-model="programapropiedadc.paginaActual"
						max-size="programapropiedadc.numeroMaximoPaginas"
						items-per-page="programapropiedadc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="programapropiedadc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>

		</div>
		<div class="row main-form" ng-show="programapropiedadc.mostraringreso">
			<h4 ng-hide="!programapropiedadc.esnuevo">Nueva Propiedad</h4>
			<h4 ng-hide="programapropiedadc.esnuevo">Edición de Propiedad</h4>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-success" ng-click="form.$valid ? programapropiedadc.guardar() : ''" ng-disabled="!form.$valid">Guardar</label>
			        <label class="btn btn-primary" ng-click="programapropiedadc.irATabla()">Ir a Tabla</label>
    			</div>
    		</div>

			<div class="col-sm-12">
				<form name="form">
						<div class="form-group">
							<label for="id">ID</label>
    						<label class="form-control" id="id">{{ programapropiedadc.programapropiedad.id }}</label>
						</div>
						<div class="form-group">
							<label for="nombre">* Nombre</label>
    						<input type="text" class="form-control" id="nombre" placeholder="Nombre" ng-model="programapropiedadc.programapropiedad.nombre" ng-required="true">
						</div>
						<div class="form-group">
							<label for="nombre">* Tipo dato</label>
							<select class="form-control" ng-model="programapropiedadc.programapropiedad.datotipoid"
								ng-options="tipo as tipo.nombre for tipo in programapropiedadc.tipodatos track by tipo.id"
								ng-readonly="true"
								ng-disabled="!programapropiedadc.esnuevo" ng-required="true">
								<option value="">Seleccione una opción</option>
							</select>
						</div>
						<div class="form-group">
							<label for="descripcion">Descripción</label>
    						<input type="text" class="form-control" id="descripcion" placeholder="Descripción" ng-model="programapropiedadc.programapropiedad.descripcion">
						</div>
						<br/>
						<div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo">Usuario que creo</label> 
									<p class="form-control-static"> {{ programapropiedadc.programapropiedad.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion">Fecha de creación</label>
									<p class="form-control-static" id="fechaCreacion"> {{ programapropiedadc.programapropiedad.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo">Usuario que actualizo</label> 
									<p class="form-control-static" id="usuarioCreo">{{ programapropiedadc.programapropiedad.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion">Fecha de actualizacion</label> 
									<p class="form-control-static" id="usuarioCreo">{{ programapropiedadc.programapropiedad.fechaActualizacion }} </p>
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
				        <label class="btn btn-success" ng-click="form.$valid ? programapropiedadc.guardar() : ''" ng-disabled="!form.$valid">Guardar</label>
				        <label class="btn btn-primary" ng-click="programapropiedadc.irATabla()">Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
