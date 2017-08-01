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
		color: #303f9e;
	}
	
	.real2 {
		color: #257129;
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
					<div class="form-group col-sm-3">
						<select  class="inputText" ng-model="desembolsosc.prestamoSeleccionado"
							ng-options="a.text for a in desembolsosc.prestamos"
							ng-readonly="true"
							ng-required="true"
							ng-change="desembolsosc.generarReporte()">
							
							<option value="">Seleccione una opción</option>
						</select>
						<label for="prestamo" class="floating-label">Préstamos</label>
					</div>
					<div class="form-group col-sm-2">
						<input type="number"  class="inputText" ng-model="desembolsosc.anioSeleccionado" maxlength="4" 
						ng-value="controller.fechaInicio" onblur="this.setAttribute('value', this.value);"
						ng-change="desembolsosc.generarReporte()"/>
					  	<label for="campo.id" class="floating-label">*Año Inicial</label>
					</div>
					
					<div class="col-sm-7" align="right" ng-hide="!desembolsosc.mostrar" >
						<div class="form-group col-sm-1">
							</div>
								<div class="col-sm-11">
									<div class="btn-group">
										<label class="btn btn-default" ng-model="desembolsosc.enMillones" uib-btn-radio="true"  uib-tooltip="Millones de Quetzales" role="button" tabindex="0" aria-invalid="false">
										<span>MQ</span></label>
										<label class="btn btn-default" ng-model="desembolsosc.enMillones" uib-btn-radio="false"  uib-tooltip="Quetzales" role="button" tabindex="1" aria-invalid="false">
										<span>Q</span></label>
							</div>
									<div class="btn-group" style="padding-left: 20px;">
										<label class="btn btn-default" ng-model="desembolsosc.agrupacion" uib-btn-radio="1" ng-click="desembolsosc.asignarSerie(1)" uib-tooltip="Mensual" role="button" tabindex="1" aria-invalid="false">
										<span>M</span></label>
										<label class="btn btn-default" ng-model="desembolsosc.agrupacion" uib-btn-radio="2" ng-click="desembolsosc.asignarSerie(2)" uib-tooltip="Bimestre" role="button" tabindex="2" aria-invalid="false">
										<span>B</span></label>
										<label class="btn btn-default" ng-model="desembolsosc.agrupacion" uib-btn-radio="3" ng-click="desembolsosc.asignarSerie(3)" uib-tooltip="Trimestre" role="button" tabindex="3" aria-invalid="false">
										<span>T</span></label>
										<label class="btn btn-default" ng-model="desembolsosc.agrupacion" uib-btn-radio="4" ng-click="desembolsosc.asignarSerie(4)" uib-tooltip="Cuatrimestre" role="button" tabindex="4" aria-invalid="false">
										<span>C</span></label>
										<label class="btn btn-default" ng-model="desembolsosc.agrupacion" uib-btn-radio="5" ng-click="desembolsosc.asignarSerie(5)" uib-tooltip="Semestre" role="button" tabindex="5" aria-invalid="false">
										<span>S</span></label>
										<label class="btn btn-default" ng-model="desembolsosc.agrupacion" uib-btn-radio="6" ng-click="desembolsosc.asignarSerie(6)" uib-tooltip="Anual" role="button" tabindex="6" aria-invalid="false">
										<span>A</span></label>
						</div>
									
									<div class="btn-group" style="padding-left: 20px;">
										<label class="btn btn-default" ng-click="desembolsosc.exportarExcel()" uib-tooltip="Exportar" ng-hide="!desembolsosc.mostrarDescargar">
										<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
							</div>
						</div>
			    	</div>
		    			<br><br><br><br>
					
					
					
				</div>
			</form>
			<br/> 
			
			
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
					 	 style="font-weight: bold;">{{desembolsosc.formato1(col) }}
					 </td>
				</tr>
				</tbody>
			</table>
		</div>
		  
	</div>
</div>
