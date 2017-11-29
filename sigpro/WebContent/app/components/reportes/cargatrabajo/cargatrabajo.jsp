<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<style type="text/css">
	.cabecera {
		position: absolute;
	    margin-top: -32px;
	    flex-shrink: 0;
	    overflow-x: hidden;
	    width: 100%;
	}
	
	.cabeceraSticky{
		display: block;float: left;
	}
		
	.divTabla{
	    width: 100%;
	    height: 200px;
	    overflow-y: auto;
	    
	}
	
	.label-form2 {
    	font-size: 15px;
    	opacity: 1;
    	color: rgba(0,0,0,0.38) !important;
    	font-weight: bold;
	}
			</style>
	<div ng-controller="cargatrabajoController as controller" class="maincontainer all_page" id="title">
		<script type="text/ng-template" id="estructuraproyecto.jsp">
    		<%@ include file="/app/components/reportes/cargatrabajo/estructuraproyecto.jsp"%>
  	    </script>
  	    <script type="text/ng-template" id="estructuraresponsable.jsp">
    		<%@ include file="/app/components/reportes/cargatrabajo/estructuraresponsable.jsp"%>
  	    </script>
		<shiro:lacksPermission name="24010">
			<p ng-init="controller.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<div class="panel panel-default">
		  <div class="panel-heading"><h3>Carga de Trabajo</h3></div>
		</div>
	    <br>
	    <div class="row">
    		<div class="form-group col-sm-6" align="left">
				<div id="prestamo" angucomplete-alt placeholder="" pause="100" selected-object="controller.cambioPrestamo"
					  local-data="controller.lprestamos" search-fields="proyectoPrograma" title-field="proyectoPrograma" field-required="true" field-label="* Préstamo"
					  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
					  initial-value="controller.prestamoNombre" focus-out="controller.blurPrestamo()" input-name="prestamo"></div>
				<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
			</div>
    	</div>
    	<div class="row">
    		<div class="form-group col-sm-6" align="left">
				<div id="pep" angucomplete-alt placeholder="" pause="100" selected-object="controller.cambioPep"
					  local-data="controller.peps" search-fields="nombre" title-field="nombre" field-required="true" field-label="* {{etiquetas.proyecto}}"
					  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
					  initial-value="controller.pepNombre" focus-out="controller.blurPep()" input-name="pep" disable-input="controller.prestamoId==null"></div>
				<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
			</div>
    	</div>
    	<div class="row">
    		<div class="form-group col-sm-6" align="left">
				<div id= "lineaBase" angucomplete-alt placeholder="" pause="100" selected-object="controller.cambioLineaBase"
					  local-data="controller.lineasBase" search-fields="nombre" title-field="nombre" 
					  field-required="true" field-label="* Linea Base" minlength="1" input-class="form-control form-control-small field-angucomplete inputText" 
					  match-class="angucomplete-highlight" initial-value="controller.lineaBaseNombre" 
					  focus-out="controller.blurLineaBase()" input-name="lineaBase"></div>
				<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
			</div>
    	</div>
    	<div class="col-sm-12">
    		<div class="row" >	    	
				<div class="form-group col-sm-1">
					<input type="number"  class="inputText" ng-model="controller.fechaInicio" maxlength="4" 
					ng-value="controller.fechaInicio" onblur="this.setAttribute('value', this.value);"
					ng-change="controller.getEstructura()"/>
					<label for="campo.id" class="floating-label" style="left: 0;">*Año Inicial</label>
				</div>
				
				<div class="form-group col-sm-1">
					<input type="number"  class="inputText" ng-model="controller.fechaFin" maxlength="4" 
					ng-value="controller.fechaFin" onblur="this.setAttribute('value', this.value);"
					ng-change="controller.getEstructura()"/>
				  	<label for="campo.id" class="floating-label">*Año Final</label>
				</div>
				
				<div class="col-sm-10 operation_buttons" style="text-align: right;">
	    			<div class="btn-group" role="group" aria-label="">
						<label class="btn btn-default" ng-click="controller.exportarExcel()" uib-tooltip="Exportar a Excel" ng-hide="!controller.mostrar">
						<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
						<label class="btn btn-default" ng-click="controller.exportarPdf()" uib-tooltip="Exportar a PDF" ng-hide="true">
							<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
					</div>
	    		</div>
		    </div>
		    	<div class="row">
		    	
		    	<br/><br/><br/>
		    	
		    	<div style=" width: 100%; ">
			    	
					<div class="divTabla">
						
					
	    			<table st-table="controller.displayedCollection" st-safe-src="controller.rowCollection" class="table table-striped"
	    			ng-if="controller.mostrar">
						<thead  class="cabecera">
							<tr style="width: 99%; display:block;">
								<th style="display: none;">Id</th>
								<th class="label-form cabeceraSticky" style="width: 40%" st-sort="row.responsable" >Responsable</th>
								<th class="label-form cabeceraSticky" style="width: 15%; text-align: center;" st-sort="row.responsable">Actividades retrasadas</th>
								<th class="label-form cabeceraSticky" style="width: 15%; text-align: center;" st-sort="row.actividadesAtrasadas">Actividades en alerta</th>
								<th class="label-form cabeceraSticky" style="width: 15%; text-align: center;" st-sort="row.actividadesACumplir">Actividades a cumplir</th>
								<th class="label-form cabeceraSticky" style="width: 15%; text-align: center;" st-sort="row.actividadesCompletadas">Actividades completadas</th>
							</tr>
						</thead>
						<tbody>
							<tr ng-repeat="row in controller.displayedCollection" ng-click="controller.actividadesResponsable(row)">
								<td style="display: none;" >{{row.id}}</td>
								<td style="width: 40%" >{{row.responsable}}</td>
								<td style="text-align: center; width: 15%;">{{row.actividadesAtrasadas}}</td>
								<td style="text-align: center; width: 15%;">{{row.actividadesAlerta}}</td>
								<td style="text-align: center; width: 15%;">{{row.actividadesACumplir}}</td>
								<td style="text-align: center; width: 15%;">{{row.actividadesCompletadas}}</td>
							</tr>
							<tr>
								<td style="display: none;">{{controller.idTotal}}</td>
								<td style="font-weight: bold">{{controller.responsableTotal}}</td>
								<td style="text-align: center; font-weight: bold">{{controller.actividadesAtrasadasTotal}}</td>
								<td style="text-align: center; font-weight: bold">{{controller.actividadesAlertaTotal}}</td>
								<td style="text-align: center; font-weight: bold">{{controller.actividadesACumplirTotal}}</td>
								<td style="text-align: center; font-weight: bold">{{controller.actividadesCompletadas}}</td>
							</tr>
						</tbody>
					</table>
					</div>
		    	</div>
	    	</div>
		    <br>
	    	<br>
	    	<div class="row" ng-if="controller.mostrar">
	    		<div class="col-sm-6">
	    		<div style="text-align: center" >
	    			<label class="label-form2"> Actividades Terminadas</label>
	    			</div>
	    			<canvas id="line"  class="chart chart-line" chart-data="controller.dataChartLine"
					chart-labels="controller.etiquetasChartLine"  chart-options="controller.options"
					chart-dataset-override="controller.datasetOverride"  
					chart-series="controller.seriesLine"
					chart-colors = "controller.lineColors"
					>
					</canvas>
	    		</div>	
	    		<div class="col-sm-6">
	    		<div style="text-align: center" >
	    			<label class="label-form2"> Estado de Actividades</label>
	    		</div>
	    		
	    			<canvas id="pie" class="chart chart-pie" 
			  	chart-data="controller.data" chart-labels="controller.labels" chart-options="controller.optionsPie"
			  	chart-colors = "controller.pieColors" 
			  	 >
				</canvas>	
	    		</div>
	    	
	    	</div>
	    	<div class="row" ng-hide="!controller.mostrarcargando">
	    	<div class="grid_loading"  style="margin-top: 50px;"   >
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