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
		
		.label-form2 {
	    	font-size: 15px;
	    	opacity: 1;
	    	color: rgba(0,0,0,0.38) !important;
	    	font-weight: bold;
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
		<script type="text/ng-template" id="objetoavance.jsp">
    		<%@ include file="/app/components/reportes/avanceactividades/objetoavance.jsp"%>
  		</script>
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
    			<div class="row" style="width: 100%; height: 15%">
		    		<div class="form-group col-sm-5">
						<select  class="inputText" ng-model="controller.prestamo"
							ng-options="a.text for a in controller.prestamos" ng-change="controller.generar()"></select>
					</div>
					<div class="form-group col-sm-3">
						<input type="text"  class="inputText" uib-datepicker-popup="{{controller.formatofecha}}" ng-model="controller.fechaCorte" is-open="controller.fi_abierto"
				            datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="controller.validarFecha(controller.fechaCorte)" 
				            ng-required="true"
				            ng-value="controller.fechaCorte" onblur="this.setAttribute('value', this.value);"/>
				            <span class="label-icon" ng-click="controller.abrirPopupFecha(1000)">
				              <i class="glyphicon glyphicon-calendar"></i>
				            </span>
					  	<label for="campo.id" class="floating-label">*Fecha de Corte</label>
					</div>
					<div class="col-sm-3 operation_buttons" align="right" style="float: right;">
		    			<div class="btn-group" role="group" aria-label="">
							<label class="btn btn-default" ng-click="controller.exportarExcel()" uib-tooltip="Exportar a Excel" ng-hide="!controller.mostrardiv" role="button" tabindex="0" aria-hidden="false" style="">
							<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
							<label class="btn btn-default" ng-click="controller.exportarPdf()" uib-tooltip="Exportar a PDF" ng-hide="!controller.mostrardiv">
							<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
						</div>
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
		    	<div style="width: 100%; height: 85%;" ng-hide="!controller.mostrardiv">
		    		<div class="row">
		    			<div class="row">
			    			<div class="col-sm-12">
				    			<div style="height: 5%; width: 100%">
									<div><h4><b>Actividades</b></h4></div>
								</div>
								<div class="divPadreDatos">
									<div class="divTabla"> 
										<table st-table="controller.displayedCollectionActividades" st-safe-src="controller.rowCollectionActividades" class="table table-striped">
											<thead class="theadDatos">
												<tr>
													<th style="display: none;">Id</th>
													<th class="label-form" style="min-width:{{controller.tamanoNombres}}px ;max-width:{{controller.tamanoNombres}}px; text-align: center; nowrap; overflow: hidden;" st-sort="row.nombre">Actividades del proyecto</th>
													<th style="min-width: {{controller.tamanoSemaforo}}px; max-width: {{controller.tamanoSemaforo}}px;"></th>
													<th class="label-form" style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; nowrap; overflow: hidden;" st-sort="row.completadas">Completadas</th>
													<th class="label-form" style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; nowrap; overflow: hidden;" st-sort="row.sinIniciar">Sin iniciar</th>
													<th class="label-form" style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; nowrap; overflow: hidden;" st-sort="row.proceso">En proceso</th>
													<th class="label-form" style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; nowrap; overflow: hidden;" st-sort="row.retrasadas">Retrasadas</th>
													<th class="label-form" style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; nowrap; overflow: hidden;" st-sort="row.esperadasfinanio">Esperadas fin de año</th>
													<th class="label-form" style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; nowrap; overflow: hidden;" st-sort="row.aniosSiguientes">Años siguientes</th>
												</tr>
											</thead>
											<tbody class="cuerpoTablaDatos">
												<tr ng-repeat="row in controller.displayedCollectionActividades" uib-tooltip="{{row.nombre}}" tooltip-placement="center" ng-click="controller.mostrarActividades(row);">
													<td style="display: none;">{{row.id}}</td>
													<td style="text-align: left; min-width:{{controller.tamanoNombres}}px ;max-width:{{controller.tamanoNombres}}px; nowrap; overflow: hidden;">{{row.nombre}}</td>
													<td style="min-width: {{controller.tamanoSemaforo}}px; max-width: {{controller.tamanoSemaforo}}px;"><span ng-style="controller.obtenerColor(row);" class="glyphicon glyphicon-certificate"></span></td>
													<td style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center">{{row.completadas}}%</td>
													<td style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center">{{row.sinIniciar}}%</td>
													<td style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center">{{row.proceso}}%</td>
													<td style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center">{{row.retrasadas}}%</td>
													<td style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center">{{row.esperadasfinanio}}%</td>
													<td style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center">{{row.aniosSiguientes}}%</td>
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
													<td style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; font-weight: bold;">{{controller.totalActividadesEsperadas}}</td>
													<td style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; font-weight: bold;">{{controller.totalActividadesAnioSiguientes}}</td>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
			    			</div>
			    		</div>
			    		<div class="row" ng-hide="!controller.mostrarActProyecto">
			    			<div class="form-group col-sm-6" align="center">
			    				<div style="text-align: center" >
					    			<label class="label-form2">Estado de Actividades</label>
					    		</div>
		    					<canvas id="pie" class="chart chart-pie"
  									chart-data="controller.dataPieProyecto" chart-labels="controller.labelsPieProyecto" 
  									chart-options="controller.optionsPieProyecto" chart-colors = "controller.pieColors" >
								</canvas> 
		    				</div>
			    			<div class="form-group col-sm-6" align="center">
			    				<div style="text-align: center" >
		    						<label class="label-form2">Totales</label>
		    					</div>
   								<canvas id="bar" class="chart chart-bar" chart-data="controller.dataBarraProyecto" chart-labels="controller.labelsBarProyecto" 
   									chart-options="controller.charOptions" chart-colors = "controller.pieColors" chart-series="controller.labelsPieProyecto">
   								</canvas>
		    				</div>
		    			</div>
		    			<br>
		    			<div class="row" ng-hide="!controller.mostrarHitos">
			    			<div class="col-sm-12">
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
													<th class="label-form" style="min-width: {{controller.tamanoColPorcentajesHitos}}px; max-width: {{controller.tamanoColPorcentajesHitos}}px; text-align: center; nowrap; overflow: hidden;">Completados</th>
													<th class="label-form" style="min-width: {{controller.tamanoColPorcentajesHitos}}px; max-width: {{controller.tamanoColPorcentajesHitos}}px; text-align: center; nowrap; overflow: hidden;">Sin iniciar</th>
													<th class="label-form" style="min-width: {{controller.tamanoColPorcentajesHitos}}px; max-width: {{controller.tamanoColPorcentajesHitos}}px; text-align: center; nowrap; overflow: hidden;">Retrasadas</th>
													<th class="label-form" style="min-width: {{controller.tamanoColPorcentajesHitos}}px; max-width: {{controller.tamanoColPorcentajesHitos}}px; text-align: center; nowrap; overflow: hidden;">Esperadas fin de año</th>
													<th class="label-form" style="min-width: {{controller.tamanoColPorcentajesHitos}}px; max-width: {{controller.tamanoColPorcentajesHitos}}px; text-align: center; nowrap; overflow: hidden;">Años siguientes</th>													
												</tr>
											</thead>
											<tbody class="cuerpoTablaDatos">
												<tr ng-repeat="row in controller.displayedCollectionHitos" uib-tooltip="{{row.nombre}}" tooltip-placement="center" ng-click="controller.mostrarActividades(row);">
													<td style="display: none;">{{row.id}}</td>
													<td style="text-align: left; min-width:{{controller.tamanoNombres}}px ;max-width:{{controller.tamanoNombres}}px; nowrap; overflow: hidden;">{{row.nombre}}</td>
													<td style="min-width: {{controller.tamanoSemaforo}}px; max-width: {{controller.tamanoSemaforo}}px;"><span ng-style="controller.obtenerColor(row);" class="glyphicon glyphicon-certificate"></span></td>
													<td style="text-align: center; min-width: {{controller.tamanoColPorcentajesHitos}}px; max-width: {{controller.tamanoColPorcentajesHitos}}px">{{row.completadas}}%</td>
													<td style="text-align: center; min-width: {{controller.tamanoColPorcentajesHitos}}px; max-width: {{controller.tamanoColPorcentajesHitos}}px">{{row.sinIniciar}}%</td>
													<td style="text-align: center; min-width: {{controller.tamanoColPorcentajesHitos}}px; max-width: {{controller.tamanoColPorcentajesHitos}}px;">{{row.retrasadas}}%</td>
													<td style="text-align: center; min-width: {{controller.tamanoColPorcentajesHitos}}px; max-width: {{controller.tamanoColPorcentajesHitos}}px;">{{row.esperadasfinanio}}%</td>
													<td style="text-align: center; min-width: {{controller.tamanoColPorcentajesHitos}}px; max-width: {{controller.tamanoColPorcentajesHitos}}px;">{{row.aniosSiguientes}}%</td>
												</tr>
											</tbody>
											<tbody>
												<tr>
													<td style="display: none;"></td>
													<td style="text-align: left; font-weight: bold; min-width:{{controller.tamanoNombres}}px ;max-width:{{controller.tamanoNombres}}px">Total de hitos: {{controller.totalHitos}}</td>
													<td style="min-width: {{controller.tamanoSemaforo}}px; max-width: {{controller.tamanoSemaforo}}px;"></td>
													<td style="text-align: center; font-weight: bold; min-width: {{controller.tamanoColPorcentajesHitos}}px; max-width: {{controller.tamanoColPorcentajesHitos}}px;">{{controller.totalHitosCompletados}}</td>
													<td style="text-align: center; font-weight: bold; min-width: {{controller.tamanoColPorcentajesHitos}}px; max-width: {{controller.tamanoColPorcentajesHitos}}px;">{{controller.totalHitosSinIniciar}}</td>
													<td style="text-align: center; font-weight: bold; min-width: {{controller.tamanoColPorcentajesHitos}}px; max-width: {{controller.tamanoColPorcentajesHitos}}px;">{{controller.totalHitosRetrasados}}</td>
													<td style="text-align: center; font-weight: bold; min-width: {{controller.tamanoColPorcentajesHitos}}px; max-width: {{controller.tamanoColPorcentajesHitos}}px;">{{controller.totalHitosEsperados}}</td>
													<td style="text-align: center; font-weight: bold; min-width: {{controller.tamanoColPorcentajesHitos}}px; max-width: {{controller.tamanoColPorcentajesHitos}}px;">{{controller.totalHitosAnioSiguientes}}</td>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
				    		</div>
		    			</div>
		    			<br/>
		    			<div class="row" ng-hide="!controller.mostrarHitos">
			    			<div class="form-group col-sm-6" align="center">
			    				<div style="text-align: center" >
					    			<label class="label-form2">Estado de Hitos</label>
					    		</div>
		    					<canvas id="pie" class="chart chart-pie"
  									chart-data="controller.dataPieHitos" chart-labels="controller.seriesBarHitos" 
  									chart-options="controller.optionsPieProyecto" chart-colors = "controller.pieColorsHitos" >
								</canvas> 
		    				</div>
			    			<div class="form-group col-sm-6" align="center">
			    				<div style="text-align: center" >
		    						<label class="label-form2">Totales</label>
		    					</div>
   								<canvas id="bar" class="chart chart-bar" chart-data="controller.dataBarraHitos" chart-labels="controller.labelsPieHitos" 
   									chart-options="controller.charOptionsHitos" chart-colors="controller.pieColorsHitos" chart-series="controller.seriesBarHitos">
   								</canvas>
		    				</div>
		    			</div>
		    			<br/>
			    		<div class="row">
				    		<div class="col-sm-12">
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
													<th class="label-form" style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; nowrap; overflow: hidden;" st-sort="esperadasfinanio">Esperadas fin de año</th>
													<th class="label-form" style="min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px; text-align: center; nowrap; overflow: hidden;" st-sort="aniosSiguientes">Años siguientes</th>
												</tr>
											</thead>
											<tbody class="cuerpoTablaDatos">
												<tr ng-repeat="row in controller.displayedProductos" uib-tooltip="{{row.nombre}}" tooltip-placement="center" ng-click="controller.mostrarActividades(row);">
													<td style="display: none;">{{row.id}}</td>
													<td style="text-align: left; min-width:{{controller.tamanoNombres}}px ;max-width:{{controller.tamanoNombres}}px; nowrap; overflow: hidden;">{{row.nombre}}</td>
													<td style="min-width: {{controller.tamanoSemaforo}}px; max-width: {{controller.tamanoSemaforo}}px;"><span ng-style="controller.obtenerColor(row);" class="glyphicon glyphicon-certificate"></span></td>
													<td style="text-align: center; min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px;">{{row.completadas}}</td>
													<td style="text-align: center; min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px;">{{row.sinIniciar}}</td>
													<td style="text-align: center; min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px;">{{row.proceso}}</td>
													<td style="text-align: center; min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px;">{{row.retrasadas}}</td>
													<td style="text-align: center; min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px;">{{row.esperadasfinanio}}</td>
													<td style="text-align: center; min-width: {{controller.tamanoColPorcentajes}}px; max-width: {{controller.tamanoColPorcentajes}}px;">{{row.aniosSiguientes}}</td>
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
	</div>
</div>