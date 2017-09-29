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
		    border-right: 1px solid #ddd; 
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
					<div class="form-group col-sm-4">
						<select  class="inputText" ng-model="desembolsosc.prestamoSeleccionado"
							ng-options="a.text for a in desembolsosc.prestamos"
							ng-readonly="true"
							ng-required="true"
							ng-change="desembolsosc.validarParametros()">
							
							<option value="">Seleccione una préstamo</option>
						</select>
					</div>
					<div class="form-group col-sm-1">
						<input type="number"  class="inputText" ng-model="desembolsosc.anio_inicio" maxlength="4" 
						ng-value="controller.anioInicial" onblur="this.setAttribute('value', this.value);"
						ng-change="desembolsosc.validarParametros()"/>
					  	<label  class="floating-label" style="left: 0">*Año Inicial</label>
					</div>
					<div class="form-group col-sm-1">
						<input type="number"  class="inputText" ng-model="desembolsosc.anio_fin" maxlength="4" 
						ng-value="controller.anio_fin" onblur="this.setAttribute('value', this.value);"
						ng-change="desembolsosc.validarParametros()"/>
					  	<label for="campo.id" class="floating-label" style="left: 0">*Año Final</label>
					</div>
					
					<div class="col-sm-6" align="right" ng-hide="!desembolsosc.mostrar" >
						<div class="form-group col-sm-1">
							</div>
								<div class="col-sm-11">
									<div class="btn-group">
										<label class="btn btn-default" ng-model="desembolsosc.enMillones" uib-btn-radio="true"  uib-tooltip="Millones de Quetzales" role="button" tabindex="0" aria-invalid="false"
										ng-click="desembolsosc.convertirMillones()">
										<span>MQ</span></label>
										<label class="btn btn-default" ng-model="desembolsosc.enMillones" uib-btn-radio="false"  uib-tooltip="Quetzales" role="button" tabindex="1" aria-invalid="false"
										ng-click="desembolsosc.convertirMillones()">
										<span>Q</span></label>
							</div>
									<div class="btn-group" style="padding-left: 20px;">
										<label class="btn btn-default" ng-model="desembolsosc.agrupacion" uib-btn-radio="1" ng-click="desembolsosc.agruparDatos(1)" uib-tooltip="Mensual" role="button" tabindex="1" aria-invalid="false">
										<span>M</span></label>
										<label class="btn btn-default" ng-model="desembolsosc.agrupacion" uib-btn-radio="2" ng-click="desembolsosc.agruparDatos(2)" uib-tooltip="Bimestre" role="button" tabindex="2" aria-invalid="false">
										<span>B</span></label>
										<label class="btn btn-default" ng-model="desembolsosc.agrupacion" uib-btn-radio="3" ng-click="desembolsosc.agruparDatos(3)" uib-tooltip="Trimestre" role="button" tabindex="3" aria-invalid="false">
										<span>T</span></label>
										<label class="btn btn-default" ng-model="desembolsosc.agrupacion" uib-btn-radio="4" ng-click="desembolsosc.agruparDatos(4)" uib-tooltip="Cuatrimestre" role="button" tabindex="4" aria-invalid="false">
										<span>C</span></label>
										<label class="btn btn-default" ng-model="desembolsosc.agrupacion" uib-btn-radio="5" ng-click="desembolsosc.agruparDatos(5)" uib-tooltip="Semestre" role="button" tabindex="5" aria-invalid="false">
										<span>S</span></label>
										<label class="btn btn-default" ng-model="desembolsosc.agrupacion" uib-btn-radio="6" ng-click="desembolsosc.agruparDatos(6)" uib-tooltip="Anual" role="button" tabindex="6" aria-invalid="false">
										<span>A</span></label>
						</div>
									
									<div class="btn-group" style="padding-left: 20px;">
										<label class="btn btn-default" ng-click="desembolsosc.exportarExcel()" uib-tooltip="Exportar" ng-hide="!desembolsosc.mostrarDescargar">
										<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
										<label class="btn btn-default" ng-click="desembolsosc.exportarPdf()" uib-tooltip="Exportar PDF" ng-hide="!desembolsosc.mostrarDescargar">
										<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
										
							</div>
						</div>
			    	</div>
		    			<br><br><br><br>
				</div>
			</form>
			<br/> 
			
			
			<br/>
			<div style="width: 70%">
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
					 	 <div ng-if=" desembolsosc.esNumero(col)" >
					 	 	{{col | formatoMillones : desembolsosc.enMillones }}
					 	 </div>
					 	 
					 	 <div ng-if="! desembolsosc.esNumero(col)" >
					 	 	{{col}}
					 	 </div>
					 	 
					 </td>
				</tr>
				</tbody>
			</table>
			</div>
			<br/>
			
		</div>
		  
	</div>
</div>
