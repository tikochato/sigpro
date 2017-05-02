<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="componentepropiedadController as componentepropiedadc" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="6010">
			<p ng-init="componentepropiedadc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<div class="panel panel-default">
		    <div class="panel-heading"><h3>Propiedad de Componentes</h3></div>
		</div>
		<div class="row" align="center" ng-hide="componentepropiedadc.mostraringreso">
			
    		<div class="col-sm-12 operation_buttons" align="right">
			  <div class="btn-group">
			  <shiro:hasPermission name="6040">
			    <label class="btn btn-primary" ng-click="componentepropiedadc.nuevo()" uib-tooltip="Nuevo">
			    <span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			  </shiro:hasPermission>
			  <shiro:hasPermission name="6010">
			    <label class="btn btn-primary" ng-click="componentepropiedadc.editar()" uib-tooltip="Editar">
			    <span class="glyphicon glyphicon-pencil"></span> Editar</label>
			  </shiro:hasPermission>
			  <shiro:hasPermission name="6030">
			    <label class="btn btn-danger" ng-click="componentepropiedadc.borrar()" uib-tooltip="Borrar">
			    <span class="glyphicon glyphicon-trash"></span> Borrar</label>
			  </shiro:hasPermission>
			  </div>
			</div>
    		<shiro:hasPermission name="6010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="componentepropiedadc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="componentepropiedadc.gridOptions" ui-grid-save-state
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!componentepropiedadc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br/>
			<div class="total-rows">Total de {{  componentepropiedadc.totalComponentePropiedades + (componentepropiedadc.totalComponentePropiedades == 1 ? " Componente" : " Componentes" ) }}</div>
				<ul uib-pagination total-items="componentepropiedadc.totalComponentePropiedades"
						ng-model="componentepropiedadc.paginaActual"
						max-size="componentepropiedadc.numeroMaximoPaginas"
						items-per-page="componentepropiedadc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="componentepropiedadc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>

		</div>
		<div class="row second-main-form" ng-show="componentepropiedadc.mostraringreso">	
			<div class="page-header">
			    <h2 ng-hide="!componentepropiedadc.esnuevo"><small>Nueva Propiedad</small></h2>
			    <h2 ng-hide="componentepropiedadc.esnuevo"><small>Edición de Propiedad</small></h2>
			</div>
    		<div class="operation_buttons" align="right">
			  <div class="btn-group">
			    <shiro:hasPermission name="6020">
			      <label class="btn btn-success" ng-click="form.$valid ? componentepropiedadc.guardar() : ''" ng-disabled="!form.$valid" title="Guardar">
			      <span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			    </shiro:hasPermission>
			    <label class="btn btn-primary" ng-click="componentepropiedadc.irATabla()" title="Ir a Tabla">
			    <span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			  </div>
			</div>

			<div class="col-sm-12">
				<form name="form">
						<div class="form-group">
							<label for="id" class="floating-label">ID {{ componentepropiedadc.componentepropiedad.id }}</label>
							<br/><br/>
						</div>
						<div class="form-group">
						   <input type="text" name="inombre"  class="inputText" id="nombre" 
						     ng-model="componentepropiedadc.componentepropiedad.nombre" value="{{componentepropiedadc.componentepropiedad.nombre}}"   
						     onblur="this.setAttribute('value', this.value);" ng-required="true" >
						   <label class="floating-label">* Nombre</label>
						</div>
						<div class="form-group">
							<select class="inputText" ng-model="componentepropiedadc.datotipo"
								ng-options="tipo as tipo.nombre for tipo in componentepropiedadc.tipodatos track by tipo.id"
								ng-readonly="true"
								ng-disabled="!componentepropiedadc.esnuevo" ng-required="true">
								<option value="">Seleccione una opción</option>
							</select>
						    <label for="nombre" class="floating-label">* Tipo dato</label>
						</div>
						<div class="form-group">
						   <input type="text" name="descripcion"  class="inputText" id="descripcion" 
						     ng-model="componentepropiedadc.componentepropiedad.descripcion" value="{{componentepropiedadc.componentepropiedad.descripcion}}"   
						     onblur="this.setAttribute('value', this.value);"  >
						   <label class="floating-label">Descripción</label>
						</div>
						<br/>
						<div class="panel panel-default">
							<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
							<div class="panel-body">
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group" style="text-align: right">
											<label for="usuarioCreo">Usuario que creo</label>
				    						<p class="form-control-static">{{ componentepropiedadc.componentepropiedad.usuarioCreo }}</p>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<label for="fechaCreacion">Fecha de creación</label>
				    						<p class="form-control-static">{{ componentepropiedadc.componentepropiedad.fechaCreacion }}</p>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group" style="text-align: right">
											<label for="usuarioActualizo">Usuario que actualizo</label>
				    						<p class="form-control-static">{{ componentepropiedadc.componentepropiedad.usuarioActualizo }}</p>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<label for="fechaActualizacion">Fecha de actualizacion</label>
				    						<p class="form-control-static">{{ componentepropiedadc.componentepropiedad.fechaActualizacion }}</p>
										</div>
									</div>
								</div>
							</div>
						</div>
				</form>
			</div>
			<div align="center">Los campos marcados con * son obligatorios</div>
    		<div class="col-sm-12 operation_buttons" align="right">
			  <div class="btn-group">
			    <shiro:hasPermission name="6020">
			      <label class="btn btn-success" ng-click="form.$valid ? componentepropiedadc.guardar() : ''" ng-disabled="!form.$valid" title="Guardar">
			      <span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			    </shiro:hasPermission>
			    <label class="btn btn-primary" ng-click="componentepropiedadc.irATabla()" title="Ir a Tabla">
			    <span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			  </div>
			</div>
		</div>
	</div>
