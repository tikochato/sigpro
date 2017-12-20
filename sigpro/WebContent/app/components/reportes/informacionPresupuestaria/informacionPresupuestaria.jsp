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
		  	width: 200px; 
			min-width: 200px; 
			max-width:200px; 
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
			max-height: 375px;
			margin-right: -15px;
			overflow-y:hidden;
			overflow-x:hidden;
		}
		
	    .cuerpoTablaNombres {
		    overflow-y: scroll;
		    overflow-x: scroll;
		    display: inline-block;
		    font-size: 13px;
		    max-width: 215px;
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
		}
		
		.tablaDatos {
			display: flex;
		    flex-direction: column;
		    align-items: stretch;
		    max-height: 375px; 
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

<div ng-controller="informacionPresupuestariaController as controller" class="maincontainer all_page" id="title" style="height: 100%">
	
    <shiro:lacksPermission name="24010">
			<p ng-init="controller.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
	<div class="row" id="reporte" style="height: 100%" align="center">
		<div class="col-sm-12">
			<div class="row" style="height: 20%">
				<div class="row" align="left">
		    		<div class="panel panel-default">
		  				<div class="panel-heading"><h3>Ejecución presupuestaria</h3></div>
					</div>
				</div>
		    	<br>
		    	<div class="row">
		    		<div class="form-group col-sm-6" align="left">
						<div id="prestamo" angucomplete-alt placeholder="" pause="100" selected-object="controller.cambioPrestamo"
							  local-data="controller.lprestamos" search-fields="proyectoPrograma" title-field="proyectoPrograma" field-required="true" field-label="* Préstamo"
							  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
							  initial-value="controller.prestamoNombre" focus-out="controller.blurPrestamo()" input-name="prestamo"></div>
						<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
					</div>
		    	</div>
		    	<div class="row">
		    		<div class="form-group col-sm-6" align="left">
						<div id="pep" angucomplete-alt placeholder="" pause="100" selected-object="controller.cambioPep"
							  local-data="controller.peps" search-fields="nombre" title-field="nombre" field-required="true" field-label="* {{etiquetas.proyecto}}"
							  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
							  initial-value="controller.pepNombre" focus-out="controller.blurPep()" input-name="pep" disable-input="controller.prestamoId==null"></div>
						<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
					</div>
		    	</div>
	    		<div class="row" style="height: 15%">
	    			<div class="form-group col-sm-4" align="left">
						<div id= "lineaBase" angucomplete-alt placeholder="" pause="100" selected-object="controller.cambioLineaBase"
							  local-data="controller.lineasBase" search-fields="nombre" title-field="nombre" 
							  field-required="true" field-label="* Linea Base" minlength="1" input-class="form-control form-control-small field-angucomplete inputText" 
							  match-class="angucomplete-highlight" initial-value="controller.lineaBaseNombre" 
							  focus-out="controller.blurLineaBase()" input-name="lineaBase"></div>
						<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
					</div>
	    			<div align="left" class="form-group col-sm-1" style="margin-top: 5px;">
						<input type="number"  class="inputText" ng-model="controller.fechaInicio" maxlength="4" 
						ng-value="controller.fechaInicio" onblur="this.setAttribute('value', this.value);"
						ng-change="controller.validar(2)"/>
					  	<label for="campo.id" class="floating-label" style="left: 0;">*Año Inicial</label>
					</div>
					
					<div align="left" class="form-group col-sm-1" style="margin-top: 5px;">
						<input type="number"  class="inputText" ng-model="controller.fechaFin" maxlength="4" 
						ng-value="controller.fechaFin" onblur="this.setAttribute('value', this.value);"
						ng-change="controller.validar(3)"/>
					  	<label for="campo.id" class="floating-label">*Año Final</label>
					</div>
					
					<div class="col-sm-6" align="right" ng-hide="!controller.mostrarDescargar">
						<div class="">
							<div class="btn-group" style="margin-left: -20px;">
								<label class="btn btn-default" ng-model="controller.enMillones" uib-btn-radio="true" ng-click="controller.calcularTamaniosCeldas(); controller.convertirMillones();" uib-tooltip="Millones de Quetzales" role="button" tabindex="0" aria-invalid="false">
								<span>MQ</span></label>
								<label class="btn btn-default" ng-model="controller.enMillones" uib-btn-radio="false" ng-click="controller.calcularTamaniosCeldas(); controller.convertirMillones();" uib-tooltip="Quetzales" role="button" tabindex="1" aria-invalid="false">
								<span>Q</span></label>
							</div>
							<div class="btn-group" style="padding-left: 20px;">
								<label class="btn btn-default" ng-model="controller.agrupacionActual" uib-btn-radio="1" ng-click="controller.cambiarAgrupacion(1)" uib-tooltip="Mensual" role="button" tabindex="1" aria-invalid="false">
								<span>M</span></label>
								<label class="btn btn-default" ng-model="controller.agrupacionActual" uib-btn-radio="2" ng-click="controller.cambiarAgrupacion(2)" uib-tooltip="Bimestral" role="button" tabindex="2" aria-invalid="false">
								<span>B</span></label>
								<label class="btn btn-default" ng-model="controller.agrupacionActual" uib-btn-radio="3" ng-click="controller.cambiarAgrupacion(3)" uib-tooltip="Trimestral" role="button" tabindex="3" aria-invalid="false">
								<span>T</span></label>
								<label class="btn btn-default" ng-model="controller.agrupacionActual" uib-btn-radio="4" ng-click="controller.cambiarAgrupacion(4)" uib-tooltip="Cuatrimestral" role="button" tabindex="4" aria-invalid="false">
								<span>C</span></label>
								<label class="btn btn-default" ng-model="controller.agrupacionActual" uib-btn-radio="5" ng-click="controller.cambiarAgrupacion(5)" uib-tooltip="Semestral" role="button" tabindex="5" aria-invalid="false">
								<span>S</span></label>
								<label class="btn btn-default" ng-model="controller.agrupacionActual" uib-btn-radio="6" ng-click="controller.cambiarAgrupacion(6)" uib-tooltip="Anual" role="button" tabindex="6" aria-invalid="false">
								<span>A</span></label>
							</div>
							<div class=" btn-group" style="padding-left: 20px;">
								<label class="btn btn-default" ng-model="controller.grupoMostrado.planificado"  uib-btn-checkbox ng-click="controller.verificaSeleccionTipo(1)" uib-tooltip="Planificado" role="button" tabindex="7" aria-invalid="false">
								<span>P</span></label>
								<label class="btn btn-default" ng-model="controller.grupoMostrado.real"  uib-btn-checkbox ng-click="controller.verificaSeleccionTipo(2)" uib-tooltip="Real" role="button" tabindex="8" aria-invalid="false">
								<span>R</span></label>
	    					</div>
							<div class="btn-group" style="padding-left: 20px;">
								<label class="btn btn-default" ng-click="controller.exportarExcel()" uib-tooltip="Exportar a Excel">
								<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
								<label class="btn btn-default" ng-click="controller.exportarPdf()" uib-tooltip="Exportar a PDF" ng-hide="true">
								<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
							</div>
						</div>
					</div>
	    		</div>
			</div>
			<br><br>
			<div class="row" style="height: 80%">
				<div ng-hide="!controller.mostrarCargando">
	    			<div class="grid_loading" ng-hide="!controller.mostrarCargando" style="height: {{controller.tamanoCargando}}px">
						<div class="msg">
							<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
								<br />
								<br /> <b>Cargando, por favor espere...</b> 
							</span>
						</div>
					</div>
	    		</div>
	    		
	    		<div class="row" ng-hide="!controller.mostrarDescargar">
		    		<div class="divPadreNombres">
			    		<div class="divTabla" style="max-height: 375px;"> 
			    			<table st-table="rowCollection" st-safe-src="datosTabla" class="table table-striped tablaDatos">
								<thead class="theadDatos">
									<tr>
				          				<th rowspan="2" st-sort="nombre" style="text-align: center; vertical-align: top; min-width:200px;" class="label-form">Nombre</th>
				          				<th style="border-bottom:2px solid #fff;" class="label-form">.</th>
				         			</tr>
				         			<tr>
				          				<th class="label-form">.</th>
				         			</tr>
								</thead>
								<tbody vs-repeat class="cuerpoTablaNombres" id="divTablaNombres"  style="max-height: 390px; margin-bottom: -15px;" onmouseover="activarScroll(this.id)" onscroll="scrollEspejo(this)">
									<tr ng-repeat="item in controller.data">
							      		<td nowrap style="min-width:200px; min-height: 35px; height: 35px;">
							      			<div uib-tooltip="{{item.nombre}}" class="nombreFormat">
							      				<span ng-class="controller.iconoObjetoTipo[item.objeto_tipo]" uib-tooltip="{{controller.tooltipObjetoTipo[item.objeto_tipo]}}" style="margin-left: {{item.nivel}}em"></span>
							      				{{item.nombre}}
							      			</div>
							      		</td>
							      		<td></td>	
							      	</tr>
								</tbody>
							</table>
		    			</div>
	    			</div>
		    		<div class="divPadreDatos" style="max-width: {{controller.tamanoTotal}}px">
	    				<div class="divTabla">
			    			<table st-table="rowCollection" st-safe-src="datosTabla" class="table table-striped tablaDatos" 
				    				style="max-width: {{controller.tamanoTotal}}px;">
								<thead id="divCabecerasDatos" class="theadDatos">
									<tr>
				         				<th colspan={{controller.colspan}} style="{{controller.estiloCabecera}}" ng-repeat="m in controller.objetoMostrar" class="label-form">{{m.nombreMes}}</th>
				          			</tr>
				          			<tr>
				          				<th ng-repeat="a in controller.aniosfinales" style="{{controller.estiloCelda}} {{controller.estiloAlineacion}}" class="label-form">{{a.anio}}</th>
							        </tr>
								</thead>
								<tbody vs-repeat class="cuerpoTablaDatos" id="divTablaDatos" onmouseover="activarScroll(this.id)" onscroll="scrollEspejo(this)">
							      	<tr ng-repeat="item in controller.data" style="">
								      		<td ng-repeat="posicion in controller.columnastotales track by $index" style="{{controller.estiloCelda}} min-height: 35px; height: 35px; {{controller.estiloAlineacion}}">
								      			<div style="{{controller.porcentajeCeldaValor}}">
												<span ng-show="controller.grupoMostrado.planificado" class="colorPlanificado">{{controller.getPlanificado(item,$index).planificado | formatoMillones : controller.enMillones}}</span>
								      			</div>
								      			<div style="{{controller.porcentajeCeldaPipe}}">
												<span ng-show="controller.grupoMostrado.planificado && controller.grupoMostrado.real && controller.getPlanificado(item,$index)" > | </span>
								      			</div>
								      			<div style="{{controller.porcentajeCeldaValor}}">
												<span ng-show="controller.grupoMostrado.real" class="colorReal">{{controller.getPlanificado(item,$index).real | formatoMillones : controller.enMillones}}</span>
												</div>
								      		</td>
							      	</tr>
								</tbody>
							</table>
						</div>
					</div>
		    		<div class="divTabla">
		    			<table st-table="rowCollection" st-safe-src="datosTabla" class="table table-striped tablaDatos">
							<thead class="theadDatos">
								<tr>
		          				<th nowrap colspan={{controller.colspan}} style="{{controller.estiloCelda}} text-align: center;" class="label-form">Total Anual</th>
			          				<th rowspan="2" style="{{controller.estiloCelda}} text-align: center; vertical-align: top;" class="label-form">Total</th>
			          			</tr>
			          			<tr>
		          				<th ng-repeat="a in controller.aniosTotal" style="{{controller.estiloCelda}} {{controller.estiloAlineacion}};" class="label-form">{{a.anio}}</th>
						        </tr>
							</thead>
							<tbody vs-repeat class="cuerpoTablaTotales bordeIzquierda" id="divTotales" onmouseover="activarScroll(this.id)" onscroll="scrollEspejo(this)" tot="{{mi.totales.length}}">
								<tr ng-repeat="totales in controller.totales ">
									<td ng-repeat="total in totales.anio" style="{{controller.estiloCelda}} {{controller.estiloAlineacion}};  min-height: 35px; height: 35px;">
										<div style="{{controller.porcentajeCeldaValor}}">
											<span ng-show="controller.grupoMostrado.planificado" class="colorPlanificado">{{total.valor.planificado | formatoMillones : controller.enMillones}}</span>
						      			</div>
						      			<div style="{{controller.porcentajeCeldaPipe}}">
											<span ng-show="controller.grupoMostrado.planificado && controller.grupoMostrado.real" > | </span>
						      			</div>
						      			<div style="{{controller.porcentajeCeldaValor}}">
											<span ng-show="controller.grupoMostrado.real" class="colorReal">{{total.valor.real | formatoMillones : controller.enMillones}}</span>
										</div>
									</td>
						      	</tr>
							</tbody>
						</table>
		    		</div>
				</div>
				<br><br>
				<div class="row" ng-hide="!controller.mostrarDescargar">
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
		    	<br><br>
		    	<div class="row" ng-hide="!controller.mostrarDescargar" style="width: 70%">
		    		<label class="label-form">Total Ejecutado</label>
		    		<canvas id="line" class="chart chart-line" chart-data="controller.dataGrafica" 
		    			chart-labels="controller.labels" chart-series="controller.series" 
		    			chart-colors = "controller.lineColors" chart-legend="true"
		    			chart-options="controller.optionsGrafica">
					</canvas>
		    	</div>
		    	
		    	<br/>
		    	<br/>
		    	
		    	<div class="row" ng-hide="!controller.mostrarDescargar" style="width: 70%">
		    		<label class="label-form">Total Acumulado</label>
		    		<canvas id="line" class="chart chart-line" chart-data="controller.dataGraficaAcumulado" 
		    			chart-labels="controller.labels" chart-series="controller.series" 
		    			chart-colors = "controller.lineColors" chart-legend="true"
		    			chart-options="controller.optionsGrafica">
					</canvas>
		    	</div>
			</div>
		</div>
	</div>
</div>