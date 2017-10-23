<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<div ng-controller="categoriaAdquisicionController as controller" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="24010">
			<p ng-init="controller.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
   		<div class="panel panel-default">
			<div class="panel-heading"><h3>Categorías de Adquisiciones</h3></div>
		</div>	
		<div class="subtitulo">
			{{ controller.objetoTipoNombre }} {{ controller.objetoNombre }}
		</div>
		
		<div class="row" align="center" ng-hide="controller.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="1040">
							<label class="btn btn-primary" ng-click="controller.nuevo()" uib-tooltip="Nueva">
						    <span class="glyphicon glyphicon-plus"></span> Nueva</label>
						</shiro:hasPermission>
						<shiro:hasPermission name="1010">
						    <label class="btn btn-primary" ng-click="controller.editar()" uib-tooltip="Editar">
						    <span class="glyphicon glyphicon-pencil"></span> Editar</label>
						</shiro:hasPermission>
						<shiro:hasPermission name="1030">
						    <label class="btn btn-danger" ng-click="controller.borrar()" uib-tooltip="Borrar">
						    <span class="glyphicon glyphicon-trash"></span> Borrar</label>
						</shiro:hasPermission>
					</div>
				</div>
			
				<shiro:hasPermission name="1010">
	    		<div class="col-sm-12" align="center">
	    			<div style="height: 35px;">
						<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
							<a class="btn btn-default" href ng-click="controller.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
						</div>
						</div>
					</div>
					<br/>
					<div id="maingrid" ui-grid="controller.gridOptions" ui-grid-save-state
							ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
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
					<div class="total-rows">Total de {{  controller.totalCategoriaAdquisiciones + (controller.totalCategoriaAdquisiciones == 1 ? " Categoría de adquisicion" : " Categorias de adquisiciones" ) }}</div>-
					<ul uib-pagination total-items="controller.totalCategoriaAdquisiciones"
							ng-model="actividadc.paginaActual"
							max-size="actividadc.numeroMaximoPaginas"
							items-per-page="actividadc.elementosPorPagina"
							first-text="Primera"
							last-text="Última"
							next-text="Siguiente"
							previous-text="Anterior"
							class="pagination-sm" boundary-links="true" force-ellipses="true"
							ng-change="actividadc.cambioPagina()"
					></ul>
				</div>
	    	</shiro:hasPermission>
		</div>		
		<div class="row second-main-form" ng-hide="!controller.mostraringreso">
			<div class="page-header">
			    <h2 ng-hide="!controller.esnuevo"><small>Nueva categoria de adquisición</small></h2>
				<h2 ng-hide="controller.esnuevo"><small>Edición de categoria de adquisición</small></h2>
			</div>
			
			<div class="operation_buttons">
				<div class="btn-group" style="float: right;">
					<shiro:hasPermission name="24020">
						<label class="btn btn-success" ng-click="form.$valid ? controller.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
						<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
					</shiro:hasPermission>
					<label class="btn btn-primary" ng-click="controller.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
				</div>
			</div>

		
		<div class="col-sm-12">
			<form name="form">
				<div class="form-group">
					<label for="id" class="floating-label id_class">ID {{controller.categoriaAdquisicion.id }}</label>
					<br/><br/>
				</div>
				
				<div class="form-group">
   					<div class="form-group">
					   <input type="text" name="inombre" class="inputText" id="inombre" ng-model="controller.categoriaAdquisicion.nombre" ng-value="controller.categoriaAdquisicion.nombre"  onblur="this.setAttribute('value', this.value);" ng-required="true">
					   <label class="floating-label">* Nombre</label>
					</div>
					<div class="form-group">
					   <input type="text" name="inombre"  class="inputText" id="inombre" ng-model="controller.categoriaAdquisicion.descripcion" ng-value="controller.categoriaAdquisicion.descripcion"  onblur="this.setAttribute('value', this.value);">
					   <label class="floating-label">Descripción</label>
					</div>
				</div>
				<br>
				<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label class="label-form" for="usuarioCreo">Usuario que creo</label>
				  					<p>{{ controller.categoriaAdquisicion.usuarioCreo }}</pl>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label  class="label-form"  for="fechaCreacion">Fecha de creación</label>
				  					<p>{{ controller.categoriaAdquisicion.fechaCreacion }}</p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label  class="label-form" for="usuarioActualizo">Usuario que actualizo</label>
				  					<p>{{ controller.categoriaAdquisicion.usuarioActualizo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaActualizacion"  class="label-form" >Fecha de actualizacion</label>
				  					<p>{{ controller.categoriaAdquisicion.fechaActualizacion }}</p>
								</div>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
		<div class="col-sm-12 operation_buttons" align="right">
		<div align="center" class="label-form">Los campos marcados con * son obligatorios</div>
			<div class="btn-group">
				<shiro:hasPermission name="24020">
					<label class="btn btn-success" ng-click="form.$valid ? controller.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="controller.irATabla()" uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
	</div>
</div>