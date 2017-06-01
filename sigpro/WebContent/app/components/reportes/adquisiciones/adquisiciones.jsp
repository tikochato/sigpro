<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>	
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	
<div ng-controller="adquisicionesController as controller" class="maincontainer all_page" id="title">
	<shiro:lacksPermission name="24010">
			<p ng-init="controller.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
	<div class="panel panel-default">
	  <div class="panel-heading"><h3>Plan de Adquisiciones</h3></div>
	</div>
    <br>
    
    <div class="container" style="width: 100%">
    	<div style="height: 30%; width: 100%;">
    		<div class="row">
				<div class="form-group col-sm-3">
					<select  class="inputText" ng-model="controller.prestamo"
						ng-options="a.text for a in controller.prestamos"></select>
					<label for="prestamo" class="floating-label">Préstamos</label>
				</div>
				
				<div class="form-group col-sm-3">
					<input type="text"  class="inputText" uib-datepicker-popup="{{controller.formatofecha}}" ng-model="controller.fechaInicio" is-open="controller.fi_abierto"
			            datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="controller.actualizarfechafin()" 
			            ng-required="true"  ng-click="controller.abrirPopupFecha(1000)"
			            ng-value="controller.fechaInicio" onblur="this.setAttribute('value', this.value);"/>
			            <span class="label-icon" ng-click="controller.abrirPopupFecha(1000)">
			              <i class="glyphicon glyphicon-calendar"></i>
			            </span>
				  	<label for="campo.id" class="floating-label">*Año</label>
				</div>
				
				<div class="form-group col-sm-3">
					<select  class="inputText" ng-model="controller.informe"
						ng-options="a.text for a in controller.informes"></select>
					<label for="informe" class="floating-label">Tipo Informe</label>
				</div>	
				
				<div class="form-group col-sm-3" >
					<label class="btn btn-default" ng-click="controller.generar()" uib-tooltip="Generar estructura préstamo" 
						tooltip-placement="bottom">
					<span class="glyphicon glyphicon-new-window"></span></label>
					
					<label class="btn btn-default" ng-click="controller.congelar()" uib-tooltip="Congelar" ng-hide="!controller.mostrarCongelar"
						tooltip-placement="bottom">
					<span class="glyphicon glyphicon-ban-circle"></span></label>
					
					<label class="btn btn-default" ng-click="controller.copiar()" uib-tooltip="Copiar" ng-hide="!controller.mostrarCopiar"
						tooltip-placement="bottom">
					<span class="glyphicon glyphicon-duplicate"></span></label>
				</div>
			</div>
    	</div>
    	<br>
    	<div style="max-height: 60%; width: 100%;">
	    	<div>
				<div><h4><b>Ejecución Financiera Anual</b></h4></div>
			</div>
			<br>
			<div id="grid1" ui-grid="controller.gridOptions" 
				ui-grid-grouping 
				ui-grid-edit 
				ui-grid-row-edit 
				ui-grid-resize-columns 
				ui-grid-selection
				ui-grid-cellNav 
				ui-grid-pinning
				class="grid">
			</div>
    	</div>
    </div>
</div>