<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<div ng-controller="ProductoTipoController as productoTipo" class="maincontainer all_page" >
	<script type="text/ng-template" id="buscarPropiedad.jsp">
	    <%@ include file="/app/components/productotipo/buscarPropiedad.jsp"%>
	</script>
	<shiro:lacksPermission name="23010">
		<p ng-init="productoTipo.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
	<div class="panel panel-default">
	  <div class="panel-heading"><h3>Tipo de Producto</h3></div>
	</div>

	<div align="center" ng-hide="productoTipo.esForma">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="23040">
					<label class="btn btn-primary" ng-click="productoTipo.nuevo()" uib-tooltip="Nuevo">
				<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="23020">
					<label class="btn btn-primary" ng-click="productoTipo.editar()" uib-tooltip="Editar">
				<span class="glyphicon glyphicon-pencil"></span> Editar</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="23030">
					<label class="btn btn-danger" ng-click="productoTipo.borrar()" uib-tooltip="Borrar">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
				</shiro:hasPermission>
			</div>
		</div>
		<shiro:hasPermission name="23010">
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
				<br/>
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
				<br/>
				<div class="total-rows">Total de {{  productoTipo.totalElementos + (productoTipo.totalElementos == 1 ? " Tipo de producto" : " Tipos de producto" ) }}</div>
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

	<div class="row second-main-form" ng-show="productoTipo.esForma">
		<div class="page-header">
			<h2 ng-hide="!productoTipo.esNuevo"><small>Nuevo tipo de producto</small></h2>
	  		<h2 ng-hide="productoTipo.esNuevo"><small>Edición de tipo de producto</small></h2>
		</div>
		<div class="col-sm-12 operation_buttons" align="right">

			<div class="btn-group">
				<shiro:hasPermission name="23020">
					<label class="btn btn-success" ng-click="form.$valid ? productoTipo.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label> 
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="productoTipo.cancelar()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>

		</div>

		<div class="col-sm-12">
			<form name="form" class="css-form">
				<div class="form-group">
						<label for="campo0" class="floating-label id_class">ID {{productoTipo.codigo}}</label>
						<br/><br/>
				</div>

				
				<div class="form-group">
					<input type="text" id="nombre" class="inputText" ng-model="productoTipo.nombre"  ng-value="productoTipo.nombre" onblur="this.setAttribute('value', this.value);" ng-required="true" />
					<label class="floating-label">* Nombre</label> 
				</div>

				<div class="form-group">
					<input type="text" class="inputText" ng-model="productoTipo.descripcion"  ng-value="productoTipo.descripcion" onblur="this.setAttribute('value', this.value);"  />
					<label class="floating-label">Descripción</label> 
				</div>
				<br/>
				<h5 class="label-form">Propiedades</h5>
				<div align="center">
					
					<div style="height: 35px; width: 75%">
								<div style="text-align: right;">
									<div class="btn-group" role="group" aria-label="">
										<a class="btn btn-default" href
											ng-click="productoTipo.agregarPropiedad()" role="button"
											uib-tooltip="Asignar nueva propiedad" tooltip-placement="left">
											<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
										</a>
									</div>
								</div>
							</div>
							<br/>
							<table style="width: 75%;" st-table="productoTipo.propiedadesTipo" class="table table-striped  table-bordered">
								<thead >
									<tr>
										<th class="label-form">ID</th>
										<th class="label-form">Nombre</th>
										<th class="label-form">Descripicon</th>
										<th class="label-form">Tipo Dato</th>
										<th class="label-form" style="width: 30px;">Quitar</th>
			
									</tr>
								</thead>
								<tbody>
								<tr st-select-row="row" st-select-mode="single" ng-repeat="propiedad in productoTipo.propiedadesTipo | filter: { estado: '!E'} track by $index">
									<td>{{propiedad.idPropiedad}}</td>
									<td>{{propiedad.propiedad}}</td>
									<td>{{propiedad.descripcion}}</td>
									<td>{{propiedad.propiedadTipo}}</td>
									<td align="center">
										<button type="button"
											 ng-click="productoTipo.eliminarPropiedad($index)"
											class="btn btn-sm btn-danger">
											<i class="glyphicon glyphicon-minus-sign"> </i>
										</button>
								</tr>
							</tbody>
						</table>
						<div class="grid_loading" ng-hide="!productoTipo.mostrarCargando">
							<div class="msg">
								<span><i class="fa fa-spinner fa-spin fa-4x"></i> <br />
									<br /> <b>Cargando, por favor espere...</b> </span>
							</div>
						</div>
				</div>
				<br />
				<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo" class="label-form">Usuario que creo</label> 
									<p class=""> {{ productoTipo.entidadSeleccionada.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion" class="label-form">Fecha de creación</label>
									<p class="" id="fechaCreacion"> {{ productoTipo.entidadSeleccionada.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo" class="label-form">Usuario que actualizo</label> 
									<p class="" id="usuarioCreo">{{ productoTipo.entidadSeleccionada.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion" class="label-form">Fecha de actualizacion</label> 
									<p class="" id="usuarioCreo">{{ productoTipo.entidadSeleccionada.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>
			
			</form>
		</div>
		
		
		<div class="col-sm-12 operation_buttons" align="right">
		<div class="label-form" align="center">Los campos marcados con * son obligatorios</div>
			<div class="btn-group">
				<shiro:hasPermission name="23020">
					<label class="btn btn-success" ng-click="form.$valid ? productoTipo.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label> 
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="productoTipo.cancelar()" uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
	</div>

</div>