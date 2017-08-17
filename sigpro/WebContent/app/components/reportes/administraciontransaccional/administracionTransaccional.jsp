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
		}
		
		.cuerpoTablafooter {
		    text-align: center;
		    margin-left: -15px;
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
	<div ng-controller="administracionTransaccionalController as controller" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="24010">
			<p ng-init="controller.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<div class="row">
	   		<div style="width: 100%; height: 15%">
	   			<div class="row">
	   				<div class="panel panel-default">
	  					<div class="panel-heading"><h3>Administración Transaccional</h3></div>
					</div>
				</div>
	   			<br>
	   		</div>	
		</div>
		<br>
		<div class="row" align="center" >
	    	<div class="col-sm-12">
	    		<div style="width: 100%; height: 85%; text-align: center" id="reporte">
	    			<div class="row">
	    				<div class="form-group col-sm-12" align="center">
							<div class="divPadreDatos" style="max-width: {{controller.tamanoPantalla}}px">
								<div class="grid_loading" ng-hide="!controller.mostrarcargando">
								  	<div class="msg">
								      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
										  <br /><br />
										  <b>Cargando, por favor espere...</b>
									  </span>
									</div>
							  	</div>
								<div class="divTabla"> 
									<table st-table="controller.displayedDatos" st-safe-src="controller.rowDatos" class="table table-striped tablaDatos">
										<thead class="theadDatos">
											<tr>
												<th style="display: none;">Id</th>
												<th class="label-form" style="max-width:{{controller.tamanoCelda}}px; min-width:{{controller.tamanoCelda}}px; text-align: center;" st-sort="row.usuario">Usuario</th>
												<th class="label-form" style="max-width:{{controller.tamanoCelda}}px; min-width:{{controller.tamanoCelda}}px; text-align: center;" st-sort="row.creados">Creados</th>
												<th class="label-form" style="max-width:{{controller.tamanoCelda}}px; min-width:{{controller.tamanoCelda}}px; text-align: center;" st-sort="row.actualizados">Actualizados</th>
												<th class="label-form" style="max-width:{{controller.tamanoCelda}}px; min-width:{{controller.tamanoCelda}}px; text-align: center;" st-sort="row.eliminados">Eliminados</th>
											</tr>
											<tr>
												<th colspan="4" ng-hide="true">
													<input st-search="" placeholder="búsqueda global..." class="input-sm form-control" type="search"/>
												</th>
											<tr>
										</thead>
										<tbody class="cuerpoTablaDatos">
											<tr ng-repeat="row in controller.displayedDatos" ng-click="controller.totalesPorUsuario(row);">
												<td style="display: none;">{{row.id}}</td>
												<td style="width:{{controller.tamanoCelda}}px; text-align: left;">{{row.usuario}}</td>
												<td style="width:{{controller.tamanoCelda}}px; text-align: center">{{row.creados}}</td>
												<td style="width:{{controller.tamanoCelda}}px; text-align: center">{{row.actualizados}}</td>
												<td style="width:{{controller.tamanoCelda}}px; text-align: center">{{row.eliminados}}</td>
											</tr>
										</tbody>
										<tbody class="cuerpoTablafooter">
											<tr ng-click="controller.totalesGenerales();">
												<td style="display: none;"></td>
												<td style="text-align: left; font-weight: bold; max-width:{{controller.tamanoCelda}}px; min-width:{{controller.tamanoCelda}}px;">Totales:</td>
												<td style="text-align: center; font-weight: bold; max-width:{{controller.tamanoCelda}}px; min-width:{{controller.tamanoCelda}}px;">{{controller.totalCreados}}</td>
												<td style="text-align: center; font-weight: bold; max-width:{{controller.tamanoCelda}}px; min-width:{{controller.tamanoCelda}}px;">{{controller.totalActualizados}}</td>
												<td style="text-align: center; font-weight: bold; max-width:{{controller.tamanoCelda}}px; min-width:{{controller.tamanoCelda}}px;">{{controller.totalEliminados}}</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
	    				</div>
	    			</div>
	    			<br>
	    			<div class="row">
	    				<div class="form-group col-sm-12" align="center">
	    					<label class="label-form">Transacciones</label>
							<div style="width: 75%;">
   								<canvas id="bar" class="chart chart-bar" chart-data="controller.data" chart-labels="controller.labels" 
   									chart-options="controller.charOptions" chart-series="controller.series" chart-legend="false">
   								</canvas>
 							</div>
	    				</div>
	    			</div>
	    		</div>
   				
	    	</div>
	    </div>
	</div>