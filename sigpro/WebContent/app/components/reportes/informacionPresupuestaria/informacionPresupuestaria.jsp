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

<script type="text/ng-template" id="category_header.html">
<!DOCTYPE html>
<div  class="ngTopPanel categoryStyle">
    <div class="ngHeaderContainer" style="height:{{headerRowHeight}}px;">
        <div class="categoryHeaderScroller" style="height:{{headerRowHeight}}px;position:absolute">  <!-- fixes scrollbar issue -->
            <div class="ngHeaderCell" ng-repeat="cat in categories"  style="left: {{cat.left}}px; width: {{cat.width}}px">
                <div class="ngVerticalBar" style="height:100%" ng-class="{ ngVerticalBarVisible: !$last }">&nbsp;</div>
                <div class="ngHeaderText" style="text-align:center">{{cat.displayName}}</div>
            </div>
        </div>
    </div>
</div>
</script>

<div ng-controller="adquisicionesController as controller" class="maincontainer all_page" id="title">
	<shiro:lacksPermission name="24010">
			<p ng-init="controller.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
	<div class="panel panel-default">
	  <div class="panel-heading"><h3>Información presupuestaria</h3></div>
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
					<input type="checkbox" id="Todo" ng-model="controller.informeCompleto" />
					<label for="controller.completo" class="floating-label">Informe completo</label>
				</div>
				
				<div class="form-group col-sm-3" >
					<label class="btn btn-default" ng-click="controller.generar()" uib-tooltip="Generar informe" 
						tooltip-placement="bottom">
					<span class="glyphicon glyphicon-new-window"></span></label>
					
					<label class="btn btn-default" ng-click="controller.descargar()" uib-tooltip="Descargar excel" ng-hide="!controller.mostrarDescargar"
						tooltip-placement="bottom">
					<span class="glyphicon glyphicon-download-alt"></span></label>
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
			</div>
    	</div>
    	<br>
    	<div style="max-height: 70%; width: 100%;">
	    	<div>
				<div><h4><b>Ejecución Financiera</b></h4></div>
			</div>
			<br>
			<div category-header="controller.gridOptions"></div>
			<div id="grid1" ui-grid="controller.gridOptions" style="width: 100%; height: 100%"
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