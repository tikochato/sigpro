<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<style>
		.etiquetaNombre{
			font-weight: bold;
		}
		.divisionColumna{
			border-right: 2px solid #ddd;
		}
		.truncate {
		  width: 300px;
		  white-space: nowrap;
		  overflow: hidden;
		  text-overflow: ellipsis;
		}
		.cuerpoTablaDatos {
		    overflow-y: scroll;
		    overflow-x: hidden;
		    text-align: center;
		    margin-right: -15px;
		    max-height: 300px;
		    min-height: 300px;
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
	<div ng-controller="administracionTransaccionalController as admintranc" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="24010">
			<p ng-init="admintranc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<div class="row"  id="reporte">
			<div class="col-sm-12">
				<div class="row">
			   		<div style="width: 100%; height: 15%">
			   			<div class="row">
			   				<div class="panel panel-default">
			  					<div class="panel-heading"><h3>Detalle de Transacciones</h3></div>
							</div>
						</div>
			   			<br>
			   		</div>
			   		
				  	<div class="row">
				  		<div class="col-sm-3">
							<div class="form-group" >
							  <input type="text"  class="inputText" uib-datepicker-popup="{{admintranc.formatofecha}}" ng-model="admintranc.fechaInicio" is-open="admintranc.fi_abierto"
						            datepicker-options="admintranc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"  
						            ng-required="true" ng-change="admintranc.validarFecha(admintranc.fechaInicio, admintranc.fechaFin)"
						            ng-value="admintranc.fechaInicio" onblur="this.setAttribute('value', this.value);"/>
						            <span class="label-icon" ng-click="admintranc.abrirPopupFecha(1000)">
						              <i class="glyphicon glyphicon-calendar"></i>
						            </span>
							  	<label for="campo.id" class="floating-label">*Fecha Inicial</label>
							</div>
						</div>
		
						<div class="col-sm-3">
							<div class="form-group" >
							  <input type="text"  class="inputText" uib-datepicker-popup="{{admintranc.formatofecha}}" ng-model="admintranc.fechaFin" is-open="admintranc.ff_abierto"
						            datepicker-options="admintranc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" 
						            ng-required="true" ng-change="admintranc.validarFecha(admintranc.fechaInicio, admintranc.fechaFin)"
						            ng-value="admintranc.fechaFin" onblur="this.setAttribute('value', this.value);"/>
						            <span class="label-icon" ng-click="admintranc.abrirPopupFecha(1001)">
						              <i class="glyphicon glyphicon-calendar"></i>
						            </span>
							  	<label for="campo.id" class="floating-label">*Fecha Final</label>
							</div>
						</div>
						
						<div class="col-sm-3">
						</div>
						<div class="col-sm-3" align="right">
							<div class="btn-group" role="group" aria-label="">
								<label class="btn btn-default" ng-click="admintranc.exportarExcel()" uib-tooltip="Exportar a Excel" ng-hide="!admintranc.mostrarGrafica">
								<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
								<label class="btn btn-default" ng-click="admintranc.exportarPdf()" uib-tooltip="Exportar a PDF" ng-hide="true">
								<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
							</div>
						</div>
				  	</div>
				</div>
				<br>
				<div class="row" align="center">
			    	<div class="col-sm-12">
			    		<div class="grid_loading" style="height: 375px" ng-hide="!admintranc.mostrarcargando">
						  	<div class="msg">
						      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
								  <br /><br />
								  <b>Cargando, por favor espere...</b>
							  </span>
							</div>
					  	</div>
			    		<div style="width: 100%; height: 85%; text-align: center" ng-hide="!admintranc.mostrarTablas">
			    			<div class="row" ng-hide="!admintranc.mostrarGrafica">
			    				<div class="form-group col-sm-12" align="center">
									<div class="divPadreDatos" style="max-width: {{admintranc.tamanoPantalla}}px">
										<div class="divTabla"> 
											<table st-table="admintranc.displayedDatos" st-safe-src="admintranc.rowDatos" class="table table-striped tablaDatos"  style="max-width:{{admintranc.tamanoPantalla}}px;">
												<thead class="theadDatos">
													<tr>
														<th style="display: none;">Id</th>
														<th class="label-form" style="max-width:300px; min-width:300px; text-align: center;">Nombre</th>
														<th class="label-form" style="max-width:{{admintranc.tamanoCelda}}px; min-width:{{admintranc.tamanoCelda}}px; text-align: center;">Creados</th>
														<th class="label-form" style="max-width:{{admintranc.tamanoCelda}}px; min-width:{{admintranc.tamanoCelda}}px; text-align: center;">Actualizados</th>
														<th class="label-form" style="max-width:{{admintranc.tamanoCelda}}px; min-width:{{admintranc.tamanoCelda}}px; text-align: center;">Eliminados</th>
														<th class="label-form" style="max-width:{{admintranc.tamanoCelda}}px; min-width:{{admintranc.tamanoCelda}}px; text-align: center;">Descargar</th>
													</tr>
													<tr ng-hide="true">
														<th>
															<input st-search="usuario" st-delay="false" placeholder="búsqueda por usuario..." class="input-sm form-control" type="search"/>
														</th>
													<tr>
												</thead>
												<tbody class="cuerpoTablaDatos">
													<tr ng-repeat="row in admintranc.displayedDatos" style=" min-height: 25px; max-height: 25px; ">
														<td style="display: none;">{{row.id}}</td>
														<td class="divisionColumna truncate" ng-class="admintranc.nombres(row)" style="max-width:300px; min-width:300px; text-align: left;">
															<div style="height: 30px;">
																<div uib-tooltip="{{row.nivel == 0 ? row.nombre : ''}}" style="margin-left: {{row.nivel}}em; min-height: 25px; max-height: 25px; max-width: 300px; min-width: 300px">{{row.nombre}}</div>
															</div>
														</td>
														<td class="divisionColumna" style="width:{{admintranc.tamanoCelda}}px; text-align: center">
															<div style="height: 30px;">
																{{row.transacciones.creados}}
															</div>
														</td>
														<td class="divisionColumna" style="width:{{admintranc.tamanoCelda}}px; text-align: center">
															<div style="height: 30px;">
																{{row.transacciones.actualizados}}
															</div>
														</td>
														<td class="divisionColumna" style="width:{{admintranc.tamanoCelda}}px; text-align: center">
															<div style="height: 30px;">
																{{row.transacciones.eliminados}}
															</div>
														</td>
														<td class="divisionColumna" style="width:{{admintranc.tamanoCelda}}px; text-align: center">
															<div style="height: 30px;">
																<button type="button"
																	ng-hide="{{row.nivel == 1}}"
																	ng-click="admintranc.descargarExcelDetalle(row)"
																	uib-tooltip="Exportar a Excel detalle de {{row.usuario}}" tooltip-placement="bottom"
																	class="btn btn-default">
																	<i class="glyphicon glyphicon-export"> </i>
																</button>
																<button type="button"
																	ng-hide="{{row.nivel == 1}}"
																	ng-click="admintranc.exportarPdfDetalle(row)"
																	uib-tooltip="Exportar a PDF detalle de {{row.usuario}}" tooltip-placement="bottom"
																	class="btn btn-default">
																	<i class="glyphicon glyphicon-save-file"> </i>
																</button>
															</div>
														</td>
													</tr>
												</tbody>
												<tbody class="cuerpoTablafooter">
													<tr ng-click="admintranc.totalesGenerales();">
														<td style="display: none;"></td>
														<td style="text-align: left; font-weight: bold; max-width:300px; min-width:300px;">Totales:</td>
														<td style="text-align: center; font-weight: bold; max-width:{{admintranc.tamanoCelda}}px; min-width:{{admintranc.tamanoCelda}}px;">{{admintranc.totalCreados}}</td>
														<td style="text-align: center; font-weight: bold; max-width:{{admintranc.tamanoCelda}}px; min-width:{{admintranc.tamanoCelda}}px;">{{admintranc.totalActualizados}}</td>
														<td style="text-align: center; font-weight: bold; max-width:{{admintranc.tamanoCelda}}px; min-width:{{admintranc.tamanoCelda}}px;">{{admintranc.totalEliminados}}</td>
														<td style="text-align: center; font-weight: bold; max-width:{{admintranc.tamanoCelda}}px; min-width:{{admintranc.tamanoCelda}}px;"></td>
													</tr>
												</tbody>
											</table>
										</div>
									</div>
			    				</div>
			    			</div>
			    			<br>
			    			<div class="row" ng-hide="!admintranc.mostrarGrafica">
			    				<div class="form-group col-sm-2" align="center">
			    				</div>
			    				<div class="form-group col-sm-8" align="center">
			    					<label class="label-form">Transacciones</label>
					    			<canvas id="line" class="chart chart-line" chart-data="admintranc.dataLineales"
										chart-labels="admintranc.labelsMeses" chart-series="admintranc.seriesLineales" chart-options="admintranc.charOptionsLineales"
										chart-colors = "admintranc.radarColors" chart-legend="true">
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