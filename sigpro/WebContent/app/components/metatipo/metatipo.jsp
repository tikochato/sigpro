<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="metatipoController as metatipoc" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="18010">
			<p ng-init="metatipoc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<div class="panel panel-default">
		  <div class="panel-heading"><h3>Tipos de Metas</h3></div>
		</div>
		
		<div class="row" align="center" ng-hide="metatipoc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="18040">
			       		<label class="btn btn-primary" ng-click="metatipoc.nueva()" uib-tooltip="Nuevo">
				<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			       </shiro:hasPermission> 
			       <shiro:hasPermission name="18010">
			       		<label class="btn btn-primary" ng-click="metatipoc.editar()" uib-tooltip="Editar">
						<span class="glyphicon glyphicon-pencil"></span> Editar</label>
			       	</shiro:hasPermission>
			       <shiro:hasPermission name="18030">
			       		<label class="btn btn-danger" ng-click="metatipoc.borrar()" uib-tooltip="Borrar">
						<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			       </shiro:hasPermission>
			        
			        
    			</div>				
    		</div>
    		<shiro:hasPermission name="18010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="metatipoc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="metatipoc.gridOptions" ui-grid-save-state 
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!metatipoc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br>
				<div class="total-rows">
				  Total de {{  metatipoc.totaltipos + (metatipoc.totaltipos == 1 ? " Tipo de Meta" : " Tipos de Meta" ) }}
				</div>
				<ul uib-pagination total-items="metatipoc.totaltipos" 
						ng-model="metatipoc.paginaActual" 
						max-size="metatipoc.numeroMaximoPaginas" 
						items-per-page="metatipoc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="metatipoc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>
    		
		</div>
		<div class="row second-main-form" ng-show="metatipoc.mostraringreso">
			<div class="page-header">
				<h2 ng-hide="!metatipoc.esnueva"><small>Nuevo Tipo Meta</small></h2>
				<h2 ng-hide="metatipoc.esnueva"><small>Edición de Tipo Meta</small></h2>
			</div>

			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="18020">
			        	<label class="btn btn-success"ng-click="form.$valid ? metatipoc.guardar(): ''" ng-disabled="form.$invalid"
						uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
					</shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="metatipoc.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
    			</div>
    		</div>
			
			<div class="col-sm-12">
				<form name="form">
						<div class="form-group" ng-show="!metatipoc.esnueva">
							<label for="id" class="floating-label">ID {{ metatipoc.tipo.id}}</label>
							<br/><br/>
						</div>
						<div class="form-group">
    						<input type="text" class="inputText"  ng-model="metatipoc.tipo.nombre" ng-required="true"
    						ng-value="metatipoc.tipo.nombre" onblur="this.setAttribute('value', this.value);"
    						show-focus="metatipoc.mostraringreso">
    						<label class="floating-label">* Nombre</label>
						</div>
						<div class="form-group">
							
    						<input type="text" class="inputText"  ng-model="metatipoc.tipo.descripcion" 
    						ng-value="metatipoc.tipo.descripcion" onblur="this.setAttribute('value', this.value);">
    						<label class="floating-label">Descripción</label>
						</div>
						<br/>
						<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label class="label-form">Usuario que creo</label> 
									<p> {{ metatipoc.tipo.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label class="label-form">Fecha de creación</label>
									<p> {{ metatipoc.tipo.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label class="label-form">Usuario que actualizo</label> 
									<p >{{ metatipoc.tipo.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label class="label-form">Fecha de actualizacion</label> 
									<p >{{ metatipoc.tipo.fechaActualizacion }} </p>
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
						<shiro:hasPermission name="18020">
				        	<label class="btn btn-success" ng-click="form.$valid ? metatipoc.guardar(): ''" ng-disabled="form.$invalid" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
						</shiro:hasPermission>
				        <label class="btn btn-primary" ng-click="metatipoc.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
