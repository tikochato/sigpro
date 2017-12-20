<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<style>
	
	 
	.planificado {
		color: #3647b2;
		 border-right: 1px solid #ddd;
	}
	
	.real2 {
		color: #2b8430;
		 border-right: 1px solid #ddd; 
		 
	}
	.label-form {
		    font-size: 13px;
		    opacity: 1;
		    color: rgba(0,0,0,0.38) !important;
		    font-weight: bold;
		}
	.colorPlanificadoFondo{
			background-color: #303f9e;
		}
		
	.colorRealFondo{
		background-color: #257129;
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
		
		.divTabla{
			
			max-height: 1174x;
			margin-right: -15px;
			overflow-y:hidden;
			overflow-x:hidden;
		}
		
		.tablaDatos {
			display: flex;
		    flex-direction: column;
		    align-items: stretch;
		    height: 375px; 
		}
		
		 .cuerpoTablaNombres {
		    
		    overflow-x: auto;
		    display: inline-block;
		    font-size: 13px;
		    max-width: 100%;
		    width: auto;
		}
	
		.btn-default.active{
			font-weight: bold;
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
		
		<br/>
		<div class="row" align="center" >
			<div class="col-sm-12 ">
			
			<form name="form">
				<div class="row">
		    		<div class="form-group col-sm-6" align="left">
						<div id="prestamo" angucomplete-alt placeholder="" pause="100" selected-object="desembolsosc.cambioPrestamo"
							  local-data="desembolsosc.lprestamos" search-fields="proyectoPrograma" title-field="proyectoPrograma" field-required="true" field-label="* Préstamo"
							  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
							  initial-value="desembolsosc.prestamoNombre" focus-out="desembolsosc.blurPrestamo()" input-name="prestamo"></div>
						<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
					</div>
		    	</div>
		    	<div class="row">
		    		<div class="form-group col-sm-6" align="left">
						<div id="pep" angucomplete-alt placeholder="" pause="100" selected-object="desembolsosc.cambioPep"
							  local-data="desembolsosc.peps" search-fields="nombre" title-field="nombre" field-required="true" field-label="* {{etiquetas.proyecto}}"
							  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
							  initial-value="desembolsosc.pepNombre" focus-out="desembolsosc.blurPep()" input-name="pep" disable-input="desembolsosc.prestamoId==null"></div>
						<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
					</div>
		    	</div>
				<div class="row">
					<div class="form-group col-sm-4" align="left">
						<div id= "lineaBase" angucomplete-alt placeholder="" pause="100" selected-object="desembolsosc.cambioLineaBase"
							  local-data="desembolsosc.lineasBase" search-fields="nombre" title-field="nombre" 
							  field-required="true" field-label="* Linea Base" minlength="1" input-class="form-control form-control-small field-angucomplete inputText" 
							  match-class="angucomplete-highlight" initial-value="desembolsosc.lineaBaseNombre" 
							  focus-out="desembolsosc.blurLineaBase()" input-name="lineaBase"></div>
						<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
					</div>
					<div class="form-group col-sm-1" style="margin-top: 5px;">
						<input type="number"  class="inputText" ng-model="desembolsosc.anio_inicio" maxlength="4" 
						ng-value="desembolsosc.anioInicial" onblur="this.setAttribute('value', this.value);"
						ng-change="desembolsosc.validarParametros()"/>
					  	<label  class="floating-label" style="left: 0">*Año Inicial</label>
					</div>
					<div class="form-group col-sm-1" style="margin-top: 5px;">
						<input type="number"  class="inputText" ng-model="desembolsosc.anio_fin" maxlength="4" 
						ng-value="desembolsosc.anio_fin" onblur="this.setAttribute('value', this.value);"
						ng-change="desembolsosc.validarParametros()"/>
					  	<label for="campo.id" class="floating-label" style="left: 0">*Año Final</label>
					</div>
					
					<div class="col-sm-6" align="right" ng-hide="!desembolsosc.mostrarBotones" >
						<div class="">
							<div class="btn-group" style="margin-left: -20px;">
								<label class="btn btn-default" ng-model="desembolsosc.enMillones" uib-btn-radio="true"  uib-tooltip="Millones de Quetzales" role="button" tabindex="0" aria-invalid="false"
								ng-click="desembolsosc.convertirMillones()">
								<span>MQ</span></label>
								<label class="btn btn-default" ng-model="desembolsosc.enMillones" uib-btn-radio="false"  uib-tooltip="Quetzales" role="button" tabindex="1" aria-invalid="false"
								ng-click="desembolsosc.convertirMillones()">
								<span>Q</span></label>
							</div>
							<div class="btn-group" style="padding-left: 20px;">
								<label class="btn btn-default" ng-model="desembolsosc.agrupacion" uib-btn-radio="1" ng-click="desembolsosc.agruparDatos(1)" uib-tooltip="Mes" role="button" tabindex="1" aria-invalid="false">
								<span>M</span></label>
								<label class="btn btn-default" ng-model="desembolsosc.agrupacion" uib-btn-radio="2" ng-click="desembolsosc.agruparDatos(2)" uib-tooltip="Bimestre" role="button" tabindex="2" aria-invalid="false">
								<span>B</span></label>
								<label class="btn btn-default" ng-model="desembolsosc.agrupacion" uib-btn-radio="3" ng-click="desembolsosc.agruparDatos(3)" uib-tooltip="Trimestre" role="button" tabindex="3" aria-invalid="false">
								<span>T</span></label>
								<label class="btn btn-default" ng-model="desembolsosc.agrupacion" uib-btn-radio="4" ng-click="desembolsosc.agruparDatos(4)" uib-tooltip="Cuatrimestre" role="button" tabindex="4" aria-invalid="false">
								<span>C</span></label>
								<label class="btn btn-default" ng-model="desembolsosc.agrupacion" uib-btn-radio="5" ng-click="desembolsosc.agruparDatos(5)" uib-tooltip="Semestre" role="button" tabindex="5" aria-invalid="false">
								<span>S</span></label>
								<label class="btn btn-default" ng-model="desembolsosc.agrupacion" uib-btn-radio="6" ng-click="desembolsosc.anio_inicio != desembolsosc.anio_fin ? desembolsosc.agruparDatos(6) : ''" 
								uib-tooltip="Año" role="button" tabindex="6" aria-invalid="false" ng-disabled="desembolsosc.anio_inicio == desembolsosc.anio_fin">
								<span>A</span></label>
							</div>
									
							<div class="btn-group" style="padding-left: 20px;">
								<label class="btn btn-default" ng-click="desembolsosc.exportarExcel()" uib-tooltip="Exportar a Excel" ng-hide="!desembolsosc.mostrarDescargar">
								<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
								<label class="btn btn-default" ng-click="desembolsosc.exportarPdf()" uib-tooltip="Exportar a PDF" ng-hide="true">
								<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
							</div>
						</div>
			    	</div>
		    			<br><br><br><br>
				</div>
			</form>
			<br/> 
			
			
			<br/>
			<div>
				
			</div>
			
			<div style="width: 70%">
				<label class="label-form"  ng-hide="!desembolsosc.mostrar">Desembolsos </label>
				<canvas id="line" class="chart chart-line" chart-data="desembolsosc.desembolsosGrafica" ng-hide="!desembolsosc.mostrar"
				chart-labels="desembolsosc.etiqutas" chart-series="desembolsosc.series" chart-options="desembolsosc.options"
				chart-dataset-override="desembolsosc.datasetOverride" 
				 chart-colors = "desembolsosc.radarColors" chart-legend="true">
				</canvas>
			</div>
			
			<br/><br/>
			<div class="divTabla"  >
				<table st-table="desembolsosc.desembolsos" class="table table-striped table-hover table-condensed table-responsive cuerpoTablaNombres "
					ng-hide="!desembolsosc.mostrar">
					<thead class="theadDatos">
					<tr >
						<th ng-repeat="n in desembolsosc.columnas track by $index" style="text-align: center" class="label-form">
						{{n}}
						</th>
					</tr>
					</thead>
					<tbody >
					<tr class = "{{desembolsosc.clase($index)}}" ng-repeat="row in desembolsosc.tabla track by $index" style="text-align: right;">
						<td ng-repeat = "col in row track by $index" 
						 	 nowrap style="font-weight: bold; border-right: 1px solid #ddd; min-width:125px;">
						 	 <div ng-if=" desembolsosc.esNumero(col) && ($parent.$index == 0 || $parent.$index == 2)" >
						 	 	{{col | formatoMillones : desembolsosc.enMillones }}
						 	 </div>
						 	 <div ng-if="! desembolsosc.esNumero(col)" >
						 	 	{{col}}
						 	 </div>
						 	 <div ng-if="desembolsosc.esNumero(col) && $parent.$index != 0 && $parent.$index != 2" >
						 	 	{{col | formatoMillonesDolares : desembolsosc.enMillones }}
						 	 </div>
						 </td>
					</tr>
					</tbody>
				</table>
			</div>
			<br/> <br/>
			
			<div style="width: 70%">
				<label class="label-form"  ng-hide="!desembolsosc.mostrar">Acumulación de Desembolsos </label>
				<canvas id="line" class="chart chart-line" chart-data="desembolsosc.acumulacion" ng-hide="!desembolsosc.mostrar"
				chart-labels="desembolsosc.etiqutas" chart-series="desembolsosc.series" chart-options="desembolsosc.options"
				chart-dataset-override="desembolsosc.datasetOverride" 
				 chart-colors = "desembolsosc.radarColors" chart-legend="true">
				</canvas>
			</div>
			
			<br/><br/>
			<div class="divTabla"  >
				<table st-table="desembolsosc.desembolsos" class="table table-striped table-hover table-condensed table-responsive cuerpoTablaNombres "
					ng-hide="!desembolsosc.mostrar">
					<thead class="theadDatos">
					<tr >
						<th ng-repeat="n in desembolsosc.columnasAcumulado track by $index" style="text-align: center" class="label-form">
						{{n}}
						</th>
					</tr>
					</thead>
					<tbody >
					<tr class = "{{desembolsosc.clase($index + 1)}}" ng-repeat="row in desembolsosc.tablaAcumulado track by $index" style="text-align: right;">
						<td ng-repeat = "col in row track by $index" 
						 	 nowrap style="font-weight: bold; border-right: 1px solid #ddd; min-width:125px;">
						 	 <div ng-if=" desembolsosc.esNumero(col) && $parent.$index != 3" >
						 	 	{{col | formatoMillonesDolares : desembolsosc.enMillones }}
						 	 </div>
						 	 <div ng-if="! desembolsosc.esNumero(col)" >
						 	 	{{col}}
						 	 </div>
						 </td>
					</tr>
					</tbody>
				</table>
			</div>
			
		</div>
		  
	</div>
</div>
