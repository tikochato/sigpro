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
					
					<div class="col-sm-5" >
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
				
			
			<div class="panel panel-default" ng-hide="!desembolsosc.mostrar">
				<div class="panel-body">
				
				</div>
			</div>
			
			<br/> 
			<div style="width: 75%">
				<canvas id="radar" class="chart chart-radar" ng-hide="!desembolsosc.mostrar"
			  chart-data="desembolsosc.dataRadar" chart-options="desembolsosc.radarOptions" chart-labels="desembolsosc.etiquetas"
			  chart-legend="true" chart-series="desembolsosc.series"
			  chart-colors = "desembolsosc.radarColors">
			</canvas>
			</div>
			 
				
		</div>
		  
	</div>
</div>
