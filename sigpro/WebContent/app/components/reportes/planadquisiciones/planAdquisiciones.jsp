<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	
	<style>
		.label-form {
		    font-size: 13px;
		    opacity: 1;
		    color: rgba(0,0,0,0.38) !important;
		    font-weight: bold;
		}
		.label-Options {
			text-overflow: ellipsis;
		    width:190px;
    		overflow: hidden;
		}
	    .ui-grid-tree-header-row {
	        font-weight: normal !important;
	    }
	   
	    .ui-grid-tree-padre {
	        font-weight: bold;
	    }
	    
	    .colorPlanificado{
			color: #303f9e;
		}
		
		.colorReal{
			color: #257129;
		}
		
		.colorCeldaBloqueado{
			background-color: #d9d9d9;
		}
		
		.colorCeldaDesbloqueado{
			background-color: #f2f2f2;
		}
		
		.divisionColumna{
			border-right: 2px solid #ddd;
		}
		
		.divisionNombre{
			border-right: 3px solid #ddd;
		}
		
		.table>thead>tr>th {
    		vertical-align: middle;
    		border-bottom: 2px solid #ddd;
		}
		
		.divTabla{
			float: left;			
			overflow-y:hidden;
			overflow-x:hidden;
			height: 100%;
			height: 100%;
		}
		
		.tablaDatos {
			display: flex;
		    flex-direction: column;
		    align-items: stretch;
		    min-height: 100%;
		    max-height: 100%;  
		}
		
		.colorPlanificadoFondo{
			background-color: #303f9e;
			
		}
		
		.colorRealFondo{
			background-color: #257129;
		}
		
		.leyendaTexto {
		    text-align: right;
		}
		
		.divTablas{
			height: calc(100% - 32px);
			width: 100%;
		}
		
		.leyendaTexto li {
		    display: inline-block;
		    position: relative;
		    padding: 1px 8px 1px 15px;
		    font-size: smaller;
		}
		
		.leyendaTexto li span {
		    position: absolute;
		    left: 0;
		    width: 12px;
		    height: 12px;
		    border-radius: 4px;
		}
	    
	    .divPadreNombres{
			float: left; 
		  	width: 400px; 
			min-width: 285px; 
			max-width:285px; 
			overflow:hidden; 
		}
		
		.divPadreDatos{		
			float: left; 
			display: inline-block;
			white-space: nowrap;	
			overflow:hidden;		
		}
		
		.theadDatos {
			flex-shrink: 0; overflow-x: hidden;
		}
		.cuerpoTablaDatos {
		    overflow-y: scroll;
		    overflow-x: scroll;
		    display: inline-block;
		    text-align: center;
		    font-size: 13px; 
		}
		.cuerpoTablaNombres {
		    overflow-y: scroll;
		    overflow-x: scroll;
		    display: inline-block;
		    font-size: 13px;
		    max-width: 400px; 
		}
	</style>
	<div ng-controller="planAdquisicionesController as controller" class="maincontainer all_page" id="title" style="height: 100%">
		<shiro:lacksPermission name="24010">
			<p ng-init="controller.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="row" id="reporte" style="height: 100%">
			<div class="col-sm-12" style=" height: 20%">
				<div class="row">
	    			<div class="panel panel-default">
		  				<div class="panel-heading"><h3>Plan de adquisiciones AÑO FISCAL {{controller.anio}}</h3></div>
					</div>
	    		</div>
	    		<br>
	    		<div class="row" style="width: 100%; height: 15%">
	    			<div class="form-group col-sm-5">
						<select  class="inputText" ng-model="controller.prestamo"
							ng-options="a.text for a in controller.prestamos" 
							ng-change="controller.generar();"></select>		
    				</div>
	    			<div class="btn-group">
						<label class="btn btn-default"  ng-click="controller.exportarExcel();" uib-tooltip="Exportar" ng-hide="!controller.mostrarBotones">
						<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
						<label class="btn btn-default" ng-click="controller.exportarPdf()" uib-tooltip="Exportar PDF" ng-hide="!controller.mostrarBotones">
						<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
					</div>
	    		</div>
			</div>
			<div class="col-sm-12" style="height: 80%">
				<div ng-hide="!controller.mostrarCargando" style="width: 100%; height: 100%">
    				<div class="grid_loading" ng-hide="!controller.mostrarCargando">
						<div class="msg">
							<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
								<br />
								<br /> <b>Cargando, por favor espere...</b> 
							</span>
						</div>
					</div>
				</div>
				<br/><br/>
				<div class="divTablas" ng-hide="!controller.mostrarTablas">
					<div class="row" style="height: 100%; max-width: {{controller.tamanoPantalla}}; min-width: {{controller.tamanoPantalla}}">
						<div class="divPadreNombres" style="height: 100%">
	    					<div class="divTabla"> 
	    						<table st-table="controller.displayedCollectionPrestamo" st-safe-src="controller.rowCollectionPrestamo" class="table table-striped tablaDatos">
		    						<thead class="theadDatos">
		    							<tr>
				    						<th style="min-width:300px;text-align: left; height:71px;;vertical-align: middle;" class="label-form" rowspan="2">Descripción de la Adquisición</th>
				    					</tr>
		    						</thead>
		    						<tbody class="cuerpoTablaNombres" id="divTablaNombres" ng-mouseover="controller.activarScroll('divTablaNombres')" scrollespejo>
		    							<tr ng-repeat="row in controller.rowCollectionPrestamo">
		    								<td nowrap ng-class="row.bloqueado == true ? 'colorCeldaBloqueado' : 'colorCeldaDesbloqueado'">
				    							<div style="height: 25px;">
				    								<div uib-tooltip="{{row.nombre}}"><span ng-class="controller.claseIcon(row);" style="margin-left: {{row.nivel}}em" uib-tooltip="{{controller.tooltipObjetoTipo[row.objetoTipo-1]}}"></span>{{row.nombre}}</div>
				    							</div>
				    						</td>
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