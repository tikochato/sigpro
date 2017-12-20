<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="formulariotipoController as formulariotipoc"
	class="maincontainer all_page" id="title">
	<shiro:lacksPermission name="14010">
		<p ng-init="formulariotipoc.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
	<div class="panel panel-default">
	  <div class="panel-heading"><h3>Tipo de Formulario</h3></div>
	</div>

	<div class="row" align="center" ng-if="!formulariotipoc.mostraringreso">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="14040">
					<label class="btn btn-primary" ng-click="formulariotipoc.nuevo()" uib-tooltip="Nuevo">
				<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="14010">
					<label class="btn btn-primary" ng-click="formulariotipoc.editar()" uib-tooltip="Editar">
				<span class="glyphicon glyphicon-pencil"></span> Editar</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="14030">
					<label class="btn btn-danger" ng-click="formulariotipoc.borrar()" uib-tooltip="Borrar">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
				</shiro:hasPermission>
			</div>
		</div>
		<shiro:hasPermission name="14010">
			<div class="col-sm-12" align="center">
			<div style="height: 35px;">
				<div style="text-align: right;">
					<div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href
							ng-click="formulariotipoc.reiniciarVista()" role="button"
							uib-tooltip="Reiniciar la vista de la tabla"
							tooltip-placement="left"><span
							class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
				</div>
			</div>
			<br />
			<div id="maingrid" ui-grid="formulariotipoc.gridOptions"
				ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns
				ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
				<div class="grid_loading" ng-hide="!formulariotipoc.mostrarcargando">
					<div class="msg">
						<span><i class="fa fa-spinner fa-spin fa-4x"></i> <br />
						<br /> <b>Cargando, por favor espere...</b> </span>
					</div>
				</div>
			</div>
			<ul uib-pagination total-items="formulariotipoc.totalFormularios"
				ng-model="formulariotipoc.paginaActual"
				max-size="formulariotipoc.numeroMaximoPaginas"
				items-per-page="formulariotipoc.elementosPorPagina"
				first-text="Primero" last-text="Último" next-text="Siguiente"
				previous-text="Anterior" class="pagination-sm" boundary-links="true"
				force-ellipses="true" ng-change="formulariotipoc.cambioPagina()"></ul>
		</div>
		</shiro:hasPermission>
		
	</div>

	<div class="row second-main-form" ng-show="formulariotipoc.mostraringreso">
		<div class="page-header">
			<h2 ng-hide="!formulariotipoc.esnuevo">Nuevo Tipo Riesgo</h2>
			<h2 ng-hide="formulariotipoc.esnuevo">Edición de Tipo Riesgo</h2>
		</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="14020">
					<label class="btn btn-success" ng-click="form.$valid ? formulariotipoc.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="formulariotipoc.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
		<div class="col-sm-12">
			<form name="form">
				<div class="form-group">
					<label for="id" class="floating-label">ID {{ formulariotipoc.formulariotipo.id  }}</label>
					<br/><br/>
				</div>

				<div class="form-group">					 
					<input type="text"  class="inputText" ng-model="formulariotipoc.formulariotipo.nombre" ng-required="true"
					ng-value="formulariotipoc.formulariotipo.nombre" onblur="this.setAttribute('value', this.value);">
					<label class="floating-label">* Nombre</label>
				</div>
				<div class="form-group">
				   <textarea class="inputText" rows="4"
				   ng-model="formulariotipoc.formulariotipo.descripcion" ng-value="formulariotipoc.formulariotipo.descripcion"   
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
									<p> {{ formulariotipoc.formulariotipo.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label  class="label-form">Fecha de creación</label>
									<p> {{ formulariotipoc.formulariotipo.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo" class="label-form">Usuario que actualizo</label> 
									<p >{{ formulariotipoc.formulariotipo.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion" class="label-form">Fecha de actualizacion</label> 
									<p>{{ formulariotipoc.formulariotipo.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<br />
			</form>
		</div>
		<br />
		<div align="center" class="label-form">Los campos marcados con * son obligatorios</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="14020">
					<label class="btn btn-success" ng-click="form.$valid ? formulariotipoc.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="formulariotipoc.irATabla()" uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
	</div>
</div>
