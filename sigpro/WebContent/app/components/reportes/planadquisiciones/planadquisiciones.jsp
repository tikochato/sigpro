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
	    
	    .table-striped>tbody>tr:nth-child(odd)>td {
    		background-color: #f3f3f3;
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
	
	<div ng-controller="planAdquisicionesController as ctrladqui" class="maincontainer all_page" id="title" style="height: 100%">
		<script type="text/ng-template" id="pago.jsp">
    		<%@ include file="/app/components/reportes/planadquisiciones/pago.jsp"%>
  		</script>
		<shiro:lacksPermission name="24010">
			<p ng-init="ctrladqui.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<div class="row" id="reporte" style="height: 100%">
			<div class="col-sm-12">
				<div class="row" style=" height: 20%">
		    		<div style="width: 100%;">
			    		<div class="row">
			    			<div class="panel panel-default">
				  				<div class="panel-heading"><h3>Plan de adquisiciones AÑO FISCAL {{ctrladqui.anio}}</h3></div>
							</div>
			    		</div>
			    		<br>
			    		<div class="row">
			    			<div class="form-group col-sm-6" align="left">
								<div id="prestamo" angucomplete-alt placeholder="" pause="100" selected-object="ctrladqui.cambioPrestamo"
									  local-data="ctrladqui.lprestamos" search-fields="proyectoPrograma" title-field="proyectoPrograma" field-required="true" field-label="* Préstamo"
									  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
									  initial-value="ctrladqui.prestamoNombre" focus-out="ctrladqui.blurPrestamo()" input-name="prestamo"></div>
								<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
							</div>
				    	</div>
				    	<div class="row">
				    		<div class="form-group col-sm-6" align="left">
								<div id="pep" angucomplete-alt placeholder="" pause="100" selected-object="ctrladqui.cambioPep"
									  local-data="ctrladqui.peps" search-fields="nombre" title-field="nombre" field-required="true" field-label="* {{etiquetas.proyecto}}"
									  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
									  initial-value="ctrladqui.pepNombre" focus-out="ctrladqui.blurPep()" input-name="pep" disable-input="ctrladqui.prestamoId==null"></div>
								<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
							</div>
						</div>
						<div class="row">
				    		<div class="form-group col-sm-6" align="left">
								<div id= "lineaBase" angucomplete-alt placeholder="" pause="100" selected-object="ctrladqui.cambioLineaBase"
									  local-data="ctrladqui.lineasBase" search-fields="nombre" title-field="nombre" 
									  field-required="true" field-label="* Linea Base" minlength="1" input-class="form-control form-control-small field-angucomplete inputText" 
									  match-class="angucomplete-highlight" initial-value="ctrladqui.lineaBaseNombre" 
									  focus-out="ctrladqui.blurLineaBase()" input-name="lineaBase"></div>
								<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
							</div>
							<div class="form-group col-sm-6" align="right">
								<div class="operation_buttons">
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
				</div>
				<br>
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
		    		<div class="divTablas">	
		    			<div class="row" style="height: 100%; max-width: {{ctrladqui.tamanoPantalla}}; min-width: {{ctrladqui.tamanoPantalla}}">
		    				<div ng-hide="!ctrladqui.mostrarTablas" class="divPadreDatos" style="height: 100%;min-width: {{ctrladqui.tamanoPantalla}}px; max-width: {{ctrladqui.tamanoPantalla}}px;">
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
					    						<th class="label-form" rowspan="2" style="text-align: center; min-width: 140px; max-width: 140px">Tipo de Revisión</th>
					    						<th class="label-form" rowspan="2" style="text-align: center; min-width: 140px; max-width: 140px">NOG</th>
					    						<th class="label-form" rowspan="2" style="text-align: center; min-width: 140px; max-width: 140px">Número de Contrato</th>
					    						<th class="label-form" rowspan="2" style="text-align: center; min-width: 140px; max-width: 140px">Monto de Contrato</th>				    						
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
				    								<div style="text-align: left" uib-tooltip="{{row.nombre}}"><span style="float: left;" ng-class="ctrladqui.claseIcon(row);" uib-tooltip="{{ctrladqui.tooltipObjetoTipo[row.objetoTipo]}}"></span>{{row.nombre}}</div>
					    						</td>
			    								<td class="divisionColumna truncate" style="min-width: 200px; max-width: 200px">
			    									<div uib-tooltip="{{row.tipoAdquisicionNombre}}">{{row.tipoAdquisicionNombre}}</div>
			    								</td>		    								
								    			<td class="divisionColumna" style="min-width: 150px; max-width: 150px">
								    				{{row.unidadMedida}}
								    			</td>
								    			<td class="divisionColumna" style="min-width: 200px; max-width: 200px">
								    				{{row.categoriaAdquisicionNombre}}								    				
								    			</td>			
					    						<td class="divisionColumna" style="text-align: right; min-width: 75px; max-width: 75px">
					    							{{row.cantidad || 0}}
					    						</td>
					    						<td class="divisionColumna" style="text-align: right; min-width: 140px; max-width: 140px">
					    							{{row.costo || 0 | formatoMillones : ctrladqui.enMillones}}
					    						</td>
					    						<td class="divisionColumna" style="min-width: 140px; max-width: 140px; text-align: right">
					    							{{row.total | formatoMillones : ctrladqui.enMillones}}
					    						</td>
					    						<td class="divisionColumna" style="min-width: 140px; max-width: 140px; text-align: center">
				    								{{row.tipoRevisionNombre}}
					    						</td>
					    						<td class="divisionColumna" style="min-width: 140px; max-width: 140px; text-align: right">
				    								{{row.nog}}
					    						</td>
					    						<td class="divisionColumna" style="min-width: 140px; max-width: 140px; text-align: right">
				    								{{row.numeroContrato}}
					    						</td>
					    						<td class="divisionColumna" style="min-width: 140px; max-width: 140px; text-align: right">
				    								{{row.montoContrato}}
					    						</td>
					    						<td class="colorPlanificado divisionColumna" style="text-align: left; min-width: 90px; max-width: 90px;">
					    							{{row.planificadoDocs}}
					    						</td>
					    						<td class="colorReal divisionColumna" style="text-align: left; min-width: 90px; max-width: 90px;">
					    							{{row.realDocs}}
					    						</td>
					    						<td class="colorPlanificado divisionColumna" style="text-align: left;min-width: 90px; max-width: 90px;">
					    							{{row.planificadoLanzamiento}}
					    						</td>
					    						<td class="colorReal divisionColumna" style="text-align: left; min-width: 90px; max-width: 90px;">
					    							{{row.realLanzamiento}}
					    						</td>
					    						<td class="colorPlanificado divisionColumna" style="text-align: left; min-width: 90px; max-width: 90px;">
					    							{{row.planificadoRecepcionEval}}
					    						</td>
					    						<td class="colorReal divisionColumna" style="text-align: left; min-width: 90px; max-width: 90px;">
					    							{{row.realRecepcionEval}}
					    						</td>
					    						<td class="colorPlanificado divisionColumna" style="text-align: left; min-width: 90px; max-width: 90px;">
					    							{{row.planificadoAdjudica}}
					    						</td>
					    						<td class="colorReal divisionColumna" style="text-align: left; min-width: 90px; max-width: 90px;">
					    							{{row.realAdjudica}}
					    						</td>
					    						<td class="colorPlanificado divisionColumna" style="text-align: left; min-width: 90px; max-width: 90px;">
					    							{{row.planificadoFirma}}
					    						</td>
					    						<td class="colorReal divisionColumna" style="text-align: left; min-width: 90px; max-width: 90px;">
					    							{{row.realFirma}}
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
	</div>