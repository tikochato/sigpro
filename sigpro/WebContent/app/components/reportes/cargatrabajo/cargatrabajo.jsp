<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="cargatrabajoController as controller" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="24010">
			<p ng-init="controller.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<div class="panel panel-default">
		  <div class="panel-heading"><h3>Carga de Trabajo</h3></div>
		</div>
	    <br>
	    <div class="row">
	    	<div class="col-sm-12">
	    		<div class="row" style="height: 300px; width: 100%;">
				    <div style="height: 50%; width: 50%; float: left">
			    		<div class="row">
							<div class="form-group col-sm-6" >
								<select  class="inputText" ng-model="controller.tObjeto"
									ng-options="a.text for a in controller.tObjetos" ng-change="controller.displayObjeto(controller.tObjeto.value)"></select>
								<label for="tObjeto" class="floating-label">Tipo Objeto</label>
							</div>
							<div class="form-group col-sm-2" >
								<label class="btn btn-default" ng-click="controller.generar();" uib-tooltip="Generar" 
									tooltip-placement="bottom">
								<span class="glyphicon glyphicon-new-window"></span></label>
							</div>
						</div>  
					    <div class="row">
							<div class="form-group col-sm-6" ng-hide="!controller.entidadHide">
								<select  class="inputText" ng-model="controller.entidad"
									ng-options="a.text for a in controller.entidades" 
									ng-change="controller.getUnidadesEjecutoras(controller.entidad.value);"></select>
								<label for="tObjeto" class="floating-label">Entidad</label>
							</div>
						</div>
						 <div class="row">
							<div class="form-group col-sm-6" ng-hide="!controller.unidadEjecutoraHide">
								<select  class="inputText" ng-model="controller.unidadEjecutora"
									ng-options="a.text for a in controller.unidadesEjecutoras"
									ng-change="controller.getPrestamos(controller.unidadEjecutora.value);"></select>
								<label for="tObjeto" class="floating-label">Uniedad Ejecutora</label>
							</div>
						</div>
						<div class="row">
							<div class="form-group col-sm-6" ng-hide="!controller.prestamoHide">
								<select  class="inputText" ng-model="controller.prestamo"
									ng-options="a.text for a in controller.prestamos" 
									ng-change="controller.getComponentes(controller.prestamo.value);"></select>
								<label for="tObjeto" class="floating-label">Préstamos</label>
							</div>
						</div>
						<div class="row">
							<div class="form-group col-sm-6" ng-hide="!controller.componenteHide">
								<select  class="inputText" ng-model="controller.componente"
									ng-options="a.text for a in controller.componentes" 
									ng-change="controller.getProductos(controller.componente.value);"></select>
								<label for="tObjeto" class="floating-label">Componente</label>
							</div>
						</div>
						<div class="row">
							<div class="form-group col-sm-6" ng-hide="!controller.productoHide">
								<select  class="inputText" ng-model="controller.producto"
									ng-options="a.text for a in controller.productos"
									ng-change="controller.getSubProductos(controller.producto.value);"></select>
								<label for="tObjeto" class="floating-label">Producto</label>
							</div>
						</div>
						<div class="row">
							<div class="form-group col-sm-6" ng-hide="!controller.subProductoHide">
								<select  class="inputText" ng-model="controller.subProducto"
									ng-options="a.text for a in controller.subProductos" ng-change=""></select>
								<label for="tObjeto" class="floating-label">Sub Producto</label>
							</div>
						</div>
			    	</div>
			    	<div style="height: 50%; width: 50%; float: right;">
				    	<div class="operation_buttons" align="right">
							<div class="btn-group">
								<label class="btn btn-primary"  ng-click="controller.exportarExcel()" uib-tooltip="Exportar" ng-hide="!controller.exportar">
								<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true">&nbsp;Exportar</span></label>
							</div>
						</div>
		    			<table st-table="controller.displayedCollection" st-safe-src="controller.rowCollection" class="table table-striped">
							<thead>
								<tr>
									<th style="display: none;">Id</th>
									<th class="label-form" style="width: 50%">Responsable</th>
									<th class="label-form" style="width: 25%; text-align: center;">Actividades atrasadas</th>
									<th class="label-form" style="width: 25%; text-align: center;">Actividades a cumplir {{controller.mesActual}}</th>
								</tr>
							</thead>
							<tbody>
								<tr ng-repeat="row in controller.displayedCollection" ng-click="controller.getActividades(row)">
									<td style="display: none;">{{row.id}}</td>
									<td>{{row.responsable}}</td>
									<td style="text-align: center">{{row.actividadesAtrasadas}}</td>
									<td style="text-align: center">{{row.actividadesProceso}}</td>
								</tr>
								<tr>
									<td style="display: none;">{{controller.idTotal}}</td>
									<td style="font-weight: bold">{{controller.responsableTotal}}</td>
									<td style="text-align: center; font-weight: bold">{{controller.actividadesAtrasadasTotal}}</td>
									<td style="text-align: center; font-weight: bold">{{controller.actividadesProcesoTotal}}</td>
								</tr>
							</tbody>
						</table>
			    	</div>
		    	</div>
			    <br>
		    	<br>
		    	<div class="row" style="height: 35%; width: 100%;" ng-hide="controller.grafica">
		    		<div style="height: 50%; width: 50%; float: left">
						<table st-table="controller.displayedCollectionActividades" st-safe-src="controller.rowCollectionActividades" class="table table-striped">
							<thead>
								<tr>
									<th style="display: none;">Id</th>
									<th class="label-form" style="width: 50%">Actividad</th>
									<th class="label-form" style="width: 25%; text-align: center;">Fecha Inicio</th>
									<th class="label-form" style="width: 25%; text-align: center;">Fecha Fin</th>
									<th class="label-form" style="width: 25%; text-align: center;">Porcentaje Avance</th>
									<th class="label-form" style="width: 25%; text-align: center;">Estado</th>
								</tr>
							</thead>
							<tbody>
								<tr ng-repeat="row in controller.displayedCollectionActividades">
									<td style="display: none;">{{row.id}}</td>
									<td>{{row.nombre}}</td>
									<td style="text-align: center">{{row.fechaInicio}}</td>
									<td style="text-align: center">{{row.fechaFin}}</td>
									<td style="text-align: center">{{row.porcentaje}}%</td>
									<td style="text-align: center">{{row.estado}}</td>
								</tr>
							</tbody>
						</table>
		    		</div>
		    		<div style="height: 50%; width: 50%; float: right">
		   		    	<canvas id="bar" class="chart chart-bar" 
				    		chart-data="controller.data" 
				    		chart-labels="controller.labels"
				    		chart-options="controller.charOptions" 
				    		chart-series="controller.series">
						</canvas>
		    		</div>
			    </div>
	    </div>
	</div>    
</div>