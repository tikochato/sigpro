<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	
	<style>
		.actividad { font-weight: normal !important; }
		.padre {font-weight: bold;}
	    .real { background-color: #f7e681 !important }
	    .realTotal { background-color: #f7e681 !important }
		
		.divPadreNombres{
			float: left; 
		  	width: 300px; 
			min-width: 300px; 
			max-width:300px; 
			overflow:hidden; 
		}	
		
		.divPadreDatos{		
			float: left; 
			display: inline-block;
			white-space: nowrap;	
			overflow:hidden;		
		}
			
		.divTabla{
			float: left;
			margin-right: -15px;
			overflow-y:hidden;
			overflow-x:hidden;
		}
				
	    .cuerpoTablaNombres {
		    overflow-y: scroll;
		    overflow-x: hidden;
		    display: inline-block;
		    font-size: 13px;
		    max-width: 315px;
		}
		
		.cuerpoTablaTotales {
		    overflow-y: scroll;
		    overflow-x: hidden;
		    display: inline-block;
		    font-size: 13px;
		}
				
		.bordeIzquierda{
		    border-left: 3px double #ccc!important;
		}
		
		.cuerpoTablaDatos {
		    overflow-y: scroll;
		    overflow-x: hidden;
		    display: inline-block;
		    text-align: center;
		    font-size: 13px;
		    margin-left: -15px;
		}
		
		.tablaDatos {
			display: flex;
		    flex-direction: column;
		    align-items: stretch;
		    margin-bottom: 0px;
		}
		
		.colorPlanificado{
			color: #303f9e;
		}
		
		.colorReal{
			color: #257129;
		}
		
		.colorPlanificadoFondo{
			background-color: #303f9e;
		}
		
		.colorRealFondo{
			background-color: #257129;
		}
		
		.theadDatos {
			flex-shrink: 0; overflow-x: hidden;
		}
		
		.table-striped>tbody>tr:nth-child(odd)>td, .table-striped>tbody>tr:nth-child(odd)>th {
    		background-color: #f3f3f3;
		}
		
		.label-form {
		    font-size: 13px;
		    opacity: 1;
		    color: rgba(0,0,0,0.38) !important;
		    font-weight: bold;
		}
		
		.nombreFormat {    
		    margin: 0 0 2px;
		    color: #333;
		    word-break: none;
		    word-wrap: break-word;
		    background-color: transparent;
		    border: none;
		    font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
		    padding: 0px;
		    line-height: normal;	
		    overflow: initial;	     
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
		
		.grid_loading {
		    top: initial;
		    bottom: initial;
		    height: 100%;
		}
		
		.btn-default.active{
			font-weight: bold;
		}
		
	</style>

<div ng-controller="flujocajaController as controller" class="maincontainer all_page" id="title">
	
	<shiro:lacksPermission name="30010">
		<p ng-init="controller.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>		
	<div class="col-sm-12" style="height: 100%;">
		<div style="width: 100%; height: 100%">
	    		<div class="row">
					<div class="panel panel-default">
						<div class="panel-heading"><h3>Flujo de Caja</h3></div>
					</div>
				</div>
	    	<br>
			<div class="row">
					<div class="form-group col-sm-3">
						<select  class="inputText" ng-model="controller.prestamo"
							ng-options="a.text for a in controller.prestamos"
							ng-change="controller.validar(1)"></select>
					</div>
					
					<div class="form-group col-sm-1">
						<input type="number"  class="inputText" ng-model="controller.fechaInicio" maxlength="4" 
						ng-value="controller.fechaInicio" onblur="this.setAttribute('value', this.value);"
						ng-change="controller.validar(2)">
					  	<label for="campo.id" class="floating-label" >*Año Inicial</label>
					</div>
					
					<div class="form-group col-sm-1">
						<input type="number"  class="inputText" ng-model="controller.fechaFin" maxlength="4" 
						ng-value="controller.fechaFin" onblur="this.setAttribute('value', this.value);"
						ng-change="controller.validar(3)" ng-hide="true"/>
					  	<label for="campo.id" class="floating-label" ng-hide="true">*Año Final</label>
					</div>
					
					<div class="col-sm-7" align="right" ng-hide="!controller.mostrarDescargar">
						<div class="form-group col-sm-1">
						</div>
						<div class="col-sm-11">
							<div class="btn-group">
								<label class="btn btn-default" ng-model="controller.enMillones" uib-btn-radio="true" ng-click="controller.calcularTamaniosCeldas(); controller.convertirMillones();" uib-tooltip="Millones de Quetzales" role="button" tabindex="0" aria-invalid="false">
								<span>MQ</span></label>
								<label class="btn btn-default" ng-model="controller.enMillones" uib-btn-radio="false" ng-click="controller.calcularTamaniosCeldas(); controller.convertirMillones();" uib-tooltip="Quetzales" role="button" tabindex="1" aria-invalid="false">
								<span>Q</span></label>
							</div>
							<div class="btn-group" style="padding-left: 20px;">
								<label class="btn btn-default" ng-model="controller.agrupacionActual" uib-btn-radio="1" ng-click="controller.cambiarAgrupacion(1)" uib-tooltip="Mes" role="button" tabindex="1" aria-invalid="false">
								<span>M</span></label>
								<label class="btn btn-default" ng-model="controller.agrupacionActual" uib-btn-radio="2" ng-click="controller.cambiarAgrupacion(2)" uib-tooltip="Bimestre" role="button" tabindex="2" aria-invalid="false">
								<span>B</span></label>
								<label class="btn btn-default" ng-model="controller.agrupacionActual" uib-btn-radio="3" ng-click="controller.cambiarAgrupacion(3)" uib-tooltip="Trimestre" role="button" tabindex="3" aria-invalid="false">
								<span>T</span></label>
								<label class="btn btn-default" ng-model="controller.agrupacionActual" uib-btn-radio="4" ng-click="controller.cambiarAgrupacion(4)" uib-tooltip="Cuatrimestre" role="button" tabindex="4" aria-invalid="false">
								<span>C</span></label>
								<label class="btn btn-default" ng-model="controller.agrupacionActual" uib-btn-radio="5" ng-click="controller.cambiarAgrupacion(5)" uib-tooltip="Semestre" role="button" tabindex="5" aria-invalid="false">
								<span>S</span></label>
								<label class="btn btn-default" ng-model="controller.agrupacionActual" uib-btn-radio="6" ng-click="controller.cambiarAgrupacion(6)" uib-tooltip="Año" role="button" tabindex="6" aria-invalid="false">
								<span>A</span></label>
							</div>
							<div class="btn-group" style="padding-left: 20px;">
								<label class="btn btn-default" ng-click="controller.exportarExcel()" uib-tooltip="Exportar a Excel" ng-hide="true">
								<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
								<label class="btn btn-default" ng-click="controller.exportarPdf()" uib-tooltip="Exportar a PDF" ng-hide="true">
								<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
							</div>
						</div>
					</div>
    			<br><br><br>
	    	</div>
	    	<div style="width: 100%; height: 100%" id="reporte">
	    		
				<div class="grid_loading" ng-hide="!controller.mostrarCargando" style="height: calc(80% - 32px)">
						<div class="msg">
							<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
								<br />
								<br /> <b>Cargando, por favor espere...</b> 
							</span>
						</div>
					</div>		
		    	<div class="row" ng-hide="!controller.mostrarDescargar" >
		    				
		    		<div class="divPadreNombres"  style="height: 100%">
			    		<div class="divTabla"  style="height: 100%"> 
			    			<table class="table table-striped tablaDatos"  style="height: 100%">
								<thead class="theadDatos">
									<tr>
				          				<th style="text-align: center; vertical-align: top; width:300px; height: 71px;" class="label-form">Nombre</th>
				         			</tr>
								</thead>
								<tbody class="cuerpoTablaNombres" id="divTablaNombres" ng-mouseover="controller.activarScroll('divTablaNombres')" style="max-height: 150px;" scrollespejo>
									<tr ng-repeat="item in controller.data">
							      		<td nowrap style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">
							      			<p class="nombreFormat">
							      				<span ng-class="controller.iconoObjetoTipo[item.objeto_tipo]" uib-tooltip="{{controller.tooltipObjetoTipo[item.objeto_tipo]}}" style="margin-left: {{item.nivel}}em"></span>
							      				<span uib-tooltip="{{item.nombre}}">{{item.nombre}}</span>
							      			</p>
							      		</td>
							      	</tr>
								</tbody>
							</table>
		    			</div>
	    			</div>
		    		<div class="divPadreDatos" style="max-width: {{controller.tamanoTotal}}px; height: 100%">
	    				<div class="divTabla"  style="height: 100%">
			    			<table st-table="rowCollection" st-safe-src="datosTabla" class="table table-striped tablaDatos" 
				    				style="max-width: {{controller.tamanoTotal}}px; height:100%;" >
								<thead id="divCabecerasDatos" class="theadDatos" style="margin-left: -15px;">
									<tr>
				         				<th colspan={{controller.colspan}} style="{{controller.estiloCabecera}}" ng-repeat="m in controller.objetoMostrar" class="label-form">{{m.nombreMes}}</th>
				          			</tr>
				          			<tr>
				          				<th ng-repeat="a in controller.aniosfinales" style="{{controller.estiloCelda}} {{controller.estiloAlineacion}}" class="label-form">{{a.anio}}</th>
							        </tr>
								</thead>
								<tbody class="cuerpoTablaDatos" id="divTablaDatos" ng-mouseover="controller.activarScroll('divTablaDatos')" scrollespejo  style="max-height: 150px;">
							      	<tr ng-repeat="item in controller.data" style="">
								      		<td ng-repeat="posicion in controller.columnastotales track by $index" style="{{controller.estiloCelda}} min-height: 35px; height: 35px; {{controller.estiloAlineacion}}">
								      			<div>
												<span class="colorPlanificado">{{controller.getPlanificado($parent.$index,$index).planificado  | formatoMillones : controller.enMillones}}</span>
								      			</div>
								      		</td>
							      	</tr>
								</tbody>
							</table>
						</div>
						</div>
			    		<div class="divTabla"  style="height: 100%">
		    			<table st-table="rowCollection" st-safe-src="datosTabla" class="table table-striped tablaDatos"  style="height: 100%">
							<thead class="theadDatos">
									<tr>
			          					<th nowrap colspan={{controller.colspan}} style="{{controller.estiloCelda}} text-align: center;" class="label-form"><span ng-show="controller.grupoMostrado.planificado && controller.grupoMostrado.real">Total Anual</span><span ng-hide="controller.grupoMostrado.planificado && controller.grupoMostrado.real">Anual</span></th>
				          				<th rowspan="2" style="{{controller.estiloCelda}} text-align: center; vertical-align: top;" class="label-form">Total</th>
				          			</tr>
				          			<tr>
			          					<th ng-repeat="a in controller.aniosTotal" style="{{controller.estiloCelda}} {{controller.estiloAlineacion}};" class="label-form">{{a.anio}}</th>
							        </tr>
								</thead>
							<tbody class="cuerpoTablaTotales bordeIzquierda" id="divTotales" ng-mouseover="controller.activarScroll('divTotales')"  style="max-height: 150px;" scrollespejo>
									<tr ng-repeat="totales in controller.totales track by $index" style="min-height:35px; height:35px;">
										<td ng-repeat="total in totales.anio" style="{{controller.estiloCelda}} {{controller.estiloAlineacion}}">
											<div>
											<span class="colorPlanificado">{{total.valor.planificado | formatoMillones : controller.enMillones}}</span>
											</div>
										</td>
							      	</tr>
								</tbody>
							</table>
			    		</div>
			    		<div class="divPadreNombres"  style="height: 100%">
			    		<div class="divTabla"  style="height: 100%"> 
			    			<table class="table table-striped tablaDatos"  style="height: 100%">
								<tbody class="cuerpoTablaNombres">
									<tr>
							      		<td style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">Total Planificado</td>
							      	</tr>
							      	<tr>
							      		<td style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">Total Planificado Acumulado</td>
							      	</tr>
							      	<tr>
							      		<td style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">Total Ejecutado</td>
							      	</tr>
							      	<tr>
							      		<td style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">Total Ejecutado Acumulado</td>
							      	</tr>
							      	<tr>
							      		<td style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">Variación</td>
							      	</tr>
							      	<tr>
							      		<td style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">%</td>
							      	</tr>
							      	<tr>
							      		<td style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">Desembolsos</td>
							      	</tr>
							      	<tr>
							      		<td style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">Saldo</td>
							      	</tr>
								</tbody>
							</table>
		    			</div>
	    			</div>
		    		<div class="divPadreDatos" style="max-width: {{controller.tamanoTotal}}px; height: 100%">
	    				<div class="divTabla"  style="height: 100%">
			    			<table class="table table-striped tablaDatos" style="max-width: {{controller.tamanoTotal}}px; height:100%;" >
								<tbody class="cuerpoTablaDatos" id="cuerpoTotalesDatos">
							      	<tr>
							      		<td ng-repeat="posicion in controller.resumenTotales.filaPlanificado track by $index" style="{{controller.estiloCelda}} min-height: 35px; height: 35px; {{controller.estiloAlineacion}}"> {{posicion | formatoMillones : controller.enMillones}} </td>
							      	</tr>
							      	<tr>
							      		<td ng-repeat="posicion in controller.resumenTotales.filaPlanificadoAcumulado track by $index" style="{{controller.estiloCelda}} min-height: 35px; height: 35px; {{controller.estiloAlineacion}}"> {{posicion | formatoMillones : controller.enMillones}} </td>
							      	</tr>
							      	<tr>
							      		<td ng-repeat="posicion in controller.resumenTotales.filaEjecutado track by $index" style="{{controller.estiloCelda}} min-height: 35px; height: 35px; {{controller.estiloAlineacion}}"> {{posicion | formatoMillones : controller.enMillones}} </td>
							      	</tr>
							      	<tr>
							      		<td ng-repeat="posicion in controller.resumenTotales.filaEjecutadoAcumulado track by $index" style="{{controller.estiloCelda}} min-height: 35px; height: 35px; {{controller.estiloAlineacion}}"> {{posicion | formatoMillones : controller.enMillones}} </td>
							      	</tr>
							      	<tr>
							      		<td ng-repeat="posicion in controller.resumenTotales.filaVariacion track by $index" style="{{controller.estiloCelda}} min-height: 35px; height: 35px; {{controller.estiloAlineacion}}"> {{posicion | formatoMillones : controller.enMillones}} </td>
							      	</tr>
							      	<tr>
							      		<td ng-repeat="posicion in controller.resumenTotales.filaVariacionPorcentaje track by $index" style="{{controller.estiloCelda}} min-height: 35px; height: 35px; {{controller.estiloAlineacion}}"> {{posicion | formatoMillones : controller.enMillones}} </td>
							      	</tr>
							      	<tr>
							      		<td ng-repeat="posicion in controller.resumenTotales.filaDesembolsos track by $index" style="{{controller.estiloCelda}} min-height: 35px; height: 35px; {{controller.estiloAlineacion}}"> {{posicion | formatoMillones : controller.enMillones}} </td>
							      	</tr>
							      	<tr>
							      		<td ng-repeat="posicion in controller.resumenTotales.filaSaldo track by $index" style="{{controller.estiloCelda}} min-height: 35px; height: 35px; {{controller.estiloAlineacion}}"> {{posicion | formatoMillones : controller.enMillones}} </td>
							      	</tr>
								</tbody>
							</table>
						</div>
						</div>
			    		<div class="divTabla"  style="height: 100%">
		    			<table class="table table-striped tablaDatos"  style="height: 100%">
							<tbody class="cuerpoTablaTotales bordeIzquierda">
								<tr>
									<td style="{{controller.estiloCabecera}} text-align: right;" ><span>-</span>
									</td>
									<td style="{{controller.estiloCelda}} {{controller.estiloAlineacion}}"><span>{{controller.resumenTotales.totalPlanificado | formatoMillones : controller.enMillones}}</span></td>
								</tr>
								<tr>
									<td style="{{controller.estiloCabecera}} text-align: right;"><span>-</span>
									</td>
									<td style="{{controller.estiloCelda}} {{controller.estiloAlineacion}}"><span>{{controller.resumenTotales.totalPlanificadoAcumulado | formatoMillones : controller.enMillones}}</span></td>
								</tr>
								<tr>
									<td style="{{controller.estiloCabecera}} text-align: right;" ><span>-</span>
									</td>
									<td style="{{controller.estiloCelda}} {{controller.estiloAlineacion}}"><span>{{controller.resumenTotales.totalEjecutado | formatoMillones : controller.enMillones}}</span></td>
								</tr>
								<tr>
									<td style="{{controller.estiloCabecera}} text-align: right;" ><span>-</span>
									</td>
									<td style="{{controller.estiloCelda}} {{controller.estiloAlineacion}}"><span>{{controller.resumenTotales.totalEjecutadoAcumulado | formatoMillones : controller.enMillones}}</span></td>
								</tr>
								<tr>
									<td style="{{controller.estiloCabecera}} text-align: right;" ><span>-</span>
									</td>
									<td style="{{controller.estiloCelda}} {{controller.estiloAlineacion}}"><span>{{controller.resumenTotales.totalVariacion | formatoMillones : controller.enMillones}}</span></td>
								</tr>
								<tr>
									<td style="{{controller.estiloCabecera}} text-align: right;" ><span>-</span>
									</td>
									<td style="{{controller.estiloCelda}} {{controller.estiloAlineacion}}"><span>{{controller.resumenTotales.totalVariacionPorcentaje | formatoMillones : controller.enMillones}}</span></td>
								</tr>
								<tr>
									<td style="{{controller.estiloCabecera}} text-align: right;" ><span>-</span>
									</td>
									<td style="{{controller.estiloCelda}} {{controller.estiloAlineacion}}"><span>{{controller.resumenTotales.totalDesembolsos | formatoMillones : controller.enMillones}}</span></td>
								</tr>
								<tr>
									<td style="{{controller.estiloCabecera}} text-align: right;" ><span>-</span>
									</td>
									<td style="{{controller.estiloCelda}} {{controller.estiloAlineacion}}"><span>{{controller.resumenTotales.totalSaldo | formatoMillones : controller.enMillones}}</span></td>
								</tr>
							</tbody>
						</table>
		    		</div>
	    	<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-6" style="text-align: center;">
						<label class="btn btn-default" ng-click="controller.anterior()" uib-tooltip="Anterior" ng-hide="!controller.movimiento" 
								tooltip-placement="bottom">
						<span class="glyphicon glyphicon-chevron-left"></span></label>
						<label class="btn btn-default" ng-click="controller.siguiente()" uib-tooltip="Siguiente" ng-hide="!controller.movimiento"
								tooltip-placement="bottom">
						<span class="glyphicon glyphicon-chevron-right"></span></label>
					</div>
					<div class="col-sm-3">
			    		<ol class="leyendaTexto"  ng-hide="!controller.mostrarDescargar">
							<li ng-show="controller.grupoMostrado.planificado"><span class="colorPlanificadoFondo"></span>Planificado</li>
					        <li ng-show="controller.grupoMostrado.real"><span class="colorRealFondo"></span>Real</li>
						</ol>
					</div>
	    	</div>
	    	
				</div>
	    	</div>
   		</div>
	</div>
</div>
