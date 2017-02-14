<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="formularioitemtipoController as formularioitemtipoc" class="maincontainer all_page" id="title">
		<h3>Tipo Item de Formulario</h3><br/>
		<div class="row" align="center" ng-if="!formularioitemtipoc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="13040">
			       		<label class="btn btn-primary" ng-click="formularioitemtipoc.nuevo()">Nuevo</label>
			       </shiro:hasPermission> 
			       <shiro:hasPermission name="13020"><label class="btn btn-primary" ng-click="formularioitemtipoc.editar()">Editar</label></shiro:hasPermission>
			       <shiro:hasPermission name="13030">
			       		<label class="btn btn-primary" ng-click="formularioitemtipoc.borrar()">Borrar</label>
			       </shiro:hasPermission>
			        			        
    			</div>				
    		</div>
    		<shiro:hasPermission name="13010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="formularioitemtipoc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="formularioitemtipoc.gridOptions" ui-grid-save-state 
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!formularioitemtipoc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<ul uib-pagination total-items="formularioitemtipoc.totalFormularioItmeTipos" 
						ng-model="formularioitemtipoc.paginaActual" 
						max-size="formularioitemtipoc.numeroMaximoPaginas" 
						items-per-page="formularioitemtipoc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="formularioitemtipoc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>
    		
		</div>
		<div class="row main-form" ng-show="formularioitemtipoc.mostraringreso">
			<h4 ng-hide="!formularioitemtipoc.esnuevo">Nuevo Tipo de Item de Formulario</h4>
			<h4 ng-hide="formularioitemtipoc.esnuevo">Edición de Tipo de Item de Formulario</h4>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-success" ng-click="form.$valid ? formularioitemtipoc.guardar() : ''" ng-disabled="!form.$valid">Guardar</label>
			        <label class="btn btn-primary" ng-click="formularioitemtipoc.irATabla()">Ir a Tabla</label>
    			</div>
    		</div>
			
			<div class="col-sm-12">
				<form name="form">
						<div class="form-group">
							<label for="id">ID</label>
    						<p class="form-control-static" >{{ formularioitemtipoc.formularioitemtipo.id }}</p>
						</div>
						<div class="form-group">
							<label for="nombre">* Nombre</label>
    						<input type="text" class="form-control" id="nombre" placeholder="Nombre" ng-model="formularioitemtipoc.formularioitemtipo.nombre">
						</div>
						<div class="form-group">
							<label for="nombre">* Tipo dato</label>
							<select class="form-control" ng-model="formularioitemtipoc.formularioitemtipo.datotipo"
								ng-options="tipo as tipo.nombre for tipo in formularioitemtipoc.tipodatos track by tipo.id"
								ng-readonly="true" 
								ng-disabled="!formularioitemtipoc.esnuevo" >
								<option value="">Seleccione una opción</option>
							</select>
						</div>
						<div class="form-group">
							<label for="descripcion">Descripción</label>
    						<input type="text" class="form-control" id="descripcion" placeholder="Descripción" ng-model="formularioitemtipoc.formularioitemtipo.descripcion">
						</div>
						<div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo">Usuario que creo</label> 
									<p class="form-control-static" id="usuarioCreo"> {{ formularioitemtipoc.formularioitemtipo.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion">Fecha de creación</label>
									<p class="form-control-static" id="fechaCreacion"> {{ formularioitemtipoc.formularioitemtipo.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo">Usuario que actualizo</label> 
									<p class="form-control-static" id="usuarioCreo">{{ formularioitemtipoc.formularioitemtipo.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion">Fecha de actualizacion</label> 
									<p class="form-control-static" id="usuarioCreo">{{ formularioitemtipoc.formularioitemtipo.fechaActualizacion }} </p>
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
				        <label class="btn btn-success"  ng-click="form.$valid ? formularioitemtipoc.guardar() : ''" ng-disabled="!form.$valid">Guardar</label>
				        <label class="btn btn-primary" ng-click="formularioitemtipoc.irATabla()">Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
