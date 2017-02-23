<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="hitotipoController as hitotipoc" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="16010">
			<p ng-init="hitotipoc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<h3>Tipo Hito</h3><br/>
		<div class="row" align="center" ng-hide="hitotipoc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="16040">
			       		<label class="btn btn-primary" ng-click="hitotipoc.nuevo()">Nuevo</label>
			       </shiro:hasPermission> 
			       <shiro:hasPermission name="16010"><label class="btn btn-primary" ng-click="hitotipoc.editar()">Editar</label></shiro:hasPermission>
			       <shiro:hasPermission name="16030">
			       		<label class="btn btn-primary" ng-click="hitotipoc.borrar()">Borrar</label>
			       </shiro:hasPermission>
    			</div>				
    		</div>
    		<shiro:hasPermission name="16010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="hitotipoc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="hitotipoc.gridOptions" ui-grid-save-state 
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!hitotipoc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br/>
			<div class="total-rows">Total de {{  hitotipoc.totalHitotipo + (hitotipoc.totalHitotipo == 1 ? " tipo de hito" : " tipos de hito" ) }}</div>
				<ul uib-pagination total-items="hitotipoc.totalHitotipo" 
						ng-model="hitotipoc.paginaActual" 
						max-size="hitotipoc.numeroMaximoPaginas" 
						items-per-page="hitotipoc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="hitotipoc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>
    		
		</div>
		<div class="row main-form" ng-show="hitotipoc.mostraringreso">
			<h4 ng-hide="!hitotipoc.esnuevo">Nuevo Tipo Hito</h4>
			<h4 ng-hide="hitotipoc.esnuevo">Edición de Tipo Hito</h4>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="16020">
			        	<label class="btn btn-success"  ng-click="form.$valid ? hitotipoc.guardar() : ''" ng-disabled="form.$invalid">Guardar</label>
					</shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="hitotipoc.irATabla()">Ir a Tabla</label>
    			</div>
    		</div>
			
			<div class="col-sm-12">
				<form name="form">
					<div class="form-group" ng-show="!hitotipoc.esnuevo">
						<label for="id">ID</label>			
   						<p class="form-control-static">{{ hitotipoc.hitotipo.id }}</p>		
					</div>
					<div class="form-group">
						<label for="nombre">* Nombre</label>
   						<input type="text" class="form-control" id="nombre" placeholder="Nombre" ng-model="hitotipoc.hitotipo.nombre" ng-required="true">
					</div>
					<div class="form-group">
				        <label for="campo2">* Tipo:</label>     			
		    			<select class="form-control" ng-model="hitotipoc.datoTipoSeleccionado" ng-options="tipo as tipo.nombre for tipo in hitotipoc.datoTipos track by tipo.id" ng-required="true"
		    			ng-readonly="true" ng-disabled="!hitotipoc.esnuevo" ng-required="true" >
							<option disabled selected value> -- Seleccione Tipo -- </option>
						</select>
		    		 </div>
					<div class="form-group">
						<label for="descripcion">Descripción</label>
   						<input type="text" class="form-control" id="descripcion" placeholder="Descripción" ng-model="hitotipoc.hitotipo.descripcion">
					</div>
					
					<br/>
				<div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo">Usuario que creo</label>
				  					<p class="form-control-static">{{ hitotipoc.hitotipo.usuarioCreo }}</pl>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaCreacion">Fecha de creación</label>
				  					<p class="form-control-static">{{ hitotipoc.hitotipo.fechaCreacion }}</p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo">Usuario que actualizo</label>
				  					<p class="form-control-static">{{ hitotipoc.hitotipo.usuarioActualizo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaActualizacion">Fecha de actualizacion</label>
				  					<p class="form-control-static">{{ hitotipoc.hitotipo.fechaActualizacion }}</p>
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
						<shiro:hasPermission name="16020">
				        	<label class="btn btn-success" ng-click="form.$valid ? hitotipoc.guardar() : ''" ng-disabled="form.$invalid">Guardar</label>
				        </shiro:hasPermission>
				        <label class="btn btn-primary" ng-click="hitotipoc.irATabla()">Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
