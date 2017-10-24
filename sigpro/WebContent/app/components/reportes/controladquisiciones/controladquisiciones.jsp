<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	
	<style>
		.truncate {
		  width: 250px;
		  white-space: nowrap;
		  overflow: hidden;
		  text-overflow: ellipsis;
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
	</style>
	
	<div ng-controller="controlAdquisicionesController as ctrladqui" class="maincontainer all_page" id="title" style="height: 100%">
		<script type="text/ng-template" id="pago.jsp">
    		<%@ include file="/app/components/reportes/controladquisiciones/pago.jsp"%>
  		</script>
		<shiro:lacksPermission name="24010">
			<p ng-init="ctrladqui.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<div class="row" id="reporte" style="height: 100%">
			<div class="col-sm-12" style=" height: 20%">
	    		<div style="width: 100%;">
		    		<div class="row">
		    			<div class="panel panel-default">
			  				<div class="panel-heading"><h3>Control de adquisiciones AÑO FISCAL {{ctrladqui.anio}}</h3></div>
						</div>
		    		</div>
		    		<br>
		    		<div class="row" style="width: 100%; height: 15%">
		    			<div class="form-group col-sm-5">
							<select  class="inputText" ng-model="ctrladqui.prestamo"
								ng-options="a.text for a in ctrladqui.prestamos" 
								ng-change="ctrladqui.generar();"></select>		
	    				</div>
						<div class="operation_buttons" align="right">
							<div class="btn-group" ng-hide="true">
								<label class="btn btn-default" ng-model="ctrladqui.enMillones"  tooltip-placement="center" uib-btn-radio="true" ng-click="ctrladqui.calcularTamaniosCeldas()" uib-tooltip="Millones de Quetzales" role="button" tabindex="0" aria-invalid="false" ng-hide="!ctrladqui.mostrarBotones">
									<span>MQ</span>
								</label>
							</div>
							<div class="btn-group" ng-hide="true">
								<label class="btn btn-default" ng-model="ctrladqui.enMillones" uib-btn-radio="false" ng-click="ctrladqui.calcularTamaniosCeldas()" uib-tooltip="Quetzales" role="button" tabindex="1" aria-invalid="false" ng-hide="!ctrladqui.mostrarBotones">
									<span>Q</span>
								</label>
							</div>
							<div class="btn-group" ng-hide="true">
								<label class="btn btn-default"  ng-click="ctrladqui.agregarPagos();" uib-tooltip="Plan de pagos" tooltip-placement="center" ng-hide="!ctrladqui.mostrarBotones">
								<span>P</span></label>
							</div>
							<div class="btn-group" ng-hide="true">
								<label class="btn btn-default"  ng-click="ctrladqui.limpiar(row);" uib-tooltip="Limpiar" ng-hide="!ctrladqui.mostrarBotones">
								<span class="glyphicon glyphicon glyphicon-erase" aria-hidden="true"></span></label>
							</div>
							<div class="btn-group" ng-hide="true">
								<label class="btn btn-default"  ng-click="ctrladqui.guardarPlan();" uib-tooltip="Guardar" ng-hide="!ctrladqui.mostrarBotones">
								<span class="glyphicon glyphicon glyphicon-floppy-disk" aria-hidden="true"></span></label>
							</div>
							<div class="btn-group">
								<label class="btn btn-default"  ng-click="ctrladqui.exportarExcel();" uib-tooltip="Exportar" ng-hide="!ctrladqui.mostrarBotones">
								<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
								<label class="btn btn-default" ng-click="ctrladqui.exportarPdf()" uib-tooltip="Exportar PDF" ng-hide="true">
								<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
							</div>
						</div>
		    		</div>
				</div>
			</div>
			<br><br><br><br><br><br><br><br>
			<div class="col-sm-12" style="height: 80%">
				<div ng-hide="!ctrladqui.mostrarCargando" style="width: 100%; height: 100%">
    				<div class="grid_loading" ng-hide="!ctrladqui.mostrarCargando">
						<div class="msg">
							<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
								<br />
								<br /> <b>Cargando, por favor espere...</b> 
							</span>
						</div>
					</div>
				</div>
	    		<div class="divTablas" ng-hide="!ctrladqui.mostrarTablas">	
	    			<div class="row" style="height: 100%; max-width: {{ctrladqui.tamanoPantalla}}; min-width: {{ctrladqui.tamanoPantalla}}">
	    				<div class="divPadreDatos" style="height: 100%;min-width: {{ctrladqui.tamanoPantalla}}px; max-width: {{ctrladqui.tamanoPantalla}}px;">
	    					<div class="divTabla">
		    					<table st-table="ctrladqui.displayedCollectionPrestamo" st-safe-src="ctrladqui.rowCollectionPrestamo" class="table table-striped tablaDatos"
		    						style="max-width: {{ctrladqui.tamanoPantalla}}px;">
		    						<thead id="divCabecerasDatos" class="theadDatos">
		    							<tr>
		    								<th class="label-form" rowspan="2" style="text-align: left; min-width:300px; max-width:300px;">Nombre</th>
				    						<th class="label-form" rowspan="2" style="text-align: center; min-width: 200px; max-width: 200px">Tipo de Adq.</th>
				    						<th class="label-form" rowspan="2" style="text-align: center; min-width: 150px; max-width: 150px">Ud. Medida</th>
				    						<th class="label-form" rowspan="2" style="text-align: center; min-width: 200px; max-width: 200px">Cat. de Adq.</th>
				    						<th class="label-form" rowspan="2" style="text-align: center; min-width: 75px; max-width: 75px">Cant.</th>
				    						<th class="label-form" rowspan="2" style="text-align: center; min-width: 140px; max-width: 140px">Costo</th>
				    						<th class="label-form" rowspan="2" style="text-align: center; min-width: 140px; max-width: 140px">Total</th>
				    						<th class="label-form" rowspan="2" style="text-align: center; min-width: 140px; max-width: 140px">NOG</th>
				    						<th class="label-form" colspan="2" style="text-align: center">Prep. de docs.</th>
				    						<th class="label-form" colspan="2" style="text-align: center">Lanz. de evento</th>
				    						<th class="label-form" colspan="2" style="text-align: center">Recep. y eval. ofertas</th>
				    						<th class="label-form" colspan="2" style="text-align: center">Adjudicación</th>
				    						<th class="label-form" colspan="2" style="text-align: center">Firma contrato</th>
		    							</tr>
		    							<tr>
		    								<th class="label-form" style="text-align: center; min-width: 90px; max-width: 90px">Plan.</th>
				    						<th class="label-form" style="text-align: center; min-width: 90px; max-width: 90px">Real</th>
				    						<th class="label-form" style="text-align: center; min-width: 90px; max-width: 90px">Plan.</th>
				    						<th class="label-form" style="text-align: center; min-width: 90px; max-width: 90px">Real</th>
				    						<th class="label-form" style="text-align: center; min-width: 90px; max-width: 90px">Plan.</th>
				    						<th class="label-form" style="text-align: center; min-width: 90px; max-width: 90px">Real</th>
				    						<th class="label-form" style="text-align: center; min-width: 90px; max-width: 90px">Plan.</th>
				    						<th class="label-form" style="text-align: center; min-width: 90px; max-width: 90px">Real</th>
				    						<th class="label-form" style="text-align: center; min-width: 90px; max-width: 90px">Plan.</th>
				    						<th class="label-form" style="text-align: center; min-width: 90px; max-width: 90px">Real</th>
		    							</tr>
		    						</thead>
		    						<tbody vs-repeat class="cuerpoTablaDatos" id="divTablaDatos" onmouseover="activarScroll(this.id)" onscroll="scrollEspejo(this)">
		    							<tr ng-repeat="row in ctrladqui.rowCollectionPrestamo">
		    								<td class="divisionColumna truncate" style="min-width:300px;max-width:300px;">
				    							<div style="height: 25px;">
				    								<div style="text-align: left" uib-tooltip="{{row.nombre}}"><span style="float: left;margin-left: {{row.nivel-1}}em;" ng-class="ctrladqui.claseIcon(row);" uib-tooltip="{{ctrladqui.tooltipObjetoTipo[row.objetoTipo]}}"></span>{{row.nombre}}</div>
				    							</div>
				    						</td>
		    								<td class="divisionColumna" style="min-width: 200px; max-width: 200px">
		    									<div style="height: 25px;">
			    									{{row.tipoAdquisicionNombre}}
							    				</div>
		    								</td>		    								
							    			<td class="divisionColumna" style="min-width: 150px; max-width: 150px">
								    			<div style="height: 25px;">
								    				{{row.unidadMedida}}
								    			</div>
							    			</td>
							    			<td class="divisionColumna" style="min-width: 200px; max-width: 200px">
								    			<div style="height: 25px;">
								    				{{row.categoriaAdquisicionNombre}}								    				
								    			</div>
							    			</td>			
				    						<td class="divisionColumna" style="text-align: right; min-width: 75px; max-width: 75px">
					    						<div style="height: 25px;">
					    							{{row.cantidad || 0}}
					    						</div>
				    						</td>
				    						<td class="divisionColumna" style="text-align: right; min-width: 140px; max-width: 140px">
					    						<div style="height: 25px;">
					    							{{row.costo || 0 | formatoMillones : ctrladqui.enMillones}}
					    						</div>
				    						</td>
				    						<td class="divisionColumna" style="min-width: 140px; max-width: 140px; text-align: right">
					    						<div style="height: 25px;">
					    							{{row.total | formatoMillones : ctrladqui.enMillones}}
					    						</div>
				    						</td>
				    						<td class="divisionColumna" style="min-width: 140px; max-width: 140px; text-align: right">
				    							<div style="height: 25px;">
				    								{{row.nog}}
				    							</div>
				    						</td>
				    						<td class="colorPlanificado divisionColumna" style="text-align: left; min-width: 90px; max-width: 90px;">
					    						<div style="height: 25px;">
					    							{{row.planificadoDocs}}
					    						</div>
				    						</td>
				    						<td class="colorReal divisionColumna" style="text-align: left; min-width: 90px; max-width: 90px;">
					    						<div style="height: 25px;">
					    							{{row.realDocs}}
					    						</div>
				    						</td>
				    						<td class="colorPlanificado divisionColumna" style="text-align: left;min-width: 90px; max-width: 90px;">
					    						<div style="height: 25px;">
					    							{{row.planificadoLanzamiento}}
					    						</div>
				    						</td>
				    						<td class="colorReal divisionColumna" style="text-align: left; min-width: 90px; max-width: 90px;">
					    						<div style="height: 25px;">
					    							{{row.realLanzamiento}}
					    						</div>
				    						</td>
				    						<td class="colorPlanificado divisionColumna" style="text-align: left; min-width: 90px; max-width: 90px;">
					    						<div style="height: 25px;">
					    							{{row.planificadoRecepcionEval}}
					    						</div>
				    						</td>
				    						<td class="colorReal divisionColumna" style="text-align: left; min-width: 90px; max-width: 90px;">
					    						<div style="height: 25px;">
					    							{{row.realRecepcionEval}}
					    						</div>
				    						</td>
				    						<td class="colorPlanificado divisionColumna" style="text-align: left; min-width: 90px; max-width: 90px;">
					    						<div style="height: 25px;">
					    							{{row.planificadoAdjudica}}
												</div>					    							
				    						</td>
				    						<td class="colorReal divisionColumna" style="text-align: left; min-width: 90px; max-width: 90px;">
					    						<div style="height: 25px;">
					    							{{row.realAdjudica}}
					    						</div>
				    						</td>
				    						<td class="colorPlanificado divisionColumna" style="text-align: left; min-width: 90px; max-width: 90px;">
					    						<div style="height: 25px;">
					    							{{row.planificadoFirma}}
					    						</div>
				    						</td>
				    						<td class="colorReal divisionColumna" style="text-align: left; min-width: 90px; max-width: 90px;">
					    						<div style="height: 25px;">
					    							{{row.realFirma}}
					    						</div>
				    						</td>
		    							</tr>
		    						</tbody>
		    					</table>
	    					</div>
	    				</div>	    			
		    		</div>
	    		</div>
	    		<div style="text-align: center;">
		   			<br>
		    		<ol class="leyendaTexto"  ng-hide="!ctrladqui.mostrarTablas">
						<li><span class="colorPlanificadoFondo"></span>Planificado</li>
				        <li><span class="colorRealFondo"></span>Real</li>
					</ol>
				</div>
	    	</div>
		</div>
	</div>