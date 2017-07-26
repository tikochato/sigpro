<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>	
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<style>
	.actividad { font-weight: normal !important; }
	.padre {font-weight: bold;}
    .real { background-color: #f7e681 !important }
    .realTotal { background-color: #f7e681 !important }
    .divTabla1{
		display: inline-block;
		overflow-x: hidden;
		white-space: nowrap;
		float: left; 
		z-index: 1; 
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
							ng-options="a.text for a in controller.prestamos"></select>
						<label for="prestamo" class="floating-label">Préstamos</label>
					</div>
					
					<div class="form-group col-sm-1">
						<input type="number"  class="inputText" ng-model="controller.fechaInicio" maxlength="4" ng-value="controller.fechaInicio" onblur="this.setAttribute('value', this.value);"/>
					  	<label for="campo.id" class="floating-label">*Año Inicial</label>
					</div>
					
					<div class="form-group col-sm-1">
						<input type="number"  class="inputText" ng-model="controller.fechaFin" maxlength="4" ng-value="controller.fechaFin" onblur="this.setAttribute('value', this.value);"/>
					  	<label for="campo.id" class="floating-label">*Año Final</label>
					</div>
				</div>
				<div class="row">
					<div style="height: 5%; width: 100%; text-align: center;">
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
						<label class="btn btn-default"  ng-click="controller.exportarExcel()" uib-tooltip="Exportar">
						<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
					</div>
				</div>
	    	</div>
	    	<div style="width: 100%; height: 80%">
		    	<div class="row">
		    		<div style="width: 200px; min-width:200px; max-width:200px; float: left; z-index: 1000;">
		    			<table st-table="controller.displayedCollectionNombre" st-safe-src="controller.rowCollectionNombre" class="table table-striped">
							<thead>
								<tr>
			          				<th rowspan="2" style="text-align: center; height: 71px; width: 200px;">Nombre</th>
			         			</tr>
							</thead>
							<tbody>
								<tr ng-repeat="row in controller.displayedCollectionNombre">
									<td>{{row.nombre}}</td>
								</tr>
							</tbody>
						</table>
	    			</div>
		    		<div id="reporte">
	    				<div style="max-width: {{controller.tamanoTotal}}px" class="divTabla1">
			    			<table st-table="controller.displayedCollectionPrestamo" st-safe-src="controller.rowCollectionPrestamo" class="table table-striped" ng-style="controller.margenTabla"
				    				style="max-width: {{controller.tamanoTotal}}px">
								<thead>
									<tr>
				         				<th colspan={{controller.colspan}} style="{{controller.estiloCabecera}}" ng-repeat="m in controller.objetoMostrar">{{m.nombreMes}}</th>
				          			</tr>
				          			<tr>
				          				<th ng-repeat="a in controller.aniosfinales" style="{{controller.estiloCelda}}">{{a.ano}}</th>
							        </tr>
								</thead>
								<tbody>
									<tr>
										
									</tr>
								</tbody>
							</table>
						</div>
			    		<div style="float: right; z-index: 1000;">
			    			<table st-table="controller.displayedCollectionPrestamo" st-safe-src="controller.rowCollectionPrestamo" class="table table-striped">
								<thead>
									<tr>
				          				<th colspan={{controller.colspan}} style="{{controller.estiloCelda}}">Total anual</th>
				          				<th style="{{controller.estiloCelda}}">Total</th>
				          			</tr>
				          			<tr>
				          				<th ng-repeat="a in controller.aniosTotal" style="{{controller.estiloCelda}}">{{a.ano}}</th>
				          				<th style="{{controller.estiloCelda}}"></th>
							        </tr>
								</thead>
								<tbody>
								</tbody>
							</table>
			    		</div>
	    			</div>
				</div>
	    	</div>
	    	<div style="text-align: center;">
					<label class="btn btn-default" ng-click="controller.atras()" uib-tooltip="Atrás" ng-hide="!controller.movimiento" 
							tooltip-placement="bottom">
					<span class="glyphicon glyphicon-chevron-left"></span></label>
					<label class="btn btn-default" ng-click="controller.siguiente()" uib-tooltip="Siguiente" ng-hide="!controller.movimiento"
							tooltip-placement="bottom">
					<span class="glyphicon glyphicon-chevron-right"></span></label>
	    	</div>
    	</div>
    </div>
</div>