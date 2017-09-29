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

<div ng-controller="prestamometasController as controller" class="maincontainer all_page" id="title">
	
	<shiro:lacksPermission name="30010">
		<p ng-init="controller.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>		
	<div class="col-sm-12" style="height: 100%;">
		<div style="width: 100%; height: 100%">
	    		<div class="row">
					<div class="panel panel-default">
						<div class="panel-heading"><h3>Metas de Préstamo</h3></div>
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
						ng-change="controller.validar(2)"/>
					  	<label for="campo.id" class="floating-label">*Año Inicial</label>
					</div>
					
					<div class="form-group col-sm-1">
						<input type="number"  class="inputText" ng-model="controller.fechaFin" maxlength="4" 
						ng-value="controller.fechaFin" onblur="this.setAttribute('value', this.value);"
						ng-change="controller.validar(3)"/>
					  	<label for="campo.id" class="floating-label">*Año Final</label>
					</div>
					
					<div class="col-sm-7" align="right" ng-hide="!controller.mostrarDescargar">
						<div class="form-group col-sm-1">
						</div>
						<div class="col-sm-11">
							<div class="btn-group">
								<label class="btn btn-default" ng-model="controller.agrupacionActual" uib-btn-radio="1" ng-click="controller.cambiarAgrupacion(1)" uib-tooltip="Mensual" role="button" tabindex="1" aria-invalid="false">
								<span>M</span></label>
								<label class="btn btn-default" ng-model="controller.agrupacionActual" uib-btn-radio="2" ng-click="controller.cambiarAgrupacion(2)" uib-tooltip="Bimestre" role="button" tabindex="2" aria-invalid="false">
								<span>B</span></label>
								<label class="btn btn-default" ng-model="controller.agrupacionActual" uib-btn-radio="3" ng-click="controller.cambiarAgrupacion(3)" uib-tooltip="Trimestre" role="button" tabindex="3" aria-invalid="false">
								<span>T</span></label>
								<label class="btn btn-default" ng-model="controller.agrupacionActual" uib-btn-radio="4" ng-click="controller.cambiarAgrupacion(4)" uib-tooltip="Cuatrimestre" role="button" tabindex="4" aria-invalid="false">
								<span>C</span></label>
								<label class="btn btn-default" ng-model="controller.agrupacionActual" uib-btn-radio="5" ng-click="controller.cambiarAgrupacion(5)" uib-tooltip="Semestre" role="button" tabindex="5" aria-invalid="false">
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
								<label class="btn btn-default" ng-click="controller.exportarExcel()" uib-tooltip="Exportar Excel" ng-hide="!controller.mostrarDescargar">
								<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
								<label class="btn btn-default" ng-click="controller.exportarPdf()" uib-tooltip="Exportar PDF" ng-hide="!controller.mostrarDescargar">
								<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
							</div>
						</div>
					</div>
    			<br><br><br><br>
	    	</div>
	    	<div style="width: 100%; height: calc(70% - 32px)" id="reporte">
	    		
				<div class="grid_loading" ng-hide="!controller.mostrarCargando" style="height: calc(80% - 32px)">
						<div class="msg">
							<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
								<br />
								<br /> <b>Cargando, por favor espere...</b> 
							</span>
						</div>
					</div>		
		    	<div class="row" ng-hide="!controller.mostrarDescargar" style="height: 100%">
		    				
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
								<tbody class="cuerpoTablaNombres" id="divTablaNombres" ng-mouseover="controller.activarScroll('divTablaNombres')" scrollespejo>
									<tr ng-repeat="item in controller.data">
							      		<td nowrap style="min-width:300px; max-width:300px; min-height: 35px; height: 35px; overflow:hidden;">
							      			<p class="nombreFormat">
							      				<span ng-class="controller.iconoObjetoTipo[item.objeto_tipo]" uib-tooltip="{{controller.tooltipObjetoTipo[item.objeto_tipo]}}" style="margin-left: {{item.nivel-1}}em"></span>
							      				<span uib-tooltip="{{item.nombre}}">{{item.nombre}}</span>
							      			</p>
							      		</td>
							      		<td nowrap style="min-width:100px; min-height: 35px; height: 35px;">
							      			<p class="nombreFormat" uib-tooltip="{{controller.nombreUnidadMedida(item.unidadDeMedida)}}">{{controller.nombreUnidadMedida(item.unidadDeMedida)}}</p>
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
								<thead id="divCabecerasDatos" class="theadDatos">
									<tr>
				         				<th colspan={{controller.colspan}} style="{{controller.estiloCabecera}}" ng-repeat="m in controller.objetoMostrar" class="label-form">{{m.nombreMes}}</th>
				          			</tr>
				          			<tr>
				          				<th ng-repeat="a in controller.aniosfinales" style="{{controller.estiloCelda}} {{controller.estiloAlineacion}}" class="label-form">{{a.anio}}</th>
							        </tr>
								</thead>
								<tbody class="cuerpoTablaDatos" id="divTablaDatos" ng-mouseover="controller.activarScroll('divTablaDatos')" scrollespejo>
							      	<tr ng-repeat="item in controller.data" style="">
								      		<td ng-repeat="posicion in controller.columnastotales track by $index" style="{{controller.estiloCelda}} min-height: 35px; height: 35px; {{controller.estiloAlineacion}}">
								      			<div style="{{controller.porcentajeCeldaValor}}">
												<span ng-show="controller.grupoMostrado.planificado" class="colorPlanificado">{{controller.getPlanificado($parent.$index,$index, controller.VALOR_PLANIFICADO)}}</span>
								      			</div>
								      			<div style="{{controller.porcentajeCeldaPipe}}">
												<span ng-show="controller.grupoMostrado.planificado && controller.grupoMostrado.real && controller.getPlanificado($parent.$index,$index, controller.VALOR_PLANIFICADO)!=null && controller.getPlanificado($parent.$index,$index, controller.VALOR_REAL)!=null" > | </span>
								      			</div>
								      			<div style="{{controller.porcentajeCeldaValor}}">
												<span ng-show="controller.grupoMostrado.real" class="colorReal">{{controller.getPlanificado($parent.$index,$index, controller.VALOR_REAL)}}</span>
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
				          				<th rowspan="2" style="{{controller.estiloCelda}} text-align: center; vertical-align: top;" class="label-form">Meta Final</th>
				          				<th rowspan="2" style="{{controller.estiloCelda}} text-align: center; vertical-align: top;" class="label-form">% Avance</th>
				          			</tr>
				          			<tr>
			          					<th ng-repeat="a in controller.aniosTotal" style="{{controller.estiloCelda}} {{controller.estiloAlineacion}};" class="label-form">{{a.anio}}</th>
							        </tr>
								</thead>
							<tbody class="cuerpoTablaTotales bordeIzquierda" id="divTotales" ng-mouseover="controller.activarScroll('divTotales')" scrollespejo tot="{{mi.totales.length}}">
									<tr ng-repeat="totales in controller.totales track by $index" style="min-height:35px; height:35px;">
										<td ng-repeat="total in totales.anio" style="{{controller.estiloCelda}} {{controller.estiloAlineacion}}">
											<div style="{{controller.porcentajeCeldaValor}}">
											<span ng-show="controller.grupoMostrado.planificado" class="colorPlanificado">{{total.valor.planificado}}</span>
											</div>
							      			<div style="{{controller.porcentajeCeldaPipe}}">
							      			<span ng-show="controller.grupoMostrado.planificado && controller.grupoMostrado.real && total.valor.planificado!=null && total.valor.real!=null"> | </span>
							      			</div>
							      			<div style="{{controller.porcentajeCeldaValor}}">
							      			<span ng-show="controller.grupoMostrado.real" class="colorReal">{{total.valor.real}}</span>
							      			</div>
										</td>
										<td style="{{controller.estiloCelda}} {{controller.estiloAlineacion}}"><span>{{controller.data[$index].metaFinal}}</span></td>
										<td style="{{controller.estiloCelda}} {{controller.estiloAlineacion}}"><span>{{controller.data[$index].porcentajeAvance}}{{controller.data[$index].porcentajeAvance!=null? '%':''}}</span></td>
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
