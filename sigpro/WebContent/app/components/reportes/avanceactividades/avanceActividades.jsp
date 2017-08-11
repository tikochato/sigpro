<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<style>
	    .grid {
     		 width: 500px;
      		height: 200px;
    	}
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
	    	
		    	<div style="width: 100%; height: 85%"  ng-hide="!controller.mostrardiv">
		    		<div class="grid_loading" ng-hide="!controller.mostrarCargando">
						<div class="msg">
							<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
								<br />
								<br /> <b>Cargando, por favor espere...</b> 
							</span>
						</div>
					</div>	
		    		<div class="row">
		    			<div style="height: 5%; width: 100%">
							<div><h4><b>Actividades</b></h4></div>
						</div>
						<div class="divPadreDatos" style="max-width: {{controller.tamanoPantalla}}px">
							<div class="divTabla"> 
								<table st-table="controller.displayedCollectionActividades" st-safe-src="controller.rowCollectionActividades" class="table table-striped">
									<thead class="theadDatos">
										<tr>
											<th style="display: none;">Id</th>
											<th class="label-form" style="width:{{controller.tamanoNombres}}px; text-align: center;" st-sort="row.nombre">Actividades del proyecto</th>
											<th style="width: {{controller.tamanoSemaforo}}px"></th>
											<th class="label-form" style="width: {{controller.tamanoColPorcentajes}}px; text-align: center;" st-sort="row.completadas">Completadas</th>
											<th class="label-form" style="width: {{controller.tamanoColPorcentajes}}px; text-align: center;" st-sort="row.sinIniciar">Sin iniciar</th>
											<th class="label-form" style="width: {{controller.tamanoColPorcentajes}}px; text-align: center;" st-sort="row.proceso">En proceso</th>
											<th class="label-form" style="width: {{controller.tamanoColPorcentajes}}px; text-align: center;" st-sort="row.retrasadas">Retrasadas</th>
										</tr>
									</thead>
									<tbody class="cuerpoTablaDatos">
										<tr ng-repeat="row in controller.displayedCollectionActividades">
											<td style="display: none;">{{row.id}}</td>
											<td style="text-align: left; width:{{controller.tamanoNombres}}px"  nowrap>{{row.nombre}}</td>
											<td style="width: {{controller.tamanoSemaforo}}px"><span ng-style="controller.obtenerColor(row);" class="glyphicon glyphicon-certificate"></span></td>
											<td style="width: {{controller.tamanoColPorcentajes}}px; text-align: center">{{row.completadas}}%</td>
											<td style="width: {{controller.tamanoColPorcentajes}}px; text-align: center">{{row.sinIniciar}}%</td>
											<td style="width: {{controller.tamanoColPorcentajes}}px; text-align: center">{{row.proceso}}%</td>
											<td style="width: {{controller.tamanoColPorcentajes}}px;text-align: center">{{row.retrasadas}}%</td>
										</tr>
									</tbody>
									<tbody>
										<tr>
											<td style="display: none;"></td>
											<td style="text-align: left; font-weight: bold;">Total de actividades: {{controller.totalActividades}}</td>
											<td></td>
											<td style="text-align: center; font-weight: bold;">{{controller.totalActividadesCompletadas}}</td>
											<td style="text-align: center; font-weight: bold;">{{controller.totalActividadesSinIniciar}}</td>
											<td style="text-align: center; font-weight: bold;">{{controller.totalActividadesProceso}}</td>
											<td style="text-align: center; font-weight: bold;">{{controller.totalActividadesRetrasadas}}</td>
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
						<div class="divPadreDatos" style="max-width: {{controller.tamanoPantalla}}px">
							<div class="divTabla"> 
								<table st-table="controller.displayedCollectionHitos" st-safe-src="controller.rowCollectionHitos" class="table table-striped">
									<thead class="theadDatos">
										<tr>
											<th style="display: none;">Id</th>
											<th class="label-form" style="width:{{controller.tamanoNombres}}px; text-align: center;">Hitos del proyecto</th>
											<th style="width: {{controller.tamanoSemaforo}}px"></th>
											<th class="label-form" style="width: {{controller.tamanoColPorcentajes}}px; text-align: center;">Completados</th>
											<th class="label-form" style="width: {{controller.tamanoColPorcentajes}}px; text-align: center;">Sin iniciar</th>
											<th class="label-form" style="text-align: center; width: 36%" colspan="2">Retrasadas</th>
											<th class="label-form" style="text-align: center;"></th>
										</tr>
									</thead>
									<tbody class="cuerpoTablaDatos">
										<tr ng-repeat="row in controller.displayedCollectionHitos">
											<td style="display: none;">{{row.id}}</td>
											<td style="text-align: left; width:{{controller.tamanoNombres}}px"  nowrap>{{row.nombre}}</td>
											<td style="width: {{controller.tamanoSemaforo}}px"><span ng-style="controller.obtenerColor(row);" class="glyphicon glyphicon-certificate"></span></td>
											<td style="text-align: center; width: {{controller.tamanoColPorcentajes}}px">{{row.completadas}}%</td>
											<td style="text-align: center; width: {{controller.tamanoColPorcentajes}}px">{{row.sinIniciar}}%</td>
											<td style="text-align: center" colspan="2">{{row.retrasadas}}%</td>
											<td></td>
										</tr>
									</tbody>
									<tbody>
										<tr>
											<td style="display: none;"></td>
											<td style="text-align: left; font-weight: bold; width:{{controller.tamanoNombres}}px">Total de hitos: {{controller.totalHitosCompletados}}</td>
											<td style="width: {{controller.tamanoSemaforo}}px"></td>
											<td style="text-align: center; font-weight: bold; width: {{controller.tamanoColPorcentajes}}px">{{controller.totalHitosCompletados}}</td>
											<td style="text-align: center; font-weight: bold; width: {{controller.tamanoColPorcentajes}}px">{{controller.totalHitosSinIniciar}}</td>
											<td style="text-align: center; font-weight: bold; width: {{controller.tamanoColPorcentajes}}px" colspan="2">{{controller.totalHitosRetrasados}}</td>
											<td>
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
						
						<div class="divPadreDatos" style="max-width: {{controller.tamanoPantalla}}px">
							<div class="divTabla"> 
								<table st-table="controller.displayedProductos" st-safe-src="controller.rowProductos" class="table table-striped tablaDatos">
									<thead class="theadDatos">
										<tr>
											<th style="display: none;">Id</th>
											<th class="label-form" style="width:{{controller.tamanoNombres}}px; text-align: center;" st-sort="nombre">Producto</th>
											<th style="width: {{controller.tamanoSemaforo}}px"></th>
											<th class="label-form" style="width: {{controller.tamanoColPorcentajes}}px; text-align: center;" st-sort="completadas">Completados</th>
											<th class="label-form" style="width: {{controller.tamanoColPorcentajes}}px; text-align: center;" st-sort="sinIniciar">Sin iniciar</th>
											<th class="label-form" style="width: {{controller.tamanoColPorcentajes}}px; text-align: center;" st-sort="proceso">En proceso</th>
											<th class="label-form" style="width: {{controller.tamanoColPorcentajes}}px; text-align: center;" st-sort="retrasadas">Retrasadas</th>
										</tr>
									</thead>
									<tbody class="cuerpoTablaDatos">
										<tr ng-repeat="row in controller.displayedProductos">
											<td style="display: none;">{{row.id}}</td>
											<td style="text-align: left; width: {{controller.tamanoNombres}}px;" nowrap>{{row.nombre}}</td>
											<td style="width: {{controller.tamanoSemaforo}}px"><span ng-style="controller.obtenerColor(row);" class="glyphicon glyphicon-certificate"></span></td>
											<td style="text-align: center; width: {{controller.tamanoColPorcentajes}}px;">{{row.completadas}}%</td>
											<td style="text-align: center; width: {{controller.tamanoColPorcentajes}}px;">{{row.sinIniciar}}%</td>
											<td style="text-align: center; width: {{controller.tamanoColPorcentajes}}px;">{{row.proceso}}%</td>
											<td style="text-align: center; width: {{controller.tamanoColPorcentajes}}px;">{{row.retrasadas}}%</td>
										</tr>
									</tbody>
									<tbody>
										<tr>
											<td style="display: none;"></td>
											<td style="text-align: left; font-weight: bold;">Total de productos: {{controller.totalProductos}}</td>
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