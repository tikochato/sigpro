<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="recursounidadmedidaController as recursounidadc" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="29010">
			<p ng-init="recursounidadc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<div class="panel panel-default">
	  		<div class="panel-heading"><h3>Unidades de Medida de Recursos</h3><br/></div>
		</div>
		<div class="row" align="center" ng-hide="recursounidadc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="29040">
			       		<label class="btn btn-primary" ng-click="recursounidadc.nueva()" uib-tooltip="Nuevo">
			       		<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			       </shiro:hasPermission> 
			       <shiro:hasPermission name="29010"><label class="btn btn-primary" ng-click="recursounidadc.editar()" uib-tooltip="Editar">
						<span class="glyphicon glyphicon-pencil"></span> Editar</label>
			       </shiro:hasPermission>
			       <shiro:hasPermission name="29030">
			       		<label class="btn btn-danger" ng-click="recursounidadc.borrar()"  uib-tooltip="Borrar">
						<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			       </shiro:hasPermission>
    			</div>				
    		</div>
    		<shiro:hasPermission name="29010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="recursounidadc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="recursounidadc.gridOptions" ui-grid-save-state 
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!recursounidadc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br/>
				<div class="total-rows">Total de {{  recursounidadc.totalmedidas + (recursounidadc.totalmedidas == 1 ? " Unidad de Medida" : " Unidades de Medida" ) }}</div>
				<ul uib-pagination total-items="recursounidadc.totalmedidas" 
						ng-model="recursounidadc.paginaActual" 
						max-size="recursounidadc.numeroMaximoPaginas" 
						items-per-page="recursounidadc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="recursounidadc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>
    		
		</div>
		<div class="row main-form" ng-show="recursounidadc.mostraringreso">
			<h2 ng-hide="!recursounidadc.esnueva"><small>Nueva Unidad de Medidad</small></h2>
			<h2 ng-hide="recursounidadc.esnueva"><small>Edición de Unidad de Medida</small></h2>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="29020">
			        	<label class="btn btn-success" ng-click="form.$valid ? recursounidadc.guardar() : ''" ng-disabled="form.$invalid" uib-tooltip="Guardar">
						<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
					</shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="recursounidadc.irATabla()" uib-tooltip="Ir a Tabla">
					<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
    			</div>
    		</div>
			
			<div class="col-sm-12">
				<form name="form">
						<div class="form-group" ng-show="!recursounidadc.esnueva">
							<label for="id" class="floating-label">ID {{ recursounidadc.medida.id }}</label>
    						<br/><br/>
						</div>
						<div class="form-group">
    						<input type="text" class="inputText" id="nombre" ng-model="recursounidadc.medida.nombre" value="{{recursounidadc.medida.nombre}}"onblur="this.setAttribute('value', this.value);"  ng-required="true">
    						<label class="floating-label">* Nombre</label>
						</div>
						<div class="form-group">
    						<input type="text" class="inputText" id="simbolo" onblur="this.setAttribute('value', this.value);" ng-model="recursounidadc.medida.simbolo" value="{{recursounidadc.medida.simbolo}}">
    						<label class="floating-label">Símbolo</label>
						</div>
						<div class="form-group">
    						<input type="text" class="inputText" id="descripcion" onblur="this.setAttribute('value', this.value);" ng-model="recursounidadc.medida.descripcion" value="{{recursounidadc.medida.descripcion}}">
    						<label class="floating-label">Descripción</label>
						</div>
						<br/>
						<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo"  class="label-form">Usuario que creo</label> 
									<p> {{ recursounidadc.medida.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label  class="label-form" for="fechaCreacion">Fecha de creación</label>
									<p id="fechaCreacion"> {{ recursounidadc.medida.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label  class="label-form" for="usuarioActualizo">Usuario que actualizo</label> 
									<p id="usuarioCreo">{{ recursounidadc.medida.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label  class="label-form" for="fechaActualizacion">Fecha de actualizacion</label> 
									<p id="usuarioCreo">{{ recursounidadc.medida.fechaActualizacion }} </p>
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
				        <shiro:hasPermission name="29020">
				        	<label class="btn btn-success" ng-click="form.$valid ? recursounidadc.guardar() : ''" ng-disabled="form.$invalid" uib-tooltip="Guardar">
							<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
						</shiro:hasPermission>
				        <label class="btn btn-primary" ng-click="recursounidadc.irATabla()" uib-tooltip="Ir a Tabla">
						<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
