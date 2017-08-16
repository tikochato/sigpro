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
		
		.colorCeldaBloqueado{
			background-color: #e6e6e6;
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
		}
		.cuerpoTablaNombres {
		    overflow-y: scroll;
		    overflow-x: scroll;
		    display: inline-block;
		    font-size: 13px;
		    max-width: 300px; 
		}
	</style>
	
	<div ng-controller="planAdquisicionesController as controller" class="maincontainer all_page" id="title" style="height: 100%">
		<script type="text/ng-template" id="pago.jsp">
    		<%@ include file="/app/components/reportes/planadquisiciones/pago.jsp"%>
  		</script>
		<shiro:lacksPermission name="24010">
			<p ng-init="controller.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<div class="row" id="reporte" style="height: 100%">
			<div class="col-sm-12" style=" height: 20%">
	    		<div style="width: 100%;">
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
							<label for="tObjeto" class="floating-label">Préstamos</label>
	    				</div>
						<div class="operation_buttons" align="right">
							<div class="btn-group">
								<label class="btn btn-default" ng-model="controller.enMillones" uib-btn-radio="true" ng-click="controller.calcularTamaniosCeldas()" uib-tooltip="Millones de Quetzales" role="button" tabindex="0" aria-invalid="false" ng-hide="!controller.mostrarBotones">
									<span>MQ</span>
								</label>
							</div>
							<div class="btn-group">
								<label class="btn btn-default" ng-model="controller.enMillones" uib-btn-radio="false" ng-click="controller.calcularTamaniosCeldas()" uib-tooltip="Quetzales" role="button" tabindex="1" aria-invalid="false" ng-hide="!controller.mostrarBotones">
									<span>Q</span>
								</label>
							</div>
							<div class="btn-group">
								<label class="btn btn-default"  ng-click="controller.agregarPagos();" uib-tooltip="Plan de pagos" ng-hide="!controller.mostrarBotones">
								<span class="glyphicon glyphicon glyphicon-usd" aria-hidden="true"></span></label>
							</div>
							<div class="btn-group">
								<label class="btn btn-default"  ng-click="controller.limpiar(row);" uib-tooltip="Limpiar" ng-hide="!controller.mostrarBotones">
								<span class="glyphicon glyphicon glyphicon-erase" aria-hidden="true"></span></label>
							</div>
							<div class="btn-group">
								<label class="btn btn-default"  ng-click="controller.guardarPlan();" uib-tooltip="Guardar" ng-hide="!controller.mostrarBotones">
								<span class="glyphicon glyphicon glyphicon-floppy-disk" aria-hidden="true"></span></label>
							</div>
							<div class="btn-group">
								<label class="btn btn-default"  ng-click="controller.exportarExcel();" uib-tooltip="Exportar" ng-hide="!controller.mostrarBotones">
								<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
							</div>
						</div>
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
				<div ng-hide="!controller.mostrarGuardando" style="width: 100%; height: 100%">
										<div class="grid_loading" ng-hide="!controller.mostrarGuardando">
						<div class="msg">
							<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
								<br />
								<br /> <b>Guardando, por favor espere...</b> 
							</span>
						</div>
					</div>
				</div>
	    		<div class="divTablas" ng-hide="!controller.mostrarTablas">	
	    			<div class="row" style="height: 100%; max-width: {{controller.tamanoPantalla}}; min-width: {{controller.tamanoPantalla}}">
	    				<div class="divPadreNombres" style="height: 100%">
	    					<div class="divTabla"> 
	    						<table st-table="controller.displayedCollectionPrestamo" st-safe-src="controller.rowCollectionPrestamo" class="table table-striped tablaDatos">
		    						<thead class="theadDatos">
		    							<tr>
				    						<th style="min-width:300px;text-align: left; height:71px;;vertical-align: middle;" class="label-form" rowspan="2">Nombre</th>
				    					</tr>
		    						</thead>
		    						<tbody class="cuerpoTablaNombres" id="divTablaNombres" ng-mouseover="controller.activarScroll('divTablaNombres')" scrollespejo>
		    							<tr ng-repeat="row in controller.rowCollectionPrestamo">
		    								<td ng-class="row.bloqueado == true ? 'colorCeldaBloqueado' : 'colorCeldaDesbloqueado'">
		    									<div style="height: 25px;">
		    										<input type="radio" class="radio-inline" name="$index" ng-checked="row.bloqueado == true ? false : ''" ng-disabled="row.bloqueado" ng-click="(row.bloqueado == false ? controller.selectedRow(row) : 'controller.datoSeleccionado = null');"></input>
		    									</div> 
		    								</td>
				    						<td nowrap ng-class="row.bloqueado == true ? 'colorCeldaBloqueado' : 'colorCeldaDesbloqueado'">
				    							<div style="height: 25px;">
				    								<div uib-tooltip="{{row.nombre}}"><span ng-class="controller.claseIcon(row);" style="margin-left: {{row.objetoTipo-1}}em" uib-tooltip="{{controller.tooltipObjetoTipo[row.objetoTipo-1]}}"></span>{{row.nombre}}</div>
				    							</div>
				    						</td>
		    							</tr>
		    						</tbody>
		    					</table>
	    					</div>
	    				</div>
	    				<div class="divPadreDatos" style="height: 100%;min-width: {{controller.tamanoTotal}}px; max-width: {{controller.tamanoTotal}}px;">
	    					<div class="divTabla">
		    					<table st-table="controller.displayedCollectionPrestamo" st-safe-src="controller.rowCollectionPrestamo" class="table table-striped tablaDatos"
		    						style="max-width: {{controller.tamanoTotal}}px;">
		    						<thead id="divCabecerasDatos" class="theadDatos">
		    							<tr>
		    								<th class="label-form" rowspan="2" style="text-align: center; min-width: 130px; max-width: 130px">Tipo de Adq.</th>
				    						<th class="label-form" rowspan="2" style="text-align: center; min-width: 150px; max-width: 150px">Ud. Medida</th>
				    						<th class="label-form" rowspan="2" style="text-align: center; min-width: 200px; max-width: 200px">Cat. de Adq.</th>
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
		    						<tbody class="cuerpoTablaDatos" id="divTablaDatos" ng-mouseover="controller.activarScroll('divTablaDatos')" scrollespejo>
		    							<tr ng-repeat="row in controller.rowCollectionPrestamo">
		    								<td class="divisionColumna" ng-class="row.bloqueado == true ? 'colorCeldaBloqueado' : 'colorCeldaDesbloqueado'" ng-blur="row.bloqueado == false ? controller.ocultar(row,0) : ''" style="min-width: 130px; max-width: 130px"  ng-focus="row.bloqueado == false ? controller.ocultar(row, 0) : ''" ng-click="row.bloqueado == false ? '' : ''" next-on-tab>
		    									<div style="height: 25px;">
			    									<label ng-show="!row.c0">{{controller.ddlOpcionesTiposAdquisicion[row.tipoAdquisicion].value}}</label>
								    				<select ng-show="row.c0" ng-blur="controller.ocultar(row,0);" ng-model="row.tipoAdquisicion" ng-change="controller.bloquearPadre(row, true); controller.bloquearHijos(row.hijos, true);" class="form-control" next-on-tab>
								    					<option ng-selected="row.tipoAdquisicion != null ? y.id=={{row.tipoAdquisicion}} : 0" ng-repeat="y in controller.ddlOpcionesTiposAdquisicion" value={{y.id}}>{{y.value}}</option>
								    				</select>
							    				</div>
		    								</td>		    								
							    			<td class="divisionColumna" ng-class="row.bloqueado == true ? 'colorCeldaBloqueado' : 'colorCeldaDesbloqueado'" style="min-width: 150px; max-width: 150px" ng-focus="row.bloqueado == false ? controller.ocultar(row, 1) : ''" ng-click="row.bloqueado == false ? controller.ocultar(row, 1) : ''">
								    			<div style="height: 25px;">
								    				<label ng-show="!row.c1">{{row.unidadMedida}}</label>
								    				<input ng-show="row.c1" ng-blur="controller.ocultar(row,1);" ng-model="row.unidadMedida" ng-change="controller.bloquearPadre(row, true); controller.bloquearHijos(row.hijos, true);" type="text" style="max-width: 130px; text-align: left" focus-on-show></input>
								    			</div>
							    			</td>
							    			<td class="divisionColumna" ng-class="row.bloqueado == true ? 'colorCeldaBloqueado' : 'colorCeldaDesbloqueado'" ng-blur="row.bloqueado == false ? controller.ocultar(row,2) : ''" style="min-width: 200px; max-width: 200px" ng-focus="row.bloqueado == false ? controller.ocultar(row, 2) : ''" ng-click="row.bloqueado == false ? '' : ''" next-on-tab>
								    			<div style="height: 25px;">
								    				<label ng-show="!row.c2">{{controller.ddlcategoriaAdquisiciones[row.categoriaAdquisicion].value}}</label>
								    				<select ng-show="row.c2" ng-blur="row.bloqueado == false ? controller.ocultar(row,2) : ''" ng-model=row.categoriaAdquisicion class="form-control" ng-change="controller.bloquearPadre(row, true); controller.bloquearHijos(row.hijos, true);" next-on-tab>
								    					<option ng-selected="row.categoriaAdquisicion != null ? y.id=={{row.categoriaAdquisicion}} : 0" ng-repeat="y in controller.ddlcategoriaAdquisiciones" value={{y.id}}>{{y.value}}</option>
								    				</select>
								    			</div>
							    			</td>			
				    						<td class="divisionColumna" ng-class="row.bloqueado == true ? 'colorCeldaBloqueado' : 'colorCeldaDesbloqueado'" style="text-align: right; min-width: 75px; max-width: 75px" ng-click="row.bloqueado == false ? controller.ocultar(row, 3) : ''" ng-focus="row.bloqueado == false ? controller.ocultar(row, 3) : ''">
					    						<div style="height: 25px;">
					    							<label ng-show="!row.c3">{{row.cantidad || 0}}</label>
					    							<input ng-show="row.c3" ng-blur="controller.ocultar(row,3);" ng-model="row.cantidad" type="number" ng-change="controller.calcularTotal(row); controller.bloquearPadre(row, true); controller.bloquearHijos(row.hijos, true);" style="max-width: 50px; text-align: right" focus-on-show></input>
					    						</div>
				    						</td>
				    						<td class="divisionColumna" ng-class="row.bloqueado == true ? 'colorCeldaBloqueado' : 'colorCeldaDesbloqueado'" style="text-align: right; min-width: 140px; max-width: 140px" ng-click="row.bloqueado == false ? controller.ocultar(row, 4) : ''" ng-focus="row.bloqueado == false ? controller.ocultar(row, 4) : ''">
					    						<div style="height: 25px;">
					    							<label ng-show="!row.c4">{{row.costo || 0 | formatoMillones : controller.enMillones}}</label>
					    							<input ng-show="row.c4" ng-blur="controller.ocultar(row,4);" ng-model="row.costo" ng-change="controller.calcularTotal(row); controller.bloquearPadre(row, true); controller.bloquearHijos(row.hijos, true);" type="number" style="max-width: 135px; text-align: right" focus-on-show></input>
					    						</div>
				    						</td>
				    						<td class="divisionColumna" ng-class="row.bloqueado == true ? 'colorCeldaBloqueado' : 'colorCeldaDesbloqueado'" style="min-width: 140px; max-width: 140px; text-align: right">
					    						<div style="height: 25px;">
					    							{{row.total | formatoMillones : controller.enMillones}}
					    						</div>
				    						</td>
				    						<td class="colorPlanificado divisionColumna" ng-class="row.bloqueado == true ? 'colorCeldaBloqueado' : 'colorCeldaDesbloqueado'" style="text-align: left; min-width: 90px; max-width: 90px;" ng-click="row.bloqueado == false ? controller.ocultar(row, 6) : ''" ng-focus="row.bloqueado == false ? controller.ocultar(row, 6) : ''">
					    						<div style="height: 25px;">
					    							<label ng-show="!row.c6">{{row.planificadoDocs}}</label>
					    							<input ng-show="row.c6" ng-change="(row.planificadoDocs.length == 10 ? controller.validarFecha(row,1,1) : ''); controller.bloquearPadre(row, true); controller.bloquearHijos(row.hijos, true);" ng-blur="row.planificadoDocs.length == 10 ? controller.ocultar(row,6) : controller.ocultar(row,6, true);" ng-model="row.planificadoDocs" type="text" style="min-width: 75px; max-width: 75px;" ui-date-mask="DD/MM/YYYY" focus-on-show></input>
					    						</div>
				    						</td>
				    						<td class="colorReal divisionColumna" ng-class="row.bloqueado == true ? 'colorCeldaBloqueado' : 'colorCeldaDesbloqueado'" style="text-align: left; min-width: 90px; max-width: 90px;" ng-click="row.bloqueado == false ? controller.ocultar(row, 7) : ''" ng-focus="row.bloqueado == false ? controller.ocultar(row, 7) : ''">
					    						<div style="height: 25px;">
					    							<label ng-show="!row.c7">{{row.realDocs}}</label>
					    							<input ng-show="row.c7" ng-change="(row.realDocs.length == 10 ? controller.validarFecha(row,1,2) : ''); controller.bloquearPadre(row, true); controller.bloquearHijos(row.hijos, true);" ng-blur="row.realDocs.length == 10 ? controller.ocultar(row,7) : controller.ocultar(row,7, true); " ng-model="row.realDocs" type="text" style="min-width: 75px; max-width: 75px;" focus-on-show></input>
					    						</div>
				    						</td>
				    						<td class="colorPlanificado divisionColumna" ng-class="row.bloqueado == true ? 'colorCeldaBloqueado' : 'colorCeldaDesbloqueado'" style="text-align: left;min-width: 90px; max-width: 90px;" ng-click="row.bloqueado == false ? controller.ocultar(row, 8) : ''" ng-focus="row.bloqueado == false ? controller.ocultar(row, 8) : ''">
					    						<div style="height: 25px;">
					    							<label ng-show="!row.c8">{{row.planificadoLanzamiento}}</label>
					    							<input ng-show="row.c8" ng-change="(row.planificadoLanzamiento.length == 10 ? controller.validarFecha(row,2,1) : ''); controller.bloquearPadre(row, true); controller.bloquearHijos(row.hijos, true);" ng-blur="row.planificadoLanzamiento.length == 10 ? controller.ocultar(row,8) : controller.ocultar(row,8, true);" ng-model="row.planificadoLanzamiento" type="text" style="min-width: 75px; max-width: 75px;" focus-on-show></input>
					    						</div>
				    						</td>
				    						<td class="colorReal divisionColumna" ng-class="row.bloqueado == true ? 'colorCeldaBloqueado' : 'colorCeldaDesbloqueado'" style="text-align: left; min-width: 90px; max-width: 90px;" ng-click="row.bloqueado == false ? controller.ocultar(row, 9) : ''" ng-focus="row.bloqueado == false ? controller.ocultar(row, 9) : ''">
					    						<div style="height: 25px;">
					    							<label ng-show="!row.c9">{{row.realLanzamiento}}</label>
					    							<input ng-show="row.c9" ng-change="(row.realLanzamiento.length == 10 ? controller.validarFecha(row,2,2) : ''); controller.bloquearPadre(row, true); controller.bloquearHijos(row.hijos, true);" ng-blur="row.realLanzamiento.length == 10 ? controller.ocultar(row,9) : controller.ocultar(row,9,true);" ng-model="row.realLanzamiento" type="text" style="min-width: 75px; max-width: 75px;" focus-on-show></input>
					    						</div>
				    						</td>
				    						<td class="colorPlanificado divisionColumna" ng-class="row.bloqueado == true ? 'colorCeldaBloqueado' : 'colorCeldaDesbloqueado'" style="text-align: left; min-width: 90px; max-width: 90px;" ng-click="row.bloqueado == false ? controller.ocultar(row, 10) : ''" ng-focus="row.bloqueado == false ? controller.ocultar(row, 10) : ''">
					    						<div style="height: 25px;">
					    							<label ng-show="!row.c10">{{row.planificadoRecepcionEval}}</label>
					    							<input ng-show="row.c10" ng-change="(row.planificadoRecepcionEval.length == 10 ? controller.validarFecha(row,3,1) : ''); controller.bloquearPadre(row, true); controller.bloquearHijos(row.hijos, true);" ng-blur="row.planificadoRecepcionEval.length == 10 ? controller.ocultar(row,10) : controller.ocultar(row,10, true); " ng-model="row.planificadoRecepcionEval" type="text" style="min-width: 75px; max-width: 75px;" focus-on-show></input>
					    						</div>
				    						</td>
				    						<td class="colorReal divisionColumna" ng-class="row.bloqueado == true ? 'colorCeldaBloqueado' : 'colorCeldaDesbloqueado'" style="text-align: left; min-width: 90px; max-width: 90px;" ng-click="row.bloqueado == false ? controller.ocultar(row, 11) : ''" ng-focus="row.bloqueado == false ? controller.ocultar(row, 11) : ''">
					    						<div style="height: 25px;">
					    							<label ng-show="!row.c11">{{row.realRecepcionEval}}</label>
					    							<input ng-show="row.c11" ng-change="(row.realRecepcionEval.length == 10 ? controller.validarFecha(row,3,2) : ''); controller.bloquearPadre(row, true); controller.bloquearHijos(row.hijos, true);" ng-blur="row.realRecepcionEval.length == 10 ? controller.ocultar(row,11) : controller.ocultar(row,11, true); " ng-model="row.realRecepcionEval" type="text" style="min-width: 75px; max-width: 75px;" focus-on-show></input>
					    						</div>
				    						</td>
				    						<td class="colorPlanificado divisionColumna" ng-class="row.bloqueado == true ? 'colorCeldaBloqueado' : 'colorCeldaDesbloqueado'" style="text-align: left; min-width: 90px; max-width: 90px;" ng-click="row.bloqueado == false ? controller.ocultar(row, 12) : ''" ng-focus="row.bloqueado == false ? controller.ocultar(row, 12) : ''">
					    						<div style="height: 25px;">
					    							<label ng-show="!row.c12">{{row.planificadoAdjudica}}</label>
													<input ng-show="row.c12" ng-change="(row.planificadoAdjudica.length == 10 ? controller.validarFecha(row,4,1) : ''); controller.bloquearPadre(row, true); controller.bloquearHijos(row.hijos, true);" ng-blur="row.planificadoAdjudica.length == 10 ? controller.ocultar(row,12) : controller.ocultar(row,12, true); " ng-model="row.planificadoAdjudica" type="text" style="min-width: 75px; max-width: 75px;" focus-on-show></input>
												</div>					    							
				    						</td>
				    						<td class="colorReal divisionColumna" ng-class="row.bloqueado == true ? 'colorCeldaBloqueado' : 'colorCeldaDesbloqueado'" style="text-align: left; min-width: 90px; max-width: 90px;" ng-click="row.bloqueado == false ? controller.ocultar(row, 13) : ''" ng-focus="row.bloqueado == false ? controller.ocultar(row, 13) : ''">
					    						<div style="height: 25px;">
					    							<label ng-show="!row.c13">{{row.realAdjudica}}</label>
					    							<input ng-show="row.c13" ng-change="(row.realAdjudica.length == 10 ? controller.validarFecha(row,4,2) : ''); controller.bloquearPadre(row, true); controller.bloquearHijos(row.hijos, true);" ng-blur="row.realAdjudica.length == 10 ? controller.ocultar(row,13) : controller.ocultar(row,13, true); " ng-model="row.realAdjudica" type="text" style="min-width: 75px; max-width: 75px;" focus-on-show></input>
					    						</div>
				    						</td>
				    						<td class="colorPlanificado divisionColumna" ng-class="row.bloqueado == true ? 'colorCeldaBloqueado' : 'colorCeldaDesbloqueado'" style="text-align: left; min-width: 90px; max-width: 90px;" ng-click="row.bloqueado == false ? controller.ocultar(row, 14) : ''" ng-focus="row.bloqueado == false ? controller.ocultar(row, 14) : ''">
					    						<div style="height: 25px;">
					    							<label ng-show="!row.c14">{{row.planificadoFirma}}</label>
					    							<input ng-show="row.c14" ng-change="(row.planificadoFirma.length == 10 ? controller.validarFecha(row,5,1) : ''); controller.bloquearPadre(row, true); controller.bloquearHijos(row.hijos, true);" ng-blur="row.planificadoFirma.length == 10 ? controller.ocultar(row,14) : controller.ocultar(row,14, true); " ng-model="row.planificadoFirma" type="text" style="min-width: 75px; max-width: 75px;" focus-on-show></input>
					    						</div>
				    						</td>
				    						<td class="colorReal divisionColumna" ng-class="row.bloqueado == true ? 'colorCeldaBloqueado' : 'colorCeldaDesbloqueado'" style="text-align: left; min-width: 90px; max-width: 90px;" ng-click="row.bloqueado == false ? controller.ocultar(row, 15) : ''" ng-focus="row.bloqueado == false ? controller.ocultar(row, 15) : ''">
					    						<div style="height: 25px;">
					    							<label ng-show="!row.c15">{{row.realFirma}}</label>
					    							<input ng-show="row.c15" ng-change="(row.realFirma.length == 10 ? controller.validarFecha(row,5,2) : ''); controller.bloquearPadre(row, true); controller.bloquearHijos(row.hijos, true);" ng-blur="row.realFirma.length == 10 ? controller.ocultar(row,15) : controller.ocultar(row,15,true); " ng-model="row.realFirma" type="text" style="min-width: 75px; max-width: 75px;" focus-on-show></input>
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
		    		<ol class="leyendaTexto"  ng-hide="!controller.mostrarTablas">
						<li><span class="colorPlanificadoFondo"></span>Planificado</li>
				        <li><span class="colorRealFondo"></span>Real</li>
					</ol>
				</div>
	    	</div>
		</div>
	</div>