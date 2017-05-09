<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="formularioitemtipoController as formularioitemtipoc" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="13010">
			<p ng-init="formularioitemtipoc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<div class="panel panel-default">
		  <div class="panel-heading"><h3>Tipo Item de Formulario</h3></div>
		</div>
		
		<div class="row" align="center" ng-if="!formularioitemtipoc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="13040">
			       		<label class="btn btn-primary" ng-click="formularioitemtipoc.nuevo()">
			       		<span class="glyphicon glyphicon-plus"> </span> Nuevo</label>
			       		
			       </shiro:hasPermission> 
			       <shiro:hasPermission name="13010"><label class="btn btn-primary" ng-click="formularioitemtipoc.editar()">
			       <span class="glyphicon glyphicon-pencil"></span> Editar</label></shiro:hasPermission>
			       <shiro:hasPermission name="13030">
			       		<label class="btn btn-danger" ng-click="formularioitemtipoc.borrar()">
			       		<span class="glyphicon glyphicon-trash"></span> Borrar</label>
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
		<div class="row second-main-form" ng-show="formularioitemtipoc.mostraringreso">
			<div class="page-header">
				<h2 ng-hide="!formularioitemtipoc.esnuevo"><small>Nuevo Tipo de Item de Formulario</small></h2>
				<h2 ng-hide="formularioitemtipoc.esnuevo"><small>Edición de Tipo de Item de Formulario</small></h2>
			</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="13020">
			        	<label class="btn btn-success" ng-click="form.$valid ? formularioitemtipoc.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			        </shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="formularioitemtipoc.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
    			</div>
    		</div>
			
			<div class="col-sm-12">
				<form name="form">
						<div class="form-group">
    						<label for="id" class="floating-label">ID {{ formularioitemtipoc.formularioitemtipo.id }}</label>
					<br/><br/>
						</div>
						<div class="form-group">
    						<input type="text"  class="inputText"  ng-model="formularioitemtipoc.formularioitemtipo.nombre"
    						ng-value="formularioitemtipoc.formularioitemtipo.nombre" onblur="this.setAttribute('value', this.value);">
    						<label class="floating-label">* Nombre</label>
						</div>
						<div class="form-group">
							
							<select class="inputText" ng-model="formularioitemtipoc.formularioitemtipo.datotipo"
								ng-options="tipo as tipo.nombre for tipo in formularioitemtipoc.tipodatos track by tipo.id"
								ng-readonly="true" 
								ng-disabled="!formularioitemtipoc.esnuevo" >
								<option value="">Seleccione una opción</option>
							</select>
							<label class="floating-label">* Tipo dato</label>
						</div>
						<div class="form-group">
    						<input type="text" class="inputText" ng-model="formularioitemtipoc.formularioitemtipo.descripcion" 
    						ng-value="formularioitemtipoc.formularioitemtipo.descripcion" onblur="this.setAttribute('value', this.value);">
    						<label class="floating-label">Descripción</label>
						</div>
					<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo" class="label-form">Usuario que creo</label> 
									<p > {{ formularioitemtipoc.formularioitemtipo.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion" class="label-form">Fecha de creación</label>
									<p > {{ formularioitemtipoc.formularioitemtipo.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo" class="label-form">Usuario que actualizo</label> 
									<p >{{ formularioitemtipoc.formularioitemtipo.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion" class="label-form">Fecha de actualizacion</label> 
									<p >{{ formularioitemtipoc.formularioitemtipo.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>
				</form>
			</div>
			<div align="center" class="label-form">Los campos marcados con * son obligatorios</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="col-sm-12 operation_buttons" align="right">
					<div class="btn-group">
						<shiro:hasPermission name="13020">
				        	<label class="btn btn-success"  ng-click="form.$valid ? formularioitemtipoc.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				        </shiro:hasPermission>
				        <label class="btn btn-primary" ng-click="formularioitemtipoc.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
