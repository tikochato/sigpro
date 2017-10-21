<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="prestamotipoController as prestamotipoc"
	class="maincontainer all_page" id="title">

  	<shiro:lacksPermission name="36010">
		<p ng-init="prestamotipoc.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>

	<div class="panel panel-default">
		<div class="panel-heading"><h3>Tipo de Préstamo</h3></div>
	</div>

	<div class="row" align="center" ng-if="!prestamotipoc.mostraringreso">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="36040">
					<label class="btn btn-primary" ng-click="prestamotipoc.nuevo()" uib-tooltip="Nuevo">
				<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="36020">
					<label class="btn btn-primary" ng-click="prestamotipoc.editar()" uib-tooltip="Editar">
				<span class="glyphicon glyphicon-pencil"></span> Editar</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="36030">
					<label class="btn btn-danger" ng-click="prestamotipoc.borrar()" uib-tooltip="Borrar">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
				</shiro:hasPermission>
			</div>
		</div>
		<shiro:hasPermission name="36010">
		<div class="col-sm-12" align="center">
			<div style="height: 35px;">
				<div style="text-align: right;">
					<div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href
							ng-click="prestamotipoc.reiniciarVista()" role="button"
							uib-tooltip="Reiniciar la vista de la tabla"
							tooltip-placement="left"><span
							class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
				</div>
			</div>
			<br />
			<div id="maingrid" ui-grid="prestamotipoc.gridOptions"
				ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns
				ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
				<div class="grid_loading" ng-hide="!prestamotipoc.mostrarcargando">
					<div class="msg">
						<span><i class="fa fa-spinner fa-spin fa-4x"></i> <br />
						<br /> <b>Cargando, por favor espere...</b> </span>
					</div>
				</div>
			</div>
			<br/>
			<div class="total-rows">Total de {{  prestamotipoc.totalPrestamotipos + (prestamotipoc.totalPrestamotipos == 1 ? " Tipo de préstamo" : " Tipos de préstamos" ) }}</div>
			<ul uib-pagination total-items="prestamotipoc.totalprestamotipos"
				ng-model="prestamotipoc.paginaActual"
				max-size="prestamotipoc.numeroMaximoPaginas"
				items-per-page="prestamotipoc.elementosPorPagina"
				first-text="Primero" last-text="Último" next-text="Siguiente"
				previous-text="Anterior" class="pagination-sm" boundary-links="true"
				force-ellipses="true" ng-change="prestamotipoc.cambioPagina()"></ul>
		</div>
		</shiro:hasPermission>	
	</div>

	<div class="row second-main-form" ng-if="prestamotipoc.mostraringreso">
		<div class="page-header">
			<h2 ng-hide="!prestamotipoc.esnuevo"><small>Nuevo tipo de {{etiquetas.proyecto}}</small></h2>
			<h2 ng-hide="prestamotipoc.esnuevo"><small>Edición tipo de {{etiquetas.proyecto}}</small></h2>
		</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="36020">
					<label class="btn btn-success" ng-click="form.$valid ? prestamotipoc.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="prestamotipoc.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
		<div class="col-sm-12">
			<form name="form" id="form">
				<div class="form-group" ng-show="!prestamotipoc.esnuevo">
					<label for="id" class="floating-label id_class">ID {{ prestamotipoc.prestamotipo.id }}</label>
					<br/><br/>
				</div>

				<div class="form-group">
				<input type="text" name="inombre"  class="inputText" id="inombre" ng-model="prestamotipoc.prestamotipo.nombre" ng-value="prestamotipoc.prestamotipo.nombre" onblur="this.setAttribute('value', this.value);" ng-required="true" >
			      <label class="floating-label">* Nombre</label>
				</div>
				<div class="form-group">
					<input type="text"
						class="inputText"  
						ng-model="prestamotipoc.prestamotipo.descripcion"
						 ng-value="prestamotipoc.prestamotipo.descripcion" onblur="this.setAttribute('value', this.value);" >
					<label  class="floating-label">Descripción</label>
				</div>
				<br />
				<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo" class="label-form">Usuario que creo</label> 
									<p> {{ prestamotipoc.prestamotipo.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion" class="label-form">Fecha de creación</label>
									<p id="fechaCreacion"> {{ prestamotipoc.prestamotipo.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo" class="label-form">Usuario que actualizo</label> 
									<p id="usuarioCreo">{{ prestamotipoc.prestamotipo.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion" class="label-form">Fecha de actualizacion</label> 
									<p id="usuarioCreo">{{ prestamotipoc.prestamotipo.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
		<br />
		<div align="center" class="label-form">Los campos marcados con * son obligatorios</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="36020">
					<label class="btn btn-success" ng-click="form.$valid ? prestamotipoc.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="prestamotipoc.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
	</div>
</div>
