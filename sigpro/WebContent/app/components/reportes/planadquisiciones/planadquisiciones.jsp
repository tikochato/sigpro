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
			margin-right: -15px;
			overflow-y:hidden;
			overflow-x:hidden;
			min-height: 448px;
			max-height: 448px;
		}
		
		.tablaDatos {
			display: flex;
		    flex-direction: column;
		    align-items: stretch;
		    min-height: 390px;
		    max-height: 390px;  
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
		
		pre {
		    margin: 0 0 0px;
		    font-size: medium;
		    line-height: normal;
		    color: #333;
		    word-break: none;
		    word-wrap: break-word;
		    background-color: transparent;
		    border: none;
		    border-radius: none;
		    font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
		    padding: 0px;
		    line-height: normal;
	    }
	    
	    .divPadreNombres{
			float: left; 
		  	width: 300px; 
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
		    min-height: 390px;
		    max-height: 390px; 
		}
		.cuerpoTablaNombres {
		    overflow-y: scroll;
		    overflow-x: scroll;
		    display: inline-block;
		    font-size: 13px;
		    max-width: 300px;
		    min-height: 390px;
		    max-height: 390px; 
		}
	</style>
	
	<div ng-controller="planAdquisicionesController as controller" class="maincontainer all_page" id="title">
		<script type="text/ng-template" id="pago.jsp">
    		<%@ include file="/app/components/reportes/planadquisiciones/pago.jsp"%>
  		</script>
		<shiro:lacksPermission name="24010">
			<p ng-init="controller.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<div class="row">
	    	<div class="col-sm-12">
	    		<div style="width: 100%; height: 20%">
		    		<div class="row">
		    			<div class="panel panel-default">
			  				<div class="panel-heading"><h3>Plan de adquisiciones AÑO FISCAL {{controller.anio}}</h3></div>
						</div>
		    		</div>
		    		<br>
		    		<div class="row">
		    			<div class="form-group col-sm-3">
							<select  class="inputText" ng-model="controller.prestamo"
								ng-options="a.text for a in controller.prestamos" 
								ng-change="controller.generar();"></select>
							<label for="tObjeto" class="floating-label">Préstamos</label>
	    				</div>
						<div class="operation_buttons" align="right">
							<div class="btn-group">
								<label class="btn btn-default"  ng-click="controller.setValores();" uib-tooltip="Plan de pagos" ng-hide="!controller.planPagos">
								<span class="glyphicon glyphicon glyphicon-usd" aria-hidden="true"></span></label>
							</div>
							<div class="btn-group">
								<label class="btn btn-default"  ng-click="controller.limpiar(row);" uib-tooltip="Limpiar" ng-hide="!controller.limpiarRow">
								<span class="glyphicon glyphicon glyphicon-erase" aria-hidden="true"></span></label>
							</div>
							<div class="btn-group">
								<label class="btn btn-default"  ng-click="controller.guardarPlan();" uib-tooltip="Guardar" ng-hide="!controller.guardar">
								<span class="glyphicon glyphicon glyphicon-floppy-disk" aria-hidden="true"></span></label>
							</div>
							<div class="btn-group">
								<label class="btn btn-default"  ng-click="controller.exportarExcel();" uib-tooltip="Exportar" ng-hide="!controller.exportar">
								<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
							</div>
						</div>
		    		</div>
		    	</div>
		    	<div class="row">
		    		<div style="width: 100%; height: 80%" id="reporte">
		    			<div class="grid_loading" ng-hide="!controller.mostrarCargando">
							<div class="msg">
								<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
									<br />
									<br /> <b>Cargando, por favor espere...</b> 
								</span>
							</div>
						</div>	
		    			<div class="row" ng-hide="!controller.mostrarDescargar">
		    				<div class="divPadreNombres">
		    					<div class="divTabla"> 
		    						<table st-table="controller.displayedCollectionPrestamo" st-safe-src="controller.rowCollectionPrestamo" class="table table-striped tablaDatos">
			    						<thead class="theadDatos">
			    							<tr>
					    						<th style="height:67px;" class="label-form" rowspan="2">Tipo</th>
					    						<th style="min-width:200px;text-align: left; height:67px;vertical-align: middle;" class="label-form" rowspan="2">Nombre</th>
					    					</tr>
			    						</thead>
			    						<tbody class="cuerpoTablaNombres" id="divTablaNombres" ng-mouseover="controller.activarScroll('divTablaNombres')" scrollespejo style="margin-bottom: -15px;">
			    							<tr ng-repeat="row in controller.rowCollectionPrestamo"  ng-click="controller.selectedRow(row)">
			    								<td><div class="{{controller.claseIcon(row)}}"></div></td>
					    						<td class="divisionNombre">
					    							<pre>{{row.nombre}}</pre>
					    						</td>
			    							</tr>
			    						</tbody>
			    					</table>
		    					</div>
		    				</div>
		    				<div class="divPadreDatos" style="min-width: {{controller.tamanoTotal}}px; max-width: {{controller.tamanoTotal}}px;">
		    					<div class="divTabla">
			    					<table st-table="controller.displayedCollectionPrestamo" st-safe-src="controller.rowCollectionPrestamo" class="table table-striped tablaDatos"
			    						style="max-width: {{controller.tamanoTotal}}px;">
			    						<thead id="divCabecerasDatos" class="theadDatos">
			    							<tr>
			    								<th class="label-form" rowspan="2" style="text-align: center; min-width: 80px; max-width: 80px">Método</th>
					    						<th class="label-form" rowspan="2" style="text-align: center; min-width: 115px; max-width: 115px">Ud. Medida</th>
					    						<th class="label-form" rowspan="2" style="text-align: center; min-width: 75px; max-width: 75px">Cant.</th>
					    						<th class="label-form" rowspan="2" style="text-align: center; min-width: 140px; max-width: 140px">Costo</th>
					    						<th class="label-form" rowspan="2" style="text-align: center; min-width: 140px; max-width: 140px">Total</th>
					    						<th class="label-form" colspan="2" style="text-align: center">Prep. de docs.</th>
					    						<th class="label-form" colspan="2" style="text-align: center">Lanz. de evento</th>
					    						<th class="label-form" colspan="2" style="text-align: center">Recep. y eval. ofertas</th>
					    						<th class="label-form" colspan="2" style="text-align: center">Adjudicación</th>
					    						<th class="label-form" colspan="2" style="text-align: center">Firma contrato</th>
			    							</tr>
			    							<tr>
			    								<th class="label-form" style="text-align: center; min-width: 80px; max-width: 80px">Plan.</th>
					    						<th class="label-form" style="text-align: center; min-width: 80px; max-width: 80px">Real</th>
					    						<th class="label-form" style="text-align: center; min-width: 80px; max-width: 80px">Plan.</th>
					    						<th class="label-form" style="text-align: center; min-width: 80px; max-width: 80px">Real</th>
					    						<th class="label-form" style="text-align: center; min-width: 80px; max-width: 80px">Plan.</th>
					    						<th class="label-form" style="text-align: center; min-width: 80px; max-width: 80px">Real</th>
					    						<th class="label-form" style="text-align: center; min-width: 80px; max-width: 80px">Plan.</th>
					    						<th class="label-form" style="text-align: center; min-width: 80px; max-width: 80px">Real</th>
					    						<th class="label-form" style="text-align: center; min-width: 80px; max-width: 80px">Plan.</th>
					    						<th class="label-form" style="text-align: center; min-width: 80px; max-width: 80px">Real</th>
			    							</tr>
			    						</thead>
			    						<tbody class="cuerpoTablaDatos" id="divTablaDatos" ng-mouseover="controller.activarScroll('divTablaDatos')" scrollespejo>
			    							<tr ng-repeat="row in controller.rowCollectionPrestamo">
			    								<td style="min-width: 80px; max-width: 80px">{{row.metodo}}</td>		    								
								    			<td style="min-width: 115px; max-width: 115px" ng-click="controller.focus(row, 1)">
								    				<select><option ng-selected="row.unidadMedida != null ? x.id=={{row.unidadMedida}} : 0" ng-repeat="x in controller.ddlOpciones" value={{x.id}}>{{x.value}}</option></select>
								    			</td>			
					    						<td style="text-align: center; min-width: 75px; max-width: 75px">{{row.cantidad}}</td>
					    						<td style="text-align: center; min-width: 140px; max-width: 140px">{{row.costo | formatoMillones : controller.enMillones}}</td>
					    						<td class="divisionColumna" style="min-width: 140px; max-width: 140px; text-align: center">{{row.total | formatoMillones : controller.enMillones}}</td>
					    						<td class="colorPlanificado divisionColumna" style="text-align: center; min-width: 80px; max-width: 80px;">{{row.planificadoDocs}}</td>
					    						<td class="colorReal divisionColumna" style="text-align: center; min-width: 80px; max-width: 80px;">{{row.realDocs}}</td>
					    						<td class="colorPlanificado divisionColumna" style="text-align: center;min-width: 80px; max-width: 80px;">{{row.planificadoLanzamiento}}</td>
					    						<td class="colorReal divisionColumna" style="text-align: center; min-width: 80px; max-width: 80px;">{{row.realLanzamiento}}</td>
					    						<td class="colorPlanificado divisionColumna" style="text-align: center; min-width: 80px; max-width: 80px;">{{row.planificadoRecepcionEval}}</td>
					    						<td class="colorReal divisionColumna" style="text-align: center; min-width: 80px; max-width: 80px;">{{row.realRecepcionEval}}</td>
					    						<td class="colorPlanificado divisionColumna" style="text-align: center; min-width: 80px; max-width: 80px;">{{row.planificadoAdjudica}}</td>
					    						<td class="colorReal divisionColumna" style="text-align: center; min-width: 80px; max-width: 80px;">{{row.realAdjudica}}</td>
					    						<td class="colorPlanificado divisionColumna" style="text-align: center; min-width: 80px; max-width: 80px;">{{row.planificadoFirma}}</td>
					    						<td class="colorReal divisionColumna" style="text-align: center; min-width: 80px; max-width: 80px;">{{row.realFirma}}</td>
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
	    <br>
	    <div class="row">
	   		<div style="text-align: center;">
	   			<br>
	    		<ol class="leyendaTexto"  ng-hide="!controller.mostrarDescargar">
					<li><span class="colorPlanificadoFondo"></span>Planificado</li>
			        <li><span class="colorRealFondo"></span>Real</li>
				</ol>
			</div>
	    </div>
</div>