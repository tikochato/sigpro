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
		
		.divTablaTotales{
			float: left;
			max-height: 375px;
			margin-right: -15px;
			overflow-y:hidden;
			overflow-x:hidden;
			text-align: right;
		}
		
	    .cuerpoTablaNombres {
		    overflow-y: scroll;
		    overflow-x: scroll;
		    display: inline-block;
		    font-size: 13px;
		    max-width: 215px;
		}
		
		.cuerpoTablaNombresTot{
			overflow-y: scroll;
		    overflow-x: hidden;
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
		
		.cuerpoTablaTotal{
			overflow-y: hidden;
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

	<div style="width: 98%" ng-controller="reporteFinancieroAdquisicionesController as planadqui" class="maincontainer all_page" id="title" style="height: 100%">
		<shiro:lacksPermission name="24010">
			<p ng-init="planadqui.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="row" id="reporte" style="height: 100%" align="center">
			<div class="col-sm-12">
				<div class="row" style="height: 20%">
					<div class="row" align="left">
		    			<div class="panel panel-default">
			  				<div class="panel-heading"><h3>Reporte Financiero de Adquisiciones AÑO FISCAL {{planadqui.anio}}</h3></div>
						</div>
					</div>
					<br>
					<div class="row">
			    		<div class="form-group col-sm-6" align="left">
							<div id="prestamo" angucomplete-alt placeholder="" pause="100" selected-object="planadqui.cambioPrestamo"
								  local-data="planadqui.lprestamos" search-fields="proyectoPrograma" title-field="proyectoPrograma" field-required="true" field-label="* Préstamo"
								  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
								  initial-value="planadqui.prestamoNombre" focus-out="planadqui.blurPrestamo()" input-name="prestamo"></div>
							<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
						</div>
			    	</div>
			    	<div class="row">
			    		<div class="form-group col-sm-6" align="left">
							<div id="pep" angucomplete-alt placeholder="" pause="100" selected-object="planadqui.cambioPep"
								  local-data="planadqui.peps" search-fields="nombre" title-field="nombre" field-required="true" field-label="* {{etiquetas.proyecto}}"
								  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
								  initial-value="planadqui.pepNombre" focus-out="planadqui.blurPep()" input-name="pep" disable-input="planadqui.prestamoId==null"></div>
							<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
						</div>
			    	</div>
			    	<div class="row">
			    		<div class="form-group col-sm-4" align="left">
							<div id= "lineaBase" angucomplete-alt placeholder="" pause="100" selected-object="planadqui.cambioLineaBase"
								  local-data="planadqui.lineasBase" search-fields="nombre" title-field="nombre" 
								  field-required="true" field-label="* Linea Base" minlength="1" input-class="form-control form-control-small field-angucomplete inputText" 
								  match-class="angucomplete-highlight" initial-value="planadqui.lineaBaseNombre" 
								  focus-out="planadqui.blurLineaBase()" input-name="lineaBase"></div>
							<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
						</div>
						<div align="left" class="form-group col-sm-1">
							<input type="number"  class="inputText" ng-model="planadqui.fechaInicio" maxlength="4" 
							ng-value="planadqui.fechaInicio" onblur="this.setAttribute('value', this.value);"
							ng-change="planadqui.validar(2)"/>
						  	<label for="campo.id" class="floating-label" style="left: 0;">*Año Inicial</label>
						</div>
				
						<div align="left" class="form-group col-sm-1">
							<input type="number"  class="inputText" ng-model="planadqui.fechaFin" maxlength="4" 
							ng-value="planadqui.fechaFin" onblur="this.setAttribute('value', this.value);"
							ng-change="planadqui.validar(3)"/>
						  	<label for="campo.id" class="floating-label">*Año Final</label>
						</div>
			    	</div>
					<div class="row" style="width: 100%; height: 15%">
	    				<div class="col-sm-12" align="right" ng-hide="!planadqui.mostrarDescargar">
	    					<div class="form-group col-sm-1">
							</div>
							<div class="col-sm-11">
								<div class="btn-group">
									<label class="btn btn-default" ng-model="planadqui.enMillones" uib-btn-radio="true" ng-click="planadqui.calcularTamaniosCeldas(); planadqui.convertirMillones();" uib-tooltip="Millones de Quetzales" role="button" tabindex="0" aria-invalid="false">
									<span>MQ</span></label>
									<label class="btn btn-default" ng-model="planadqui.enMillones" uib-btn-radio="false" ng-click="planadqui.calcularTamaniosCeldas(); planadqui.convertirMillones();" uib-tooltip="Quetzales" role="button" tabindex="1" aria-invalid="false">
									<span>Q</span></label>
								</div>
								<div class="btn-group" style="padding-left: 20px;">
									<label class="btn btn-default" ng-model="planadqui.agrupacionActual" uib-btn-radio="1" ng-click="planadqui.cambiarAgrupacion(1)" uib-tooltip="Mes" role="button" tabindex="1" aria-invalid="false">
									<span>M</span></label>
									<label class="btn btn-default" ng-model="planadqui.agrupacionActual" uib-btn-radio="2" ng-click="planadqui.cambiarAgrupacion(2)" uib-tooltip="Bimestre" role="button" tabindex="2" aria-invalid="false">
									<span>B</span></label>
									<label class="btn btn-default" ng-model="planadqui.agrupacionActual" uib-btn-radio="3" ng-click="planadqui.cambiarAgrupacion(3)" uib-tooltip="Trimestre" role="button" tabindex="3" aria-invalid="false">
									<span>T</span></label>
									<label class="btn btn-default" ng-model="planadqui.agrupacionActual" uib-btn-radio="4" ng-click="planadqui.cambiarAgrupacion(4)" uib-tooltip="Cuatrimestre" role="button" tabindex="4" aria-invalid="false">
									<span>C</span></label>
									<label class="btn btn-default" ng-model="planadqui.agrupacionActual" uib-btn-radio="5" ng-click="planadqui.cambiarAgrupacion(5)" uib-tooltip="Semestre" role="button" tabindex="5" aria-invalid="false">
									<span>S</span></label>
									<label class="btn btn-default" ng-model="planadqui.agrupacionActual" uib-btn-radio="6" ng-click="planadqui.cambiarAgrupacion(6)" uib-tooltip="Año" role="button" tabindex="6" aria-invalid="false">
									<span>A</span></label>
								</div>
								<div class="btn-group" style="padding-left: 20px;">
									<label class="btn btn-default" ng-click="planadqui.exportarExcel()" uib-tooltip="Exportar a Excel">
									<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
									<label class="btn btn-default" ng-click="planadqui.exportarPdf()" uib-tooltip="Exportar a PDF" ng-hide="true">
									<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
								</div>
							</div>							
						</div>
		    		</div>
	    		</div>	
	    		<br/>			
				<div class="row" style="height: 80%">
					<div ng-hide="!planadqui.mostrarCargando" style="width: 100%;">
	    				<div class="grid_loading" ng-hide="!planadqui.mostrarCargando">
							<div class="msg">
								<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
									<br />
									<br /> <b>Cargando, por favor espere...</b> 
								</span>
							</div>
						</div>
					</div>
					
					<div class="row" ng-hide="!planadqui.mostrarDescargar">
						<div class="divPadreNombres">
	    					<div class="divTabla"> 
	    						<table st-table="planadqui.displayedCollectionPrestamo" st-safe-src="planadqui.rowCollectionPrestamo" class="table table-striped tablaDatos">
		    						<thead class="theadDatos">
		    							<tr>
				    						<th style="min-width:300px;text-align: left;vertical-align: middle;" class="label-form" rowspan="2">Descripción de la Adquisición</th>
				    						<th style="border-bottom:2px solid #fff;" class="label-form">.</th>
				    					</tr>
				    					<tr>
					          				<th class="label-form">.</th>
					         			</tr>
		    						</thead>
		    						<tbody vs-repeat class="cuerpoTablaNombres" style="max-height: 315px; min-height: 315px" id="divTablaNombres" onmouseover="activarScroll(this.id)" onscroll="scrollEspejo(this)">
		    							<tr ng-repeat="row in planadqui.data" style="height: 35px; max-height: 35px; min-height: 35px">
		    								<td nowrap style="min-width:200px; {{planadqui.padre(row)}}">
				    							<div uib-tooltip="{{item.nombre}}" class="nombreFormat">
				    								<div uib-tooltip="{{row.nombre}}"><span ng-class="row.objetoTipo == 1 ? planadqui.claseIcon(row) : ''" style="margin-left: {{row.nivel}}em" uib-tooltip="{{planadqui.tooltipObjetoTipo[row.objetoTipo]}}"></span>{{row.nombre}}</div>
				    							</div>
				    						</td>
		    							</tr>
		    						</tbody>
		    					</table>
	    					</div>
	    				</div>
	    				<div class="divPadreDatos" style="max-width: {{planadqui.tamanoTotal}}px">
		    				<div class="divTabla">
				    			<table st-table="planadqui.displayedCollectionPrestamo" st-safe-src="planadqui.rowCollectionPrestamo" class="table table-striped tablaDatos" 
					    				style="max-width: {{planadqui.tamanoTotal}}px;">
									<thead id="divCabecerasDatos" class="theadDatos">
										<tr>
					         				<th colspan={{planadqui.colspan}} style="{{planadqui.estiloCabecera}}" ng-repeat="m in planadqui.objetoMostrar" class="label-form">{{m.nombreMes}}</th>
					          			</tr>
					          			<tr>
				          					<th ng-repeat="a in planadqui.aniosfinales" style="{{planadqui.estiloCelda}} {{planadqui.estiloAlineacion}}" class="label-form">{{a.anio}}</th>
							        	</tr>
									</thead>
									<tbody vs-repeat class="cuerpoTablaDatos" style="max-height: 300px; min-height: 300px" id="divTablaDatos" onmouseover="activarScroll(this.id)" onscroll="scrollEspejo(this)">
								      	<tr ng-repeat="item in planadqui.data" style="height: 35px; max-height: 35px; min-height: 35px">
								      		<td ng-repeat="posicion in planadqui.columnastotales track by $index" style="{{planadqui.estiloCelda}}; {{planadqui.estiloAlineacion}}">
								      			<div style="{{planadqui.estiloCelda}}">
												<span ng-show="planadqui.grupoMostrado.planificado" class="colorPlanificado">{{planadqui.getPlanificado(item,$index).planificado | formatoMillones : planadqui.enMillones}}</span>
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
										<th nowrap colspan={{planadqui.colspan}} style="{{planadqui.estiloCelda}} text-align: center;" class="label-form">Total</th>
				          				<th rowspan="2" style="{{planadqui.estiloCelda}} text-align: center; vertical-align: top;" class="label-form">Costo Est. de Adquisición</th>
				          			</tr>
				          			<tr>
			          					<th ng-repeat="a in planadqui.aniosTotal" style="{{planadqui.estiloCelda}} {{planadqui.estiloAlineacion}};" class="label-form">{{a.anio}}</th>
							        </tr>
								</thead>
								<tbody vs-repeat class="cuerpoTablaTotales bordeIzquierda" style="max-height: 300px; min-height: 300px" id="divTotales" style="margin-right: 30px" onmouseover="activarScroll(this.id)" onscroll="scrollEspejo(this)" tot="{{mi.totales.length}}">
									<tr ng-repeat="item in planadqui.data" style="height: 35px; max-height: 35px; min-height: 35px">
							      		<td ng-repeat="posicion in planadqui.aniosTotal track by $index" style="{{planadqui.estiloCelda}}; {{planadqui.estiloAlineacion}}">
							      			<div style="{{planadqui.estiloCelda}}">
											<span ng-show="planadqui.grupoMostrado.planificado" class="colorPlanificado">{{planadqui.getTotalPlanificado(item,$index).planificado | formatoMillones : planadqui.enMillones}}</span>
							      			</div>
							      		</td>
							      		<td style="{{planadqui.estiloCelda}} {{planadqui.estiloAlineacion}}">
							      			<div style="{{planadqui.estiloCelda}} text-align: center;">
							      				{{item.total == 0 ? null : item.total | formatoMillones : planadqui.enMillones}}
							      			</div>
							      		</td>
							      	</tr>
								</tbody>
							</table>
			    		</div>
					</div>
					<div class="row" ng-hide="!planadqui.mostrarDescargar">
						<div class="divPadreNombres">
	    					<div class="divTabla">
								<table class="table table-striped tablaDatos">
									<tbody class="cuerpoTablaNombresTot">
										<tr style="height: 35px; max-height: 35px; min-height: 35px">
											<td style="font-weight: bold; width: 200px; max-width: 200px; min-width: 200px;" align="right">
												Total Adquisiciones: 
											</td>									
										</tr>
									</tbody>									
								</table>
							</div>
						</div>
						<div class="divPadreDatos" style="max-width: {{planadqui.tamanoTotal}}px">
							<div class="divTabla">
								<table class="table table-striped tablaDatos" style="max-width: {{planadqui.tamanoTotal}}px;">
									<tbody class="cuerpoTablaDatos" id="divTablaDatosTot" onmouseover="activarScroll(this.id)" onscroll="scrollEspejo(this)">
										<tr>
											<td ng-repeat="posicion in planadqui.columnastotales track by $index" style="font-weight: bold; {{planadqui.estiloCelda}} min-height: 35px; height: 35px; {{planadqui.estiloAlineacion}}">
												<div style="{{planadqui.estiloCelda}}">
													{{planadqui.getTotales($parent.$index, $index) | formatoMillones : planadqui.enMillones}}
												</div>
											<td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="divTabla">
							<table class="table table-striped tablaDatos">
								<tbody class="cuerpoTablaTotal bordeIzquierda">
									<tr style="height: 35px; max-height: 35px; min-height: 35px">
							      		<td ng-repeat="posicion in planadqui.sumTotalesAnuales track by $index" style="font-weight: bold; {{planadqui.estiloCelda}}; {{planadqui.estiloAlineacion}}">
							      			<div style="{{planadqui.estiloCelda}}">
												{{planadqui.getTotalesAnuales($index) | formatoMillones : planadqui.enMillones}}
							      			</div>
							      		</td>
							      		<td style="font-weight: bold; {{planadqui.estiloCelda}} {{planadqui.estiloAlineacion}}">
							      			<div style="{{planadqui.estiloCelda}} text-align: center;">
							      				{{planadqui.totalGeneral | formatoMillones : planadqui.enMillones}}
							      			</div>
							      		</td>
							      	</tr>									
								</tbody>
							</table>
						</div>									
					</div>
					<br>
					<div class="row" ng-hide="!planadqui.mostrarDescargar">
						<div class="col-sm-3"></div>
						<div class="col-sm-6" style="text-align: center;">
							<label class="btn btn-default" ng-click="planadqui.anterior()" uib-tooltip="Anterior" ng-hide="!planadqui.movimiento" 
									tooltip-placement="bottom">
							<span class="glyphicon glyphicon-chevron-left"></span></label>
							<label class="btn btn-default" ng-click="planadqui.siguiente()" uib-tooltip="Siguiente" ng-hide="!planadqui.movimiento"
									tooltip-placement="bottom">
							<span class="glyphicon glyphicon-chevron-right"></span></label>
						</div>
						<div class="col-sm-3">
				    		<ol class="leyendaTexto"  ng-hide="!planadqui.mostrarDescargar">
								<li ng-show="planadqui.grupoMostrado.planificado"><span class="colorPlanificadoFondo"></span>Planificado</li>					        
							</ol>
						</div>
			    	</div>
				</div>
			</div>
		</div>
	</div>