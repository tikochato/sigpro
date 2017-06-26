<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>	
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<style>
    .ui-grid-tree-header-row {
        font-weight: normal !important;
    }
   
    .ui-grid-tree-padre {
        font-weight: bold;
    }
</style>

<div ng-controller="adquisicionesController as controller" class="maincontainer all_page" id="title">
	<shiro:lacksPermission name="24010">
			<p ng-init="controller.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
	<div class="panel panel-default">
	  <div class="panel-heading"><h3>Información presupuestaria</h3></div>
	</div>
    <br>
    
    <div class="row">
	    <div class="col-sm-12">
	    	<div style="height: 100%; width: 100%; height: 20%">
	    		<div class="row">
					<div class="form-group col-sm-3">
						<select  class="inputText" ng-model="controller.prestamo"
							ng-options="a.text for a in controller.prestamos"></select>
						<label for="prestamo" class="floating-label">Préstamos</label>
					</div>
					
					<div class="form-group col-sm-3" style="display: none;">					
						<input type="checkbox" id="Todo" ng-model="controller.informeCompleto" />
						<label for="controller.completo" class="floating-label">Informe completo</label>
					</div>
				</div>
				<div class="row">				
					<div class="form-group col-sm-3">
						<input type="text"  class="inputText" uib-datepicker-popup="{{controller.formatofecha}}" ng-model="controller.fechaInicio" is-open="controller.fi_abierto"
				            datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="controller.actualizarfechainicio()" 
				            ng-required="true"  ng-click="controller.abrirPopupFecha(1000)"
				            ng-value="controller.fechaInicio" onblur="this.setAttribute('value', this.value);"/>
				            <span class="label-icon" ng-click="controller.abrirPopupFecha(1000)">
				              <i class="glyphicon glyphicon-calendar"></i>
				            </span>
					  	<label for="campo.id" class="floating-label">*Año Inicial</label>
					</div>
					
					<div class="form-group col-sm-3">
						<input type="text"  class="inputText" uib-datepicker-popup="{{controller.formatofecha}}" ng-model="controller.fechaFin" is-open="controller.ff_abierto"
				            datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="controller.actualizarfechafin()" 
				            ng-required="true"  ng-click="controller.abrirPopupFecha(1001)"
				            ng-value="controller.fechaFin" onblur="this.setAttribute('value', this.value);"/>
				            <span class="label-icon" ng-click="controller.abrirPopupFecha(1001)">
				              <i class="glyphicon glyphicon-calendar"></i>
				            </span>
					  	<label for="campo.id" class="floating-label">*Año Final</label>
					</div>
					
					<div class="form-group col-sm-3" >
						<label class="btn btn-default" ng-click="controller.generar()" uib-tooltip="Generar informe" 
							tooltip-placement="bottom">
						<span class="glyphicon glyphicon-new-window"></span></label>
					</div>
				</div>
	    	</div>
	    	<br>
	    	<div style="height: 100%; width: 100%; z-index: 0">
		    	<div style="height: 5%; width: 100%">
					<div style="float: left;"><h4><b>Ejecución Presupuestaria</b></h4></div>
					<div style="float: right;" class="operation_buttons" align="right">
						<div class="btn-group">
							<label class="btn btn-primary"  ng-click="controller.exportarExcel()" uib-tooltip="Exportar" ng-hide="!controller.mostrarDescargar">
							<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true">&nbsp;Exportar</span></label>
						</div>
					</div>
				</div>
				<br>
				<br>
				<div id="grid1" ui-grid="controller.gridOptions" style="width: 100%; height: 600px"
					ui-grid-grouping 
					ui-grid-edit 
					 
					ui-grid-resize-columns 
					ui-grid-cellNav 
					ui-grid-pinning
					class="grid">
					<div class="grid_loading" ng-hide="!controller.mostrarcargando" style="position: absolute; z-index: 1">
		  				<div class="msg">
		      				<span><i class="fa fa-spinner fa-spin fa-4x"></i>
				  				<br /><br />
				  				<b>Cargando, por favor espere...</b>
			  				</span>
						</div>
					</div>
				</div>
	    	</div>
    	</div>
    </div>
</div>