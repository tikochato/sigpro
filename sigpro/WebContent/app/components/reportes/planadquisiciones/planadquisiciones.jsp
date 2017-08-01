<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	
	<style>
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
	    
	    .cuerpoTablaNombres {
		    overflow-y: scroll;
		    overflow-x: scroll;
		}
		.table>thead>tr>th {
    		vertical-align: middle;
    		border-bottom: 2px solid #ddd;
		}
		
		.divTabla{
			witdh: 100%;
			max-height: 375px;
		}
		
		.tablaDatos {
			width: 100%;
		    height: 375px; 
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
	    		<div style="width: 100%; height: 15%">
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
								<label class="btn btn-default"  ng-click="controller.agregarPago(row);" uib-tooltip="Plan de pagos" ng-hide="!controller.planPagos">
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
	    		<div style="width: 100%; height: 85%">
	    			<div class="row" ng-hide="!controller.mostrarDatos">
	    				<div class="divTabla" style="max-height: 430px; ;overflow: scroll;">
	    					<table st-table="controller.displayedCollectionPrestamo" st-safe-src="controller.rowCollectionPrestamo" class="table table-striped tablaDatos">
			    				<thead class="table>thead>tr>th">
			    					<tr>
			    						<th class="label-form" rowspan="2">Tipo</th>
			    						<th class="label-form" rowspan="2" style="min-width: 500px; max-width:500px">Nombre</th>
			    						<th class="label-form" rowspan="2">Método</th>
			    						<th class="label-form" rowspan="2">Unidad Medida</th>
			    						<th class="label-form" rowspan="2">Cantidad</th>
			    						<th class="label-form" rowspan="2" style="min-width: 150px;">Costo</th>
			    						<th class="label-form" rowspan="2" style="min-width: 150px;">Total</th>
			    						<th class="label-form" colspan="2" style="text-align: center">Preparación de documentos</th>
			    						<th class="label-form" colspan="2" style="text-align: center">Lanzamiento de evento</th>
			    						<th class="label-form" colspan="2" style="text-align: center">Recepción y evaluación de ofertas</th>
			    						<th class="label-form" colspan="2" style="text-align: center">Adjudicación</th>
			    						<th class="label-form" colspan="2" style="text-align: center">Firma contrato</th>
			    					</tr>
			    					<tr>
			    						<th class="label-form" style="text-align: center">Planificado</th>
			    						<th class="label-form" style="text-align: center">Real</th>
			    						<th class="label-form" style="text-align: center">Planificado</th>
			    						<th class="label-form" style="text-align: center">Real</th>
			    						<th class="label-form" style="text-align: center">Planificado</th>
			    						<th class="label-form" style="text-align: center">Real</th>
			    						<th class="label-form" style="text-align: center">Planificado</th>
			    						<th class="label-form" style="text-align: center">Real</th>
			    						<th class="label-form" style="text-align: center">Planificado</th>
			    						<th class="label-form" style="text-align: center">Real</th>
			    					</tr>
			    				</thead>
			    				<tbody class="cuerpoTablaNombres">
			    					<tr ng-repeat="row in controller.rowCollectionPrestamo" ng-click="controller.setValores(row)">
			    						<td><div class="{{controller.claseIcon(row)}}"></div></td>
			    						<td class="divisionNombre" style="min-width: 500px; max-width:500px">
			    							<pre>{{row.nombre}}</pre>
			    						</td>
			    						<td>{{row.metodo}}</td>
						    			<td>{{row.unidadMedida}}</td>			
			    						<td>{{row.cantidad}}</td>
			    						<td style="min-width: 150px;">{{row.costo | formatoMillones : controller.enMillones}}</td>
			    						<td class="divisionColumna" style="min-width: 150px;">{{row.total | formatoMillones : controller.enMillones}}</td>
			    						<td class="colorPlanificado divisionColumna">{{row.planificadoDocs}}</td>
			    						<td class="colorReal divisionColumna">{{row.realDocs}}</td>
			    						<td class="colorPlanificado divisionColumna">{{row.planificadoLanzamiento}}</td>
			    						<td class="colorReal divisionColumna">{{row.realLanzamiento}}</td>
			    						<td class="colorPlanificado divisionColumna">{{row.planificadoRecepcionEval}}</td>
			    						<td class="colorReal divisionColumna">{{row.realRecepcionEval}}</td>
			    						<td class="colorPlanificado divisionColumna">{{row.planificadoAdjudica}}</td>
			    						<td class="colorReal divisionColumna">{{row.realAdjudica}}</td>
			    						<td class="colorPlanificado divisionColumna">{{row.planificadoFirma}}</td>
			    						<td class="colorReal divisionColumna">{{row.realFirma}}</td>
			    					</tr>
			    				</tbody>
		    				</table>
	    				</div>
	    			</div>
	    		</div>
	    	</div>
	    </div>
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