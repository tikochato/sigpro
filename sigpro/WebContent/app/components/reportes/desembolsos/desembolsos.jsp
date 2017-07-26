<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<style>
	.label-form1{
		 font-size: 13px;
		 opacity: 1;
		 pointer-events: none;
		 color: rgba(0,0,0,0.38) !important;
		 font-weight: bold;
	}
	 table.borderless td,table.borderless th{
     	border: none !important;
	}
	.planificado {
		background-color: #d9edf7 !important;
	}
	
	.real {
		background-color: #ffd7c4 !important;
	}
	
</style>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="desembolsosController as desembolsosc" class="maincontainer all_page" id="title">
	
	
  	    <shiro:lacksPermission name="30010">
			<p ng-init="desembolsosc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
			<div class="panel-heading"><h3>Desembolsos</h3></div>
		</div>
		<div class="subtitulo">
			{{ desembolsosc.objetoTipoNombre }} {{ desembolsosc.proyectoNombre }}
		</div>
		
		<div class="row" align="center" >
			<div class="col-sm-12 ">
			
			<form name="form">
				<div class="row">
					<div class="form-group col-sm-5">
						<select  class="inputText" ng-model="desembolsosc.prestamoSeleccionado"
							ng-options="a.text for a in desembolsosc.prestamos"
							ng-readonly="true"
							ng-required="true">
							<option value="">Seleccione una opción</option>
							</select>
						<label for="prestamo" class="floating-label">Préstamos</label>
					</div>
					
					<div class="col-sm-5" ng-if="false">
						<div class="form-group" >
						  <input type="text"  class="inputText" uib-datepicker-popup="{{desembolsosc.formatofecha}}" ng-model="desembolsosc.ejercicioFiscal" is-open="desembolsosc.ef_abierto"
						            datepicker-options="desembolsosc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"  ng-required="true"  ng-click="desembolsosc.abrirPopupFecha(1)"
						             date-disabled="disabled(date, mode)"
						            ng-value="desembolsosc.ejercicioFiscal" onblur="this.setAttribute('value', this.value);"/>
						            <span class="label-icon" ng-click="desembolsosc.abrirPopupFecha(1)">
						              <i class="glyphicon glyphicon-calendar"></i>
						            </span>
						  <label  class="floating-label">Ejercicio fiscal</label>
						</div>
					</div>
					<div class="form-group col-sm-1" >
						<label class="btn btn-default" ng-click="form.$valid ? desembolsosc.generarReporte() : ''" uib-tooltip="Generar" 
							tooltip-placement="bottom"
							 ng-disabled="!form.$valid"
							>
						<span class="glyphicon glyphicon-new-window"></span></label>
					</div>
				</div>
			</form>
			<br/> 
			
			<div class="row" ng-hide="!desembolsosc.mostrar" >
			<div class="form-group col-sm-2">
						<select class="inputText" ng-model="desembolsosc.agrupacion"
							ng-options="a.id as a.nombre for a in desembolsosc.agrupaciones "
							ng-readonly="true"
							ng-change = "desembolsosc.asignarSerie()"
							>
						</select>
					    <label for="nombre" class="floating-label">* Agrupación</label>
				</div>
				<div class="form-group col-sm-2">
						<select class="inputText" ng-model="desembolsosc.anioSeleccionado"
							ng-options="a.id as a.nombre for a in desembolsosc.anios "
							ng-readonly="true"
							ng-change = "desembolsosc.asignarSerie()"
							>
						</select>
					    <label for="nombre" class="floating-label">* Año</label>
				</div>
			</div>
			<br/>
			<div style="width: 70%">
				<canvas id="line" class="chart chart-line" chart-data="desembolsosc.desembolsos" ng-hide="!desembolsosc.mostrar"
				chart-labels="desembolsosc.etiqutas" chart-series="desembolsosc.series" chart-options="desembolsosc.options"
				chart-dataset-override="desembolsosc.datasetOverride" 
				 chart-colors = "desembolsosc.radarColors" chart-legend="true">
				</canvas>
			</div>
			<br/><br/>
			<table st-table="desembolsosc.desembolsos" class="table table-striped table-hover table-condensed"
				ng-hide="!desembolsosc.mostrar">
				<thead>
				<tr >
					<th ng-repeat="n in desembolsosc.columnas track by $index" style="text-align: center">
					{{n}}
					</th>
				</tr>
				</thead>
				<tbody>
				<tr class = "{{desembolsosc.clase($index)}}" ng-repeat="row in desembolsosc.tabla track by $index" style="text-align: right;">
					<td ng-repeat = "col in row track by $index"
					 	 style="font-weight: bold;">{{desembolsosc.formato1(col)}}
					 </td>
				</tr>
				</tbody>
			</table>
			 
				
		</div>
		  
	</div>
</div>
