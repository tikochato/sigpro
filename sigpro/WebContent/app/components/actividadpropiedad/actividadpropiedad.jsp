<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="actividadpropiedadController as actividadpropiedadc" class="maincontainer all_page" id="title">
		 <shiro:lacksPermission name="2010">
			<p ng-init="actividadpropiedadc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
		    <div class="panel-heading"><h3>Propiedades de Actividad</h3></div>
		</div>
		
		<div class="row" align="center" ng-hide="actividadpropiedadc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
			  <div class="btn-group">
			  <shiro:hasPermission name="2040">
			    <label class="btn btn-primary" ng-click="actividadpropiedadc.nuevo()" uib-tooltip="Nueva">
			    <span class="glyphicon glyphicon-plus"></span> Nueva</label>
			  </shiro:hasPermission>
			  <shiro:hasPermission name="2010">
			    <label class="btn btn-primary" ng-click="actividadpropiedadc.editar()" uib-tooltip="Editar">
			    <span class="glyphicon glyphicon-pencil"></span> Editar</label>
			  </shiro:hasPermission>
			  <shiro:hasPermission name="2030">
			    <label class="btn btn-danger" ng-click="actividadpropiedadc.borrar()" uib-tooltip="Borrar">
			    <span class="glyphicon glyphicon-trash"></span> Borrar</label>
			  </shiro:hasPermission>
			  </div>
			</div>
			
    		<shiro:hasPermission name="2010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="actividadpropiedadc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="actividadpropiedadc.gridOptions" ui-grid-save-state
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!actividadpropiedadc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br/>
				<div class="total-rows">Total de {{  actividadpropiedadc.totalActividadPropiedades + (actividadpropiedadc.totalActividadPropiedades == 1 ? " Propiedade de Actividad" : " Propiedades de Actividad" ) }}</div>
				<ul uib-pagination total-items="actividadpropiedadc.totalActividadPropiedades"
						ng-model="actividadpropiedadc.paginaActual"
						max-size="actividadpropiedadc.numeroMaximoPaginas"
						items-per-page="actividadpropiedadc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="actividadpropiedadc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>

		</div>
		<div class="row second-main-form" ng-show="actividadpropiedadc.mostraringreso">
			<div class="page-header">
				<h2 ng-hide="!actividadpropiedadc.esnuevo"><small>Nueva Propiedad</small></h2>
				<h2 ng-hide="actividadpropiedadc.esnuevo"><small>Edición de Propiedad</small></h2>
			</div>
			
    		<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="2020">
						<label class="btn btn-success" ng-click="form.$valid ? actividadpropiedadc.guardar() : ''" ng-disabled="!form.$valid" title="Guardar">
						<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
					</shiro:hasPermission>
					<label class="btn btn-primary" ng-click="actividadpropiedadc.irATabla()" title="Ir a Tabla">
					<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
				</div>
			</div>
			<div class="col-sm-12">
				<form name="form">
						<div class="form-group">
							<label for="id" class="floating-label">ID {{ actividadpropiedadc.actividadpropiedad.id }}</label>
							<br/><br/>
						</div>
						<div class="form-group">
						   <input type="text" name="inombre"  class="inputText" id="nombre" 
						     ng-model="actividadpropiedadc.actividadpropiedad.nombre" ng-value="actividadpropiedadc.actividadpropiedad.nombre"   
						     onblur="this.setAttribute('value', this.value);" ng-required="true" >
						   <label class="floating-label">* Nombre</label>
						</div>
						
						 <div class="form-group">
							<select class="inputText" ng-model="actividadpropiedadc.actividadpropiedad.datotipoid"
								ng-options="tipo as tipo.nombre for tipo in actividadpropiedadc.tipodatos track by tipo.id"
								ng-readonly="true"
								ng-disabled="!actividadpropiedadc.esnuevo" ng-required="true">
								<option value="">Seleccione una opción</option>
							</select>
						    <label for="nombre" class="floating-label">* Tipo dato</label>
						</div>
						<div class="form-group">
						   <input type="text" name="descripcion"  class="inputText" id="descripcion" 
						     ng-model="actividadpropiedadc.actividadpropiedad.descripcion" ng-value="actividadpropiedadc.actividadpropiedad.descripcion"   
						     onblur="this.setAttribute('value', this.value);"  >
						   <label class="floating-label">Descripción</label>
						</div>
						<br/>
						<div class="panel panel-default">
							<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
							<div class="panel-body">
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group" style="text-align: right">
											<label  class="label-form"  for="usuarioCreo">Usuario que creo</label>
				    						<p>{{ actividadpropiedadc.actividadpropiedad.usuarioCreo }}</p>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<label class="label-form"  for="fechaCreacion">Fecha de creación</label>
				    						<p>{{ actividadpropiedadc.actividadpropiedad.fechaCreacion }}</p>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group" style="text-align: right">
											<label  class="label-form"  for="usuarioActualizo">Usuario que actualizo</label>
				    						<p>{{ actividadpropiedadc.actividadpropiedad.usuarioActualizo }}</p>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<label  class="label-form"  for="fechaActualizacion">Fecha de actualizacion</label>
				    						<p>{{ actividadpropiedadc.actividadpropiedad.fechaActualizacion }}</p>
										</div>
									</div>
								</div>
							</div>
						</div>
				</form>
			</div>
			<div align="center" class="label-form">Los campos marcados con * son obligatorios</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="2020">
						<label class="btn btn-success" ng-click="form.$valid ? actividadpropiedadc.guardar() : ''" ng-disabled="!form.$valid" title="Guardar">
						<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
					</shiro:hasPermission>
					<label class="btn btn-primary" ng-click="actividadpropiedadc.irATabla()" title="Ir a Tabla">
					<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
				</div>
			</div>
		</div>
	</div>
