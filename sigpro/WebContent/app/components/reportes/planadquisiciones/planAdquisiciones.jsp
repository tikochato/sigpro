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

	<div ng-controller="planAdquisicionesController as controller" class="maincontainer all_page" id="title" style="height: 100%">
		<shiro:lacksPermission name="24010">
			<p ng-init="controller.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="row" id="reporte" style="height: 100%" align="center">
			<div class="col-sm-12">
				<div class="row" style="height: 20%">
					<div class="row" align="left">
		    			<div class="panel panel-default">
			  				<div class="panel-heading"><h3>Plan de adquisiciones AÑO FISCAL {{controller.anio}}</h3></div>
						</div>
					</div>
					<br>
					<div class="row" style="width: 100%; height: 15%">
						<div class="row">
							<div class="form-group col-sm-3" align="left">
								<select  class="inputText" ng-model="controller.prestamo"
									ng-options="a.text for a in controller.prestamos" 
									ng-change="controller.validar(1)"></select>		
		    				</div>
		    				
		    				<div align="left" class="form-group col-sm-1">
								<input type="number"  class="inputText" ng-model="controller.fechaInicio" maxlength="4" 
								ng-value="controller.fechaInicio" onblur="this.setAttribute('value', this.value);"
								ng-change="controller.validar(2)"/>
							  	<label for="campo.id" class="floating-label">*Año Inicial</label>
							</div>
					
							<div align="left" class="form-group col-sm-1">
								<input type="number"  class="inputText" ng-model="controller.fechaFin" maxlength="4" 
								ng-value="controller.fechaFin" onblur="this.setAttribute('value', this.value);"
								ng-change="controller.validar(3)"/>
							  	<label for="campo.id" class="floating-label">*Año Final</label>
							</div>
						</div>
						<br/>
		    			<div class="row">
		    				<div class="col-sm-12" align="right" ng-hide="!controller.mostrarDescargar">
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
									<label class="btn btn-default" ng-click="controller.exportarExcel()" uib-tooltip="Exportar a Excel">
									<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
									<label class="btn btn-default" ng-click="controller.exportarPdf()" uib-tooltip="Exportar a PDF" ng-hide="!controller.mostrarDescargar">
									<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
								</div>								
							</div>
		    			</div>	
		    		</div>
	    		</div>				
				<div class="row" style="height: 80%">
					<div ng-hide="!controller.mostrarCargando" style="width: 100%; height: 95%">
	    				<div class="grid_loading" ng-hide="!controller.mostrarCargando">
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
	    						<table st-table="controller.displayedCollectionPrestamo" st-safe-src="controller.rowCollectionPrestamo" class="table table-striped tablaDatos">
		    						<thead class="theadDatos">
		    							<tr>
				    						<th style="min-width:300px;text-align: left;vertical-align: middle;" class="label-form" rowspan="2">Descripción de la Adquisición</th>
				    						<th style="border-bottom:2px solid #fff;" class="label-form">.</th>
				    					</tr>
		    						</thead>
		    						<tbody class="cuerpoTablaNombres" style="height: 350px; max-height: 350px; min-height: 350px" id="divTablaNombres" ng-mouseover="controller.activarScroll('divTablaNombres')" scrollespejo>
		    							<tr ng-repeat="row in controller.data" style="height: 35px; max-height: 35px; min-height: 35px">
		    								<td nowrap style="min-width:200px;">
				    							<div uib-tooltip="{{item.nombre}}" class="nombreFormat">
				    								<div uib-tooltip="{{row.nombre}}"><span ng-class="row.objetoTipo == 2 ? controller.claseIcon(row) : ''" style="margin-left: {{row.nivel}}em" uib-tooltip="{{controller.tooltipObjetoTipo[row.objetoTipo-1]}}"></span>{{row.nombre}}</div>
				    							</div>
				    						</td>
		    							</tr>
		    						</tbody>
		    					</table>
	    					</div>
	    				</div>
	    				<div class="divPadreDatos" style="max-width: {{controller.tamanoTotal}}px">
		    				<div class="divTabla">
				    			<table st-table="controller.displayedCollectionPrestamo" st-safe-src="controller.rowCollectionPrestamo" class="table table-striped tablaDatos" 
					    				style="max-width: {{controller.tamanoTotal}}px;">
									<thead id="divCabecerasDatos" class="theadDatos">
										<tr>
					         				<th colspan={{controller.colspan}} style="{{controller.estiloCabecera}}" ng-repeat="m in controller.objetoMostrar" class="label-form">{{m.nombreMes}}</th>
					          			</tr>
									</thead>
									<tbody class="cuerpoTablaDatos" style="height: 350px; max-height: 350px; min-height: 350px" id="divTablaDatos" ng-mouseover="controller.activarScroll('divTablaDatos')" scrollespejo>
								      	<tr ng-repeat="item in controller.data" style="height: 35px; max-height: 35px; min-height: 35px">
								      		<td ng-repeat="posicion in controller.columnastotales track by $index" style="{{controller.estiloCelda}}; {{controller.estiloAlineacion}}">
								      			<div style="{{controller.estiloCelda}}">
												<span ng-show="controller.grupoMostrado.planificado" class="colorPlanificado">{{controller.getPlanificado($parent.$index,$index).planificado | formatoMillones : controller.enMillones}}</span>
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
				          				<th nowrap colspan={{controller.colspan}} style="min-width:200px; text-align: center" class="label-form">Costo Estimado de Adquisición</th>
				          			</tr>				          			
								</thead>
								<tbody class="cuerpoTablaTotales bordeIzquierda" style="height: 350px; max-height: 350px; min-height: 350px" id="divTotales" style="margin-right: 30px" ng-mouseover="controller.activarScroll('divTotales')" scrollespejo tot="{{mi.totales.length}}">
									<tr ng-repeat="totales in controller.totales" style="height: 35px; max-height: 35px; min-height: 35px">
										<td ng-repeat="total in totales.anio" style="min-width:200px; text-align: left">
											<div style="min-width:150px;">
												<span ng-show="controller.grupoMostrado.planificado" class="colorPlanificado">{{total.valor.planificado | formatoMillones : controller.enMillones}}</span>
							      			</div>
										</td>
							      	</tr>
								</tbody>
							</table>
			    		</div>
					</div>
					<div class="row" ng-hide="!controller.mostrarDescargar">
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
						<div class="divPadreDatos" style="max-width: {{controller.tamanoTotal}}px">
							<div class="divTabla">
								<table class="table table-striped tablaDatos" style="max-width: {{controller.tamanoTotal + 15}}px;">
									<tbody class="cuerpoTablaTotal" id="divTablaDatosTot" ng-mouseover="controller.activarScroll('divTablaDatosTot')" scrollespejo>
										<tr>
											<td ng-repeat="posicion in controller.sumTotales track by $index" style="font-weight: bold; {{controller.estiloCelda}} min-height: 35px; height: 35px; {{controller.estiloAlineacion}}">
												{{posicion | formatoMillones : controller.enMillones}}
											<td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="divTabla" style="margin-left: -15px">
							<table class="table table-striped tablaDatos">
								<tbody class="cuerpoTablaTotal bordeIzquierda">
									<tr>
										<td style="font-weight: bold; min-width:215px; text-align: left; height: 35px; max-height: 35px; min-height: 35px">											
												{{controller.totalAdquisicion | formatoMillones : controller.enMillones}}
										</td>
									</tr>
								</tbody>
							</table>
						</div>									
					</div>
					<br>
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
							</ol>
						</div>
			    	</div>
				</div>
			</div>
		</div>
	</div>