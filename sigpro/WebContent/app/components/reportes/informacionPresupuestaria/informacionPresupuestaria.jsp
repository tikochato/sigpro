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
		    overflow-x: hidden;
		    display: inline-block;
		}
		
		.cuerpoTablaDatos {
		    overflow-y: scroll;
		    overflow-x: hidden;
		    display: inline-block;
		}
		
		table {
			display: flex;
		    flex-direction: column;
		    align-items: stretch;
		    height: 300px; /* this can vary */
		    max-width: 300px; 
		}
		
		thead {
			flex-shrink: 0; overflow-x: hidden;
		}
		
	</style>

<div ng-controller="adquisicionesController as controller" class="maincontainer all_page" id="title">
	
    <shiro:lacksPermission name="24010">
			<p ng-init="controller.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
    <div class="row">
	    <div class="col-sm-12">
	    	<div style="width: 100%; height: 20%">
	    		<div class="row">
		    		<div class="panel panel-default">
		  				<div class="panel-heading"><h3>Ejecución presupuestaria</h3></div>
					</div>
				</div>
    			<br>
	    		<div class="row">
					<div class="form-group col-sm-3">
						<select  class="inputText" ng-model="controller.prestamo"
							ng-options="a.text for a in controller.prestamos"
							ng-change="controller.validar()"></select>
						<label for="prestamo" class="floating-label">Préstamos</label>
					</div>
					
					<div class="form-group col-sm-1">
						<input type="number"  class="inputText" ng-model="controller.fechaInicio" maxlength="4" 
						ng-value="controller.fechaInicio" onblur="this.setAttribute('value', this.value);"
						ng-change="controller.validar()"/>
					  	<label for="campo.id" class="floating-label">*Año Inicial</label>
					</div>
					
					<div class="form-group col-sm-1">
						<input type="number"  class="inputText" ng-model="controller.fechaFin" maxlength="4" 
						ng-value="controller.fechaFin" onblur="this.setAttribute('value', this.value);"
						ng-change="controller.validar()"/>
					  	<label for="campo.id" class="floating-label">*Año Final</label>
					</div>
				</div>
    			<br>
	    	</div>
	    	<div style="width: 100%; height: 80%" id="reporte">
	    		<div class="operation_buttons" style="margin-left: -15px;" ng-hide="!controller.mostrarDescargar">
					<div class="btn-group" >
						<label class="btn btn-default"  ng-click="controller.generar(1)" uib-tooltip="Mensual">
						<span class="glyphicon glyphicon-calendar"></span></label>
						<label class="btn btn-default"  ng-click="controller.generar(2)" uib-tooltip="Bimestre">
						<span class="glyphicon glyphicon-calendar"></span></label>
						<label class="btn btn-default"  ng-click="controller.generar(3)" uib-tooltip="Trimestre">
						<span class="glyphicon glyphicon-calendar"></span></label>
						<label class="btn btn-default"  ng-click="controller.generar(4)" uib-tooltip="Cuatrimestre">
						<span class="glyphicon glyphicon-calendar"></span></label>
						<label class="btn btn-default"  ng-click="controller.generar(5)" uib-tooltip="Semestre">
						<span class="glyphicon glyphicon-calendar"></span></label>
						<label class="btn btn-default"  ng-click="controller.generar(6)" uib-tooltip="Anual">
						<span class="glyphicon glyphicon-calendar"></span></label>
					</div>
					<div class="btn-group" style="float: right;">
						<label class="btn btn-default"  ng-click="controller.exportarExcel()" uib-tooltip="Exportar" ng-hide="!controller.mostrarDescargar">
						<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
					</div>
				</div>
				<div class="grid_loading" ng-hide="!controller.mostrarCargando">
						<div class="msg">
							<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
								<br />
								<br /> <b>Cargando, por favor espere...</b> 
							</span>
						</div>
					</div>		
		    	<div class="row" ng-hide="!controller.mostrarDescargar">
		    				
		    		<div class="divPadreNombres">
			    		<div class="divTabla"> 
			    			<table st-table="controller.displayedCollectionPrestamo" st-safe-src="controller.rowCollectionPrestamo" class="table table-striped">
								<thead>
									<tr>
				          				<th style="height: 75px; text-align: center; vertical-align: inherit; min-width:200px;">Nombre</th>
				         			</tr>
								</thead>
								<tbody class="cuerpoTablaNombres" id="divTablaNombres" ng-mouseover="controller.activarScroll('divTablaNombres')" scrollespejo>
									<tr ng-repeat="row in rowCollection">
							      		<td style="min-width:200px;">{{row[1]}}</td>
							      	</tr>
								</tbody>
							</table>
		    			</div>
	    			</div>
		    		<div class="divPadreDatos" style="max-width: {{controller.tamanoTotal}}px">
	    				<div class="divTabla">
			    			<table st-table="controller.displayedCollectionPrestamo" st-safe-src="controller.rowCollectionPrestamo" class="table table-striped" ng-style="controller.margenTabla"
				    				style="max-width: {{controller.tamanoTotal}}px">
								<thead id="divCabecerasDatos" >
									<tr>
				         				<th colspan={{controller.colspan}} style="{{controller.estiloCabecera}}" ng-repeat="m in controller.objetoMostrar">{{m.nombreMes}}</th>
				          			</tr>
				          			<tr>
				          				<th ng-repeat="a in controller.aniosfinales" style="{{controller.estiloCelda}} text-align: center;">{{a.ano}}</th>
							        </tr>
								</thead>
								<tbody class="cuerpoTablaDatos" id="divTablaDatos" ng-mouseover="controller.activarScroll('divTablaDatos')" scrollespejo>
							      	<tr ng-repeat="row in rowCollection">
							      		<td ng-repeat="col in columns" style="{{controller.estiloCelda}}">{{row[col]}}</td>
							      	</tr>
								</tbody>
							</table>
						</div>
						</div>
			    		<div class="divTabla">
			    			<table st-table="controller.displayedCollectionPrestamo" st-safe-src="controller.rowCollectionPrestamo" class="table table-striped">
								<thead>
									<tr>
				          				<th colspan={{controller.colspan}} style="{{controller.estiloCelda}} text-align: center;">Total anual</th>
				          				<th rowspan="2" style="{{controller.estiloCelda}} text-align: center; vertical-align: inherit;" >Total</th>
				          			</tr>
				          			<tr>
				          				<th ng-repeat="a in controller.aniosTotal" style="{{controller.estiloCelda}} text-align: center;">{{a.ano}}</th>
							        </tr>
								</thead>
								<tbody class="cuerpoTablaNombres" id="divTotales" ng-mouseover="controller.activarScroll('divTotales')" scrollespejo>
									<tr ng-repeat="row in rowCollection">
							      		<td style="{{controller.estiloCelda}}">{{row[1]}}</td>
							      		<td style="{{controller.estiloCelda}}">{{row[2]}}</td>
							      		<td style="{{controller.estiloCelda}}">{{row[3]}}</td>
							      		<td style="{{controller.estiloCelda}}">{{row[4]}}</td>
							      	</tr>
								</tbody>
							</table>
			    		</div>
				</div>
	    	</div>
	    	<div style="text-align: center;">
	    	<br>
					<label class="btn btn-default" ng-click="controller.atras()" uib-tooltip="Atrás" ng-hide="!controller.movimiento" 
							tooltip-placement="bottom" ng-disabled="!controller.AtrasActivo">
					<span class="glyphicon glyphicon-chevron-left"></span></label>
					<label class="btn btn-default" ng-click="controller.siguiente()" uib-tooltip="Siguiente" ng-hide="!controller.movimiento"
							tooltip-placement="bottom" ng-disabled="!controller.SiguienteActivo">
					<span class="glyphicon glyphicon-chevron-right"></span></label>
	    	</div>
    	</div>
    </div>
</div>