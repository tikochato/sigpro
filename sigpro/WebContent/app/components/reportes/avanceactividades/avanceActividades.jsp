<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<style>
    	.red {color: red !important;}
    	.yellow { color: yellow !important;}
    	.green {color: green !important;}
    	
    	.cuerpoTablaDatos {
		    overflow-y: scroll;
		    overflow-x: hidden;
		    text-align: center;
		}
		
		.tablaDatos {
			display: flex;
		    flex-direction: column;
		    align-items: stretch;
		    max-height: 375px; 
		}
		
		.theadDatos {
			flex-shrink: 0; overflow-x: hidden;
		}
		
		.divPadreDatos{		
			float: left; 
			display: inline-block;
			white-space: nowrap;	
			overflow:hidden;		
		}
		
		.divTabla{
			float: left;
			max-height: 375px;
			overflow-y:hidden;
			overflow-x:hidden;
		}
	</style>
	<div ng-controller="avanceActividadesController as controller" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="24010">
			<p ng-init="controller.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
	    <div class="row" id="reporte">
	    	<div class="col-sm-12">
	    		<div class="row">
    				<div class="panel panel-default" style="max-width:{{controller.tamanoPantalla}}px;">
	  					<div class="panel-heading"><h3>Reporte de Avance</h3></div>
					</div>
				</div>
    			<br>
    			<div class="row"  style="width: 100%; height: 15%">
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
		    <div class="col-sm-12">
	    		<div ng-hide="!controller.mostrarCargando" style="width: 100%; height: 400px;">
		    		<div class="grid_loading" ng-hide="!controller.mostrarCargando">
						<div class="msg">
							<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
								<br />
								<br /> <b>Cargando, por favor espere...</b> 
							</span>
						</div>
					</div>	
	    		</div>
		    	<div style="width: 100%; height: 85%"  ng-hide="!controller.mostrardiv">
		    		<div class="row">
		    			<div style="height: 5%; width: 100%">
							<div><h4><b>Actividades</b></h4></div>
						</div>
						<div class="divPadreDatos">
							<div class="divTabla"> 
								<table st-table="controller.displayedCollectionActividades" st-safe-src="controller.rowCollectionActividades" class="table table-striped" style="max-width:{{controller.tamanoPantalla}}px;">
									<thead class="theadDatos">
										<tr>
											<th style="display: none;">Id</th>
											<th class="label-form" style="min-width:{{controller.tamanoNombres}}px ;max-width:{{controller.tamanoNombres}}px; text-align: center; nowrap; overflow: hidden;" st-sort="row.nombre">Actividades del proyecto</th>
											<th style="min-width: {{controller.tamanoSemaforo}}px; max-width: {{controller.tamanoSemaforo}}px;"></th>
											<th class="label-form" style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; nowrap; overflow: hidden;" st-sort="row.completadas">Completadas</th>
											<th class="label-form" style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; nowrap; overflow: hidden;" st-sort="row.sinIniciar">Sin iniciar</th>
											<th class="label-form" style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; nowrap; overflow: hidden;" st-sort="row.proceso">En proceso</th>
											<th class="label-form" style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; nowrap; overflow: hidden;" st-sort="row.retrasadas">Retrasadas</th>
										</tr>
									</thead>
									<tbody class="cuerpoTablaDatos">
										<tr ng-repeat="row in controller.displayedCollectionActividades" uib-tooltip="{{row.nombre}}" tooltip-placement="center">
											<td style="display: none;">{{row.id}}</td>
											<td style="text-align: left; min-width:{{controller.tamanoNombres}}px ;max-width:{{controller.tamanoNombres}}px; nowrap; overflow: hidden;">{{row.nombre}}</td>
											<td style="min-width: {{controller.tamanoSemaforo}}px; max-width: {{controller.tamanoSemaforo}}px;"><span ng-style="controller.obtenerColor(row);" class="glyphicon glyphicon-certificate"></span></td>
											<td style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center">{{row.completadas}}%</td>
											<td style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center">{{row.sinIniciar}}%</td>
											<td style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center">{{row.proceso}}%</td>
											<td style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center">{{row.retrasadas}}%</td>
										</tr>
									</tbody>
									<tbody>
										<tr>
											<td style="display: none;"></td>
											<td style="min-width:{{controller.tamanoNombres}}px ;max-width:{{controller.tamanoNombres}}px; text-align: left; font-weight: bold;">Total de actividades: {{controller.totalActividades}}</td>
											<td style="min-width: {{controller.tamanoSemaforo}}px; max-width: {{controller.tamanoSemaforo}}px;"></td>
											<td style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; font-weight: bold;">{{controller.totalActividadesCompletadas}}</td>
											<td style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; font-weight: bold;">{{controller.totalActividadesSinIniciar}}</td>
											<td style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; font-weight: bold;">{{controller.totalActividadesProceso}}</td>
											<td style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; font-weight: bold;">{{controller.totalActividadesRetrasadas}}</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
		    		</div>
		    		<div class="row">
		    			<div style="height: 5%; width: 100%">
							<div><h4><b>Hitos</b></h4></div>
						</div>
						<div class="divPadreDatos">
							<div class="divTabla"> 
								<table st-table="controller.displayedCollectionHitos" st-safe-src="controller.rowCollectionHitos" class="table table-striped" style="max-width:{{controller.tamanoPantalla}}px;">
									<thead class="theadDatos">
										<tr>
											<th style="display: none;">Id</th>
											<th class="label-form" style="min-width:{{controller.tamanoNombres}}px ;max-width:{{controller.tamanoNombres}}px; text-align: center; nowrap; overflow: hidden;">Hitos del proyecto</th>
											<th style="min-width: {{controller.tamanoSemaforo}}px; max-width: {{controller.tamanoSemaforo}}px;"></th>
											<th class="label-form" style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; nowrap; overflow: hidden;">Completados</th>
											<th class="label-form" style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; nowrap; overflow: hidden;">Sin iniciar</th>
											<th class="label-form" style="min-width: {{2 * controller.tamanoColPorcentajes}}px; max-width: {{2 * controller.tamanoColPorcentajes}}px; text-align: center; nowrap; overflow: hidden;">Retrasadas</th>
										</tr>
									</thead>
									<tbody class="cuerpoTablaDatos">
										<tr ng-repeat="row in controller.displayedCollectionHitos" uib-tooltip="{{row.nombre}}" tooltip-placement="center">
											<td style="display: none;">{{row.id}}</td>
											<td style="text-align: left; min-width:{{controller.tamanoNombres}}px ;max-width:{{controller.tamanoNombres}}px; nowrap; overflow: hidden;">{{row.nombre}}</td>
											<td style="min-width: {{controller.tamanoSemaforo}}px; max-width: {{controller.tamanoSemaforo}}px;"><span ng-style="controller.obtenerColor(row);" class="glyphicon glyphicon-certificate"></span></td>
											<td style="text-align: center; min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px">{{row.completadas}}%</td>
											<td style="text-align: center; min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px">{{row.sinIniciar}}%</td>
											<td style="text-align: center; min-width: {{2 * controller.tamanoColPorcentajes}}px; max-width: {{2 * controller.tamanoColPorcentajes}}px;">{{row.retrasadas}}%</td>
										</tr>
									</tbody>
									<tbody>
										<tr>
											<td style="display: none;"></td>
											<td style="text-align: left; font-weight: bold; min-width:{{controller.tamanoNombres}}px ;max-width:{{controller.tamanoNombres}}px">Total de hitos: {{controller.totalHitos}}</td>
											<td style="min-width: {{controller.tamanoSemaforo}}px; max-width: {{controller.tamanoSemaforo}}px;"></td>
											<td style="text-align: center; font-weight: bold; min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px">{{controller.totalHitosCompletados}}</td>
											<td style="text-align: center; font-weight: bold; min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px">{{controller.totalHitosSinIniciar}}</td>
											<td style="text-align: center; font-weight: bold; min-width: {{2 * controller.tamanoColPorcentajes}}px; max-width: {{2 * controller.tamanoColPorcentajes}}px">{{controller.totalHitosRetrasados}}</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
		    		</div>
		    		<div class="row">
						<div style="height: 5%; width: 100%">
							<div><h4><b>Productos</b></h4></div>
						</div>
						
						<div class="divPadreDatos">
							<div class="divTabla"> 
								<table st-table="controller.displayedProductos" st-safe-src="controller.rowProductos" class="table table-striped tablaDatos" style="max-width:{{controller.tamanoPantalla}}px;">
									<thead class="theadDatos">
										<tr>
											<th style="display: none;">Id</th>
											<th class="label-form" style="min-width:{{controller.tamanoNombres}}px ;max-width:{{controller.tamanoNombres}}px; text-align: center; nowrap; overflow: hidden;" st-sort="nombre">Producto</th>
											<th style="min-width: {{controller.tamanoSemaforo}}px; max-width: {{controller.tamanoSemaforo}}px;"></th>
											<th class="label-form" style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; nowrap; overflow: hidden;" st-sort="completadas">Completados</th>
											<th class="label-form" style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; nowrap; overflow: hidden;" st-sort="sinIniciar">Sin iniciar</th>
											<th class="label-form" style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; nowrap; overflow: hidden;" st-sort="proceso">En proceso</th>
											<th class="label-form" style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; nowrap; overflow: hidden;" st-sort="retrasadas">Retrasadas</th>
										</tr>
									</thead>
									<tbody class="cuerpoTablaDatos">
										<tr ng-repeat="row in controller.displayedProductos" uib-tooltip="{{row.nombre}}" tooltip-placement="center">
											<td style="display: none;">{{row.id}}</td>
											<td style="text-align: left; min-width:{{controller.tamanoNombres}}px ;max-width:{{controller.tamanoNombres}}px; nowrap; overflow: hidden;">{{row.nombre}}</td>
											<td style="min-width: {{controller.tamanoSemaforo}}px; max-width: {{controller.tamanoSemaforo}}px;"><span ng-style="controller.obtenerColor(row);" class="glyphicon glyphicon-certificate"></span></td>
											<td style="text-align: center; min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px;">{{row.completadas}}%</td>
											<td style="text-align: center; min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px;">{{row.sinIniciar}}%</td>
											<td style="text-align: center; min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px;">{{row.proceso}}%</td>
											<td style="text-align: center; min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px;">{{row.retrasadas}}%</td>
										</tr>
									</tbody>
									<tbody>
										<tr>
											<td style="display: none;"></td>
											<td style="text-align: left; min-width:{{controller.tamanoNombres}}px ;max-width:{{controller.tamanoNombres}}px; font-weight: bold;">Total de productos: {{controller.totalProductos}}</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
		    		</div>
		    	</div>
	    	</div>
	    </div>
	</div>