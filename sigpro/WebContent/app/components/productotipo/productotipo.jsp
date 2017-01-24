<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<div ng-controller="controlProductoTipo as productoTipo" class="maincontainer all_page">
	<script type="text/ng-template" id="buscarPropiedad.jsp">
	    <%@ include file="/app/components/productotipo/buscarPropiedad.jsp"%>
	</script>

	<h3>{{ productoTipo.esForma ? (productoTipo.esNuevo ? "Nuevo Tipo de Producto" : "Editar Tipo de Producto") : "Tipo de Producto" }}</h3>

	<br />

	<div align="center" ng-hide="productoTipo.esForma">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="crearTipoProducto">
					<label class="btn btn-primary" ng-click="productoTipo.nuevo()">Nuevo</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="editarTipoProducto">
					<label class="btn btn-primary" ng-click="productoTipo.editar()">Editar</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="eliminarTipoProducto">
					<label class="btn btn-primary" ng-click="productoTipo.borrar()">Borrar</label>
				</shiro:hasPermission>
			</div>
		</div>
		<shiro:hasPermission name="verTipoProducto">
			<div class="col-sm-12" align="center">
				<div style="height: 35px;">
					<div style="text-align: right;">
						<div class="btn-group" role="group" aria-label="">
							<a class="btn btn-default" href ng-click="productoTipo.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left">
								<span class="glyphicon glyphicon-repeat" aria-hidden="true"></span>
							</a>
						</div>
					</div>
				</div>
				<div id="grid1" ui-grid="productoTipo.opcionesGrid"
					ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns
					ui-grid-selection ui-grid-pinning ui-grid-pagination>
					<div class="grid_loading" ng-hide="!productoTipo.mostrarCargando">
						<div class="msg">
							<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
								<br />
								<br /> <b>Cargando, por favor espere...</b> 
							</span>
						</div>
					</div>
				</div>
				<ul uib-pagination total-items="productoTipo.totalElementos"
					ng-model="productoTipo.paginaActual"
					max-size="productoTipo.numeroMaximoPaginas"
					items-per-page="productoTipo.elementosPorPagina"
					first-text="Primero" last-text="Último" next-text="Siguiente"
					previous-text="Anterior" class="pagination-sm"
					boundary-links="true" force-ellipses="true"
					ng-change="productoTipo.cambioPagina()"></ul>
			</div>
		</shiro:hasPermission>

	</div>

	<div ng-show="productoTipo.esForma">

		<div class="col-sm-12 operation_buttons" align="right">

			<div class="btn-group">
				<label class="btn btn-success" ng-click="form.$valid ? productoTipo.guardar() : ''" ng-disabled="!form.$valid">Guardar</label> 
				<label class="btn btn-primary" ng-click="productoTipo.cancelar()">Ir a Tabla</label>
			</div>

		</div>

		<div>
			<form name="form" class="css-form" novalidate>

				<div class="row">
					<div class="form-group col-sm-3" ng-show="!productoTipo.esNuevo">
						<label for="campo0">ID:</label> <input type="text" class="form-control" id="campo0" name="campo0" placeholder="ID" ng-model="productoTipo.codigo" ng-readonly="true" />
					</div>
				</div>

				<div class="row">
					<div class="form-group col-sm-12"
						ng-class="{ 'has-error' : form.campo1.$invalid }">
						<label for="campo1">* Nombre:</label> 
						<input type="text" class="form-control" id="campo1" name="campo1" placeholder="Nombre de tipo" ng-model="productoTipo.nombre" required />
					</div>

					<div class="form-group col-sm-12"
						ng-class="{ 'has-error' : form.campo3.$invalid }">
						<label for="campo3">* Descripción:</label> 
						<input type="text" class="form-control" id="campo3" name="campo3" placeholder="Descripcion de tipo" ng-model="productoTipo.descripcion" required />
					</div>
				</div>
			</form>
		</div>

		<br />

		<h4>Propiedades</h4>
		<div>
			<div class="col-sm-12" align="center">
				<table st-table="productoTipo.propiedadesTipo" class="table table-striped" style="width: 400px;" ng-hide="productoTipo.mostrarCargando">
					<thead>
						<tr>
							<th>Nombre</th>
							<th>Tipo</th>
							<th style="width: 5%"></th>
						</tr>
					</thead>
					<tbody>
						<tr st-select-row="row" st-select-mode="single" ng-repeat="propiedad in productoTipo.propiedadesTipo | filter: { estado: '!E'} track by $index">
							<td>{{propiedad.propiedad}}</td>
							<td>{{propiedad.propiedadTipo}}</td>
							<td align="center"><span ng-click="productoTipo.eliminarPropiedad($index)" uib-tooltip="Eliminar Propiedad"><i style="color: red; font-size: 20px;" class="glyphicon glyphicon-minus-sign"></i></span></td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="3" align="center"><span ng-click="productoTipo.agregarPropiedad()" uib-tooltip="Agregar Propiedad"><i style="color: blue; font-size: 20px;" class="glyphicon glyphicon-plus-sign"></i></span></td>
						</tr>				
					</tfoot>
				</table>
				<div class="grid_loading" ng-hide="!productoTipo.mostrarCargando">
					<div class="msg">
						<span><i class="fa fa-spinner fa-spin fa-4x"></i> <br />
							<br /> <b>Cargando, por favor espere...</b> </span>
					</div>
				</div>
			</div>
		</div>

		<div class="col-sm-12" align="center">Los campos marcados con * son obligatorios</div>
		<br />
		
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-success" ng-click="form.$valid ? productoTipo.guardar() : '' " ng-disabled="!form.$valid">Guardar</label> 
				<label class="btn btn-primary" ng-click="productoTipo.cancelar()">Ir a Taba</label>
			</div>
		</div>
	</div>

</div>