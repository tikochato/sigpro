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
		  	width: 400px; 
			min-width: 400px; 
			max-width:400px; 
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
		    max-width: 415px;
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
	
<div ng-controller="prestamoindicadoresController as indicadoresc" class="maincontainer all_page" id="title">
	
	<shiro:lacksPermission name="30010">
		<p ng-init="indicadoresc.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>		
	<div class="col-sm-12" style="height: 100%;">
		<div style="width: 100%; height: 100%">
	    		<div class="row">
					<div class="panel panel-default">
						<div class="panel-heading"><h3>Avance de Indicadores</h3></div>
					</div>
				</div>
	    	<br>
	    	<div class="row">
	    		<div class="form-group col-sm-6" align="left">
					<div id="prestamo" angucomplete-alt placeholder="" pause="100" selected-object="indicadoresc.cambioPrestamo"
						  local-data="indicadoresc.lprestamos" search-fields="proyectoPrograma" title-field="proyectoPrograma" field-required="true" field-label="* Préstamo"
						  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
						  initial-value="indicadoresc.prestamoNombre" focus-out="indicadoresc.blurPrestamo()" input-name="prestamo"></div>
					<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
				</div>
	    	</div>
	    	<div class="row">	
					<div class="form-group col-sm-1">
						<input type="number"  class="inputText" ng-model="indicadoresc.fechaInicio" maxlength="4" 
						ng-value="indicadoresc.fechaInicio" onblur="this.setAttribute('value', this.value);"
						ng-change="indicadoresc.validar(2)"/>
					  	<label for="campo.id" class="floating-label" style="left: 0;">*Año Inicial</label>
					</div>
					
					<div class="form-group col-sm-1">
						<input type="number"  class="inputText" ng-model="indicadoresc.fechaFin" maxlength="4" 
						ng-value="indicadoresc.fechaFin" onblur="this.setAttribute('value', this.value);"
						ng-change="indicadoresc.validar(3)"/>
					  	<label for="campo.id" class="floating-label">*Año Final</label>
					</div>
					
					<div class="col-sm-10" align="right" ng-hide="!indicadoresc.mostrarDescargar">
						<div class="form-group col-sm-1">
						</div>
						<div class="col-sm-11">
							<div class="btn-group">
								<label class="btn btn-default" ng-model="indicadoresc.agrupacionActual" uib-btn-radio="1" ng-click="indicadoresc.cambiarAgrupacion(1)" uib-tooltip="Mes" role="button" tabindex="1" aria-invalid="false">
								<span>M</span></label>
								<label class="btn btn-default" ng-model="indicadoresc.agrupacionActual" uib-btn-radio="2" ng-click="indicadoresc.cambiarAgrupacion(2)" uib-tooltip="Bimestre" role="button" tabindex="2" aria-invalid="false">
								<span>B</span></label>
								<label class="btn btn-default" ng-model="indicadoresc.agrupacionActual" uib-btn-radio="3" ng-click="indicadoresc.cambiarAgrupacion(3)" uib-tooltip="Trimestre" role="button" tabindex="3" aria-invalid="false">
								<span>T</span></label>
								<label class="btn btn-default" ng-model="indicadoresc.agrupacionActual" uib-btn-radio="4" ng-click="indicadoresc.cambiarAgrupacion(4)" uib-tooltip="Cuatrimestre" role="button" tabindex="4" aria-invalid="false">
								<span>C</span></label>
								<label class="btn btn-default" ng-model="indicadoresc.agrupacionActual" uib-btn-radio="5" ng-click="indicadoresc.cambiarAgrupacion(5)" uib-tooltip="Semestre" role="button" tabindex="5" aria-invalid="false">
								<span>S</span></label>
								<label class="btn btn-default" ng-model="indicadoresc.agrupacionActual" uib-btn-radio="6" ng-click="indicadoresc.cambiarAgrupacion(6)" uib-tooltip="Año" role="button" tabindex="6" aria-invalid="false">
								<span>A</span></label>
							</div>
							<div class=" btn-group" style="padding-left: 20px;" ng-hide="true">
								<label class="btn btn-default" ng-model="indicadoresc.grupoMostrado.planificado"  uib-btn-checkbox ng-click="indicadoresc.verificaSeleccionTipo(1)" uib-tooltip="Planificado" role="button" tabindex="7" aria-invalid="false">
								<span>P</span></label>
								<label class="btn btn-default" ng-model="indicadoresc.grupoMostrado.real"  uib-btn-checkbox ng-click="indicadoresc.verificaSeleccionTipo(2)" uib-tooltip="Real" role="button" tabindex="8" aria-invalid="false">
								<span>R</span></label>
	    					</div>
							<div class="btn-group" style="padding-left: 20px;">
								<label class="btn btn-default" ng-click="indicadoresc.exportarExcel()" uib-tooltip="Exportar a Excel" ng-hide="!indicadoresc.mostrarDescargar">
								<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
								<label class="btn btn-default" ng-click="indicadoresc.exportarPdf()" uib-tooltip="Exportar a PDF" ng-hide="true">
								<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
							</div>
						</div>
					</div>
    			<br><br><br><br>
	    	</div>
	    	<div style="width: 100%; height: calc(70% - 32px)" id="reporte">
	    		
				<div class="grid_loading" ng-hide="!indicadoresc.mostrarCargando" style="height: calc(80% - 32px)">
						<div class="msg">
							<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
								<br />
								<br /> <b>Cargando, por favor espere...</b> 
							</span>
						</div>
					</div>		
		    	<div class="row" ng-hide="!indicadoresc.mostrarDescargar" style="height: 100%">
		    				
		    		<div class="divPadreNombres"  style="height: 100%">
			    		<div class="divTabla"  style="height: 100%"> 
			    			<table st-table="rowCollection" st-safe-src="datosTabla" class="table table-striped tablaDatos"  style="height: 100%">
								<thead class="theadDatos">
									<tr>
				          				<th rowspan="2" st-sort="nombre" style="text-align: center; vertical-align: top; min-width:300px;" class="label-form">Nombre</th>
				          				<th style="text-align: center; vertical-align: top; min-width:100px; border-bottom:2px solid #fff;" class="label-form">Unidad</th>
				         			</tr>
				         			<tr>
				          				<th style="text-align: center; vertical-align: top; min-width:100px;" class="label-form">de Medida</th>
				         			</tr>
								</thead>
								<tbody class="cuerpoTablaNombres" id="divTablaNombres" onmouseover="activarScroll(this.id)" onscroll="scrollEspejo(this)">
									<tr ng-repeat="item in indicadoresc.data">
							      		<td nowrap style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">
							      			<p class="nombreFormat">
							      				<span ng-class="indicadoresc.iconoObjetoTipo[item.objeto_tipo]" uib-tooltip="{{indicadoresc.tooltipObjetoTipo[item.objeto_tipo]}}" style="margin-left: {{item.nivel-1}}em"></span>
							      				<span uib-tooltip="{{item.nombre}}">{{item.nombre}}</span>
							      			</p>
							      		</td>
							      		<td nowrap style="min-width:100px; min-height: 35px; height: 35px;">
							      			<p class="nombreFormat" uib-tooltip="{{indicadoresc.nombreUnidadMedida(item.unidadDeMedida)}}">{{indicadoresc.nombreUnidadMedida(item.unidadDeMedida)}}</p>
							      		</td>
							      	</tr>
								</tbody>
							</table>
		    			</div>
	    			</div>
		    		<div class="divPadreDatos" style="max-width: {{indicadoresc.tamanoTotal}}px; height: 100%">
	    				<div class="divTabla"  style="height: 100%">
			    			<table st-table="rowCollection" st-safe-src="datosTabla" class="table table-striped tablaDatos" 
				    				style="max-width: {{indicadoresc.tamanoTotal}}px; height:100%;" >
								<thead id="divCabecerasDatos" class="theadDatos">
									<tr>
				         				<th colspan={{indicadoresc.colspan}} style="{{indicadoresc.estiloCabecera}}" ng-repeat="m in indicadoresc.objetoMostrar" class="label-form">{{m.nombreMes}}</th>
				          			</tr>
				          			<tr>
				          				<th ng-repeat="a in indicadoresc.aniosfinales" style="{{indicadoresc.estiloCelda}} {{indicadoresc.estiloAlineacion}}" class="label-form">{{a.anio}}</th>
							        </tr>
								</thead>
								<tbody class="cuerpoTablaDatos" id="divTablaDatos" onmouseover="activarScroll(this.id)" onscroll="scrollEspejo(this)">
							      	<tr ng-repeat="item in indicadoresc.data" style="">
								      		<td ng-repeat="posicion in indicadoresc.columnastotales track by $index" style="{{indicadoresc.estiloCelda}} min-height: 35px; height: 35px; {{indicadoresc.estiloAlineacion}}">
								      			<div style="{{indicadoresc.porcentajeCeldaValor}}">
												<span ng-show="indicadoresc.grupoMostrado.planificado" class="colorPlanificado">{{indicadoresc.getPlanificado($parent.$index,$index, indicadoresc.VALOR_PLANIFICADO)}}</span>
								      			</div>
								      			<div style="{{indicadoresc.porcentajeCeldaPipe}}">
												<span ng-show="indicadoresc.grupoMostrado.planificado && indicadoresc.grupoMostrado.real && indicadoresc.getPlanificado($parent.$index,$index, indicadoresc.VALOR_PLANIFICADO)!=null && indicadoresc.getPlanificado($parent.$index,$index, indicadoresc.VALOR_REAL)!=null" > | </span>
								      			</div>
								      			<div style="{{indicadoresc.porcentajeCeldaValor}}">
												<span ng-show="indicadoresc.grupoMostrado.real" class="colorReal">{{indicadoresc.getPlanificado($parent.$index,$index, indicadoresc.VALOR_REAL)}}</span>
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
			          					<th nowrap colspan={{indicadoresc.colspan}} style="{{indicadoresc.estiloCelda}} text-align: center;" class="label-form"><span ng-show="indicadoresc.grupoMostrado.planificado && indicadoresc.grupoMostrado.real">Total Anual</span><span ng-hide="indicadoresc.grupoMostrado.planificado && indicadoresc.grupoMostrado.real">Anual</span></th>
				          				<th rowspan="2" style="{{indicadoresc.estiloCelda}} text-align: center; vertical-align: top;" class="label-form">Total</th>
				          				<th rowspan="2" style="{{indicadoresc.estiloCelda}} text-align: center; vertical-align: top;" class="label-form">Meta Final</th>
				          				<th rowspan="2" style="{{indicadoresc.estiloCelda}} text-align: center; vertical-align: top;" class="label-form">% Avance Final</th>
				          			</tr>
				          			<tr>
			          					<th ng-repeat="a in indicadoresc.aniosTotal" style="{{indicadoresc.estiloCelda}} {{indicadoresc.estiloAlineacion}};" class="label-form">{{a.anio}}</th>
							        </tr>
								</thead>
							<tbody class="cuerpoTablaTotales bordeIzquierda" id="divTotales" onmouseover="activarScroll(this.id)" onscroll="scrollEspejo(this)" tot="{{mi.totales.length}}">
									<tr ng-repeat="totales in indicadoresc.totales track by $index" style="min-height:35px; height:35px;">
										<td ng-repeat="total in totales.anio" style="{{indicadoresc.estiloCelda}} {{indicadoresc.estiloAlineacion}}">
											<div style="{{indicadoresc.porcentajeCeldaValor}}">
											<span ng-show="indicadoresc.grupoMostrado.planificado" class="colorPlanificado">{{total.valor.planificado}}</span>
											</div>
							      			<div style="{{indicadoresc.porcentajeCeldaPipe}}">
							      			<span ng-show="indicadoresc.grupoMostrado.planificado && indicadoresc.grupoMostrado.real && total.valor.planificado!=null && total.valor.real!=null"> | </span>
							      			</div>
							      			<div style="{{indicadoresc.porcentajeCeldaValor}}">
							      			<span ng-show="indicadoresc.grupoMostrado.real" class="colorReal">{{total.valor.real}}</span>
							      			</div>
										</td>
										<td style="{{indicadoresc.estiloCelda}} {{indicadoresc.estiloAlineacion}}"><span>{{indicadoresc.data[$index].metaFinal}}</span></td>
										<td style="{{indicadoresc.estiloCelda}} {{indicadoresc.estiloAlineacion}}"><span>{{indicadoresc.data[$index].porcentajeAvance}}{{indicadoresc.data[$index].porcentajeAvance!=null? '%':''}}</span></td>
							      	</tr>
								</tbody>
							</table>
			    		</div>
				</div>
	    	</div>
	    	<br>
	    	<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-6" style="text-align: center;">
						<label class="btn btn-default" ng-click="indicadoresc.anterior()" uib-tooltip="Anterior" ng-hide="!indicadoresc.movimiento" 
								tooltip-placement="bottom">
						<span class="glyphicon glyphicon-chevron-left"></span></label>
						<label class="btn btn-default" ng-click="indicadoresc.siguiente()" uib-tooltip="Siguiente" ng-hide="!indicadoresc.movimiento"
								tooltip-placement="bottom">
						<span class="glyphicon glyphicon-chevron-right"></span></label>
					</div>
					<div class="col-sm-3">
			    		<ol class="leyendaTexto"  ng-hide="!indicadoresc.mostrarDescargar">
							<li ng-show="indicadoresc.grupoMostrado.planificado"><span class="colorPlanificadoFondo"></span>Planificado</li>
					        <li ng-show="indicadoresc.grupoMostrado.real"><span class="colorRealFondo"></span>Real</li>
						</ol>
					</div>
	    	</div>
   		</div>
	</div>
</div>
