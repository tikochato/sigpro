<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="desembolsotipoController as desembolsotipoc" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="35010">
			<p ng-init="desembolsotipoc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<h3>Tipo Desembolso</h3><br/>
		<div class="row" align="center" ng-if="!desembolsotipoc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="35040">
			       		<label class="btn btn-primary" ng-click="desembolsotipoc.nuevo()">Nuevo</label>
			       </shiro:hasPermission> 
			       <shiro:hasPermission name="35010"><label class="btn btn-primary" ng-click="desembolsotipoc.editar()">Editar</label></shiro:hasPermission>
			       <shiro:hasPermission name="35030">
			       		<label class="btn btn-primary" ng-click="desembolsotipoc.borrar()">Borrar</label>
			       </shiro:hasPermission>
			        			        
    			</div>				
    		</div>
    		<shiro:hasPermission name="35010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="desembolsotipoc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="desembolsotipoc.gridOptions" ui-grid-save-state 
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!desembolsotipoc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<ul uib-pagination total-items="desembolsotipoc.totalTiposDesembolso" 
						ng-model="desembolsotipoc.paginaActual" 
						max-size="desembolsotipoc.numeroMaximoPaginas" 
						items-per-page="desembolsotipoc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="desembolsotipoc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>
    		
		</div>
		<div class="row main-form" ng-show="desembolsotipoc.mostraringreso">
			<h4 ng-hide="!desembolsotipoc.esnuevo">Nuevo Tipo de Desembolso</h4>
			<h4 ng-hide="desembolsotipoc.esnuevo">Edición de Tipo de Desembolso</h4>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="35020">
						<label class="btn btn-success" ng-click="form.$valid ? desembolsotipoc.guardar() : ''" ng-disabled="!form.$valid">Guardar</label>
					</shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="desembolsotipoc.irATabla()">Ir a Tabla</label>
    			</div>
    		</div>
			
			<div class="col-sm-12">
				<form name="form">
						<div class="form-group">
							<label for="id">ID</label>
    						<p class="form-control-static">{{ desembolsotipoc.desembolsotipo.id }}</p>
						</div>
						<div class="form-group">
							<label for="nombre">* Nombre</label>
    						<input type="text" class="form-control" id="nombre" placeholder="Nombre" ng-model="desembolsotipoc.desembolsotipo.nombre" ng-required="true">
						</div>
						<div class="form-group">
							<label for="descripcion">Descripción</label>
    						<input type="text" class="form-control" id="descripcion" placeholder="Descripción" ng-model="desembolsotipoc.desembolsotipo.descripcion">
						</div>
						<br/>
						<div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo">Usuario que creo</label> 
									<p class="form-control-static" id="usuarioCreo"> {{ desembolsotipoc.desembolsotipo.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion">Fecha de creación</label>
									<p class="form-control-static" id="fechaCreacion"> {{ desembolsotipoc.desembolsotipo.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo">Usuario que actualizo</label> 
									<p class="form-control-static" id="usuarioCreo">{{ desembolsotipoc.desembolsotipo.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion">Fecha de actualizacion</label> 
									<p class="form-control-static" id="usuarioCreo">{{ desembolsotipoc.desembolsotipo.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<br />
			</form>
			</div>
			<div align="center">Los campos marcados con * son obligatorios</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="col-sm-12 operation_buttons" align="right">
					<div class="btn-group">
						<shiro:hasPermission name="35020">
							<label class="btn btn-success" ng-click="form.$valid ? desembolsotipoc.guardar() : ''" ng-disabled="!form.$valid">Guardar</label>
						</shiro:hasPermission>
				        <label class="btn btn-primary" ng-click="desembolsotipoc.irATabla()">Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
