<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="subcomponentepropiedadController as subcomponentepropiedadc" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="6010">
			<p ng-init="subcomponentepropiedadc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
		    <div class="panel-heading"><h3>Propiedad de SubComponentes</h3></div>
		</div>
		<div class="row" align="center" ng-hide="subcomponentepropiedadc.mostraringreso">
			
    		<div class="col-sm-12 operation_buttons" align="right">
			  <div class="btn-group">
			  <shiro:hasPermission name="6040">
			    <label class="btn btn-primary" ng-click="subcomponentepropiedadc.nuevo()" uib-tooltip="Nuevo">
			    <span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			  </shiro:hasPermission>
			  <shiro:hasPermission name="6020">
			    <label class="btn btn-primary" ng-click="subcomponentepropiedadc.editar()" uib-tooltip="Editar">
			    <span class="glyphicon glyphicon-pencil"></span> Editar</label>
			  </shiro:hasPermission>
			  <shiro:hasPermission name="6030">
			    <label class="btn btn-danger" ng-click="subcomponentepropiedadc.borrar()" uib-tooltip="Borrar">
			    <span class="glyphicon glyphicon-trash"></span> Borrar</label>
			  </shiro:hasPermission>
			  </div>
			</div>
    		<shiro:hasPermission name="6010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="subcomponentepropiedadc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="subcomponentepropiedadc.gridOptions" ui-grid-save-state
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!subcomponentepropiedadc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br/>
			<div class="total-rows">Total de {{  subcomponentepropiedadc.totalSubComponentePropiedades + (subcomponentepropiedadc.totalSubComponentePropiedades == 1 ? " Propiedad de SubComponente" : " Propiedades de SubComponentes" ) }}</div>
				<ul uib-pagination total-items="subcomponentepropiedadc.totalSubComponentePropiedades"
						ng-model="subcomponentepropiedadc.paginaActual"
						max-size="subcomponentepropiedadc.numeroMaximoPaginas"
						items-per-page="subcomponentepropiedadc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="subcomponentepropiedadc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>

		</div>
		<div class="row second-main-form" ng-show="subcomponentepropiedadc.mostraringreso">	
			<div class="page-header">
			    <h2 ng-hide="!subcomponentepropiedadc.esnuevo"><small>Nueva Propiedad</small></h2>
			    <h2 ng-hide="subcomponentepropiedadc.esnuevo"><small>Edición de Propiedad</small></h2>
			</div>
    		<div class="operation_buttons" align="right">
			  <div class="btn-group">
			    <shiro:hasPermission name="6020">
			      <label class="btn btn-success" ng-click="form.$valid ? subcomponentepropiedadc.guardar() : ''" ng-disabled="!form.$valid" title="Guardar">
			      <span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			    </shiro:hasPermission>
			    <label class="btn btn-primary" ng-click="subcomponentepropiedadc.irATabla()" title="Ir a Tabla">
			    <span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			  </div>
			</div>

			<div class="col-sm-12">
				<form name="form">
						<div class="form-group">
							<label for="id" class="floating-label id_class">ID {{ subcomponentepropiedadc.subcomponentepropiedad.id }}</label>
							<br/><br/>
						</div>
						<div class="form-group">
						   <input type="text" name="inombre"  class="inputText" id="nombre" 
						     ng-model="subcomponentepropiedadc.subcomponentepropiedad.nombre" ng-value="subcomponentepropiedadc.subcomponentepropiedad.nombre"   
						     onblur="this.setAttribute('value', this.value);" ng-required="true">
						   <label class="floating-label">* Nombre</label>
						</div>
						<div class="form-group">
							<select class="inputText" ng-model="subcomponentepropiedadc.datotipo"
								ng-options="tipo as tipo.nombre for tipo in subcomponentepropiedadc.tipodatos track by tipo.id"
								ng-readonly="true"
								ng-disabled="!subcomponentepropiedadc.esnuevo" ng-required="true">
								<option value="">Seleccione una opción</option>
							</select>
						    <label for="nombre" class="floating-label">* Tipo dato</label>
						</div>
						<div class="form-group">
						   <input type="text" name="descripcion"  class="inputText" id="descripcion" 
						     ng-model="subcomponentepropiedadc.subcomponentepropiedad.descripcion" ng-value="subcomponentepropiedadc.subcomponentepropiedad.descripcion"   
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
											<label for="usuarioCreo" class="label-form" >Usuario que creo</label>
				    						<p>{{ subcomponentepropiedadc.subcomponentepropiedad.usuarioCreo }}</p>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<label class="label-form" for="fechaCreacion">Fecha de creación</label>
				    						<p>{{ subcomponentepropiedadc.subcomponentepropiedad.fechaCreacion }}</p>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group" style="text-align: right">
											<label class="label-form"  for="usuarioActualizo">Usuario que actualizo</label>
				    						<p>{{ subcomponentepropiedadc.subcomponentepropiedad.usuarioActualizo }}</p>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<label for="fechaActualizacion" class="label-form" >Fecha de actualizacion</label>
				    						<p>{{ subcomponentepropiedadc.subcomponentepropiedad.fechaActualizacion }}</p>
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
			    <shiro:hasPermission name="6020">
			      <label class="btn btn-success" ng-click="form.$valid ? subcomponentepropiedadc.guardar() : ''" ng-disabled="!form.$valid" title="Guardar">
			      <span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			    </shiro:hasPermission>
			    <label class="btn btn-primary" ng-click="subcomponentepropiedadc.irATabla()" title="Ir a Tabla">
			    <span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			  </div>
			</div>
		</div>
	</div>
