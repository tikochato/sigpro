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
			z-index: 1; 
		}	
		
		.divPadreDatos{		
			float: left; 
			display: inline-block;
			white-space: nowrap;	
			overflow:hidden;	
			z-index: 2;	
		}
			
		.divTabla{
			float: left;
			overflow-y: visible;
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

<div ng-controller="flujocajaController as flujoc" class="maincontainer all_page" id="title">
	
	<shiro:lacksPermission name="30010">
		<p ng-init="flujoc.redireccionSinPermisos()"></p>
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
	    		<div class="form-group col-sm-6" align="left">
					<div id="prestamo" angucomplete-alt placeholder="" pause="100" selected-object="flujoc.cambioPrestamo"
						  local-data="flujoc.lprestamos" search-fields="proyectoPrograma" title-field="proyectoPrograma" field-required="true" field-label="* Préstamo"
						  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
						  initial-value="flujoc.prestamoNombre" focus-out="flujoc.blurPrestamo()" input-name="prestamo"></div>
					<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
				</div>
	    	</div>
	    	<div class="row">
	    		<div class="form-group col-sm-6" align="left">
					<div id="pep" angucomplete-alt placeholder="" pause="100" selected-object="flujoc.cambioPep"
						  local-data="flujoc.peps" search-fields="nombre" title-field="nombre" field-required="true" field-label="* {{etiquetas.proyecto}}"
						  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
						  initial-value="flujoc.pepNombre" focus-out="flujoc.blurPep()" input-name="pep" disable-input="flujoc.prestamoId==null"></div>
					<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
				</div>
	    	</div>
	    	
			<div class="row">
				<div class="form-group col-sm-4" align="left">
						<div id= "lineaBase" angucomplete-alt placeholder="" pause="100" selected-object="flujoc.cambioLineaBase"
							  local-data="flujoc.lineasBase" search-fields="nombre" title-field="nombre" 
							  field-required="true" field-label="* Linea Base" minlength="1" input-class="form-control form-control-small field-angucomplete inputText" 
							  match-class="angucomplete-highlight" initial-value="flujoc.lineaBaseNombre" 
							  focus-out="flujoc.blurLineaBase()" input-name="lineaBase"></div>
						<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
					</div>
				<div class="form-group col-sm-2" style="margin-top: 5px;">
					<input type="text"  class="inputText" uib-datepicker-popup="{{flujoc.formatofecha}}" alt-input-formats="{{flujoc.altformatofecha}}"
						ng-model="flujoc.fechaCorte" is-open="flujoc.isOpen"
			            datepicker-options="flujoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"  
			            ng-required="true" ng-change="flujoc.validar()"
			            ng-value="flujoc.fechaCorte" onblur="this.setAttribute('value', this.value);"/>
			            <span class="label-icon" ng-click="flujoc.abrirPopupFecha()">
			              <i class="glyphicon glyphicon-calendar"></i>
			            </span>
				  	<label for="campo.id" class="floating-label">*Fecha Corte</label>
				</div>
				
				<div class="col-sm-6" align="right" ng-hide="!flujoc.mostrarDescargar">
					<div class="col-sm-12">
						<div class="btn-group">
							<label class="btn btn-default" ng-model="flujoc.enMillones" uib-btn-radio="true" ng-click="flujoc.calcularTamaniosCeldas(); flujoc.convertirMillones();" uib-tooltip="Millones de Quetzales" role="button" tabindex="0" aria-invalid="false">
							<span>MQ</span></label>
							<label class="btn btn-default" ng-model="flujoc.enMillones" uib-btn-radio="false" ng-click="flujoc.calcularTamaniosCeldas(); flujoc.convertirMillones();" uib-tooltip="Quetzales" role="button" tabindex="1" aria-invalid="false">
							<span>Q</span></label>
						</div>
						<div class="btn-group" style="padding-left: 20px;">
							<label class="btn btn-default" ng-model="flujoc.agrupacionActual" uib-btn-radio="1" ng-click="flujoc.cambiarAgrupacion(1)" uib-tooltip="Mes" role="button" tabindex="1" aria-invalid="false">
							<span>M</span></label>
							<label class="btn btn-default" ng-model="flujoc.agrupacionActual" uib-btn-radio="2" ng-click="flujoc.cambiarAgrupacion(2)" uib-tooltip="Bimestre" role="button" tabindex="2" aria-invalid="false">
							<span>B</span></label>
							<label class="btn btn-default" ng-model="flujoc.agrupacionActual" uib-btn-radio="3" ng-click="flujoc.cambiarAgrupacion(3)" uib-tooltip="Trimestre" role="button" tabindex="3" aria-invalid="false">
							<span>T</span></label>
							<label class="btn btn-default" ng-model="flujoc.agrupacionActual" uib-btn-radio="4" ng-click="flujoc.cambiarAgrupacion(4)" uib-tooltip="Cuatrimestre" role="button" tabindex="4" aria-invalid="false">
							<span>C</span></label>
							<label class="btn btn-default" ng-model="flujoc.agrupacionActual" uib-btn-radio="5" ng-click="flujoc.cambiarAgrupacion(5)" uib-tooltip="Semestre" role="button" tabindex="5" aria-invalid="false">
							<span>S</span></label>
							<label class="btn btn-default" ng-model="flujoc.agrupacionActual" uib-btn-radio="6" ng-click="flujoc.cambiarAgrupacion(6)" uib-tooltip="Año" role="button" tabindex="6" aria-invalid="false">
							<span>A</span></label>
						</div>
						<div class="btn-group" style="padding-left: 20px;">
							<label class="btn btn-default" ng-click="flujoc.exportarExcel()" uib-tooltip="Exportar a Excel" ng-hide="!flujoc.mostrarDescargar">
							<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
							<label class="btn btn-default" ng-click="flujoc.exportarPdf()" uib-tooltip="Exportar a PDF" ng-hide="true">
							<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
						</div>
					</div>
				</div>
    			<br><br><br>
	    	</div>
	    	<div style="width: 100%; height: 100%" id="reporte">
	    		
				<div class="grid_loading" ng-hide="!flujoc.mostrarCargando" style="height: calc(80% - 32px)">
						<div class="msg">
							<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
								<br />
								<br /> <b>Cargando, por favor espere...</b> 
							</span>
						</div>
					</div>		
		    	<div class="row" ng-hide="!flujoc.mostrarDescargar" >
		    				
		    		<div class="divPadreNombres"  style="height: 100%">
			    		<div class="divTabla"  style="height: 100%"> 
			    			<table class="table table-striped tablaDatos"  style="height: 100%">
								<thead class="theadDatos">
									<tr>
				          				<th style="text-align: center; vertical-align: top; width:300px; height: 74px;" class="label-form">Nombre</th>
				         			</tr>
								</thead>
								<tbody vs-repeat class="cuerpoTablaNombres" id="divTablaNombres" style="max-height: 300px;" onmouseover="activarScroll(this.id)" onscroll="scrollEspejo(this)">
									<tr ng-repeat="item in flujoc.data">
							      		<td nowrap style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">
							      			<p class="nombreFormat">
							      				<span ng-class="flujoc.iconoObjetoTipo[item.objeto_tipo]" uib-tooltip="{{flujoc.tooltipObjetoTipo[item.objeto_tipo]}}" style="margin-left: {{item.nivel-1}}em"></span>
							      				<span uib-tooltip="{{item.nombre}}">{{item.nombre}}</span>
							      			</p>
							      		</td>
							      	</tr>
								</tbody>
							</table>
		    			</div>
	    			</div>
		    		<div class="divPadreDatos" style="max-width: {{flujoc.tamanoTotal}}px; height: 100%; margin-top: 3px;">
	    				<div class="divTabla"  style="height: 100%">
			    			<table class="table table-striped tablaDatos" 
				    				style="max-width: {{flujoc.tamanoTotal}}px; height:100%;" >
								<thead id="divCabecerasDatos" class="theadDatos" style="margin-left: -15px;">
									<tr>
				         				<th colspan={{flujoc.colspan}} style="{{flujoc.estiloCabecera}}" ng-repeat="m in flujoc.objetoMostrar" class="label-form">{{m.nombreMes}}</th>
				          			</tr>
				          			<tr>
				          				<th ng-repeat="a in flujoc.aniosfinales" style="{{flujoc.estiloCelda}} {{flujoc.estiloAlineacion}}" class="label-form">{{a.anio}}</th>
							        </tr>
								</thead>
								<tbody vs-repeat class="cuerpoTablaDatos" id="divTablaDatos" onmouseover="activarScroll(this.id)" onscroll="scrollEspejo(this)" style="max-height: 300px;">
							      	<tr ng-repeat="item in flujoc.data" style="">
								      		<td ng-repeat="posicion in flujoc.columnastotales track by $index" style="{{flujoc.estiloCelda}} min-height: 35px; height: 35px; {{flujoc.estiloAlineacion}}">
								      			<div>
												<span class="colorPlanificado">{{flujoc.getPlanificado(item,$index).planificado  | formatoMillones : flujoc.enMillones}}</span>
								      			</div>
								      		</td>
							      	</tr>
								</tbody>
							</table>
						</div>
						</div>
			    		<div class="divTabla"  style="height: 100%">
		    			<table class="table table-striped tablaDatos" style="max-width: {{flujoc.tamanoTotal}}px; height:100%;" >
							<thead class="theadDatos">
									<tr>
			          					<th style="{{flujoc.estiloCelda}} text-align:center; vertical-align: top; height: 72px;" class="label-form">Total</th>
							        </tr>
								</thead>
							<tbody vs-repeat class="cuerpoTablaTotales bordeIzquierda" id="divTotales" style="max-height: 300px;" onmouseover="activarScroll(this.id)" onscroll="scrollEspejo(this)">
									<tr ng-repeat="totales in flujoc.totales" style="min-height:35px; height:35px;">
										<td style="{{flujoc.estiloCelda}} {{flujoc.estiloAlineacion}}">
											<div>
											<span class="colorPlanificado">{{totales.anio[0].valor.planificado | formatoMillones : flujoc.enMillones}}</span>
											</div>
										</td>
							      	</tr>
								</tbody>
							</table>
			    		</div>
	    		</div>
	    		<div class="row" ng-hide="!flujoc.mostrarDescargar" >
			    		<div class="divPadreNombres"  style="height: 100%">
			    		<div class="divTabla"  style="height: 100%"> 
			    			<table class="table table-striped tablaDatos"  style="height: 100%">
								<tbody class="cuerpoTablaNombres">
									<tr>
							      		<td class="colorPlanificado" style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">Total Planificado</td>
							      	</tr>
							      	<tr>
							      		<td class="colorPlanificado" style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">Total Planificado Acumulado</td>
							      	</tr>
							      	<tr>
							      		<td class="colorReal" style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">Total Ejecutado</td>
							      	</tr>
							      	<tr>
							      		<td class="colorReal" style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">Total Ejecutado Acumulado</td>
							      	</tr>
							      	<tr>
							      		<td style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">Variación</td>
							      	</tr>
							      	<tr>
							      		<td style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">%</td>
							      	</tr>
							      	<tr>
							      		<td style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">Desembolsos Reales</td>
							      	</tr>
							      	<tr>
							      		<td style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">Desembolsos Planificados</td>
							      	</tr>
							      	<tr>
							      		<td style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">Saldo de Cuenta</td>
							      	</tr>
							      	<tr>
							      		<td style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">Anticipos</td>
							      	</tr>
							      	<tr>
							      		<td style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden; font-weight: bold;">Saldo</td>
							      	</tr>
								</tbody>
							</table>
		    			</div>
	    			</div>
		    		<div class="divPadreDatos" style="max-width: {{flujoc.tamanoTotal}}px; height: 100%">
	    				<div class="divTabla"  style="height: 100%">
			    			<table class="table table-striped tablaDatos" style="max-width: {{flujoc.tamanoTotal}}px; height:100%;" >
								<tbody class="cuerpoTablaDatos" id="cuerpoTotalesDatos">
							      	<tr>
							      		<td ng-repeat="posicion in flujoc.resumenTotales.filaPlanificado track by $index" class="colorPlanificado" style="{{flujoc.estiloCelda}} min-height: 35px; height: 35px; {{flujoc.estiloAlineacion}}"> {{posicion | formatoMillones : flujoc.enMillones}} </td>
							      	</tr>
							      	<tr>
							      		<td ng-repeat="posicion in flujoc.resumenTotales.filaPlanificadoAcumulado track by $index" class="colorPlanificado" style="{{flujoc.estiloCelda}} min-height: 35px; height: 35px; {{flujoc.estiloAlineacion}}"> {{posicion | formatoMillones : flujoc.enMillones}} </td>
							      	</tr>
							      	<tr>
							      		<td ng-repeat="posicion in flujoc.resumenTotales.filaEjecutado track by $index" class="colorReal" style="{{flujoc.estiloCelda}} min-height: 35px; height: 35px; {{flujoc.estiloAlineacion}}"> {{posicion | formatoMillones : flujoc.enMillones}} </td>
							      	</tr>
							      	<tr>
							      		<td ng-repeat="posicion in flujoc.resumenTotales.filaEjecutadoAcumulado track by $index" class="colorReal" style="{{flujoc.estiloCelda}} min-height: 35px; height: 35px; {{flujoc.estiloAlineacion}}"> {{posicion | formatoMillones : flujoc.enMillones}} </td>
							      	</tr>
							      	<tr>
							      		<td ng-repeat="posicion in flujoc.resumenTotales.filaVariacion track by $index" style="{{flujoc.estiloCelda}} min-height: 35px; height: 35px; {{flujoc.estiloAlineacion}}"> {{posicion | formatoMillones : flujoc.enMillones}} </td>
							      	</tr>
							      	<tr>
							      		<td ng-repeat="posicion in flujoc.resumenTotales.filaVariacionPorcentaje track by $index" style="{{flujoc.estiloCelda}} min-height: 35px; height: 35px; {{flujoc.estiloAlineacion}}"> {{posicion}}%</td>
							      	</tr>
							      	<tr>
							      		<td ng-repeat="posicion in flujoc.resumenTotales.filaDesembolsosReal track by $index" style="{{flujoc.estiloCelda}} min-height: 35px; height: 35px; {{flujoc.estiloAlineacion}}"> {{posicion | formatoMillones : flujoc.enMillones}} </td>
							      	</tr>
							      	<tr>
							      		<td ng-repeat="posicion in flujoc.resumenTotales.filaDesembolsos track by $index" style="{{flujoc.estiloCelda}} min-height: 35px; height: 35px; {{flujoc.estiloAlineacion}}"> {{posicion | formatoMillones : flujoc.enMillones}} </td>
							      	</tr>
							      	<tr>
							      		<td ng-repeat="posicion in flujoc.resumenTotales.filaSaldoCuenta track by $index" style="{{flujoc.estiloCelda}} min-height: 35px; height: 35px; {{flujoc.estiloAlineacion}}"> {{posicion | formatoMillones : flujoc.enMillones}} </td>
							      	</tr>
							      	<tr>
							      		<td ng-repeat="posicion in flujoc.resumenTotales.filaAnticipos track by $index" style="{{flujoc.estiloCelda}} min-height: 35px; height: 35px; {{flujoc.estiloAlineacion}}"> {{posicion | formatoMillones : flujoc.enMillones}} </td>
							      	</tr>
							      	<tr>
							      		<td ng-repeat="posicion in flujoc.resumenTotales.filaSaldo track by $index" style="{{flujoc.estiloCelda}} font-weight: bold; min-height: 35px; height: 35px; {{flujoc.estiloAlineacion}}"> {{posicion | formatoMillones : flujoc.enMillones}} </td>
							      	</tr>
								</tbody>
							</table>
						</div>
						</div>
			    		<div class="divTabla"  style="height: 100%">
		    			<table class="table table-striped tablaDatos" style="max-width: {{flujoc.tamanoTotal}}px; height:100%;" >
							<tbody class="cuerpoTablaTotales bordeIzquierda">
								<tr>
									<td style="{{flujoc.estiloCelda}} {{flujoc.estiloAlineacion}} height: 35px;"><span class="colorPlanificado">{{flujoc.resumenTotales.totalPlanificado | formatoMillones : flujoc.enMillones}}</span></td>
								</tr>
								<tr>
									<td style="{{flujoc.estiloCelda}} {{flujoc.estiloAlineacion}} height: 35px;"><span class="colorPlanificado">{{flujoc.resumenTotales.totalPlanificadoAcumulado | formatoMillones : flujoc.enMillones}}</span></td>
								</tr>
								<tr>
									<td style="{{flujoc.estiloCelda}} {{flujoc.estiloAlineacion}} height: 35px;"><span>{{flujoc.resumenTotales.totalEjecutado | formatoMillones : flujoc.enMillones}}</span></td>
								</tr>
								<tr>
									<td style="{{flujoc.estiloCelda}} {{flujoc.estiloAlineacion}} height: 35px;"><span>{{flujoc.resumenTotales.totalEjecutadoAcumulado | formatoMillones : flujoc.enMillones}}</span></td>
								</tr>
								<tr>
									<td style="{{flujoc.estiloCelda}} {{flujoc.estiloAlineacion}} height: 35px;"><span>{{flujoc.resumenTotales.totalVariacion | formatoMillones : flujoc.enMillones}}</span></td>
								</tr>
								<tr>
									<td style="{{flujoc.estiloCelda}} {{flujoc.estiloAlineacion}} height: 35px;"><span></span></td>
								</tr>
								<tr>
									<td style="{{flujoc.estiloCelda}} {{flujoc.estiloAlineacion}} height: 35px;"><span>{{flujoc.resumenTotales.totalDesembolsosReal | formatoMillones : flujoc.enMillones}}</span></td>
								</tr>
								<tr>
									<td style="{{flujoc.estiloCelda}} {{flujoc.estiloAlineacion}} height: 35px;"><span>{{flujoc.resumenTotales.totalDesembolsos | formatoMillones : flujoc.enMillones}}</span></td>
								</tr>
								<tr>
									<td style="{{flujoc.estiloCelda}} {{flujoc.estiloAlineacion}} height: 35px;"><span>{{flujoc.resumenTotales.totalSaldoCuenta | formatoMillones : flujoc.enMillones}}</span></td>
								</tr>
								<tr>
									<td style="{{flujoc.estiloCelda}} {{flujoc.estiloAlineacion}} height: 35px;"><span>{{flujoc.resumenTotales.totalAnticipos | formatoMillones : flujoc.enMillones}}</span></td>
								</tr>
								<tr>
									<td style="{{flujoc.estiloCelda}} {{flujoc.estiloAlineacion}} height: 35px; font-weight: bold;"><span>{{flujoc.resumenTotales.totalSaldo | formatoMillones : flujoc.enMillones}}</span></td>
								</tr>
							</tbody>
						</table>
		    		</div>
			</div>
			<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-6" style="text-align: center;">
						<label class="btn btn-default" ng-click="flujoc.anterior()" uib-tooltip="Anterior" ng-hide="!flujoc.movimiento" 
								tooltip-placement="bottom">
						<span class="glyphicon glyphicon-chevron-left"></span></label>
						<label class="btn btn-default" ng-click="flujoc.siguiente()" uib-tooltip="Siguiente" ng-hide="!flujoc.movimiento"
								tooltip-placement="bottom">
						<span class="glyphicon glyphicon-chevron-right"></span></label>
					</div>
					<div class="col-sm-3" style="margin-top: 10px;">
			    		<ol class="leyendaTexto" ng-hide="!flujoc.mostrarDescargar">
							<li><span class="colorPlanificadoFondo"></span>Planificado</li>
					        <li><span class="colorRealFondo"></span>Real</li>
						</ol>
					</div>
	    	</div>
	    	</div>
    		<div class="row" align="center" >
				<div class="col-sm-12 ">
				<br>
					<div style="width: 70%">
						<div style="width: 30%">
							<div class="well well-lg">* Flujo de Caja Aproximado</div>
						</div>
						<label class="label-form"  >Saldos </label>
						<canvas id="line" class="chart chart-line" chart-data="flujoc.saldosGrafica" 
						chart-labels="flujoc.etiqutas" chart-series="flujoc.series" chart-options="flujoc.options"
						chart-dataset-override="flujoc.datasetOverride" chart-colors = "desembolsosc.lineColors"
						  chart-legend="true">
						</canvas>
					</div>
				</div>
			</div>
   		</div>
	</div>
</div>
