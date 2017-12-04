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
	
	<div ng-controller="planEstructuralProyectoController as pep" class="maincontainer all_page" id="title" style="height: 100%">
		<shiro:lacksPermission name="24010">
			<p ng-init="pep.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<div class="row" id="reporte" style="height: 100%">
		<div class="col-sm-12">
			<div class="row" style=" height: 20%">
	    		<div style="width: 100%;">
		    		<div class="row">
		    			<div class="panel panel-default">
			  				<div class="panel-heading"><h3>Plan estructural del préstamo</h3></div>
						</div>
		    		</div>
		    		<br>
		    		<div class="row">
		    		<div class="form-group col-sm-6" align="left">
						<div id="prestamo" angucomplete-alt placeholder="" pause="100" selected-object="pep.cambioPrestamo"
							  local-data="pep.lprestamos" search-fields="proyectoPrograma" title-field="proyectoPrograma" field-required="true" field-label="* Préstamo"
							  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
							  initial-value="pep.prestamoNombre" focus-out="pep.blurPrestamo()" input-name="prestamo"></div>
						<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
						</div>
			    	</div>
			    	<div class="row">
			    		<div class="form-group col-sm-6" align="left">
							<div id="pep" angucomplete-alt placeholder="" pause="100" selected-object="pep.cambioPep"
								  local-data="pep.peps" search-fields="nombre" title-field="nombre" field-required="true" field-label="* {{etiquetas.proyecto}}"
								  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
								  initial-value="pep.pepNombre" focus-out="pep.blurPep()" input-name="pep" disable-input="pep.prestamoId==null"></div>
							<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
						</div>
					</div>
					<div class="row">
						<div class="form-group col-sm-6" align="left">
							<div id= "lineaBase" angucomplete-alt placeholder="" pause="100" selected-object="pep.cambioLineaBase"
								  local-data="pep.lineasBase" search-fields="nombre" title-field="nombre" 
								  field-required="true" field-label="* Linea Base" minlength="1" input-class="form-control form-control-small field-angucomplete inputText" 
								  match-class="angucomplete-highlight" initial-value="pep.lineaBaseNombre" 
								  focus-out="pep.blurLineaBase()" input-name="lineaBase"></div>
							<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
						</div>
						<div class="form-group col-sm-6" align="right">
							<div class="operation_buttons">
								<div class="btn-group">
									<label class="btn btn-default"  ng-click="pep.exportarExcel();" uib-tooltip="Exportar" ng-hide="!pep.mostrarBotones">
									<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
									<label class="btn btn-default" ng-click="pep.exportarPdf()" uib-tooltip="Exportar PDF" ng-hide="true">
									<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
								</div>
							</div>
						</div>
			    	</div>
				</div>
			</div>
			<br>
			<div class="col-sm-12" style="height: 80%">
				<div ng-hide="!pep.mostrarCargando" style="width: 100%; height: 100%">
    				<div class="grid_loading" ng-hide="!pep.mostrarCargando">
						<div class="msg">
							<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
								<br />
								<br /> <b>Cargando, por favor espere...</b> 
							</span>
						</div>
					</div>
				</div>
	    		<div class="divTablas" ng-hide="!pep.mostrarTablas">	
	    			<div class="row" style="height: 100%; max-width: {{pep.tamanoPantalla}}; min-width: {{pep.tamanoPantalla}}">
	    				<div class="divPadreDatos" style="height: 100%;min-width: {{pep.tamanoPantalla}}px; max-width: {{pep.tamanoPantalla}}px;">
	    					<div class="divTabla">
		    					<table st-table="pep.displayedCollectionPrestamo" st-safe-src="pep.rowCollectionPrestamo" class="table table-striped tablaDatos"
		    						style="max-width: {{pep.tamanoPantalla}}px;">
		    						<thead id="divCabecerasDatos" class="theadDatos">
		    							<tr>
		    								<th class="label-form" style="text-align: left; min-width:300px; max-width:300px;">Nombre</th>
				    						<th class="label-form" style="text-align: center; min-width:90px; max-width:90px;">Duración</th>
				    						<th class="label-form" style="text-align: center; min-width: 90px; max-width: 90px;">Fecha Inicial</th>
				    						<th class="label-form" style="text-align: center; min-width: 90px; max-width: 90px;">Fecha Final</th>
				    						<th class="label-form" style="text-align: center; min-width: 140px; max-width: 140px;">Fecha Inicial Real</th>
				    						<th class="label-form" style="text-align: center; min-width: 140px; max-width: 140px;">Fecha Final Real</th>
				    						<th class="label-form" style="text-align: center; min-width: 90px; max-width: 90px;">% de Avance</th>
				    						<th class="label-form" style="text-align: center; min-width: 140px; max-width: 140px;">Acumulación Costo</th>
				    						<th class="label-form" style="text-align: center; min-width: 140px; max-width: 140px">Presupuesto Apr.</th>
				    						<th class="label-form" style="text-align: center; min-width: 140px; max-width: 140px">Costo Planificado</th>
				    						<th class="label-form" style="text-align: center; min-width: 140px; max-width: 140px">Asignación Prep. V.</th>
				    						<th class="label-form" style="text-align: center; min-width: 140px; max-width: 140px">Presupuesto Dev.</th>
				    						<th class="label-form" style="text-align: center; min-width: 140px; max-width: 140px">% Avance Financiero</th>
		    							</tr>
		    						</thead>
		    						<tbody vs-repeat class="cuerpoTablaDatos" id="divTablaDatos" onmouseover="activarScroll(this.id)" onscroll="scrollEspejo(this)">
		    							<tr ng-repeat="row in pep.rowCollectionPrestamo">
		    								<td class="divisionColumna truncate" style="min-width:300px;max-width:300px;">
			    								<div style="text-align: left" uib-tooltip="{{row.nombre}}"><span style="float: left;margin-left: {{row.nivel-1}}em;" ng-class="pep.claseIcon(row);" uib-tooltip="{{pep.tooltipObjetoTipo[row.objetoTipo]}}"></span>{{row.nombre}}</div>
				    						</td>
		    								<td class="divisionColumna" style="text-align: right; min-width:90px; max-width:90px;">
		    									{{row.duracion || 0}}
		    								</td>		    								
							    			<td class="divisionColumna" style="min-width: 90px; max-width: 90px;">
							    				{{row.fechaInicial}}
							    			</td>
							    			<td class="divisionColumna" style="min-width: 90px; max-width: 90px;">
							    				{{row.fechaFinal}}								    				
							    			</td>
							    			<td class="divisionColumna" style="min-width: 140px; max-width: 140px;">
							    				{{row.fechaInicialReal}}
							    			</td>
							    			<td class="divisionColumna" style="min-width: 140px; max-width: 140px;">
							    				{{row.fechaFinReal}}								    				
							    			</td>
							    			<td class="divisionColumna" style="min-width: 90px; max-width: 90px;">
							    				{{row.avance}}								    				
							    			</td>
							    			<td class="divisionColumna" style="min-width: 140px; max-width: 140px;">
							    				{{row.acumulacionCosto}}								    				
							    			</td>			
				    						<td class="divisionColumna" style="text-align: right; min-width: 140px; max-width: 140px">
				    							{{row.presupuestoAprobado || 0 | formatoMillones : pep.enMillones}}
				    						</td>
				    						<td class="divisionColumna" style="text-align: right; min-width: 140px; max-width: 140px">
				    							{{row.costoPlanificado || 0 | formatoMillones : pep.enMillones}}
				    						</td>
				    						<td class="divisionColumna" style="text-align: right; min-width: 140px; max-width: 140px">
				    							{{row.asignacionPresupuestariaVigente || 0 | formatoMillones : pep.enMillones}}
				    						</td>
				    						<td class="divisionColumna" style="text-align: right; min-width: 140px; max-width: 140px">
			    								{{row.presupuestoDevengado || 0 | formatoMillones : pep.enMillones}}
				    						</td>
				    						<td class="divisionColumna" style="text-align: right; min-width: 140px; max-width: 140px">
				    							{{row.avanceFinanciero || 0 }}
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
	</div>