<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<style>
		.ui-grid-header-cell-wrapper {
			margin-left: 0px;
			text-align: center;
		}
		.text-center {
			text-align: center;
		}
		
		.cuerpoTablaDatos {
		    overflow-y: scroll;
		    overflow-x: hidden;
		    text-align: center;
		    margin-right: -15px;
		}
		
		.cuerpoTablafooter {
		    text-align: center;
		    margin-left: -5px;
		}
		
		.tablaDatos {
			display: flex;
		    flex-direction: column;
		    align-items: stretch;
		    max-height: 375px; 
		}
		
		.theadDatos {
			flex-shrink: 0; overflow-x: hidden;
			overflow: hidden;
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
	<div ng-controller="administracionTransaccionalController as controller" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="24010">
			<p ng-init="controller.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<div class="row"  id="reporte">
			<div class="col-sm-12">
				<div class="row">
			   		<div style="width: 100%; height: 15%">
			   			<div class="row">
			   				<div class="panel panel-default">
			  					<div class="panel-heading"><h3>Administración Transaccional</h3></div>
							</div>
						</div>
			   			<br>
			   		</div>
			   		
				  	<div class="row">
				  		<div class="col-sm-3">
							<div class="form-group" >
							  <input type="text"  class="inputText" uib-datepicker-popup="{{controller.formatofecha}}" ng-model="controller.fechaInicio" is-open="controller.fi_abierto"
						            datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="controller.fechaFin != null ? controller.generar() : ''" 
						            ng-required="true"  ng-click="controller.abrirPopupFecha(1000)"
						            ng-value="controller.fechaInicio" onblur="this.setAttribute('value', this.value);"
						            ng-readonly="true" ng-click="controller.abrirPopupFecha(1000)"/>
						            <span class="label-icon" ng-click="controller.abrirPopupFecha(1000)">
						              <i class="glyphicon glyphicon-calendar"></i>
						            </span>
							  	<label for="campo.id" class="floating-label">*Fecha Inicial</label>
							</div>
						</div>
		
						<div class="col-sm-3">
							<div class="form-group" >
							  <input type="text"  class="inputText" uib-datepicker-popup="{{controller.formatofecha}}" ng-model="controller.fechaFin" is-open="controller.ff_abierto"
						            datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="controller.fechaInicio != null ? controller.generar() : ''" 
						            ng-required="true"  ng-click="controller.abrirPopupFecha(1001)"
						            ng-value="controller.fechaFin" onblur="this.setAttribute('value', this.value);"
						            ng-readonly="true" ng-click="controller.abrirPopupFecha(1001)"/>
						            <span class="label-icon" ng-click="controller.abrirPopupFecha(1001)">
						              <i class="glyphicon glyphicon-calendar"></i>
						            </span>
							  	<label for="campo.id" class="floating-label">*Fecha Final</label>
							</div>
						</div>
						
						<div class="col-sm-3">
						</div>
						<div class="col-sm-3" align="right">
							<div class="btn-group" role="group" aria-label="">
								<label class="btn btn-default" ng-click="controller.exportarExcel()" uib-tooltip="Exportar a Excel" ng-hide="!controller.mostrarTablas">
								<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
								<label class="btn btn-default" ng-click="controller.exportarPdf()" uib-tooltip="Exportar a PDF" ng-hide="!controller.mostrarTablas">
								<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
							</div>
						</div>
				  	</div>
				</div>
				<br>
				<div class="row" align="center" ng-hide="!controller.mostrarTablas">
			    	<div class="col-sm-12">
			    		<div style="width: 100%; height: 85%; text-align: center">
			    			<div class="row">
			    				<div class="form-group col-sm-12" align="center">
									<div class="divPadreDatos" style="max-width: {{controller.tamanoPantalla}}px">
										<div class="grid_loading" ng-hide="!controller.mostrarcargando" style="height: {{controller.anchoPantalla}}px">
										  	<div class="msg">
										      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
												  <br /><br />
												  <b>Cargando, por favor espere...</b>
											  </span>
											</div>
									  	</div>
										<div class="divTabla"> 
											<table st-table="controller.displayedDatos" st-safe-src="controller.rowDatos" class="table table-striped tablaDatos"  style="max-width:{{controller.tamanoPantalla}}px;">
												<thead class="theadDatos">
													<tr>
														<th style="display: none;">Id</th>
														<th class="label-form" style="max-width:{{controller.tamanoCelda}}px; min-width:{{controller.tamanoCelda}}px; text-align: center;" st-sort="row.usuario">Usuario</th>
														<th class="label-form" style="max-width:{{controller.tamanoCelda}}px; min-width:{{controller.tamanoCelda}}px; text-align: center;" st-sort="row.creados">Creados</th>
														<th class="label-form" style="max-width:{{controller.tamanoCelda}}px; min-width:{{controller.tamanoCelda}}px; text-align: center;" st-sort="row.actualizados">Actualizados</th>
														<th class="label-form" style="max-width:{{controller.tamanoCelda}}px; min-width:{{controller.tamanoCelda}}px; text-align: center;" st-sort="row.eliminados">Eliminados</th>
														<th class="label-form" style="max-width:{{controller.tamanoCelda}}px; min-width:{{controller.tamanoCelda}}px; text-align: center;">Descargar</th>
													</tr>
													<tr>
														<th colspan="5">
															<input st-search="usuario" placeholder="búsqueda por usuario..." class="input-sm form-control" type="search"/>
														</th>
													<tr>
												</thead>
												<tbody class="cuerpoTablaDatos">
													<tr ng-repeat="row in controller.displayedDatos">
														<td style="display: none;">{{row.id}}</td>
														<td style="width:{{controller.tamanoCelda}}px; text-align: left;">{{row.usuario}}</td>
														<td style="width:{{controller.tamanoCelda}}px; text-align: center">{{row.creados}}</td>
														<td style="width:{{controller.tamanoCelda}}px; text-align: center">{{row.actualizados}}</td>
														<td style="width:{{controller.tamanoCelda}}px; text-align: center">{{row.eliminados}}</td>
														<td style="width:{{controller.tamanoCelda}}px; text-align: center">
															<button type="button"
																ng-click="controller.descargarExcelDetalle(row)"
																uib-tooltip="Exportar a Excel detalle de {{row.usuario}}" tooltip-placement="bottom"
																class="btn btn-default">
																<i class="glyphicon glyphicon-export"> </i>
															</button>
															<button type="button"
																ng-click="controller.exportarPdfDetalle(row)"
																uib-tooltip="Exportar a PDF detalle de {{row.usuario}}" tooltip-placement="bottom"
																class="btn btn-default">
																<i class="glyphicon glyphicon-save-file"> </i>
															</button>
														</td>
													</tr>
												</tbody>
												<tbody class="cuerpoTablafooter">
													<tr ng-click="controller.totalesGenerales();">
														<td style="display: none;"></td>
														<td style="text-align: left; font-weight: bold; max-width:{{controller.tamanoCelda}}px; min-width:{{controller.tamanoCelda}}px;">Totales:</td>
														<td style="text-align: center; font-weight: bold; max-width:{{controller.tamanoCelda}}px; min-width:{{controller.tamanoCelda}}px;">{{controller.totalCreados}}</td>
														<td style="text-align: center; font-weight: bold; max-width:{{controller.tamanoCelda}}px; min-width:{{controller.tamanoCelda}}px;">{{controller.totalActualizados}}</td>
														<td style="text-align: center; font-weight: bold; max-width:{{controller.tamanoCelda}}px; min-width:{{controller.tamanoCelda}}px;">{{controller.totalEliminados}}</td>
														<td style="text-align: center; font-weight: bold; max-width:{{controller.tamanoCelda}}px; min-width:{{controller.tamanoCelda}}px;"></td>
													</tr>
												</tbody>
											</table>
										</div>
									</div>
			    				</div>
			    			</div>
			    			<br>
			    			<div class="row">
			    				<div class="form-group col-sm-2" align="center">
			    				</div>
			    				<div class="form-group col-sm-8" align="center">
			    					<label class="label-form">Transacciones</label>
					    			<canvas id="line" class="chart chart-line" chart-data="controller.dataLineales"
										chart-labels="controller.labelsMeses" chart-series="controller.seriesLineales" chart-options="controller.charOptionsLineales"
										chart-colors = "controller.radarColors" chart-legend="true">
									</canvas>
								</div>
								<div class="form-group col-sm-2" align="center">
			    				</div>
			    			</div>
			    		</div>
		   				
			    	</div>
			    </div>	
			</div>
		</div>
	</div>