<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="metaunidadmedidaController as metaunidadc" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="19010">
			<p ng-init="metaunidadc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<div class="panel panel-default">
		  <div class="panel-heading"><h3>Unidades de Medida de Metas</h3></div>
		</div>
		<div class="row" align="center" ng-hide="metaunidadc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="19040">
			       		<label class="btn btn-primary" ng-click="metaunidadc.nueva()">
							<span class="glyphicon glyphicon-plus"></span> Nuevo
						</label>
			       </shiro:hasPermission> 
			       <shiro:hasPermission name="19010">
			       		<label class="btn btn-primary" ng-click="metaunidadc.editar()">
			       		<span class="glyphicon glyphicon-pencil"></span> Editar
			       		</label>
			       	</shiro:hasPermission>
			       <shiro:hasPermission name="19030">
			       		<label class="btn btn-danger" ng-click="metaunidadc.borrar()">
			       		<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			       </shiro:hasPermission>
			        
			        
    			</div>				
    		</div>
    		<shiro:hasPermission name="19010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="metaunidadc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="metaunidadc.gridOptions" ui-grid-save-state 
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!metaunidadc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br/>
			<div class="total-rows">Total de {{  metaunidadc.totalmedidas + (metaunidadc.totalmedidas == 1 ? " unidad de medida" : " unidad de medida" ) }}</div>
				<ul uib-pagination total-items="metaunidadc.totalmedidas" 
						ng-model="metaunidadc.paginaActual" 
						max-size="metaunidadc.numeroMaximoPaginas" 
						items-per-page="metaunidadc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="metaunidadc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>
    		
		</div>
		<div class="row second-main-form" ng-show="metaunidadc.mostraringreso">
			<div class="page-header">
				<h2 ng-hide="!metaunidadc.esnueva"><small>Nueva Unidad de Medidad</small></h2>
				<h2 ng-hide="metaunidadc.esnueva"><small>Edición de Unidad de Medida</small></h2>
			</div>
			
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="19020">
			        	<label class="btn btn-success"  ng-click="form.$valid ? metaunidadc.guardar() : ''" ng-disabled="form.$invalid">Guardar</label>
			        </shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="metaunidadc.irATabla()">Ir a Tabla</label>
    			</div>
    		</div>
			
			<div class="col-sm-12">
				<form name="form">
						<div class="form-group" ng-show="!metaunidadc.esnueva">
    						<label for="id" class="floating-label">ID {{ metaunidadc.medida.id }}</label>
							<br/><br/>
						</div>
						<div class="form-group">
    						<input type="text" class="inputText" ng-model="metaunidadc.medida.nombre" ng-required="true"
    						value="{{metaunidadc.medida.nombre}}" onblur="this.setAttribute('value', this.value);">
    						<label class="floating-label">* Nombre</label>
						</div>
						<div class="form-group">
    						<input type="text" class="inputText"  ng-model="metaunidadc.medida.descripcion"
    						value="{{metaunidadc.medida.descripcion}}" onblur="this.setAttribute('value', this.value);">
    						<label class="floating-label">Descripción</label>
						</div>
						<div class="form-group">
    						<input type="text" class="inputText" ng-model="metaunidadc.medida.simbolo"
    						value="{{metaunidadc.medida.simbolo}}" onblur="this.setAttribute('value', this.value);">
    						<label class="floating-label">Símbolo</label>
						</div>
						<br/>
						<div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo">Usuario que creo</label> 
									<p class="form-control-static"> {{ metaunidadc.medida.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion">Fecha de creación</label>
									<p class="form-control-static" id="fechaCreacion"> {{ metaunidadc.medida.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo">Usuario que actualizo</label> 
									<p class="form-control-static" id="usuarioCreo">{{ metaunidadc.medida.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion">Fecha de actualizacion</label> 
									<p class="form-control-static" id="usuarioCreo">{{ metaunidadc.medida.fechaActualizacion }} </p>
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
						<shiro:hasPermission name="19020">
				        	<label class="btn btn-success" ng-click="form.$valid ? metaunidadc.guardar() : ''" ng-disabled="form.$invalid">Guardar</label>
						</shiro:hasPermission>
				        <label class="btn btn-primary" ng-click="metaunidadc.irATabla()">Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
