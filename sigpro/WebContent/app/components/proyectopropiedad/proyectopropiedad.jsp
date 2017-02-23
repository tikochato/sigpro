<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="proyectopropiedadController as proyectopropiedadc" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="25010">
			<p ng-init="proyectopropiedadc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<h3>Propiedades de Proyecto</h3><br/>
		<div class="row" align="center" ng-hide="proyectopropiedadc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="25040">
			       		<label class="btn btn-primary" ng-click="proyectopropiedadc.nuevo()">Nuevo</label>
			       </shiro:hasPermission>
			       <shiro:hasPermission name="25010"><label class="btn btn-primary" ng-click="proyectopropiedadc.editar()">Editar</label></shiro:hasPermission>
			       <shiro:hasPermission name="25030">
			       		<label class="btn btn-primary" ng-click="proyectopropiedadc.borrar()">Borrar</label>
			       </shiro:hasPermission>
    			</div>
    		</div>
    		<shiro:hasPermission name="25010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="proyectopropiedadc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left">
							<span class="glyphicon glyphicon-repeat" aria-hidden="true"></span>
						</a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="proyectopropiedadc.gridOptions" ui-grid-save-state
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!proyectopropiedadc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br/>
				<div class="total-rows">Total de {{  proyectopropiedadc.totalProyectoPropiedades + (proyectopropiedadc.totalProyectoPropiedades == 1 ? " Propiedad" : " Propiedades" ) }}</div>
				<ul uib-pagination total-items="proyectopropiedadc.totalProyectoPropiedades"
						ng-model="proyectopropiedadc.paginaActual"
						max-size="proyectopropiedadc.numeroMaximoPaginas"
						items-per-page="proyectopropiedadc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="proyectopropiedadc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>

		</div>
		<div class="row main-form" ng-show="proyectopropiedadc.mostraringreso">
			<h4 ng-hide="!proyectopropiedadc.esnuevo">Nueva Propiedad</h4>
			<h4 ng-hide="proyectopropiedadc.esnuevo">Edición de Propiedad</h4>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="25020">					
			        	<label class="btn btn-success" ng-click="form.$valid ? proyectopropiedadc.guardar() : ''" ng-disabled="!form.$valid">Guardar</label>
					</shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="proyectopropiedadc.irATabla()">Ir a Tabla</label>
    			</div>
    		</div>

			<div class="col-sm-12">
				<form name="form">
						<div class="form-group">
							<label for="id">ID</label>
    						<label class="form-control" id="id">{{ proyectopropiedadc.proyectopropiedad.id }}</label>
						</div>
						<div class="form-group">
							<label for="nombre">* Nombre</label>
    						<input type="text" class="form-control" id="nombre" placeholder="Nombre" ng-model="proyectopropiedadc.proyectopropiedad.nombre" ng-required="true">
						</div>
						<div class="form-group">
							<label for="nombre">* Tipo dato</label>
							<select class="form-control" ng-model="proyectopropiedadc.proyectopropiedad.datotipoid"
								ng-options="tipo as tipo.nombre for tipo in proyectopropiedadc.tipodatos track by tipo.id"
								ng-readonly="true"
								ng-disabled="!proyectopropiedadc.esnuevo" ng-required="true">
								<option value="">Seleccione una opción</option>
							</select>
						</div>
						<div class="form-group">
							<label for="descripcion">Descripción</label>
    						<input type="text" class="form-control" id="descripcion" placeholder="Descripción" ng-model="proyectopropiedadc.proyectopropiedad.descripcion">
						</div>
						<br/>
						<div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo">Usuario que creo</label> 
									<p class="form-control-static"> {{ proyectopropiedadc.proyectopropiedad.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion">Fecha de creación</label>
									<p class="form-control-static" id="fechaCreacion"> {{ proyectopropiedadc.proyectopropiedad.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo">Usuario que actualizo</label> 
									<p class="form-control-static" id="usuarioCreo">{{ proyectopropiedadc.proyectopropiedad.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion">Fecha de actualizacion</label> 
									<p class="form-control-static" id="usuarioCreo">{{ proyectopropiedadc.proyectopropiedad.fechaActualizacion }} </p>
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
						<shiro:hasPermission name="25020">					
			        		<label class="btn btn-success" ng-click="form.$valid ? proyectopropiedadc.guardar() : ''" ng-disabled="!form.$valid">Guardar</label>
						</shiro:hasPermission>
				        <label class="btn btn-primary" ng-click="proyectopropiedadc.irATabla()">Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
