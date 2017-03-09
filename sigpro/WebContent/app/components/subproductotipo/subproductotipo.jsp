<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<div ng-controller="controlSubproductoTipo as subproductoTipo" class="maincontainer all_page">
	<script type="text/ng-template" id="buscarPropiedad.jsp">
	    <%@ include file="/app/components/subproductotipo/buscarPropiedad.jsp"%>
	</script>
	<shiro:lacksPermission name="23010">
		<p ng-init="subproductoTipo.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
	<h3>Tipo de Subproducto</h3>

	<br />

	<div align="center" ng-hide="subproductoTipo.esForma">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="23040">
					<label class="btn btn-primary" ng-click="subproductoTipo.nuevo()">Nuevo</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="23010">
					<label class="btn btn-primary" ng-click="subproductoTipo.editar()">Editar</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="23030">
					<label class="btn btn-primary" ng-click="subproductoTipo.borrar()">Borrar</label>
				</shiro:hasPermission>
			</div>
		</div>
		<shiro:hasPermission name="23010">
			<div class="col-sm-12" align="center">
				<div style="height: 35px;">
					<div style="text-align: right;">
						<div class="btn-group" role="group" aria-label="">
							<a class="btn btn-default" href ng-click="subproductoTipo.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left">
								<span class="glyphicon glyphicon-repeat" aria-hidden="true"></span>
							</a>
						</div>
					</div>
				</div>
				<br/>
				<div id="grid1" ui-grid="subproductoTipo.opcionesGrid"
					ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns
					ui-grid-selection ui-grid-pinning ui-grid-pagination>
					<div class="grid_loading" ng-hide="!subproductoTipo.mostrarCargando">
						<div class="msg">
							<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
								<br />
								<br /> <b>Cargando, por favor espere...</b> 
							</span>
						</div>
					</div>
				</div>
				<br/>
				<div class="total-rows">Total de {{  subproductoTipo.totalElementos + (subproductoTipo.totalElementos == 1 ? " Tipo de subproducto" : " Tipos de subproducto" ) }}</div>
				<ul uib-pagination total-items="subproductoTipo.totalElementos"
					ng-model="subproductoTipo.paginaActual"
					max-size="subproductoTipo.numeroMaximoPaginas"
					items-per-page="subproductoTipo.elementosPorPagina"
					first-text="Primero" last-text="Último" next-text="Siguiente"
					previous-text="Anterior" class="pagination-sm"
					boundary-links="true" force-ellipses="true"
					ng-change="subproductoTipo.cambioPagina()"></ul>
			</div>
		</shiro:hasPermission>

	</div>

	<div class="row main-form" ng-show="subproductoTipo.esForma">
		<h4 ng-hide="!subproductoTipo.esNuevo">Nuevo tipo de subproducto</h4>
	  	<h4 ng-hide="subproductoTipo.esNuevo">Edición de tipo de subproducto</h4>

		<div class="col-sm-12 operation_buttons" align="right">

			<div class="btn-group">
				<shiro:hasPermission name="23020">
					<label class="btn btn-success" ng-click="form.$valid ? subproductoTipo.guardar() : ''" ng-disabled="!form.$valid">Guardar</label> 
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="subproductoTipo.cancelar()">Ir a Tabla</label>
			</div>

		</div>

		<div class="col-sm-12">
			<form name="form" >

				<div class="form-group">
					<div class="form-group">
						<label for="campo0">Id</label>
						<p class="form-control-static">{{subproductoTipo.codigo}} </p>
					</div>
				</div>

				<div class="form-group">
					<label>* Nombre</label> 
					<input type="text" class="form-control" placeholder="Nombre de tipo" ng-model="subproductoTipo.nombre" ng-required="true" />
				</div>
				<div class="form-group">
					<label>Descripción</label> 
					<input type="text" class="form-control" placeholder="Descripcion de tipo" ng-model="subproductoTipo.descripcion"/>
				</div>
				
				<br />
	
				<h5>Propiedades</h5>
				<div align="center">
					
				<div style="height: 35px; width: 75%">
					<div style="text-align: right;">
						<div class="btn-group" role="group" aria-label="">
							<a class="btn btn-default" href
								ng-click="subproductoTipo.agregarPropiedad()" role="button"
								uib-tooltip="Asignar nueva propiedad" tooltip-placement="left">
								<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
							</a>
						</div>
					</div>
				</div>
				<br/>
				<table style="width: 75%;" st-table="subproductoTipo.propiedadesTipo" class="table table-striped  table-bordered">
					<thead >
						<tr>
							<th>ID</th>
							<th>Nombre</th>
							<th>Descripicon</th>
							<th>Tipo Dato</th>
							<th style="width: 30px;">Quitar</th>

						</tr>
					</thead>
					<tbody>
						<tr st-select-row="row" st-select-mode="single" ng-repeat="propiedad in subproductoTipo.propiedadesTipo | filter: { estado: '!E'} track by $index">
							<td>{{propiedad.idPropiedad}}</td>
							<td>{{propiedad.propiedad}}</td>
							<td>{{propiedad.descripcion}}</td>
							<td>{{propiedad.propiedadTipo}}</td>
							<td align="center">
								<button type="button"
									 ng-click="subproductoTipo.eliminarPropiedad($index)"
									class="btn btn-sm btn-danger">
									<i class="glyphicon glyphicon-minus-sign"> </i>
								</button>
						</tr>
					</tbody>
				</table>
				<div class="grid_loading" ng-hide="!subproductoTipo.mostrarCargando">
					<div class="msg">
						<span><i class="fa fa-spinner fa-spin fa-4x"></i> <br />
							<br /> <b>Cargando, por favor espere...</b> </span>
					</div>
				</div>
			</div>
			<br/>
			<div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo">Usuario que creo</label> 
									<p class="form-control-static"> {{ subproductoTipo.entidadSeleccionada.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion">Fecha de creación</label>
									<p class="form-control-static" id="fechaCreacion"> {{ subproductoTipo.entidadSeleccionada.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo">Usuario que actualizo</label> 
									<p class="form-control-static" id="usuarioCreo">{{ subproductoTipo.entidadSeleccionada.usuairoActulizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion">Fecha de actualizacion</label> 
									<p class="form-control-static" id="usuarioCreo">{{ subproductoTipo.entidadSeleccionada.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>

			</form>
		</div>
		<div class="col-sm-12" align="center">Los campos marcados con * son obligatorios</div>
		<br />
		
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="23020">
					<label class="btn btn-success" ng-click="form.$valid ? subproductoTipo.guardar() : ''" ng-disabled="!form.$valid">Guardar</label> 
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="subproductoTipo.cancelar()">Ir a Taba</label>
			</div>
		</div>
	</div>

</div>