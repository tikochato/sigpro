<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="responsabletipoController as controller"
	class="maincontainer all_page" id="title">
	
	<div class="panel panel-default">
	  <div class="panel-heading"><h3>Tipo de responsable</h3></div>
	</div>
	
	<div align="center" ng-hide="controller.esColapsado">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
			<shiro:hasPermission name="24040">
				<label class="btn btn-primary" ng-click="controller.nuevo()" uib-tooltip="Nuevo">
				<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="24010">
				<label class="btn btn-primary" ng-click="controller.editar()" uib-tooltip="Editar">
				<span class="glyphicon glyphicon-pencil"></span> Editar</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="24030">
				<label class="btn btn-danger" ng-click="controller.borrar()" uib-tooltip="Borrar">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			</shiro:hasPermission>
			</div>
		</div>
		<shiro:hasPermission name="24010">
			<div class="col-sm-12" align="center">
				<div style="height: 35px;">
					<div style="text-align: right;">
						<div class="btn-group" role="group" aria-label="">
							<a class="btn btn-default" href ng-click="controller.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
						</div>
					</div>
				</div>
				<div id="maingrid" ui-grid="controller.gridOpciones" ui-grid-save-state 
					ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!controller.mostrarcargando">
					  	<div class="msg">
					      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
							  <br /><br />
							  <b>Cargando, por favor espere...</b>
						  </span>
						</div>
					  </div>
				</div>
				<br/>
				<div class="total-rows">
					Total de {{  controller.totalResponsablesTipos + (controller.totalResponsablesTipos == 1 ? " Tipo Responsable" : " Tipo Responsable") }}
				</div>
				<ul uib-pagination total-items="controller.totalResponsablesTipos"
						ng-model="controller.paginaActual"
						max-size="controller.numeroMaximoPaginas"
						items-per-page="controller.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="controller.cambioPagina()"
				></ul>
			</div>
		</shiro:hasPermission>
	</div>
	<div class="row second-main-form" ng-show="controller.esColapsado">
		<div class="page-header">
			<h2 ng-hide="!controller.esNuevo"><small>Nuevo tipo responsable</small></h2>
			<h2 ng-hide="controller.esNuevo"><small>Edición tipo responsable</small></h2>
		</div>
		<div class="operation_buttons">
			<div class="btn-group" style="float: right;">
				<shiro:hasPermission name="24020">
					<label class="btn btn-success" ng-click="form.$valid ? controller.guardar(form.$valid) : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="controller.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
		<br>
		<div class="col-sm-12">
			<form name="form">
				<div class="form-group">
					<label for="id" class="floating-label id_class">ID {{ controller.responsableTipo.id }}</label>
					<br/><br/>
				</div>
				
				<div class="form-group">
			      <input type="text" name="inombre"  class="inputText" id="nombre" ng-model="controller.responsableTipo.nombre" ng-value="controller.responsableTipo.nombre" onblur="this.setAttribute('value', this.value);" ng-required="true" >
			      <label class="floating-label">* Nombre</label>
				</div>
				
				<div class="form-group">
				   <textarea class="inputText" rows="4"
				   ng-model="controller.responsableTipo.descripcion" ng-value="controller.responsableTipo.descripcion"   
				   onblur="this.setAttribute('value', this.value);" ng-required="false" ></textarea>
				   <label class="floating-label">Descripción</label>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo" class="label-form">Usuario que creo</label>
				  					<p class="">{{ controller.responsableTipo.usuarioCreo }}</pl>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaCreacion" class="label-form">Fecha de creación</label>
				  					<p class="">{{ controller.responsableTipo.fechaCreacion }}</p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo" class="label-form">Usuario que actualizo</label>
				  					<p class="">{{ controller.responsableTipo.usuarioActualizo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaActualizacion" class="label-form">Fecha de actualizacion</label>
				  					<p class="">{{ controller.responsableTipo.fechaActualizacion }}</p>
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
	       				<label class="btn btn-success" ng-click="form.$valid ? controller.guardar(): ''" ng-disabled="form.$invalid" uib-tooltip="Guardar" tooltip-placement="bottom">
						<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
					</shiro:hasPermission>
	        		<label class="btn btn-primary" ng-click="controller.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
	 			</div>
	 		</div>
		</div>
	</div>
</div>