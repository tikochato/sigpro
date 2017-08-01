<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<style>
	    .grid {
     		 width: 500px;
      		height: 200px;
    	}
    	.red { color: black; background-color: red !important;}
    	.yellow { color: black;  background-color: yellow !important;}
    	.green {color: black; background-color: green !important;}
	</style>
	<div ng-controller="avanceActividadesController as controller" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="24010">
			<p ng-init="controller.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
	    <div class="row">
	    	<div class="col-sm-12">
		    	<div style="width: 100%; height: 15%">
		    		<div class="row">
	    				<div class="panel panel-default">
		  					<div class="panel-heading"><h3>Reporte de Avance</h3></div>
						</div>
					</div>
	    			<br>
	    			<div class="row">
			    		<div class="form-group col-sm-3">
							<select  class="inputText" ng-model="controller.prestamo"
								ng-options="a.text for a in controller.prestamos" ng-change="controller.generar()"></select>
							<label for="prestamo" class="floating-label">Préstamos</label>
						</div>
						<div class="form-group col-sm-3">
							<input type="text"  class="inputText" uib-datepicker-popup="{{controller.formatofecha}}" ng-model="controller.fechaCorte" is-open="controller.fi_abierto"
					            datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="controller.generar()" 
					            ng-required="true"  ng-click="controller.abrirPopupFecha(1000)"
					            ng-value="controller.fechaCorte" onblur="this.setAttribute('value', this.value);"/>
					            <span class="label-icon" ng-click="controller.abrirPopupFecha(1000)">
					              <i class="glyphicon glyphicon-calendar"></i>
					            </span>
						  	<label for="campo.id" class="floating-label">*Fecha Corte</label>
						</div>
		    		</div>
		    	</div>
	    	
		    	<div style="width: 100%; height: 85%">
		    		<div class="row">
		    			<div style="height: 5%; width: 100%">
							<div><h4><b>Actividades</b></h4></div>
						</div>
		    			<table st-table="controller.displayedCollectionActividades" st-safe-src="controller.rowCollectionActividades" class="table table-striped">
							<thead>
								<tr>
									<th style="display: none;">Id</th>
									<th class="label-form" style="width: 30%; text-align: center;">Actividades del proyecto</th>
									<th class="label-form" style="width: 17%; text-align: center;">Completadas</th>
									<th class="label-form" style="width: 17%; text-align: center;">Sin iniciar</th>
									<th class="label-form" style="width: 17%; text-align: center;">En proceso</th>
									<th class="label-form" style="width: 17%; text-align: center;">Retrasadas</th>
								</tr>
							</thead>
							<tbody>
								<tr ng-repeat="row in controller.displayedCollectionActividades">
									<td style="display: none;">{{row.id}}</td>
									<td style="text-align: center">{{row.nombre}}</td>
									<td style="text-align: center" ng-class="{'red': row.completadas >= 0 && row.completadas <= 40, 'yellow': row.completadas > 40 && row.completadas <= 60, 'green': row.completadas > 60 && row.completadas <= 100}">{{row.completadas}}%</td>
									<td style="text-align: center">{{row.sinIniciar}}%</td>
									<td style="text-align: center">{{row.proceso}}%</td>
									<td style="text-align: center">{{row.retrasadas}}%</td>
								</tr>
								<tr>
									<td style="display: none;"></td>
									<td style="text-align: center; font-weight: bold;">Total de actividades: {{controller.totalActividades}}</td>
									<td style="text-align: center; font-weight: bold;">{{controller.totalActividadesCompletadas}}</td>
									<td style="text-align: center; font-weight: bold;">{{controller.totalActividadesSinIniciar}}</td>
									<td style="text-align: center; font-weight: bold;">{{controller.totalActividadesProceso}}</td>
									<td style="text-align: center; font-weight: bold;">{{controller.totalActividadesRetrasadas}}</td>
								</tr>
							</tbody>
						</table>
		    		</div>
		    		<div class="row">
		    			<div style="height: 5%; width: 100%">
							<div><h4><b>Hitos</b></h4></div>
						</div>
						
						<table st-table="controller.displayedCollectionHitos" st-safe-src="controller.rowCollectionHitos" class="table table-striped">
							<thead>
								<tr>
									<th style="display: none;">Id</th>
									<th class="label-form" style="width: 30%; text-align: center;">Hitos del proyecto</th>
									<th class="label-form" style="width: 17%; text-align: center;">Completados</th>
									<th class="label-form" style="width: 17%; text-align: center;">Sin iniciar</th>
									<th class="label-form" style="width: 17%; text-align: center;">Retrasadas</th>
									<th class="label-form" style="width: 17%; text-align: center;"></th>
								</tr>
							</thead>
							<tbody>
								<tr ng-repeat="row in controller.displayedCollectionHitos">
									<td style="display: none;">{{row.id}}</td>
									<td style="text-align: center">{{row.nombre}}</td>
									<td style="text-align: center" ng-class="{'red': row.completadas >= 0 && row.completadas <= 40, 'yellow': row.completadas > 40 && row.completadas <= 60, 'green': row.completadas > 60 && row.completadas <= 100}">{{row.completadas}}%</td>
									<td style="text-align: center">{{row.sinIniciar}}%</td>
									<td style="text-align: center">{{row.retrasadas}}%</td>
								</tr>
								<tr>
									<td style="display: none;"></td>
									<td style="text-align: center; font-weight: bold;">Total de hitos: {{controller.totalHitosCompletados}}</td>
									<td style="text-align: center; font-weight: bold;">{{controller.totalHitosCompletados}}</td>
									<td style="text-align: center; font-weight: bold;">{{controller.totalHitosSinIniciar}}</td>
									<td style="text-align: center; font-weight: bold;">{{controller.totalHitosRetrasados}}</td>
								</tr>
							</tbody>
						</table>
		    		</div>
		    		<div class="row">
						<div style="height: 5%; width: 100%">
							<div><h4><b>Productos</b></h4></div>
						</div>
						
						<table st-table="controller.displayedCollectionProductos" st-safe-src="controller.rowCollectionProducto" class="table table-striped">
							<thead>
								<tr>
									<th style="display: none;">Id</th>
									<th class="label-form" style="width: 30%; text-align: center;">Producto</th>
									<th class="label-form" style="width: 17%; text-align: center;">Completados</th>
									<th class="label-form" style="width: 17%; text-align: center;">Sin iniciar</th>
									<th class="label-form" style="width: 17%; text-align: center;">En proceso</th>
									<th class="label-form" style="width: 17%; text-align: center;">Retrasadas</th>
								</tr>
							</thead>
							<tbody>
								<tr ng-repeat="row in controller.displayedCollectionProductos">
									<td style="display: none;">{{row.id}}</td>
									<td style="text-align: center">{{row.nombre}}</td>
									<td style="text-align: center" ng-class="{'red': row.completadas >= 0 && row.completadas <= 40, 'yellow': row.completadas > 40 && row.completadas <= 60, 'green': row.completadas > 60 && row.completadas <= 100}">{{row.completadas}}%</td>
									<td style="text-align: center">{{row.sinIniciar}}%</td>
									<td style="text-align: center">{{row.proceso}}%</td>
									<td style="text-align: center">{{row.retrasadas}}%</td>
								</tr>
								<tr>
									<td style="display: none;"></td>
									<td style="text-align: center; font-weight: bold;">Total de productos: {{controller.totalProductos}}</td>
								</tr>
							</tbody>
						</table>
		    		</div>
		    	</div>
	    	</div>
	    </div>
	</div>